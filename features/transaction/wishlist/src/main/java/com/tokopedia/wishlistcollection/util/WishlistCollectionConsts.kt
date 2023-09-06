package com.tokopedia.wishlistcollection.util

import com.tokopedia.imageassets.TokopediaImageUrl

object WishlistCollectionConsts {
    const val REQUEST_CODE_COLLECTION_DETAIL = 128
    const val EXTRA_NEED_REFRESH = "EXTRA_NEED_REFRESH"
    const val SRC_WISHLIST_COLLECTION = "wishlist"
    const val SRC_WISHLIST_COLLECTION_SHARING = "sharing"
    const val SRC_WISHLIST_COLLECTION_BULK_ADD = "wishlist_bulk_add"
    const val SOURCE_COLLECTION = "collection"
    const val PAGE_NAME = "wishlist_collection"
    const val PARAM_INSIDE_COLLECTION = "inside"
    const val DELAY_REFETCH_PROGRESS_DELETION = 5000L
    const val DELAY_SHOW_COACHMARK_TOOLBAR = 500L
    const val SOURCE_PDP = "pdp"
    const val OK = "OK"

    const val TYPE_COLLECTION_TICKER = "collection_ticker"
    const val TYPE_COLLECTION_ITEM = "collection_item"
    const val TYPE_COLLECTION_CREATE = "collection_create"
    const val TYPE_COLLECTION_EMPTY_CAROUSEL = "collection_empty_carousel"
    const val TYPE_COLLECTION_DIVIDER = "collection_divider"
    const val TYPE_COLLECTION_LOADER = "collection_loader"

    const val EXTRA_IS_BULK_ADD = "IS_BULK_ADD"
    const val EXTRA_IS_SHOW_CLEANER_BOTTOMSHEET = "IS_SHOW_CLEANER_BOTTOMSHEET"
    const val EXTRA_COLLECTION_ID_DESTINATION = "EXTRA_COLLECTION_ID_DESTINATION"
    const val EXTRA_COLLECTION_NAME_DESTINATION = "EXTRA_COLLECTION_NAME_DESTINATION"
    const val EXTRA_CHANNEL = "channel"

    const val BG_TICKER = TokopediaImageUrl.BG_TICKER

    const val ACTION_KEBAB_UPDATE_COLLECTION = "UPDATE_COLLECTION"
    const val ACTION_KEBAB_SHARE_COLLECTION = "SHARE_COLLECTION"
    const val ACTION_KEBAB_DELETE_COLLECTION = "DELETE_COLLECTION"
    const val ACTION_KEBAB_MANAGE_ITEMS_IN_COLLECTION = "MANAGE_ITEMS_IN_COLLECTION"

    const val COLLECTION_NAME = "collection_name"
    const val COLLECTION_ID = "collection_id"
    const val COLLECTION_TYPE = "collection_type"
    const val COLLECTION_INDICATOR_TITLE = "collection_indicator_title"
    const val COLLECTION_ACTIONS = "collection_actions"

    const val TYPE_COLLECTION_PRIVATE_SELF = 1
    const val TYPE_COLLECTION_SHARE = 2
    const val TYPE_COLLECTION_PUBLIC_SELF = 3
    const val TYPE_COLLECTION_PUBLIC_OTHERS = 4

    const val COLLECTION_PRIVATE = "private"
    const val COLLECTION_PUBLIC = "public"
}
