package com.tokopedia.scp_rewards_widgets.views.rewardsview

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.scp_rewards_widgets.model.MedalRewardsModel
import com.tokopedia.scp_rewards_widgets.model.RewardsErrorModel

class RewardsViewTypeFactory : BaseAdapterTypeFactory() {

    fun type(model:MedalRewardsModel) = RewardsViewHolder.LAYOUT

    fun type(model:RewardsErrorModel) = CouponErrorViewHolder.LAYOUT

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type){
            RewardsViewHolder.LAYOUT -> RewardsViewHolder(parent)
            CouponErrorViewHolder.LAYOUT -> CouponErrorViewHolder(parent)
            else -> super.createViewHolder(parent, type)
        }
    }
}
