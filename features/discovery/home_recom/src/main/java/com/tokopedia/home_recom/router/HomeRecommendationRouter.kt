package com.tokopedia.home_recom.router

import rx.Observable

interface HomeRecommendationRouter {
    fun getNormalCheckoutIntent(productId: Int, quantity: Int, shopId: Int, isOneClickShipment: Boolean): Observable<Map<String, Any>>
}