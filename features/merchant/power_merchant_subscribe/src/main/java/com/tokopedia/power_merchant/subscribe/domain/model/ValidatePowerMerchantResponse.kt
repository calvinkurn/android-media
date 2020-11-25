package com.tokopedia.power_merchant.subscribe.domain.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.common.network.exception.Header

data class ValidatePowerMerchantResponse(
    @SerializedName("header")
    val header: Header = Header(),
    @SerializedName("data")
    val data: String
) {
    companion object {
        private const val VALID = "valid"

        private const val ERROR_CODE_KYC = "err.validation.kyc"
        private const val ERROR_CODE_SHOP_SCORE = "err.validation.shop_score"
    }

    fun isValid(): Boolean {
        return data == VALID
    }

    fun kycNotVerified(): Boolean {
        return header.errorCode == ERROR_CODE_KYC
    }

    fun shopScoreNotEligible(): Boolean {
        return header.errorCode == ERROR_CODE_SHOP_SCORE
    }

    fun getMessage(): String {
        return header.messages.firstOrNull().orEmpty()
    }
}