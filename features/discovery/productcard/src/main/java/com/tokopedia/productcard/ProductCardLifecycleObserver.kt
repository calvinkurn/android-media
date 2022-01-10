package com.tokopedia.productcard

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class ProductCardLifecycleObserver: LifecycleObserver {

    private val productCardPairs = mutableMapOf<IProductCardView, ProductCardModel>()

    fun register(productCardView: IProductCardView, productCardModel: ProductCardModel) {
        productCardPairs[productCardView] = productCardModel
    }

    fun unregister(productCardView: IProductCardView) {
        productCardPairs.remove(productCardView)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun start() {
        productCardPairs.forEach { it.key.setProductModel(it.value) }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun stop() {
        productCardPairs.forEach { it.key.recycle() }
    }
}