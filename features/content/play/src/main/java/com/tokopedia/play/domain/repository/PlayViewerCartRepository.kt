package com.tokopedia.play.domain.repository

import com.tokopedia.play.data.CartFeedbackResponseModel

/**
 * Created by jegul on 29/07/21
 */
interface PlayViewerCartRepository {

    suspend fun addItemToCart(
            productId: String,
            productName: String,
            productShopId: String,
            price: String,
            qty: Int,
    ): CartFeedbackResponseModel
}