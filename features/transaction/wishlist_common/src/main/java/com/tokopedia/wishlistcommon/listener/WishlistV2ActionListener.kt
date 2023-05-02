package com.tokopedia.wishlistcommon.listener

import com.tokopedia.wishlistcommon.data.response.AddToWishlistV2Response
import com.tokopedia.wishlistcommon.data.response.DeleteWishlistV2Response

interface WishlistV2ActionListener {
    fun onErrorAddWishList(throwable: Throwable, productId: String)
    fun onSuccessAddWishlist(result: AddToWishlistV2Response.Data.WishlistAddV2, productId: String)
    fun onErrorRemoveWishlist(throwable: Throwable, productId: String)
    fun onSuccessRemoveWishlist(result: DeleteWishlistV2Response.Data.WishlistRemoveV2, productId: String)
}
