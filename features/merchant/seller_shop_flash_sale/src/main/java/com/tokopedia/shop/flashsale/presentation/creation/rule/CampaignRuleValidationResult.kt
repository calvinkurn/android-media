package com.tokopedia.shop.flashsale.presentation.creation.rule

sealed class CampaignRuleValidationResult {
    val isInvalid: Boolean = this !is Valid

    object Valid : CampaignRuleValidationResult()

    object BothSectionsInvalid : CampaignRuleValidationResult()
    object InvalidPaymentMethod : CampaignRuleValidationResult()
    object InvalidBuyerOptions : CampaignRuleValidationResult()

    object TNCNotAccepted : CampaignRuleValidationResult()

    data class InvalidCampaignTime(
        val campaignId: Long
    ) : CampaignRuleValidationResult()

    object NotEligible: CampaignRuleValidationResult()
}
