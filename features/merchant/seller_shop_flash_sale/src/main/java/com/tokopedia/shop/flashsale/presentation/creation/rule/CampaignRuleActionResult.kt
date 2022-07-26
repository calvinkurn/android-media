package com.tokopedia.shop.flashsale.presentation.creation.rule

sealed class CampaignRuleActionResult {
    object Success : CampaignRuleActionResult()
    object Loading : CampaignRuleActionResult()

    object ShowConfirmation : CampaignRuleActionResult()

    data class ValidationFail(
        val result: CampaignRuleValidationResult
    ) : CampaignRuleActionResult()

    object DetailNotLoaded : CampaignRuleActionResult()

    data class Fail(
        val error: CampaignRuleError
    ) : CampaignRuleActionResult()
}
