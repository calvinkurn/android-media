package com.tokopedia.productcard.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase.Companion.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.WishlistResult
import com.tokopedia.discovery.common.utils.SimilarSearchCoachMarkLocalCache
import com.tokopedia.product.share.ProductData
import com.tokopedia.productcard.options.divider.ProductCardOptionsItemDivider
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RollenceKey
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import dagger.Lazy
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rx.Subscriber

internal class ProductCardOptionsViewModel(
    private val dispatcherProvider: CoroutineDispatchers,
    val productCardOptionsModel: ProductCardOptionsModel?,
    private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
    private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
    private val addToCartUseCase: UseCase<AddToCartDataModel>,
    private val userSession: UserSessionInterface,
    private val similarSearchCoachMarkLocalCache: SimilarSearchCoachMarkLocalCache,
    private val abTestRemoteConfig: Lazy<RemoteConfig>,
): BaseViewModel(dispatcherProvider.main) {

    private val productCardOptionsItemListLiveData = MutableLiveData<List<Visitable<*>>>()
    private val productCardOptionsItemList = mutableListOf<Visitable<*>>()
    private val routeToSimilarProductsEventLiveData = MutableLiveData<Event<Boolean>>()
    private val closeProductCardOptionsEventLiveData = MutableLiveData<Event<Boolean>>()
    private val wishlistEventLiveData = MutableLiveData<Event<Boolean>>()
    private val trackingSeeSimilarProductEventLiveData = MutableLiveData<Event<Boolean>>()
    private val addToCartEventLiveData = MutableLiveData<Event<Boolean>>()
    private val routeToShopPageEventLiveData = MutableLiveData<Event<Boolean>>()
    private val shareProductEventLiveData = MutableLiveData<Event<ProductData>>()
    private val isLoadingEventLiveData = MutableLiveData<Event<Boolean>>()
    private val coachmarkEventLiveData = MutableLiveData<Event<CoachmarkEvent>>()

    private val isEnabledRollence by lazy(LazyThreadSafetyMode.NONE, ::getIsEnabledRollence)

    private fun getIsEnabledRollence() = try {
        abTestRemoteConfig.get()
            .getString(
                RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK,
                ""
            ) == RollenceKey.SEARCH_SIMILAR_SEARCH_COACHMARK_VARIANT
    } catch (ignored: Throwable) {
        false
    }

    init {
        initWishlistOption()
        initAddToCartOption()
        initVisitShopOption()
        initShareProductOption()
        initSeeSimilarProductsOption()

        postOptionListLiveData()
    }

    private fun MutableList<Visitable<*>>.addOption(title: String, onClick: () -> Unit) {
        this.add(ProductCardOptionsItemModel(title, onClick))
    }

    private fun MutableList<Visitable<*>>.addDivider() {
        this.add(ProductCardOptionsItemDivider())
    }

    private fun postOptionListLiveData() {
        productCardOptionsItemListLiveData.postValue(productCardOptionsItemList)
    }

    private fun initWishlistOption() {
        if (productCardOptionsModel?.hasWishlist == true) {
            productCardOptionsItemList.addWishlistOptions(productCardOptionsModel.isWishlisted)
            productCardOptionsItemList.addDivider()
        }
    }

    private fun MutableList<Visitable<*>>.addWishlistOptions(isWishlisted: Boolean) {
        if (!isWishlisted) {
            this.addOption(SAVE_TO_WISHLIST) { tryToggleWishlist(true) }
        } else {
            this.addOption(DELETE_FROM_WISHLIST) { tryToggleWishlist(false) }
        }
    }

    private fun tryToggleWishlist(isAddWishlist: Boolean) {
        if (userSession.isLoggedIn) {
            doWishlistActionV2(isAddWishlist)
        }
        else rejectWishlistAction()
    }

    private fun rejectWishlistAction() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = false)
        wishlistEventLiveData.postValue(Event(true))
        closeProductCardOptionsEventLiveData.postValue(Event(true))
    }

    private fun doWishlistActionV2(isAddWishlist: Boolean) {
        postLoadingEvent()

        if (!isAddWishlist) removeWishlistV2()
        else addWishlistV2()
    }

    private fun onSuccessRemoveWishlistV2(result: DeleteWishlistV2Response.Data.WishlistRemoveV2) {
        productCardOptionsModel?.wishlistResult = WishlistResult(
            isUserLoggedIn = true,
            isSuccess = result.success,
            isAddWishlist = false,
            messageV2 = result.message,
            toasterColorV2 = result.toasterColor,
            ctaTextV2 = result.button.text,
            ctaActionV2 = result.button.action)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onErrorRemoveWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(
            isUserLoggedIn = true,
            isSuccess = false,
            isAddWishlist = false)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onErrorAddWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(
            isUserLoggedIn = true,
            isSuccess = false,
            isAddWishlist = true)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onSuccessAddWishlistV2(resultWishlistV2: AddToWishlistV2Response.Data.WishlistAddV2) {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true,
            isSuccess = resultWishlistV2.success,
            isAddWishlist = true,
            messageV2 = resultWishlistV2.message,
            toasterColorV2 = resultWishlistV2.toasterColor,
            ctaTextV2 = resultWishlistV2.button.text,
            ctaActionV2 = resultWishlistV2.button.action)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun postLoadingEvent() {
        isLoadingEventLiveData.postValue(Event(true))
    }

    private fun removeWishlistV2() {
        launch(dispatcherProvider.main) {
            deleteWishlistV2UseCase.setParams(getProductId(), userSession.userId)
            val result = withContext(dispatcherProvider.io) { deleteWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                onSuccessRemoveWishlistV2(result.data)
            } else if (result is Fail) {
                onErrorRemoveWishlist()
            }
        }
    }

    private fun addWishlistV2() {
        launch(dispatcherProvider.main) {
            addToWishlistV2UseCase.setParams(getProductId(), userSession.userId)
            val result = withContext(dispatcherProvider.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                onSuccessAddWishlistV2(result.data)
            } else if (result is Fail) {
                onErrorAddWishlist()
            }
        }
    }

    private fun getProductId() = productCardOptionsModel?.productId ?: "0"

    private fun initAddToCartOption() {
        if (productCardOptionsModel?.canAddToCart() == true) {
            productCardOptionsItemList.addOption(ADD_TO_CART) { executeAddToCart() }
            productCardOptionsItemList.addDivider()
        }
    }

    private fun executeAddToCart() {
        if (userSession.isLoggedIn) {
            postLoadingEvent()

            addToCartUseCase.unsubscribe()
            addToCartUseCase.execute(createAddToCartRequestParams(), createAddToCartSubscriber())
        }
        else {
            addToCartEventLiveData.postValue(Event(true))
        }
    }

    private fun createAddToCartRequestParams(): RequestParams {
        val requestParams = RequestParams.create()

        productCardOptionsModel?.let { productCardOptionsModel ->
            requestParams.putObject(
                    REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST,
                    AddToCartRequestParams(
                            productId = productCardOptionsModel.productId,
                            shopId = productCardOptionsModel.shopId,
                            quantity = productCardOptionsModel.addToCartParams?.quantity ?: 0,
                            productName = productCardOptionsModel.productName,
                            category = productCardOptionsModel.categoryName,
                            price = productCardOptionsModel.formattedPrice,
                            userId = userSession.userId
                    )
            )
        }

        return requestParams
    }

    private fun createAddToCartSubscriber(): Subscriber<AddToCartDataModel> {
        return object : Subscriber<AddToCartDataModel>() {
            override fun onNext(addToCartDataModel: AddToCartDataModel?) {
                processAddToCartUseCaseSuccess(addToCartDataModel)
            }

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                processAddToCartUseCaseFailed()
            }
        }
    }

    private fun processAddToCartUseCaseSuccess(addToCartDataModel: AddToCartDataModel?) {
        addToCartEventLiveData.postValue(Event(true))

        if (isAddToCartStatusOK(addToCartDataModel)) {
            productCardOptionsModel?.addToCartResult = ProductCardOptionsModel.AddToCartResult(
                    isUserLoggedIn = true, isSuccess = true, cartId = addToCartDataModel?.data?.cartId ?: ""
            )
        } else {
            val errorMessage = addToCartDataModel?.getAtcErrorMessage()
            productCardOptionsModel?.addToCartResult = ProductCardOptionsModel.AddToCartResult(
                    isUserLoggedIn = true, isSuccess = false, errorMessage = errorMessage ?: ""
            )
        }
    }

    private fun isAddToCartStatusOK(addToCartDataModel: AddToCartDataModel?): Boolean {
        return addToCartDataModel?.status == AddToCartDataModel.STATUS_OK
                && addToCartDataModel.data.success == 1
    }

    private fun processAddToCartUseCaseFailed() {
        addToCartEventLiveData.postValue(Event(true))
        productCardOptionsModel?.addToCartResult = ProductCardOptionsModel.AddToCartResult(
                isUserLoggedIn = true, isSuccess = false, errorMessage = ATC_DEFAULT_ERROR_MESSAGE
        )
    }

    private fun initVisitShopOption() {
        if (productCardOptionsModel?.canVisitShop() == true) {
            productCardOptionsItemList.addOption(VISIT_SHOP) {
                routeToShopPageEventLiveData.postValue(Event(true))
            }

            productCardOptionsItemList.addDivider()
        }
    }

    private fun initShareProductOption() {
        if (productCardOptionsModel?.canShareProduct() == true) {
            productCardOptionsItemList.addOption(SHARE_PRODUCT) {
                shareProductEventLiveData.postValue(Event(ProductData(
                        productId = productCardOptionsModel.productId,
                        productName = productCardOptionsModel.productName,
                        priceText = productCardOptionsModel.formattedPrice,
                        productImageUrl = productCardOptionsModel.productImageUrl,
                        productUrl = productCardOptionsModel.productUrl,
                        shopName = productCardOptionsModel.shopName,
                        shopUrl = productCardOptionsModel.shopUrl
                )))
            }
            productCardOptionsItemList.addDivider()
        }
    }

    private fun initSeeSimilarProductsOption() {
        if (productCardOptionsModel?.hasSimilarSearch == true) {
            productCardOptionsItemList.addOption(SEE_SIMILAR_PRODUCTS) { onSeeSimilarProductsOptionClicked() }
            productCardOptionsItemList.addDivider()
        }
    }

    private fun onSeeSimilarProductsOptionClicked() {
        trackingSeeSimilarProductEventLiveData.postValue(Event(true))
        routeToSimilarProductsEventLiveData.postValue(Event(true))
        closeProductCardOptionsEventLiveData.postValue(Event(true))
    }

    fun getOptionsListLiveData(): LiveData<List<Visitable<*>>> = productCardOptionsItemListLiveData

    fun getRouteToSimilarSearchEventLiveData(): LiveData<Event<Boolean>> = routeToSimilarProductsEventLiveData

    fun getCloseProductCardOptionsEventLiveData(): LiveData<Event<Boolean>> = closeProductCardOptionsEventLiveData

    fun getWishlistEventLiveData(): LiveData<Event<Boolean>> = wishlistEventLiveData

    fun getTrackingSeeSimilarProductEventLiveData(): LiveData<Event<Boolean>> = trackingSeeSimilarProductEventLiveData

    fun getAddToCartEventLiveData(): LiveData<Event<Boolean>> = addToCartEventLiveData

    fun getRouteToShopPageEventLiveData(): LiveData<Event<Boolean>> = routeToShopPageEventLiveData

    fun getShareProductEventLiveData(): LiveData<Event<ProductData>> = shareProductEventLiveData

    fun getIsLoadingEventLiveData(): LiveData<Event<Boolean>> = isLoadingEventLiveData

    fun getCoachmarkEventLiveData(): LiveData<Event<CoachmarkEvent>> = coachmarkEventLiveData

    fun checkShouldDisplaySimilarSearchCoachmark(
        option: ProductCardOptionsItemModel,
        adapterPosition: Int,
    ) {
        if (option.title == SEE_SIMILAR_PRODUCTS && isEnabledRollence) {
            if (similarSearchCoachMarkLocalCache.shouldShowSimilarSearchProductOptionCoachmark()) {
                coachmarkEventLiveData.postValue(Event(CoachmarkEvent(adapterPosition)))
            }
        }
    }
}
