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

    // Wishlist Collection
    const val WISHLIST_COLLECTION = "$INTERNAL_TRANSACTION/wishlist-collection"
    const val WISHLIST_COLLECTION_BOTTOMSHEET = "$INTERNAL_TRANSACTION/wishlist-collection-bottomsheet"
    const val WISHLIST_COLLECTION_DETAIL_INTERNAL = "$INTERNAL_TRANSACTION/wishlist/collection/{collection_id}/"
    const val PATH_PRODUCT_ID = "productId"
    const val PATH_COLLECTION_ID = "collectionId"
    const val PATH_SRC = "src"
    const val BOOLEAN_EXTRA_NEED_REFRESH = "needRefresh"
    const val BOOLEAN_EXTRA_SUCCESS = "success"
    const val STRING_EXTRA_MESSAGE_TOASTER = "messageToaster"
    const val STRING_EXTRA_COLLECTION_ID = "collectionId"
    const val REQUEST_CODE_ADD_WISHLIST_COLLECTION = 588
    const val NEED_FINISH_ACTIVITY = "needFinishActivity"
}
