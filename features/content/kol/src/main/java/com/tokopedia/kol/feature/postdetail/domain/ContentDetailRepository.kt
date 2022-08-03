package com.tokopedia.kol.feature.postdetail.domain

import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response

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

    suspend fun addToWishlist(
        productId: String
    ): Result<AddToWishlistV2Response.Data.WishlistAddV2>
}