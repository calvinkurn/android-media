package com.tokopedia.home.beranda.data.newatf.balance

import com.tokopedia.home.beranda.data.model.TokopointsDrawer
import com.tokopedia.home_component.widget.common.DataStatus

data class BalanceRewardsModel (
    val data: TokopointsDrawer? = null,
    override val state: DataStatus,
): BalanceItemModel {
    override val type: String
        get() = BalanceItemModel.REWARDS
}
