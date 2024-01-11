package com.tokopedia.sellerhomecommon.presentation.adapter.factory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneItemRewardUiModel

interface MilestoneAdapterTypeFactory: AdapterTypeFactory {

    fun type(uiModel: BaseMilestoneMissionUiModel): Int
    fun type(uiModel: MilestoneItemRewardUiModel): Int

}
