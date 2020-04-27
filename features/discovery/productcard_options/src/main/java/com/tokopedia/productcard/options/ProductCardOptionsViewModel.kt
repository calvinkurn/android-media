package com.tokopedia.productcard.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.model.ProductCardOptionsModel
import com.tokopedia.discovery.common.model.WishlistTrackingModel
import com.tokopedia.productcard.options.item.ProductCardOptionsItemModel
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.listener.WishListActionListener
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase

internal class ProductCardOptionsViewModel(
        dispatcherProvider: DispatcherProvider,
        val productCardOptionsModel: ProductCardOptionsModel?,
        private val addWishListUseCase: AddWishListUseCase,
        private val removeWishListUseCase: RemoveWishListUseCase,
        private val userSession: UserSessionInterface
): BaseViewModel(dispatcherProvider.ui()) {

    private val productCardOptionsItemListLiveData = MutableLiveData<List<Any>>()
    private val productCardOptionsItemList = mutableListOf<Any>()
    private val routeToSimilarProductsEventLiveData = MutableLiveData<Event<Boolean>>()
    private val closeProductCardOptionsEventLiveData = MutableLiveData<Event<Boolean>>()
    private val routeToLoginPageEventLiveData = MutableLiveData<Event<Boolean>>()
    private val wishlistEventLiveData = MutableLiveData<Event<Boolean>>()
    private val trackingWishlistEventLiveData = MutableLiveData<Event<WishlistTrackingModel>>()
    private val trackingSeeSimilarProductEventLiveData = MutableLiveData<Event<Boolean>>()

    init {
        initSeeSimilarProductsOption()
        initWishlistOption()

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
        if (!userSession.isLoggedIn) {
            trackingWishlistEventLiveData.postValue(Event(createWishlistTrackingModel(isAddWishlist)))
            routeToLoginPageEventLiveData.postValue(Event(true))
            closeProductCardOptionsEventLiveData.postValue(Event(true))
        } else {
            doWishlistAction(isAddWishlist)
        }
    }

    private fun createWishlistTrackingModel(isAddWishlist: Boolean): WishlistTrackingModel {
        return WishlistTrackingModel(
                isAddWishlist = isAddWishlist,
                productId = productCardOptionsModel?.productId ?: "",
                isTopAds = productCardOptionsModel?.isTopAds ?: false,
                keyword = productCardOptionsModel?.keyword ?: "",
                isUserLoggedIn = userSession.isLoggedIn
        )
    }

    private fun doWishlistAction(isAddWishlist: Boolean) {
        val wishListActionListener = createWishlistActionListener()

        if (isAddWishlist) {
            addWishlist(wishListActionListener)
        } else {
            removeWishlist(wishListActionListener)
        }
    }

    private fun createWishlistActionListener(): WishListActionListener {
        return object: WishListActionListener {
            override fun onSuccessRemoveWishlist(productId: String?) {
                onSuccessRemoveWishlist()
            }

            override fun onErrorRemoveWishlist(errorMessage: String?, productId: String?) {
                onErrorRemoveWishlist()
            }

            override fun onErrorAddWishList(errorMessage: String?, productId: String?) {
                onErrorAddWishlist()
            }

            override fun onSuccessAddWishlist(productId: String?) {
                onSuccessAddWishlist()
            }
        }
    }

    private fun onSuccessRemoveWishlist() {
        trackingWishlistEventLiveData.postValue(Event(createWishlistTrackingModel(false)))
        wishlistEventLiveData.postValue(Event(true))
        productCardOptionsModel?.wishlistResult = createWishlistResult(isSuccess = true, isAddWishlist = false)
    }

    private fun onErrorRemoveWishlist() {
        wishlistEventLiveData.postValue(Event(true))
        productCardOptionsModel?.wishlistResult = createWishlistResult(isSuccess = false, isAddWishlist = false)
    }

    private fun onErrorAddWishlist() {
        wishlistEventLiveData.postValue(Event(true))
        productCardOptionsModel?.wishlistResult = createWishlistResult(isSuccess = false, isAddWishlist = true)
    }

    private fun onSuccessAddWishlist() {
        trackingWishlistEventLiveData.postValue(Event(createWishlistTrackingModel(true)))
        wishlistEventLiveData.postValue(Event(true))
        productCardOptionsModel?.wishlistResult = createWishlistResult(isSuccess = true, isAddWishlist = true)
    }

    private fun createWishlistResult(isSuccess: Boolean, isAddWishlist: Boolean): ProductCardOptionsModel.WishlistResult {
        return ProductCardOptionsModel.WishlistResult(
                isSuccess = isSuccess,
                isAddWishlist = isAddWishlist
        )
    }

    private fun addWishlist(wishListActionListener: WishListActionListener) {
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

    private fun catchAddWishlistError(throwable: Throwable) {
        throwable.printStackTrace()
        onErrorAddWishlist()
    }

    private fun getProductId() = productCardOptionsModel?.productId ?: "0"

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

    private fun catchRemoveWishlistError(throwable: Throwable) {
        throwable.printStackTrace()
        onErrorRemoveWishlist()
    }

    private fun postOptionListLiveData() {
        productCardOptionsItemListLiveData.postValue(productCardOptionsItemList)
    }

    fun getOptionsListLiveData(): LiveData<List<Any>> = productCardOptionsItemListLiveData

    fun getRouteToSimilarSearchEventLiveData(): LiveData<Event<Boolean>> = routeToSimilarProductsEventLiveData

    fun getCloseProductCardOptionsEventLiveData(): LiveData<Event<Boolean>> = closeProductCardOptionsEventLiveData

    fun getRouteToLoginPageEventLiveData(): LiveData<Event<Boolean>> = routeToLoginPageEventLiveData

    fun getWishlistEventLiveData(): LiveData<Event<Boolean>> = wishlistEventLiveData

    fun getTrackingWishlistEventLiveData(): LiveData<Event<WishlistTrackingModel>> = trackingWishlistEventLiveData

    fun getTrackingSeeSimilarProductEventLiveData(): LiveData<Event<Boolean>> = trackingSeeSimilarProductEventLiveData
}