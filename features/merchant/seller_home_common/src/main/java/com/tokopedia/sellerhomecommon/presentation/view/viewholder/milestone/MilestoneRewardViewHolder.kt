package com.tokopedia.sellerhomecommon.presentation.view.viewholder.milestone

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemMissionRewardMilestoneWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.MilestoneMissionAdapter
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneItemRewardUiModel
import com.tokopedia.utils.view.binding.viewBinding

class MilestoneRewardViewHolder(
    itemView: View,
    private val listener: MilestoneMissionAdapter.Listener
): AbstractViewHolder<MilestoneItemRewardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shc_item_mission_reward_milestone_widget
    }

    private val binding: ShcItemMissionRewardMilestoneWidgetBinding? by viewBinding()

    override fun bind(element: MilestoneItemRewardUiModel) {
        binding?.run {
            tvShcMissionRewardTitle.text = element.title
            tvShcMissionRewardDesc.text = element.description
        }
        setupCtaButton(element)
    }

    private fun setupCtaButton(element: MilestoneItemRewardUiModel) {
        if (element.buttonText.isNotBlank()) {
            // TODO: set button by type
            when(element.buttonType) {
                0 -> {
                    binding?.tvShcMissionRewardCta?.run {
                        text = element.buttonText
                        setOnClickListener {
                            listener.onRewardActionClick(element)
                        }
                    }
                }
            }
        } else {
            binding?.tvShcMissionRewardCta?.gone()
            binding?.btnShcMissionReward?.gone()
        }
    }
}
