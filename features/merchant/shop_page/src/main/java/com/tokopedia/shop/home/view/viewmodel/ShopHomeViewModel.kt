package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel.Companion.STATUS_OK
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.util.Util
import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import com.tokopedia.shop.home.util.asyncCatchError
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.*
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ShopHomeViewModel @Inject constructor(
        userSession: UserSessionInterface,
        private val getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase,
        private val getShopProductUseCase: GqlGetShopProductUseCase,
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val addToCartUseCase: AddToCartUseCase
) : BaseViewModel(dispatcherProvider.main()) {

    companion object {
        const val ALL_SHOWCASE_ID = "etalase"
    }

    val productListData: LiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>
        get() = _productListData
    private val _productListData = MutableLiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>()

    val shopHomeLayoutData: LiveData<Result<ShopPageHomeLayoutUiModel>>
        get() = _shopHomeLayoutData
    private val _shopHomeLayoutData = MutableLiveData<Result<ShopPageHomeLayoutUiModel>>()

    val addToCartSubmitData: LiveData<Result<ShopHomeAddToCartSuccessDataModel>>
        get() = _addToCartSubmitData
    private val _addToCartSubmitData = MutableLiveData<Result<ShopHomeAddToCartSuccessDataModel>>()

    val userSessionShopId = userSession.shopId ?: ""
    val isLogin = userSession.isLoggedIn

    fun getShopPageHomeData(shopId: String) {
        launchCatchError(block = {
            val shopLayoutWidget = asyncCatchError(
                    dispatcherProvider.io(),
                    block = {
                        getShopPageHomeLayout(shopId)
                    },
                    onError = {
                        _shopHomeLayoutData.postValue(Fail(it))
                        null
                    }
            )
            val productList = asyncCatchError(
                    dispatcherProvider.io(),
                    block = { getProductList(shopId, 1) },
                    onError = { null }
            )
            shopLayoutWidget.await()?.let {
                _shopHomeLayoutData.postValue(Success(it))
                productList.await()?.let {
                    _productListData.postValue(Success(it))
                }
            }
        }) {
        }
    }

    private suspend fun getShopPageHomeLayout(shopId: String): ShopPageHomeLayoutUiModel {
        getShopPageHomeLayoutUseCase.params = GetShopPageHomeLayoutUseCase.createParams(shopId)
        return ShopPageHomeMapper.mapToShopPageHomeLayoutUiModel(
                getShopPageHomeLayoutUseCase.executeOnBackground(),
                Util.isMyShop(shopId, userSessionShopId)
        )
    }

    private suspend fun getProductList(
            shopId: String,
            page: Int
    ): Pair<Boolean, List<ShopHomeProductViewModel>> {
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(
                shopId,
                ShopProductFilterInput().apply {
                    etalaseMenu = ALL_SHOWCASE_ID
                    this.page = page
                }
        )
        val productListResponse = getShopProductUseCase.executeOnBackground()
        val isHasNextPage = Util.isHasNextPage(page, ShopPageConstant.DEFAULT_PER_PAGE, productListResponse.totalData)
        return Pair(
                isHasNextPage,
                productListResponse.data.map {
                    ShopPageHomeMapper.mapShopProductToProductViewModel(
                            it,
                            Util.isMyShop(shopId, userSessionShopId)
                    )
                }
        )
    }

    fun getNextProductList(
            shopId: String,
            page: Int
    ) {
        launchCatchError(block = {
            val listProductData = withContext(dispatcherProvider.io()) {
                getProductList(shopId, page)
            }
            _productListData.postValue(Success(listProductData))
        }) {
            _productListData.postValue(Fail(it))
        }
    }

    fun addProductToCart(
            parentPosition: Int,
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel,
            shopHomeProductViewModel: ShopHomeProductViewModel,
            shopId: String
    ) {
        launchCatchError(block = {
            val addToCartSubmitData = withContext(dispatcherProvider.io()) {
                submitAddProductToCart(shopId, shopHomeProductViewModel.id ?: "")
            }
            if (addToCartSubmitData.status == STATUS_OK)
                _addToCartSubmitData.postValue(Success(ShopHomeAddToCartSuccessDataModel(
                        parentPosition,
                        shopHomeCarousellProductUiModel,
                        shopHomeProductViewModel,
                        addToCartSubmitData.data
                )))
        }) {}
    }

    private fun submitAddProductToCart(shopId: String, productId: String): AddToCartDataModel {
        val requestParams = AddToCartUseCase.getMinimumParams(productId, shopId)
        return addToCartUseCase.createObservable(requestParams).toBlocking().first()
    }
}