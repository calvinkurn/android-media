package com.tokopedia.shop.flashsale.presentation.creation.rule

data class CampaignRuleError(
    val title: String? = null,
    override val message: String = "",
    override val cause: Throwable? = null,
) : Throwable()
