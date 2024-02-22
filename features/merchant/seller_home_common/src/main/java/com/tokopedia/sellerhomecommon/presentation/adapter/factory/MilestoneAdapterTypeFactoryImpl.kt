package com.tokopedia.sellerhomecommon.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.sellerhomecommon.presentation.adapter.MilestoneMissionAdapter
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneItemRewardUiModel
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.milestone.MilestoneMissionViewHolder
import com.tokopedia.sellerhomecommon.presentation.view.viewholder.milestone.MilestoneRewardViewHolder

class MilestoneAdapterTypeFactoryImpl(
    private val listener: MilestoneMissionAdapter.Listener
) : BaseAdapterTypeFactory(), MilestoneAdapterTypeFactory {

    override fun type(uiModel: BaseMilestoneMissionUiModel): Int {
        return MilestoneMissionViewHolder.LAYOUT
    }

    override fun type(uiModel: MilestoneItemRewardUiModel): Int {
        return MilestoneRewardViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            MilestoneMissionViewHolder.LAYOUT -> MilestoneMissionViewHolder(parent, listener)
            MilestoneRewardViewHolder.LAYOUT -> MilestoneRewardViewHolder(parent, listener)
            else -> super.createViewHolder(parent, type)
        }
    }

}
