package com.tokopedia.sellerhomecommon.presentation.view.viewholder.milestone

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadAsGif
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemMissionRewardMilestoneWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.MilestoneMissionAdapter
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneItemRewardUiModel
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class MilestoneRewardViewHolder(
    itemView: View,
    private val listener: MilestoneMissionAdapter.Listener
) : AbstractViewHolder<MilestoneItemRewardUiModel>(itemView) {

    companion object {
        private const val GIF_EXT = ".gif"

        @LayoutRes
        val LAYOUT = R.layout.shc_item_mission_reward_milestone_widget
    }

    private val binding: ShcItemMissionRewardMilestoneWidgetBinding? by viewBinding()

    override fun bind(element: MilestoneItemRewardUiModel) {
        binding?.run {
            tvShcMissionRewardTitle.text = element.title
            tvShcMissionRewardDesc.text = element.subtitle
        }
        setupAnimation(element.animationUrl)
        setupCtaButton(element)
        setupRewardImpressionListener(element)
    }

    private fun setupAnimation(animationUrl: String) {
        val isGif = animationUrl.endsWith(GIF_EXT)
        binding?.imgShcMissionReward?.run {
            if (isGif) {
                loadAsGif(animationUrl)
            } else {
                loadImage(animationUrl)
            }
        }
    }

    private fun setupCtaButton(element: MilestoneItemRewardUiModel) {
        if (element.buttonText.isNotBlank()) {
            if (element.buttonVariant == UnifyButton.Variant.TEXT_ONLY) {
                setupRewardTextCta(element)
                binding?.btnShcMissionReward?.gone()
            } else {
                setupRewardButtonCta(element)
                binding?.tvShcMissionRewardCta?.gone()
            }
        } else {
            binding?.tvShcMissionRewardCta?.gone()
            binding?.btnShcMissionReward?.gone()
        }
    }

    private fun setupRewardTextCta(element: MilestoneItemRewardUiModel) {
        binding?.tvShcMissionRewardCta?.run {
            when (element.buttonStatus) {
                MilestoneItemRewardUiModel.ButtonStatus.HIDDEN -> {
                    gone()
                }
                MilestoneItemRewardUiModel.ButtonStatus.ENABLED -> {
                    visible()
                    setTextColor(
                        MethodChecker.getColor(
                            context,
                            unifyprinciplesR.color.Unify_GN500
                        )
                    )
                    setOnClickListener {
                        listener.onRewardActionClick(element, absoluteAdapterPosition.plus(Int.ONE))
                    }
                }
                MilestoneItemRewardUiModel.ButtonStatus.DISABLED -> {
                    visible()
                    setTextColor(
                        MethodChecker.getColor(
                            context,
                            unifyprinciplesR.color.Unify_NN300
                        )
                    )
                    setOnClickListener(null)
                }
            }
            text = element.buttonText
        }
    }

    private fun setupRewardButtonCta(element: MilestoneItemRewardUiModel) {
        binding?.btnShcMissionReward?.run {
            text = element.buttonText
            buttonVariant = element.buttonVariant
            buttonType =
                if (element.buttonVariant == UnifyButton.Variant.GHOST) {
                    UnifyButton.Type.ALTERNATE
                } else {
                    UnifyButton.Type.MAIN
                }
            when (element.buttonStatus) {
                MilestoneItemRewardUiModel.ButtonStatus.HIDDEN -> {
                    gone()
                }
                MilestoneItemRewardUiModel.ButtonStatus.ENABLED -> {
                    visible()
                    isEnabled = true
                    setOnClickListener {
                        listener.onRewardActionClick(element, absoluteAdapterPosition.plus(Int.ONE))
                    }
                }
                MilestoneItemRewardUiModel.ButtonStatus.DISABLED -> {
                    visible()
                    isEnabled = false
                    setOnClickListener(null)
                }
            }
        }
    }

    private fun setupRewardImpressionListener(element: MilestoneItemRewardUiModel) {
        binding?.root?.addOnImpressionListener(element.impressHolder) {
            listener.onRewardImpressionListener(
                element,
                absoluteAdapterPosition.plus(
                    Int.ONE
                )
            )
        }
    }
}
