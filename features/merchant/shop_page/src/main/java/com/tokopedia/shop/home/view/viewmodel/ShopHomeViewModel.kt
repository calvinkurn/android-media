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
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
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
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Provider

class ShopHomeViewModel @Inject constructor(
        private val userSession: UserSessionInterface,
        private val getShopPageHomeLayoutUseCase: GetShopPageHomeLayoutUseCase,
        private val getShopProductUseCase: GqlGetShopProductUseCase,
        private val dispatcherProvider: CoroutineDispatcherProvider,
        private val addToCartUseCase: AddToCartUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        private val gqlCheckWishlistUseCase: Provider<GQLCheckWishlistUseCase>
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

    val checkWishlistData: LiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>>>>
        get() = _checkWishlistData
    private val _checkWishlistData = MutableLiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>>>>()

    val userSessionShopId = userSession.shopId ?: ""
    val isLogin = userSession.isLoggedIn
    val userId: String
        get() = userSession.userId

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
                productList.await()?.let { productListData ->
                    _productListData.postValue(Success(productListData))
                }
            }
        }) {
        }
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
            productId: String,
            shopId: String,
            onSuccessAddToCart: (dataModelAtc: DataModel) -> Unit
    ) {
        launchCatchError(block = {
            val addToCartSubmitData = withContext(dispatcherProvider.io()) {
                submitAddProductToCart(shopId, productId)
            }
            if (addToCartSubmitData.status == STATUS_OK)
                onSuccessAddToCart(addToCartSubmitData.data)
        }) {}
    }

    fun clearGetShopProductUseCase() {
        getShopProductUseCase.clearCache()
    }

    fun removeWishList(
            productId: String,
            onSuccessRemoveWishList: () -> Unit,
            onErrorRemoveWishList: (errorMessage: String?) -> Unit
    ) {
        removeWishlistUseCase.createObservable(productId, userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {}

            override fun onSuccessAddWishlist(productId: String?) {}

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                onErrorRemoveWishList.invoke(errorMessage)
            }

            override fun onSuccessRemoveWishlist(productId: String?) {
                onSuccessRemoveWishList.invoke()
            }

        })
    }

    fun addWishList(
            productId: String,
            onSuccessAddWishList: () -> Unit,
            onErrorAddWishList: (errorMessage: String?) -> Unit
    ) {
        addWishListUseCase.createObservable(productId, userId, object : WishListActionListener {
            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                onErrorAddWishList.invoke(errorMessage)
            }

            override fun onSuccessAddWishlist(productId: String?) {
                onSuccessAddWishList.invoke()
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {}

            override fun onSuccessRemoveWishlist(productId: String?) {}

        })
    }

    fun getWishlistStatus(shopHomeCarousellProductUiModel: List<ShopHomeCarousellProductUiModel>) {
        launchCatchError(block = {
            val listResultCheckWishlist = shopHomeCarousellProductUiModel.map {
                async(dispatcherProvider.io()) {
                    val resultCheckWishlist = checkListProductWishlist(it)
                    Pair(it, resultCheckWishlist)
                }
            }.awaitAll()
            listResultCheckWishlist.onEach {
                _checkWishlistData.value = Success(listResultCheckWishlist)
            }
        }) {}
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

    private fun submitAddProductToCart(shopId: String, productId: String): AddToCartDataModel {
        val requestParams = AddToCartUseCase.getMinimumParams(productId, shopId)
        return addToCartUseCase.createObservable(requestParams).toBlocking().first()
    }

    private suspend fun checkListProductWishlist(
            shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel
    ): List<CheckWishlistResult> {
        val useCase = gqlCheckWishlistUseCase.get()
        val listProductIdString = mutableListOf<String>().apply {
            shopHomeCarousellProductUiModel.productList.onEach {
                add(it.id ?: "")
            }
        }.joinToString(separator = ",")
        useCase.params = GQLCheckWishlistUseCase.createParams(listProductIdString)
        return useCase.executeOnBackground()
    }

    fun clearCache() {
        clearGetShopProductUseCase()
        getShopPageHomeLayoutUseCase.clearCache()
    }
}