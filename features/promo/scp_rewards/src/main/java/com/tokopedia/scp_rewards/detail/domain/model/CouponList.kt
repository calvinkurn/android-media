package com.tokopedia.scp_rewards.detail.domain.model

import com.tokopedia.scp_rewards_widgets.model.MedalBenefitModel

data class CouponList(
        val title: String,
        val status : String,
        val list: List<MedalBenefitModel>?,
)
