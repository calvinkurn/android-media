package com.tokopedia.power_merchant.subscribe.view_old.model

sealed class PowerMerchantActivationResult(
    open val message: String? = null
) {

    object ActivationSuccess: PowerMerchantActivationResult()
    object KycNotVerified: PowerMerchantActivationResult()
    object ShopScoreNotEligible: PowerMerchantActivationResult()

    data class GeneralError(override val message: String): PowerMerchantActivationResult(message)
}