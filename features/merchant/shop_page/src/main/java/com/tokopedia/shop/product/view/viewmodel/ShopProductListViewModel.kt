package com.tokopedia.shop.product.view.viewmodel

import android.arch.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.constant.ShopEtalaseTypeDef
import com.tokopedia.shop.common.domain.interactor.GQLGetShopInfoUseCase
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopInfo
import com.tokopedia.shop.common.graphql.domain.usecase.shopetalase.GetShopEtalaseByShopUseCase
import com.tokopedia.shop.etalase.view.model.ShopEtalaseViewModel
import com.tokopedia.shop.product.data.model.ShopProduct
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.product.view.model.ShopProductViewModel
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
import rx.Subscriber
import javax.inject.Inject

class ShopProductListViewModel @Inject constructor(private val userSession: UserSessionInterface,
                                                   private val getShopInfoUseCase: GQLGetShopInfoUseCase,
                                                   private val getShopEtalaseByShopUseCase: GetShopEtalaseByShopUseCase,
                                                   private val getShopProductUseCase: GqlGetShopProductUseCase,
                                                   private val addWishListUseCase: AddWishListUseCase,
                                                   private val removeWishlistUseCase: RemoveWishListUseCase,
                                                   dispatcher: CoroutineDispatcher): BaseViewModel(dispatcher){

    fun isMyShop(shopId: String) = userSession.shopId == shopId

    val isEtalaseEmpty: Boolean
        get() = etalaseResponse.value?.let { (it as? Success)?.data?.isEmpty() } ?: true

    val isLogin: Boolean
        get() = userSession.isLoggedIn

    private val filterInput = ShopProductFilterInput()

    val shopInfoResp = MutableLiveData<Result<ShopInfo>>()
    val productResponse = MutableLiveData<Result<Pair<Boolean, List<ShopProductViewModel>>>>()
    val etalaseResponse = MutableLiveData<Result<List<ShopEtalaseViewModel>>>()

    fun getShop(shopId: String? = null, shopDomain: String? = null, isRefresh: Boolean = false){
        val id = shopId?.toIntOrNull() ?: 0
        if (id == 0 && shopDomain == null) return
        launchCatchError(block = {
            getShopInfoUseCase.params = GQLGetShopInfoUseCase
                    .createParams(if (id == 0)listOf() else listOf(id), shopDomain)
            getShopInfoUseCase.isFromCacheFirst = !isRefresh
            val shopInfo = withContext(Dispatchers.IO){getShopInfoUseCase.executeOnBackground()}
            shopInfoResp.value = Success(shopInfo)
        }){
            shopInfoResp.value = Fail(it)
        }
    }

    fun getShopEtalase(shopId: String){
        val params = GetShopEtalaseByShopUseCase.createRequestParams(shopId, true, false, isMyShop(shopId))
        getShopEtalaseByShopUseCase.execute(params, object : Subscriber<ArrayList<ShopEtalaseModel>>() {
            override fun onNext(list: ArrayList<ShopEtalaseModel>?) {
                list?.let { etalaseResponse.value = Success(it.map { item -> item.toViewModel() }) }
            }

            override fun onCompleted() {}

            override fun onError(t: Throwable?) {
                t?.let { etalaseResponse.value = Fail(it) }
            }

        })
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
                        to getProductResp.data.map { it.toProductViewModel(isMyShop(shopId)) })
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

    fun clearEtalaseCache(){
        getShopEtalaseByShopUseCase.clearCache()
    }

    override fun onCleared() {
        super.onCleared()
        addWishListUseCase.unsubscribe()
        removeWishlistUseCase.unsubscribe()
    }

    private fun isHasNextPage(page: Int, perPage: Int, totalData: Int): Boolean = page * perPage < totalData

    private fun ShopEtalaseModel.toViewModel(): ShopEtalaseViewModel {
        val _id = if (type == ShopEtalaseTypeDef.ETALASE_DEFAULT) alias else id
        return ShopEtalaseViewModel(_id, name, useAce, type, highlighted).also {
            it.etalaseCount = count.toLong()
            it.etalaseBadge = badge
        }
    }

    private fun ShopProduct.toProductViewModel(isMyOwnProduct: Boolean): ShopProductViewModel = ShopProductViewModel().also {
        it.id = productId
        it.name = name
        it.displayedPrice = price.textIdr
        it.originalPrice = campaign.originalPriceFmt
        it.discountPercentage = campaign.discountedPercentage
        it.imageUrl = primaryImage.original
        it.imageUrl300 = primaryImage.resize300
        it.totalReview = stats.reviewCount.toString()
        it.rating = stats.rating.toDouble()
        if (cashback.cashbackPercent > 0) {
            it.cashback = cashback.cashbackPercent.toDouble()
        }
        it.isWholesale = flags.isWholesale
        it.isPo = flags.isPreorder
        it.isFreeReturn = flags.isFreereturn
        it.isWishList = flags.isWishlist
        it.productUrl = productUrl
        it.isSoldOut = flags.isSold
        it.isShowWishList = !isMyOwnProduct
    }
}