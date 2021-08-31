package com.tokopedia.sellerhomecommon.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.BaseMilestoneMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneDataUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneFinishMissionUiModel
import com.tokopedia.sellerhomecommon.presentation.model.MilestoneMissionUiModel
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.shc_item_mission_milestone_widget.view.*
import com.tokopedia.iconunify.R.drawable as iconR

/**
 * Created By @ilhamsuaib on 31/08/21
 */

class MilestoneMissionAdapter(
    private val milestoneData: MilestoneDataUiModel,
    private val onCtaClick: (BaseMilestoneMissionUiModel) -> Unit
) : RecyclerView.Adapter<MilestoneMissionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent)
    }

    override fun getItemCount(): Int = milestoneData.milestoneMissions.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mission = milestoneData.milestoneMissions[position]
        holder.bind(mission, milestoneData.showNumber, onCtaClick)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.shc_item_mission_milestone_widget, parent, false)
                return ViewHolder(view)
            }
        }

        fun bind(
            mission: BaseMilestoneMissionUiModel,
            shouldShowNumber: Boolean,
            onCtaClick: (BaseMilestoneMissionUiModel) -> Unit
        ) {
            with(itemView) {
                tvShcTitleItemMission.text = mission.title
                tvShcDescItemMission.text = mission.subTitle
                tvShcMissionPosition.isVisible = shouldShowNumber
                tvShcMissionPosition.text = adapterPosition.toString()

                imgShcMission.loadImage(mission.imageUrl)

                setupCtaButton(mission, onCtaClick)
            }
        }

        private fun setupCtaButton(
            mission: BaseMilestoneMissionUiModel,
            onCtaClick: (BaseMilestoneMissionUiModel) -> Unit
        ) {
            with(itemView) {
                when (mission) {
                    is MilestoneMissionUiModel -> setupMissionButton(mission)
                    is MilestoneFinishMissionUiModel -> {
                        btnShcMissionCta.buttonType = UnifyButton.Type.ALTERNATE
                    }
                }

                btnShcMissionCta.text = mission.buttonMissionButton.title
                btnShcMissionCta.setOnClickListener {
                    onCtaClick(mission)
                }
            }
        }

        private fun setupMissionButton(mission: MilestoneMissionUiModel) {
            with(itemView) {
                val getDrawable = context.getResDrawable(R.drawable.ic_action_back)
                btnShcMissionCta.setDrawable(getDrawable, UnifyButton.DrawablePosition.LEFT)
                if (mission.missionCompletionStatus) {
                    btnShcMissionCta.buttonType = UnifyButton.Type.ALTERNATE
                    btnShcMissionCta.isEnabled = false
                    val unifyCheckIcon = AppCompatResources.getDrawable(
                        context, iconR.iconunify_check
                    )
                } else {
                    btnShcMissionCta.buttonType = UnifyButton.Type.MAIN
                    btnShcMissionCta.isEnabled = true

                    when (mission.buttonMissionButton.buttonStatus) {
                        BaseMilestoneMissionUiModel.ENABLED_BUTTON_STATUS -> {
                            btnShcMissionCta.buttonType = UnifyButton.Type.MAIN
                            btnShcMissionCta.isEnabled = true
                            btnShcMissionCta.visible()
                        }
                        BaseMilestoneMissionUiModel.DISABLED_BUTTON_STATUS -> {
                            btnShcMissionCta.buttonType = UnifyButton.Type.ALTERNATE
                            btnShcMissionCta.isEnabled = false
                            btnShcMissionCta.visible()
                        }
                        BaseMilestoneMissionUiModel.HIDDEN_BUTTON_STATUS -> {
                            btnShcMissionCta.gone()
                        }
                    }
                }
            }
        }
    }
}