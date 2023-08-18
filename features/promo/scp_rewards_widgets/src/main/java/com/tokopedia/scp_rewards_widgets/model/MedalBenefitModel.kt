package com.tokopedia.scp_rewards_widgets.model

import com.tokopedia.scp_rewards_widgets.common.model.CtaButton

class MedalBenefitModel(
    val title: String? = null,
    val isActive: Boolean = false,
    val status: String? = null,
    val appLink: String? = null,
    val tncList: List<String>? = null,
    val iconImageURL: String? = null,
    val podiumImageURL: String? = null,
    val backgroundImageURL: String? = null,
    val ribbonImageURL: String? = null,
    val statusBadgeText: String? = null,
    val statusBadgeColor: String? = null,
    val statusDescription: String? = null,
    val expiryCounter: String? = null,
    val additionalInfo: String? = null,
    val cta: CtaButton? = null
)
