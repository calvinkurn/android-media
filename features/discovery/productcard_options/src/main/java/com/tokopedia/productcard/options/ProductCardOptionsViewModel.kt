package com.tokopedia.productcard.options

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.discovery.common.DispatcherProvider
import com.tokopedia.discovery.common.Event
import com.tokopedia.discovery.common.model.ProductCardOptionsModel

internal class ProductCardOptionsViewModel(
        dispatcherProvider: DispatcherProvider,
        val productCardOptionsModel: ProductCardOptionsModel?
): BaseViewModel(dispatcherProvider.ui()) {

    private val productCardOptionsItemListLiveData = MutableLiveData<List<Any>>()
    private val productCardOptionsItemList = mutableListOf<Any>()
    private val routeToSimilarProductsEventLiveData = MutableLiveData<Event<Boolean>>()
    private val closeProductCardOptionsEventLiveData = MutableLiveData<Event<Boolean>>()

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
            this.addOption(SAVE_TO_WISHLIST) { }
        } else {
            this.addOption(DELETE_FROM_WISHLIST) { }
        }
    }

    private fun postOptionListLiveData() {
        productCardOptionsItemListLiveData.postValue(productCardOptionsItemList)
    }

    fun getOptionsListLiveData(): LiveData<List<Any>> = productCardOptionsItemListLiveData

    fun getRouteToSimilarSearchEventLiveData(): LiveData<Event<Boolean>> = routeToSimilarProductsEventLiveData

    fun getCloseProductCardOptionsEventLiveData(): LiveData<Event<Boolean>> = closeProductCardOptionsEventLiveData
}