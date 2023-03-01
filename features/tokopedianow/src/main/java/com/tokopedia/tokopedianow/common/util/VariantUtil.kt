package com.tokopedia.tokopedianow.common.util

import com.tokopedia.tokopedianow.common.constant.ConstantValue.DEFAULT_PRODUCT_PARENT_ID

object VariantUtil {

    fun isVariant(parentProductId: String?): Boolean {
        return !parentProductId.isNullOrEmpty() &&
            parentProductId != DEFAULT_PRODUCT_PARENT_ID
    }
}
