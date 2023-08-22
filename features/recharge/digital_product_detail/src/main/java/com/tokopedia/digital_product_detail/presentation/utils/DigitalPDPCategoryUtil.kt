package com.tokopedia.digital_product_detail.presentation.utils

import com.tokopedia.common.topupbills.data.constant.GeneralCategoryType
import com.tokopedia.common.topupbills.data.constant.GeneralComponentName
import com.tokopedia.common.topupbills.data.constant.GeneralOperatorType
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.constant.TelcoComponentName

object DigitalPDPCategoryUtil {

    private val indosatOperatorIds: List<String> = listOf("2", "16", "17", "114", "237", "3020")

    fun getCategoryName(categoryId: Int): String {
        return when (categoryId) {
            TelcoCategoryType.CATEGORY_PULSA -> TelcoComponentName.PRODUCT_PULSA
            TelcoCategoryType.CATEGORY_PAKET_DATA -> TelcoComponentName.PRODUCT_PAKET_DATA
            TelcoCategoryType.CATEGORY_ROAMING -> TelcoComponentName.PRODUCT_ROAMING
            GeneralCategoryType.CATEGORY_LISTRIK_PLN -> GeneralComponentName.LISTRIK_PLN
            else -> TelcoComponentName.PRODUCT_PASCABAYAR
        }
    }

    fun getOperatorName(operatorId: String): String {
        return when (operatorId) {
            GeneralOperatorType.OPERATOR_TOKEN_LISTRIK -> GeneralComponentName.OPERATOR_TOKEN_LISTRIK
            GeneralOperatorType.OPERATOR_TAGLIS -> GeneralComponentName.OPERATOR_TAGLIS
            GeneralOperatorType.OPERATOR_NON_TAGLIS -> GeneralComponentName.OPERATOR_NON_TAGLIS
            else -> ""
        }
    }

    fun isOperatorIndosat(operatorId: String): Boolean {
        return indosatOperatorIds.contains(operatorId)
    }

    const val DEFAULT_MENU_ID_TELCO = "2"
}
