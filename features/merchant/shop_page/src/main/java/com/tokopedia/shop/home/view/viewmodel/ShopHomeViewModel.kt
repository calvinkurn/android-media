package com.tokopedia.shop.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.model.response.DataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
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

    val initialProductListData: LiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>
        get() = _initialProductListData
    private val _initialProductListData = MutableLiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>()

    val newProductListData: LiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>
        get() = _newProductListData
    private val _newProductListData = MutableLiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>()

    val shopHomeLayoutData: LiveData<Result<ShopPageHomeLayoutUiModel>>
        get() = _shopHomeLayoutData
    private val _shopHomeLayoutData = MutableLiveData<Result<ShopPageHomeLayoutUiModel>>()

    val checkWishlistData: LiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>?>>>
        get() = _checkWishlistData
    private val _checkWishlistData = MutableLiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>?>>>()

    val userSessionShopId: String
        get() = userSession.shopId ?: ""
    val isLogin: Boolean
        get() = userSession.isLoggedIn
    val userId: String
        get() = userSession.userId

    fun getShopPageHomeData(
            shopId: String,
            sortId: Int
    ) {
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
                    block = { getProductListData(shopId, sortId, 1) },
                    onError = { null }
            )
            shopLayoutWidget.await()?.let {
                _shopHomeLayoutData.postValue(Success(it))
                productList.await()?.let { productListData ->
                    _initialProductListData.postValue(Success(productListData))
                }
            }
        }) {
        }
    }

    fun getNewProductList(
            shopId: String,
            sortId: Int,
            page: Int
    ) {
        launchCatchError(block = {
            val listProductData = withContext(dispatcherProvider.io()) {
                getProductListData(shopId,sortId, page)
            }
            _newProductListData.postValue(Success(listProductData))
        }) {
            _newProductListData.postValue(Fail(it))
        }
    }

    fun addProductToCart(
            product: ShopHomeProductViewModel,
            shopId: String,
            onSuccessAddToCart: (dataModelAtc: DataModel) -> Unit,
            onErrorAddToCart: (exception: Throwable) -> Unit
    ) {
        launchCatchError(block = {
            val addToCartSubmitData = withContext(dispatcherProvider.io()) {
                submitAddProductToCart(shopId, product)
            }
            if (addToCartSubmitData.data.success == 1)
                onSuccessAddToCart(addToCartSubmitData.data)
            else
                onErrorAddToCart(MessageErrorException(addToCartSubmitData.data.message.first()))
        }) {
            onErrorAddToCart(it)
        }
    }

    fun clearGetShopProductUseCase() {
        getShopProductUseCase.clearCache()
    }

    fun getWishlistStatus(shopHomeCarousellProductUiModel: List<ShopHomeCarousellProductUiModel>) {
        launchCatchError(block = {
            val listResultCheckWishlist = shopHomeCarousellProductUiModel.map {
                asyncCatchError(
                        dispatcherProvider.io(),
                        block = {
                            val resultCheckWishlist = checkListProductWishlist(it)
                            Pair(it, resultCheckWishlist)
                        },
                        onError = { null }
                )
            }.awaitAll()
            _checkWishlistData.value = Success(listResultCheckWishlist)
        }) {}
    }

    private suspend fun getShopPageHomeLayout(shopId: String): ShopPageHomeLayoutUiModel {
        getShopPageHomeLayoutUseCase.params = GetShopPageHomeLayoutUseCase.createParams(shopId)
        return ShopPageHomeMapper.mapToShopPageHomeLayoutUiModel(
                getShopPageHomeLayoutUseCase.executeOnBackground(),
                ShopUtil.isMyShop(shopId, userSessionShopId)
        )
    }

    private suspend fun getProductListData(
            shopId: String,
            sortId: Int,
            page: Int
    ): Pair<Boolean, List<ShopHomeProductViewModel>> {
        getShopProductUseCase.params = GqlGetShopProductUseCase.createParams(
                shopId,
                ShopProductFilterInput().apply {
                    etalaseMenu = ALL_SHOWCASE_ID
                    this.page = page
                    sort = sortId
                }
        )
        val productListResponse = getShopProductUseCase.executeOnBackground()
        val isHasNextPage = ShopUtil.isHasNextPage(page, ShopPageConstant.DEFAULT_PER_PAGE, productListResponse.totalData)
        return Pair(
                isHasNextPage,
                productListResponse.data.map {
                    ShopPageHomeMapper.mapToHomeProductViewModelForAllProduct(
                            it,
                            ShopUtil.isMyShop(shopId, userSessionShopId)
                    )
                }
        )
    }

    private fun submitAddProductToCart(shopId: String, product: ShopHomeProductViewModel): AddToCartDataModel {
        val requestParams = AddToCartUseCase.getMinimumParams(product.id ?: "", shopId, productName = product.name ?: "", price = product.displayedPrice ?: "")
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