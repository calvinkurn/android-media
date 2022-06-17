package com.tokopedia.wishlist.util

import android.content.res.Resources

object WishlistV2Utils {
    fun toDp(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
}