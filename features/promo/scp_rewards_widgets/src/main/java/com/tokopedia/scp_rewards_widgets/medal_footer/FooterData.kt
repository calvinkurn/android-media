package com.tokopedia.scp_rewards_widgets.medal_footer

data class FooterData(
    val text: String? = null,
    val appLink: String? = null,
    val url: String? = null,
    val style: String? = null,
    val autoApply: Boolean = false,
    var isLoading: Boolean = false,
    var id: Int? = null,
    val couponCode: String? = null
)
