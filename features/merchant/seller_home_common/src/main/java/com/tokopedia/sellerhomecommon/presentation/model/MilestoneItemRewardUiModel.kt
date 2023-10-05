package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MilestoneAdapterTypeFactory

data class MilestoneItemRewardUiModel(
    val title: String,
    val description: String,
    val buttonText: String,
    val buttonType: Int
): Visitable<MilestoneAdapterTypeFactory> {

    override fun type(typeFactory: MilestoneAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }

}
