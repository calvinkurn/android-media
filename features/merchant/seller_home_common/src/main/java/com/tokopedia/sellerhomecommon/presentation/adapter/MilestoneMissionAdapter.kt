package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.databinding.ShcItemMissionMilestoneWidgetBinding
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneFinishMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneMissionUiModel
import com.tokopedia.unifycomponents.ProgressBarUnify
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created By @ilhamsuaib on 31/08/21
 */

class MilestoneMissionAdapter(
    private val milestoneData: MilestoneDataUiModel,
    private val listener: Listener
) : RecyclerView.Adapter<MilestoneMissionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun getItemCount(): Int = milestoneData.milestoneMissions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mission = milestoneData.milestoneMissions[position]
        holder.bind(mission, milestoneData.showNumber, listener)
    }

    class ViewHolder(
        private val binding: ShcItemMissionMilestoneWidgetBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            private const val PLUS_ONE = 1

            fun create(parent: ViewGroup): ViewHolder {
                val binding = ShcItemMissionMilestoneWidgetBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                return ViewHolder(binding)
            }
        }

        fun bind(
            mission: BaseMilestoneMissionUiModel,
            shouldShowNumber: Boolean,
            listener: Listener
        ) {
            with(binding) {
                tvShcTitleItemMission.text = mission.title
                tvShcDescItemMission.text = mission.subTitle
                tvShcMissionPosition.isVisible = shouldShowNumber
                tvShcMissionPosition.text = adapterPosition.plus(PLUS_ONE).toString()

                if (mission is MilestoneMissionUiModel) {
                    setupProgress(mission)
                }

                imgShcMission.loadImage(mission.imageUrl)

                setupCtaButton(mission) {
                    listener.onMissionActionClick(it, adapterPosition.plus(PLUS_ONE))
                }
                root.addOnImpressionListener(mission.impressHolder) {
                    listener.onMissionImpressionListener(mission, adapterPosition.plus(PLUS_ONE))
                }
            }
        }

        private fun setupProgress(mission: MilestoneMissionUiModel) {
            with(binding) {
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
            with(binding) {
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
            with(binding) {
                btnShcMissionCta.buttonType = UnifyButton.Type.ALTERNATE
                btnShcMissionCta.isEnabled = true
            }
        }

        private fun setupMissionButton(mission: MilestoneMissionUiModel) {
            with(binding) {
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

    interface Listener {
        fun onMissionActionClick(mission: BaseMilestoneMissionUiModel, position: Int)
        fun onMissionImpressionListener(mission: BaseMilestoneMissionUiModel, position: Int)
    }
}