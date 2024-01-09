package com.tokopedia.sellerhomecommon.presentation.view.viewholder.milestone

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemMissionMilestoneWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.MilestoneMissionAdapter
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneFinishMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneMissionUiModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

class MilestoneMissionViewHolder(
    itemView: View,
    private val listener: MilestoneMissionAdapter.Listener
): AbstractViewHolder<BaseMilestoneMissionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.shc_item_mission_milestone_widget

        private const val PLUS_ONE = 1
    }

    private val binding: ShcItemMissionMilestoneWidgetBinding? by viewBinding()

    override fun bind(element: BaseMilestoneMissionUiModel) {
        binding?.run {
            tvShcTitleItemMission.text = element.title
            tvShcDescItemMission.text = element.subTitle
            tvShcMissionPosition.isVisible = element.showNumber
            tvShcMissionPosition.text = absoluteAdapterPosition.plus(PLUS_ONE).toString()

            if (element is MilestoneMissionUiModel) {
                setupProgress(element)
            }

            imgShcMission.loadImage(element.imageUrl)

            setupCtaButton(element) {
                listener.onMissionActionClick(it, absoluteAdapterPosition.plus(PLUS_ONE))
            }
            root.addOnImpressionListener(element.impressHolder) {
                listener.onMissionImpressionListener(element, absoluteAdapterPosition.plus(PLUS_ONE))
            }
        }
    }

    private fun setupProgress(mission: MilestoneMissionUiModel) {
        binding?.run {
            progressBarShcMilestoneCard.isVisible = mission.isProgressAvailable()
            tvShcMilestoneCardDescription.isVisible = progressBarShcMilestoneCard.isVisible

            if (mission.isProgressAvailable()) {
                progressBarShcMilestoneCard.progressBarHeight = ProgressBarUnify.SIZE_MEDIUM
                progressBarShcMilestoneCard.setValue(mission.missionProgress.percentage)
                val progress = mission.missionProgress
                val description =
                    "<b>${progress.completed}</b> / ${progress.target} ${progress.description}"
                tvShcMilestoneCardDescription.text = description.parseAsHtml()
            }
        }
    }

    private fun setupCtaButton(
        mission: BaseMilestoneMissionUiModel,
        onCtaClick: (BaseMilestoneMissionUiModel) -> Unit
    ) {
        binding?.run {
            when (mission) {
                is MilestoneMissionUiModel -> setupMissionButton(mission)
                is MilestoneFinishMissionUiModel -> setupFinishedMissionButton()
            }

            btnShcMissionCta.text = mission.missionButton.title
            btnShcMissionCta.setOnClickListener {
                onCtaClick(mission)
            }
        }
    }

    private fun setupFinishedMissionButton() {
        binding?.run {
            btnShcMissionCta.buttonType = UnifyButton.Type.ALTERNATE
            btnShcMissionCta.isEnabled = true
        }
    }

    private fun setupMissionButton(mission: MilestoneMissionUiModel) {
        binding?.run {
            if (mission.missionCompletionStatus) {
                btnShcMissionCta.buttonType = UnifyButton.Type.ALTERNATE
                btnShcMissionCta.isEnabled = false
            } else {
                when (mission.missionButton.buttonStatus) {
                    BaseMilestoneMissionUiModel.ButtonStatus.ENABLED -> {
                        btnShcMissionCta.buttonType = UnifyButton.Type.MAIN
                        btnShcMissionCta.isEnabled = true
                        btnShcMissionCta.visible()
                    }
                    BaseMilestoneMissionUiModel.ButtonStatus.DISABLED -> {
                        btnShcMissionCta.buttonType = UnifyButton.Type.ALTERNATE
                        btnShcMissionCta.isEnabled = false
                        btnShcMissionCta.visible()
                    }
                    BaseMilestoneMissionUiModel.ButtonStatus.HIDDEN -> {
                        btnShcMissionCta.gone()
                    }
                }
            }
        }
    }

}
