package com.tokopedia.sellerhomecommon.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhomecommon.presentation.adapter.factory.MilestoneAdapterTypeFactory
import com.tokopedia.unifycomponents.UnifyButton

data class MilestoneItemRewardUiModel(
    val title: String,
    val subtitle: String,
    val buttonText: String,
    val buttonVariant: Int,
    val buttonStatus: Int,
    val buttonApplink: String,
    val lottieUrl: String,
    val rewardDetailUiModel: RewardDetailUiModel?
) : Visitable<MilestoneAdapterTypeFactory> {

    object ButtonStatus {
        const val HIDDEN = 0
        const val ENABLED = 1
        const val DISABLED = 2
    }

    object QuestStatus {
        const val NOT_STARTED = 1
        const val ON_GOING = 2
        const val QUEST_FINISH = 3
        const val REWARD_CLAIMED = 4
    }

    override fun type(typeFactory: MilestoneAdapterTypeFactory): Int {
        UnifyButton.Type.ALTERNATE
        return typeFactory.type(this)
    }
}
