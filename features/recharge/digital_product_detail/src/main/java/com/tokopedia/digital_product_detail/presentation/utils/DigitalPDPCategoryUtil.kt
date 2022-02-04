package com.tokopedia.digital_product_detail.presentation.utils

import com.tokopedia.common.topupbills.data.constant.GeneralCategoryType
import com.tokopedia.common.topupbills.data.constant.GeneralComponentName
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.constant.TelcoComponentName

object DigitalPDPCategoryUtil {
    fun getCategoryName(categoryId: Int): String {
        return when (categoryId) {
            TelcoCategoryType.CATEGORY_PULSA -> TelcoComponentName.PRODUCT_PULSA
            TelcoCategoryType.CATEGORY_PAKET_DATA -> TelcoComponentName.PRODUCT_PAKET_DATA
            TelcoCategoryType.CATEGORY_ROAMING -> TelcoComponentName.PRODUCT_ROAMING
            GeneralCategoryType.CATEGORY_TOKEN_LISTRIK -> GeneralComponentName.PRODUCT_TOKEN_LISTRIK
            else -> TelcoComponentName.PRODUCT_PASCABAYAR
        }
    }

    const val DEFAULT_MENU_ID_TELCO = "2"

}