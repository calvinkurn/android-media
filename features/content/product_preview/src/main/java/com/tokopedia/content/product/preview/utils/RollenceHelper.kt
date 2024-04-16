package com.tokopedia.content.product.preview.utils

import com.tokopedia.remoteconfig.RemoteConfigInstance

const val ROLLENCE_KEY_PRODUCT_PREVIEW = "pdp_content_full"
const val ROLLENCE_NEW_VALUE_PRODUCT_PREVIEW = "new_layout"
const val ROLLENCE_OLD_VALUE_PRODUCT_PREVIEW = "old_layout"

val enableRollenceContentProductPreview: Boolean
    get() {
        val value = RemoteConfigInstance
            .getInstance()
            .abTestPlatform
            .getString(ROLLENCE_KEY_PRODUCT_PREVIEW, ROLLENCE_OLD_VALUE_PRODUCT_PREVIEW)
        return value == ROLLENCE_NEW_VALUE_PRODUCT_PREVIEW
    }
