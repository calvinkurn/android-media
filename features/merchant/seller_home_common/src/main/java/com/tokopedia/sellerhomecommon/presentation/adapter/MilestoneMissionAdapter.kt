package com.tokopedia.sellerhomecommon.presentation.adapter

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MilestoneAdapterTypeFactory
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneItemRewardUiModel

/**
 * Created By @ilhamsuaib on 31/08/21
 */

class MilestoneMissionAdapter(
    milestoneData: MilestoneDataUiModel,
    adapterTypeFactory: MilestoneAdapterTypeFactory
) : BaseAdapter<MilestoneAdapterTypeFactory>(adapterTypeFactory) {

    init {
        setVisitables(milestoneData.milestoneMissions)
    }

    interface Listener {
        fun onMissionActionClick(mission: BaseMilestoneMissionUiModel, position: Int)
        fun onMissionImpressionListener(mission: BaseMilestoneMissionUiModel, position: Int)
        fun onRewardActionClick(reward: MilestoneItemRewardUiModel, position: Int)
        fun onRewardImpressionListener(reward: MilestoneItemRewardUiModel, position: Int)
    }
}
