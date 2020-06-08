package com.tokopedia.shop.product.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase.Companion.SHOP_PRODUCT_LIST_RESULT_SOURCE
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.product.utils.mapper.ShopPageProductListMapper
import com.tokopedia.shop.product.view.datamodel.ShopProductEtalaseChipItemViewModel
import com.tokopedia.shop.product.view.datamodel.ShopProductViewModel
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopPageProductListResultViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                             private val getShopInfoUseCase: GQLGetShopInfoUseCase,
                                                             private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
                                                             private val getShopProductUseCase: GqlGetShopProductUseCase,
                                                             private val addWishListUseCase: AddWishListUseCase,
                                                             private val removeWishlistUseCase: RemoveWishListUseCase,
                                                             dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun isMyShop(shopId: String) = userSession.shopId == shopId

    val isEtalaseEmpty: Boolean
        get() = etalaseListData.value?.let { (it as? Success)?.data?.isEmpty() } ?: true

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    private val filterInput = ShopProductFilterInput()

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()
    val productResponse = MutableLiveData<Result<Pair<Boolean, List<ShopProductViewModel>>>>()
    val etalaseListData = MutableLiveData<Result<List<ShopProductEtalaseChipItemViewModel>>>()

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false){
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            getShopInfoUseCase.params = GQLGetShopInfoUseCase
                    .createParams(if (id == 0)listOf() else listOf(id), shopDomain, source = SHOP_PRODUCT_LIST_RESULT_SOURCE)
            getShopInfoUseCase.isFromCacheFirst = !isRefresh
            val shopInfo = withContext(Dispatchers.IO){getShopInfoUseCase.executeOnBackground()}
            shopInfoResp.value = Success(shopInfo)
        }){
            shopInfoResp.value = Fail(it)
        }
    }

    fun getEtalaseData(shopId: String, isOwner: Boolean, isNeedToReloadData: Boolean = false) {
        launchCatchError(coroutineContext, block = {
            val etalaseListDataResult = withContext(Dispatchers.IO) { getShopEtalaseData(shopId, isOwner, isNeedToReloadData) }
            etalaseListData.postValue(Success(etalaseListDataResult))
        }) {
            etalaseListData.postValue(Fail(it))
        }
    }

    private fun getShopEtalaseData(shopId: String, isOwner: Boolean, isNeedToReloadData: Boolean = false): List<ShopProductEtalaseChipItemViewModel> {
        var params: RequestParams

        if (isOwner) {
            params = GetShopEtalaseByShopUseCase.createRequestParams(
                    shopId,
                    GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.HIDE_NO_COUNT_VALUE,
                    GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.HIDE_SHOWCASE_GROUP_VALUE,
                    GetShopEtalaseByShopUseCase.Companion.SellerQueryParam.IS_OWNER_VALUE
            )
        } else {
            params = GetShopEtalaseByShopUseCase.createRequestParams(
                    shopId,
                    GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.HIDE_NO_COUNT_VALUE,
                    GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.HIDE_SHOWCASE_GROUP_VALUE,
                    GetShopEtalaseByShopUseCase.Companion.BuyerQueryParam.IS_OWNER_VALUE
            )
        }

        getShopEtalaseByShopUseCase.isFromCacheFirst = !isNeedToReloadData
        val listShopEtalaseResponse = getShopEtalaseByShopUseCase.createObservable(params).toBlocking().first()
        return ShopPageProductListMapper.mapToShopProductEtalaseListDataModel(listShopEtalaseResponse)
    }

    fun getShopProduct(shopId: String, page: Int = 1, perPage: Int = 10, sortId: Int = 0,
                       etalase: String = "", search: String = "", isForceRefresh: Boolean = false){
        with(filterInput){
            this.page = page
            this.perPage = perPage
            this.etalaseMenu = etalase
            this.searchKeyword = search
            this.sort = sortId
        }

        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(shopId, filterInput)
        getShopProductUseCase.isFromCacheFirst = !isForceRefresh

        launchCatchError( block = {
            val getProductResp = withContext(Dispatchers.IO) {getShopProductUseCase.executeOnBackground()}
            productResponse.value = if (getProductResp.errors.isNotEmpty())
                Fail(MessageErrorException(getProductResp.errors))
            else
                Success(isHasNextPage(page, perPage, getProductResp.totalData)
                        to getProductResp.data.map {
                    ShopPageProductListMapper.mapShopProductToProductViewModel(it, isMyShop(shopId), filterInput.etalaseMenu)
                })
        }){
            productResponse.value = Fail(it)
        }
    }

    fun removeWishList(productId: String, listener: WishListActionListener) {
        removeWishlistUseCase.createObservable(productId, userSession.userId, listener)
    }

    fun addWishList(productId: String, listener: WishListActionListener) {
        addWishListUseCase.createObservable(productId, userSession.userId, listener)
    }

    fun clearCache() {
        getShopEtalaseByShopUseCase.clearCache()
        clearGetShopProductUseCase()

    }

    fun clearGetShopProductUseCase() {
        getShopProductUseCase.clearCache()
    }

    override fun onCleared() {
        super.onCleared()
        addWishListUseCase.unsubscribe()
        removeWishlistUseCase.unsubscribe()
    }

    private fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

}