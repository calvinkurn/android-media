package com.tokopedia.scp_rewards_widgets.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.scp_rewards_widgets.views.rewardsview.RewardsViewTypeFactory

class MedalRewardsModel(
    val imageUrl:String = "",
    val isActive:Boolean = false,
    val status:String = "",
    val statusDescription:String = ""
) : Visitable<RewardsViewTypeFactory> {

    override fun type(typeFactory: RewardsViewTypeFactory) = typeFactory.type(this)
}
