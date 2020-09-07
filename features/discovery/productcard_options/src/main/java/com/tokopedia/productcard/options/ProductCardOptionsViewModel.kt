package com.tokopedia.productcard.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.ProductCardOptionsModel.WishlistResult
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.topads.sdk.domain.interactor.TopAdsWishlishedUseCase
import com.tokopedia.usecase.RequestParams
import com.tokopedia.usecase.UseCase
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import rx.Subscriber

internal class ProductCardOptionsViewModel(
        dispatcherProvider: DispatcherProvider,
        val productCardOptionsModel: ProductCardOptionsModel?,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val topAdsWishlistUseCase: UseCase<Boolean>,
        private val userSession: UserSessionInterface
): BaseViewModel(dispatcherProvider.ui()) {

    private val productCardOptionsItemListLiveData = MutableLiveData<List<Any>>()
    private val productCardOptionsItemList = mutableListOf<Any>()
    private val routeToSimilarProductsEventLiveData = MutableLiveData<Event<Boolean>>()
    private val closeProductCardOptionsEventLiveData = MutableLiveData<Event<Boolean>>()
    private val wishlistEventLiveData = MutableLiveData<Event<Boolean>>()
    private val trackingSeeSimilarProductEventLiveData = MutableLiveData<Event<Boolean>>()
    private val routeToLoginPageEventLiveData = MutableLiveData<Event<Boolean>>()

    init {
        initSeeSimilarProductsOption()
        initWishlistOption()
        initAddToCartOption()

        postOptionListLiveData()
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

    private fun MutableList<Any>.addOption(title: String, onClick: () -> Unit) {
        this.add(ProductCardOptionsItemModel(title, onClick))
    }

    private fun MutableList<Any>.addDivider() {
        this.add(ProductCardOptionsItemDivider())
    }

    private fun initWishlistOption() {
        if (productCardOptionsModel?.hasWishlist == true) {
            productCardOptionsItemList.addWishlistOptions(productCardOptionsModel.isWishlisted)
        }
    }

    private fun MutableList<Any>.addWishlistOptions(isWishlisted: Boolean) {
        if (!isWishlisted) {
            this.addOption(SAVE_TO_WISHLIST) { tryToggleWishlist(true) }
        } else {
            this.addOption(DELETE_FROM_WISHLIST) { tryToggleWishlist(false) }
        }
    }

    private fun tryToggleWishlist(isAddWishlist: Boolean) {
        if (userSession.isLoggedIn) doWishlistAction(isAddWishlist)
        else rejectWishlistAction()
    }

    private fun rejectWishlistAction() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = false)
        wishlistEventLiveData.postValue(Event(true))
        closeProductCardOptionsEventLiveData.postValue(Event(true))
    }

    private fun doWishlistAction(isAddWishlist: Boolean) {
        val wishListActionListener = createWishlistActionListener()

        if (!isAddWishlist) removeWishlist(wishListActionListener)
        else addWishlist(wishListActionListener)
    }

    private fun createWishlistActionListener(): WishListActionListener {
        return object: WishListActionListener {
            override fun onSuccessRemoveWishlist(productId: String?) = onSuccessRemoveWishlist()

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) = onErrorRemoveWishlist()

            override fun onErrorAddWishList(errorMessage: String?, productId: String?) = onErrorAddWishlist()

            override fun onSuccessAddWishlist(productId: String?) = onSuccessAddWishlist()
        }
    }

    private fun onSuccessRemoveWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = false)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onErrorRemoveWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = false, isAddWishlist = false)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onErrorAddWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = false, isAddWishlist = true)
        wishlistEventLiveData.postValue(Event(true))
    }

    private fun onSuccessAddWishlist() {
        productCardOptionsModel?.wishlistResult = WishlistResult(isUserLoggedIn = true, isSuccess = true, isAddWishlist = true)
        wishlistEventLiveData.postValue(Event(true))
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

    private fun catchAddWishlistError(throwable: Throwable?) {
        throwable?.printStackTrace()
        onErrorAddWishlist()
    }

    private fun getProductId() = productCardOptionsModel?.productId ?: "0"

    private fun initAddToCartOption() {
        if (productCardOptionsModel?.canAddToCart() == true) {
            productCardOptionsItemList.addOption(ADD_TO_CART) {
                routeToLoginPageEventLiveData.postValue(Event(true))
            }
            productCardOptionsItemList.addDivider()
        }
    }

    private fun postOptionListLiveData() {
        productCardOptionsItemListLiveData.postValue(productCardOptionsItemList)
    }

    fun getOptionsListLiveData(): LiveData<List<Any>> = productCardOptionsItemListLiveData

    fun getRouteToSimilarSearchEventLiveData(): LiveData<Event<Boolean>> = routeToSimilarProductsEventLiveData

    fun getCloseProductCardOptionsEventLiveData(): LiveData<Event<Boolean>> = closeProductCardOptionsEventLiveData

    fun getWishlistEventLiveData(): LiveData<Event<Boolean>> = wishlistEventLiveData

    fun getTrackingSeeSimilarProductEventLiveData(): LiveData<Event<Boolean>> = trackingSeeSimilarProductEventLiveData

    fun routeToLoginPageEventLiveData(): LiveData<Event<Boolean>> {
        return routeToLoginPageEventLiveData
    }
}