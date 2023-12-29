package com.tokopedia.centralizedpromo.view.model

import com.tokopedia.nest.components.CoachMarkAnchor

data class CoachMarkAnchorWithKey(
    val anchor: CoachMarkAnchor = CoachMarkAnchor(),
    val key: String = ""
)
