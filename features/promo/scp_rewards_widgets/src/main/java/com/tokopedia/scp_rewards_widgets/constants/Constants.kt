package com.tokopedia.scp_rewards_widgets.constants

object CouponState {
    const val ACTIVE = "active"
    const val USED = "used"
    const val INACTIVE = "inactive"
    const val EXPIRED = "expired"
    const val ERROR = "error"
}


sealed class MedalType {
    object Earned : MedalType()
    object InProgress : MedalType()
    object None : MedalType()
}

