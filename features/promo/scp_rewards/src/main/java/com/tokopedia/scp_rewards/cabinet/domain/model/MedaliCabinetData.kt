package com.tokopedia.scp_rewards.cabinet.domain.model

import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeader
import com.tokopedia.scp_rewards_widgets.medal.MedalData

data class MedaliCabinetData(
    val header: CabinetHeader? = null,
    val earnedMedaliData: MedalData? = null,
    val progressMedaliData: MedalData? = null
)
