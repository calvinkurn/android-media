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
import com.tokopedia.play_common.domain.model.PlayToggleChannelReminder
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase.Companion.SHOP_AUTHOR_TYPE
import com.tokopedia.play_common.domain.usecases.GetPlayWidgetUseCase.Companion.SHOP_WIDGET_TYPE
import com.tokopedia.play_common.domain.usecases.PlayToggleChannelReminderUseCase
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.domain.interactor.GQLCheckWishlistUseCase
import com.tokopedia.shop.common.graphql.data.checkwishlist.CheckWishlistResult
import com.tokopedia.shop.common.util.ShopUtil
import com.tokopedia.shop.home.domain.GetShopPageHomeLayoutUseCase
import com.tokopedia.shop.home.util.CoroutineDispatcherProvider
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomePlayCarouselUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel
import com.tokopedia.shop.home.view.model.ShopPageHomeLayoutUiModel
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductFilterInput
import com.tokopedia.shop.product.domain.interactor.GqlGetShopProductUseCase
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
        private val getPlayWidgetUseCase: GetPlayWidgetUseCase,
        private val playToggleChannelReminderUseCase: PlayToggleChannelReminderUseCase,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishlistUseCase: RemoveWishListUseCase,
        private val gqlCheckWishlistUseCase: Provider<GQLCheckWishlistUseCase>
) : BaseViewModel(dispatcherProvider.main()) {

    companion object {
        const val ALL_SHOWCASE_ID = "etalase"
    }
    private var shopId: String = ""

    val productListData: LiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>
        get() = _productListData
    private val _productListData = MutableLiveData<Result<Pair<Boolean, List<ShopHomeProductViewModel>>>>()

    val shopHomeLayoutData: LiveData<Result<ShopPageHomeLayoutUiModel>>
        get() = _shopHomeLayoutData
    private val _shopHomeLayoutData = MutableLiveData<Result<ShopPageHomeLayoutUiModel>>()

    val checkWishlistData: LiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>>>>
        get() = _checkWishlistData
    private val _checkWishlistData = MutableLiveData<Result<List<Pair<ShopHomeCarousellProductUiModel, List<CheckWishlistResult>>>>>()

    val reminderPlayLiveData: LiveData<Pair<Int, Result<Boolean>>> get() = _reminderPlayLiveData
    private val _reminderPlayLiveData = MutableLiveData<Pair<Int, Result<Boolean>>>()

    val userSessionShopId: String
        get() = userSession.shopId ?: ""
    val isLogin: Boolean
        get() = userSession.isLoggedIn
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

                val newShopPageHomeLayoutUiModel = asyncCatchError(
                        dispatcherProvider.io(),
                        block = { getPlayWidgetCarousel(shopId, it) },
                        onError = {null}
                )

                newShopPageHomeLayoutUiModel.await()?.let { newShopPageHomeLayoutUiModelData ->
                    _shopHomeLayoutData.postValue(Success(newShopPageHomeLayoutUiModelData))
                }
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

    fun onRefreshPlayBanner(shopId: String){
        val result = _shopHomeLayoutData.value
        if(result is Success){
            launchCatchError(block = {
                val newShopPageHomeLayoutUiModel = asyncCatchError(
                        dispatcherProvider.io(),
                        block = { getPlayWidgetCarousel(shopId, result.data) },
                        onError = {null}
                )
                newShopPageHomeLayoutUiModel.await()?.let {
                    _shopHomeLayoutData.postValue(Success(it))
                }
            }){
            }
        }
    }

    fun setToggleReminderPlayBanner(channelId: String, isSet: Boolean, position: Int){
        launchCatchError(block = {
            playToggleChannelReminderUseCase.setParams(channelId, isSet)
            val reminder = playToggleChannelReminderUseCase.executeOnBackground()
            if(reminder.header.status == PlayToggleChannelReminder.SUCCESS_STATUS){
                _reminderPlayLiveData.postValue(Pair(position, Success(isSet)))
            } else {
                _reminderPlayLiveData.postValue(Pair(position, Fail(Throwable(reminder.header.message))))
            }
        }){
            _reminderPlayLiveData.postValue(Pair(position, Fail(it)))
        }
    }

    private suspend fun getPlayWidgetCarousel(shopId: String, shopPageHomeLayoutUiModel: ShopPageHomeLayoutUiModel): ShopPageHomeLayoutUiModel?{
        getPlayWidgetUseCase.setParams(SHOP_WIDGET_TYPE, shopId, SHOP_AUTHOR_TYPE)
        val playBannerDataModel = getPlayWidgetUseCase.executeOnBackground()
        shopPageHomeLayoutUiModel.listWidget.withIndex().find { (_, data) -> data is ShopHomePlayCarouselUiModel }?.let { (index, uiModel) ->
            val newHomeLayout = shopPageHomeLayoutUiModel.listWidget.toMutableList()
            if(playBannerDataModel.channelList.isNotEmpty()){
                newHomeLayout[index] = (uiModel as ShopHomePlayCarouselUiModel).copy(playBannerCarouselDataModel = playBannerDataModel)
            }else{
                newHomeLayout.remove(uiModel)
            }
            return shopPageHomeLayoutUiModel.copy(
                    listWidget = newHomeLayout
            )
        }
        return null
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
                ShopUtil.isMyShop(shopId, userSessionShopId)
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