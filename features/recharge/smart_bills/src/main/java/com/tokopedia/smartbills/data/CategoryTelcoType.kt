package com.tokopedia.smartbills.data

import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.constant.TelcoComponentName
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.toIntSafely


object CategoryTelcoType {
        @JvmStatic
        fun getCategoryString(category: String?): String {
            val categoryId = category.toIntSafely()
            return when (categoryId) {
                TelcoCategoryType.CATEGORY_PULSA -> TelcoComponentName.PRODUCT_PULSA
                TelcoCategoryType.CATEGORY_PAKET_DATA -> TelcoComponentName.PRODUCT_PAKET_DATA
                TelcoCategoryType.CATEGORY_PASCABAYAR -> TelcoComponentName.PRODUCT_PASCABAYAR
                else -> TelcoComponentName.PRODUCT_PULSA
            }
        }

        @JvmStatic
        fun isCategoryPacketData(category: String?): Boolean {
            val categoryId = category.toIntSafely()
            return categoryId == TelcoCategoryType.CATEGORY_PAKET_DATA
        }
}