package com.tokopedia.sellerhomecommon.presentation.model

/**
 * Created by @ilhamsuaib on 28/11/22.
 */

enum class ShopStateUiModel {
    None, NewRegisteredShop, AddedProduct, ViewedProduct, HasOrder;

    companion object {
        const val NONE = 0L
        const val NEW_REGISTERED_SHOP = 1L
        const val ADDED_PRODUCT = 2L
        const val VIEWED_PRODUCT = 3L
        const val HAS_ORDER = 4L
    }
}