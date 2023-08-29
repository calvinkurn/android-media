package com.tokopedia.scp_rewards_widgets.model

import com.tokopedia.scp_rewards_widgets.common.model.CtaButton

class MedalBenefitModel(
    val title: String? = null,
    val isActive: Boolean = false,
    val status: String? = null,
    val appLink: String? = null,
    val tncList: List<String>? = null,
    val medaliImageURL: String? = null,
    val podiumImageURL: String? = null,
    val backgroundImageURL: String? = null,
    val statusBadgeText: String? = null,
    val statusBadgeColor: String? = null,
    var statusBadgeEnabled : Boolean = true,
    val statusDescription: String? = null,
    val additionalInfoText: String? = null,
    val additionalInfoColor: String? = null,
    val typeImageURL: String? = null,
    val typeBackgroundColor: String? = null,
    val cta: CtaButton? = null
)
