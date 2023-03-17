package com.tokopedia.feedplus.presentation.uiview

import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.databinding.LayoutFeedCampaignRibbonBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created By : Muhammad Furqan on 16/03/23
 */
enum class FeedCampaignRibbonType {
    TITLE_ONLY,
    TITLE_WITH_TIMER,
    START_IN,
    ON_GOING
}

class FeedCampaignRibbonView(
    private val binding: LayoutFeedCampaignRibbonBinding,
    private val listener: FeedListener
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    var campaign: FeedCardCampaignModel? = null
    var type: FeedCampaignRibbonType = FeedCampaignRibbonType.TITLE_ONLY

    fun bindData(campaign: FeedCardCampaignModel, isTypeHighlight: Boolean) {
        this.campaign = campaign
        with(binding) {
            if (!isTypeHighlight) {
                root.hide()
            } else {
                root.show()
            }

            if (campaign.isUpcoming) {
                type = FeedCampaignRibbonType.TITLE_ONLY
                startDelayProcess(TWO_SECOND, ::showStartInGradientCampaignRibbon)
            }

            tyFeedCampaignRibbonTitle.text = campaign.shortName
            renderRibbonByType(campaign.isReminderActive)
            setupTimer(campaign.endTime) {
                if (campaign.isUpcoming) {
                    listener.onTimerFinishUpcoming()
                } else if (campaign.isOngoing) {
                    listener.onTimerFinishOnGoing()
                }
            }
        }
    }

    fun renderRibbonByType(isActiveReminder: Boolean) {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.TITLE_ONLY -> {
                    tyFeedCampaignRibbonTitle.show()
                    tyFeedCampaignRibbonSmall.hide()
                    pbFeedCampaignRibbon.hide()
                    timerFeedCampaignRibbon.hide()

                    if (isActiveReminder) {
                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL_FILLED)
                    } else {
                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL)
                    }

                    val constraintSet = ConstraintSet()
                    constraintSet.connect(
                        guidelineFeedCampaignRibbon.id,
                        ConstraintSet.START,
                        tyFeedCampaignRibbonTitle.id,
                        ConstraintSet.END
                    )
                    constraintSet.applyTo(root)
                }
                FeedCampaignRibbonType.TITLE_WITH_TIMER -> {
                    tyFeedCampaignRibbonTitle.show()
                    tyFeedCampaignRibbonSmall.hide()
                    pbFeedCampaignRibbon.hide()
                    timerFeedCampaignRibbon.show()

                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)

                    val constraintSet = ConstraintSet()
                    constraintSet.connect(
                        guidelineFeedCampaignRibbon.id,
                        ConstraintSet.START,
                        tyFeedCampaignRibbonTitle.id,
                        ConstraintSet.END
                    )
                    constraintSet.applyTo(root)
                }
                FeedCampaignRibbonType.START_IN -> {
                    tyFeedCampaignRibbonTitle.hide()
                    tyFeedCampaignRibbonSmall.show()
                    pbFeedCampaignRibbon.hide()
                    timerFeedCampaignRibbon.show()

                    if (isActiveReminder) {
                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL_FILLED)
                    } else {
                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL)
                    }

                    val constraintSet = ConstraintSet()
                    constraintSet.connect(
                        guidelineFeedCampaignRibbon.id,
                        ConstraintSet.START,
                        tyFeedCampaignRibbonSmall.id,
                        ConstraintSet.END
                    )
                    constraintSet.applyTo(root)
                }
                FeedCampaignRibbonType.ON_GOING -> {
                    tyFeedCampaignRibbonTitle.hide()
                    tyFeedCampaignRibbonSmall.show()
                    pbFeedCampaignRibbon.show()
                    timerFeedCampaignRibbon.show()

                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)

                    val constraintSet = ConstraintSet()
                    constraintSet.connect(
                        guidelineFeedCampaignRibbon.id,
                        ConstraintSet.START,
                        pbFeedCampaignRibbon.id,
                        ConstraintSet.END
                    )
                    constraintSet.applyTo(root)
                }
            }
        }
    }

    private fun setupTimer(timeTarget: String, onFinish: () -> Unit) {
        val targetCalendar = TimeConverter.convertToCalendar(timeTarget)
        targetCalendar?.let {
            if (it.timeInMillis > Calendar.getInstance().timeInMillis) {
                binding.timerFeedCampaignRibbon.apply {
                    targetDate = it
                    this.onFinish = onFinish
                }
            }
        } ?: binding.timerFeedCampaignRibbon.hide()
    }

    private fun startDelayProcess(delayDurationInMilis: Long, block: () -> Unit) {
        scope.launch {
            delay(delayDurationInMilis)
            block()
        }
    }

    private fun setupReminderButtonAction() {
        binding.icFeedCampaignRibbonIcon.setOnClickListener {
            listener.setReminder()
        }
    }

    private fun removeReminderButtonAction() {
        binding.icFeedCampaignRibbonIcon.setOnClickListener { }
    }

    private fun showStartInGradientCampaignRibbon() {
        type = FeedCampaignRibbonType.START_IN
        binding.root.background = ContextCompat.getDrawable(
            binding.root.context,
            R.drawable.bg_feed_campaign_ribbon_flashsale_gradient
        )
        renderRibbonByType(campaign?.isReminderActive ?: false)
    }

    companion object {
        private const val TWO_SECOND = 2000L
        private const val THREE_SECOND = 3000L
    }

}
