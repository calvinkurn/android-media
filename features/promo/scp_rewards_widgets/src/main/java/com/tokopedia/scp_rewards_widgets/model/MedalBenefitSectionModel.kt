package com.tokopedia.scp_rewards_widgets.model

import com.tokopedia.scp_rewards_widgets.medal.Cta

class MedalBenefitSectionModel(
    val title: String? = null,
    val backgroundColor: String? = null,
    val benefitInfo: String? = null,
    val benefitList: List<MedalBenefitModel>? = null,
    val jsonParameter: String? = null,
    val cta: Cta? = null,
)
