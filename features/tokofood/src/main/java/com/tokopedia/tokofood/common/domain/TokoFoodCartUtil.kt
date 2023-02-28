package com.tokopedia.tokofood.common.domain

import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl

object TokoFoodCartUtil {

    const val TOKOFOOD_BUSINESS_ID: Long = 1
    const val TOKOFOOD_BUSINESS_TYPE = 42
    const val TOKOFOOD_DATA_TYPE = "TOKOFOOD"

    const val SUCCESS_STATUS_INT = 1
    const val SUCCESS_STATUS = "OK"
    const val ERROR_STATUS = "ERROR"

    const val IS_MAIN_ADDRESS_STATUS = 2

    const val AVAILABLE_SECTION = "available_section"
    const val UNAVAILABLE_SECTION = "unavilable_section"

    const val SOURCE_MERCHANT_PAGE = "merchant_page"

    const val BUSINESS_ID_PRODUCTION = "2fe98008-ac73-4cb9-ba7f-d8bf77eedcac"
    const val BUSINESS_ID_STAGING = "dc30f53e-761f-4f20-8cf4-fe5d354ded33"

    fun getBusinessId(): String {
        return if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            BUSINESS_ID_STAGING
        } else {
            BUSINESS_ID_PRODUCTION
        }
    }
}
