package com.tokopedia.applink.purchaseplatform

import android.content.Context
import com.tokopedia.applink.internal.ApplinkConstInternalPurchasePlatform

object DeeplinkMapperWishlist {
    fun getRegisteredNavigationWishlist(context: Context): String {
        return ApplinkConstInternalPurchasePlatform.WISHLIST_COLLECTION
    }
}
