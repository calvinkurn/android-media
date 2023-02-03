package com.tokopedia.product_bundle.common.data.constant

import com.tokopedia.imageassets.TokopediaImageUrl

object ProductBundleConstants {

    /**
     * Deprecated - Please use BundlingPageSource
     */
    // page sources
    const val PAGE_SOURCE_CART = "cart"
    const val PAGE_SOURCE_MINI_CART = "minicart"
    const val PAGE_SOURCE_PDP = "pdp"

    // page result extras
    const val EXTRA_OLD_BUNDLE_ID = "old_bundle_id"
    const val EXTRA_NEW_BUNDLE_ID = "new_bundle_id"
    const val EXTRA_IS_VARIANT_CHANGED = "is_variant_changed"

    // image resource links
    const val BUNDLE_EMPTY_IMAGE_URL = TokopediaImageUrl.BUNDLE_EMPTY_IMAGE_URL

    // preorder time unit
    const val PREORDER_TYPE_DAY: Int = 1
    const val PREORDER_TYPE_WEEK: Int = 2
    const val PREORDER_TYPE_MONTH: Int = 3

    // product bundle types
    const val BUNDLE_TYPE_SINGLE: Int = 1
    const val BUNDLE_TYPE_MULTIPLE: Int = 2
}
