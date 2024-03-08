package com.tokopedia.product.detail.data.util

sealed class ProductDetailSwipeTrackingState
object ProductDetailAlreadyHit : ProductDetailSwipeTrackingState()
object ProductDetailAlreadySwipe : ProductDetailSwipeTrackingState()
