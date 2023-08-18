package com.tokopedia.scp_rewards_widgets.common.model

data class CtaButton(
    val unifiedStyle: String? = null,
    val text: String? = null,
    val appLink: String? = null,
    val isAutoApply: Boolean? = false,
    val couponCode: String? = null
)
