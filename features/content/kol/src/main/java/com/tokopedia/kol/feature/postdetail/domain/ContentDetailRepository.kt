package com.tokopedia.kol.feature.postdetail.domain

/**
 * Created by meyta.taliti on 02/08/22.
 */
interface ContentDetailRepository {

    suspend fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String,
    ): Boolean
}