package com.tokopedia.play.domain.repository

/**
 * Created by jegul on 29/07/21
 */
interface PlayViewerCartRepository {

    suspend fun getItemCountInCart(): Int

    suspend fun addItemToCart(
            productId: String,
            productName: String,
            productShopId: String,
            price: String,
            qty: Int,
    ): Boolean
}