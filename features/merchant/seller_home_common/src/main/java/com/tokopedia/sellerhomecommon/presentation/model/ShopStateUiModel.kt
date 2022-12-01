package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 28/11/22.
 */

enum class ShopStateUiModel {
    None, NewRegisteredShop, AddedProduct, ViewedProduct, HasOrder;

    companion object {
        internal const val NONE = 0L
        internal const val NEW_REGISTERED_SHOP = 1L
        internal const val ADDED_PRODUCT = 2L
        internal const val VIEWED_PRODUCT = 3L
        internal const val HAS_ORDER = 4L
    }
}