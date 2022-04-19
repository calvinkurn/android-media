package com.tokopedia.applink.internal

import com.tokopedia.applink.constant.DeeplinkConstant

/**
 * This class is used to store deeplink "tokopedia-android-internal://transaction".
 * Order by name
 * Only create "tokopedia-android-internal://" if this deeplink is used only for android app, and not shared to iOs and web.
 * If the deeplink is shared between iOS and web, it should use "tokopedia://" scheme.
 */
object ApplinkConstInternalPurchasePlatform {
    const val HOST_TRANSACTION = "transaction"

    const val INTERNAL_TRANSACTION = "${DeeplinkConstant.SCHEME_INTERNAL}://${HOST_TRANSACTION}"

    // WishlistV2Activity
    const val WISHLIST_V2 = "$INTERNAL_TRANSACTION/wishlist"
}