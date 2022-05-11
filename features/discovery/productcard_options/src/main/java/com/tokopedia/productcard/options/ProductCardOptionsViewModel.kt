package com.tokopedia.productcard.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.atc_common.data.model.request.AddToCartRequestParams
import com.tokopedia.atc_common.domain.model.response.AddToCartDataModel
import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase.Companion.REQUEST_PARAM_KEY_ADD_TO_CART_REQUEST
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.WishlistResult
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.product.share.ProductData
import com.tokopedia.productcard.options.divider.ProductCardOptionsItemDivider
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import rx.Subscriber

internal class ProductCardOptionsViewModel(
        dispatcherProvider: CoroutineDispatchers,
        val productCardOptionsModel: ProductCardOptionsModel?,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
        private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
        private val topAdsWishlistUseCase: UseCase<Boolean>,
        private val addToCartUseCase: UseCase<AddToCartDataModel>,
        private val userSession: UserSessionInterface,
        private val isUsingWishlistV2: Boolean
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
            if (isUsingWishlistV2) doWishlistActionV2(isAddWishlist)
            else doWishlistAction(isAddWishlist)
        }
        else rejectWishlistAction()
    }

    private fun rejectWishlistAction() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = false)
        wishlistEventLiveData.postValue(Event(true))
        closeProductCardOptionsEventLiveData.postValue(Event(true))
    }

    private fun createWishlistActionListener(): WishListActionListener {
        return object: WishListActionListener {
            override fun onSuccessRemoveWishlist(productId: String?) = onSuccessRemoveWishlist()

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) = onErrorRemoveWishlist()

            override fun onErrorAddWishList(errorMessage: String?, productId: String?) = onErrorAddWishlist()

            override fun onSuccessAddWishlist(productId: String?) = onSuccessAddWishlist()
        }
    }

    private fun doWishlistAction(isAddWishlist: Boolean) {
        val wishListActionListener = createWishlistActionListener()
        postLoadingEvent()

        if (!isAddWishlist) removeWishlist(wishListActionListener)
        else addWishlist(wishListActionListener)
    }

    private fun doWishlistActionV2(isAddWishlist: Boolean) {
        postLoadingEvent()

        if (!isAddWishlist) removeWishlistV2()
        else addWishlistV2()
    }

    private fun onSuccessRemoveWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = false, isUsingWishlistV2 = isUsingWishlistV2)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onSuccessRemoveWishlistV2(result: DeleteWishlistV2Response.Data.WishlistRemoveV2) {
        productCardOptionsModel?.wishlistResult = WishlistResult(
            isUserLoggedIn = true,
            isSuccess = result.success,
            isAddWishlist = false,
            isUsingWishlistV2 = isUsingWishlistV2,
            messageV2 = result.message,
            toasterColorV2 = result.toasterColor,
            ctaTextV2 = result.button.text,
            ctaActionV2 = result.button.action)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onErrorRemoveWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = false, isAddWishlist = false, isUsingWishlistV2 = isUsingWishlistV2)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onErrorAddWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = false, isAddWishlist = true, isUsingWishlistV2 = isUsingWishlistV2)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onSuccessAddWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = true, isUsingWishlistV2 = isUsingWishlistV2)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onSuccessAddWishlistV2(resultWishlistV2: AddToWishlistV2Response.Data.WishlistAddV2) {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true,
            isSuccess = resultWishlistV2.success, isAddWishlist = true,
            isUsingWishlistV2 = isUsingWishlistV2, messageV2 = resultWishlistV2.message,
            toasterColorV2 = resultWishlistV2.toasterColor,
            ctaTextV2 = resultWishlistV2.button.text,
            ctaActionV2 = resultWishlistV2.button.action)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun postLoadingEvent() {
        isLoadingEventLiveData.postValue(Event(true))
    }

    private fun removeWishlistV2() {
        try {
            tryRemoveWishlistV2()
        }
        catch(throwable: Throwable) {
            catchRemoveWishlistError(throwable)
        }
    }

    private fun removeWishlist(wishListActionListener: WishListActionListener) {
        try {
            tryRemoveWishlist(wishListActionListener)
        }
        catch(throwable: Throwable) {
            catchRemoveWishlistError(throwable)
        }
    }

    private fun tryRemoveWishlist(wishListActionListener: WishListActionListener) {
        removeWishListUseCase.unsubscribe()
        removeWishListUseCase.createObservable(getProductId(), userSession.userId, wishListActionListener)
    }

    private fun tryRemoveWishlistV2() {
        deleteWishlistV2UseCase.setParams(getProductId(), userSession.userId)
        deleteWishlistV2UseCase.execute(
            onSuccess = { result ->
                if (result is Success) {
                    onSuccessRemoveWishlistV2(result.data)
                } },
            onError = { onErrorRemoveWishlist() })
    }

    private fun catchRemoveWishlistError(throwable: Throwable?) {
        throwable?.printStackTrace()
        onErrorRemoveWishlist()
    }

    private fun addWishlist(wishListActionListener: WishListActionListener) {
        if (productCardOptionsModel?.isTopAds == true) {
            addWishlistTopAds()
        }
        else {
            addWishlistNonTopAds(wishListActionListener)
        }
    }

    private fun addWishlistV2() {
        if (productCardOptionsModel?.isTopAds == true) {
            addWishlistTopAds()
        }
        else {
            addWishlistNonTopAdsV2()
        }
    }

    private fun addWishlistTopAds() {
        topAdsWishlistUseCase.execute(
                createAddWishlistTopAdsRequestParams(),
                createAddWishlistTopAdsSubscriber()
        )
    }

    private fun createAddWishlistTopAdsRequestParams() = RequestParams.create().also {
        it.putString(TopAdsWishlishedUseCase.WISHSLIST_URL, productCardOptionsModel?.topAdsWishlistUrl)
    }

    private fun createAddWishlistTopAdsSubscriber() = object: Subscriber<Boolean>() {
        override fun onNext(t: Boolean?) = onSuccessAddWishlist()

        override fun onError(e: Throwable?) = catchAddWishlistError(e)

        override fun onCompleted() { }
    }

    private fun addWishlistNonTopAdsV2() {
        try {
            tryAddWishlistV2()
        }
        catch(throwable: Throwable) {
            catchAddWishlistError(throwable)
        }
    }

    private fun addWishlistNonTopAds(wishListActionListener: WishListActionListener) {
        try {
            tryAddWishlist(wishListActionListener)
        }
        catch(throwable: Throwable) {
            catchAddWishlistError(throwable)
        }
    }

    private fun tryAddWishlist(wishListActionListener: WishListActionListener) {
        addWishListUseCase.unsubscribe()
        addWishListUseCase.createObservable(getProductId(), userSession.userId, wishListActionListener)
    }

    private fun tryAddWishlistV2() {
        addToWishlistV2UseCase.setParams(getProductId(), userSession.userId)
        addToWishlistV2UseCase.execute(
            onSuccess = { result ->
                if (result is Success) {
                    onSuccessAddWishlistV2(result.data)
                } },
            onError = {
                onErrorAddWishlist()
            })
    }

    private fun catchAddWishlistError(throwable: Throwable?) {
        throwable?.printStackTrace()
        onErrorAddWishlist()
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
                            productId = productCardOptionsModel.productId.toLongOrZero(),
                            shopId = productCardOptionsModel.shopId.toIntOrZero(),
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
}