package com.tokopedia.feedplus.presentation.uiview

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_NEW_PRODUCTS
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_RESTOCK
import com.tokopedia.feedplus.databinding.LayoutFeedCampaignRibbonBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

/**
 * Created By : Muhammad Furqan on 16/03/23
 */
enum class FeedCampaignRibbonType {
    ASGC_GENERAL,
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

    fun bindData(
        modelType: String,
        campaign: FeedCardCampaignModel,
        ctaModel: FeedCardCtaModel,
        hasVoucher: Boolean,
        isTypeHighlight: Boolean
    ) {
        this.campaign = campaign
        with(binding) {
            if (!isTypeHighlight) {
                root.hide()
            } else {
                root.show()
            }

            type = getRibbonType(modelType)
            buildRibbonBasedOnType(campaign, ctaModel)

//            tyFeedCampaignRibbonTitle.text = campaign.shortName
//            renderRibbonByType(campaign.isReminderActive)
//            setupTimer(campaign.endTime) {
//                if (campaign.isUpcoming) {
//                    listener.onTimerFinishUpcoming()
//                } else if (campaign.isOngoing) {
//                    listener.onTimerFinishOnGoing()
//                }
//            }
        }
    }

    private fun getRibbonType(type: String) = when (type) {
        TYPE_FEED_ASGC_RESTOCK, TYPE_FEED_ASGC_NEW_PRODUCTS -> FeedCampaignRibbonType.ASGC_GENERAL
        else -> FeedCampaignRibbonType.TITLE_ONLY
    }

    private fun setBackgroundGradient(cta: FeedCardCtaModel) {
        with(binding) {
            when {
                type == FeedCampaignRibbonType.ASGC_GENERAL && cta.colorGradient.isEmpty() ->
                    root.setBackgroundColor(Color.parseColor(cta.color))
                type == FeedCampaignRibbonType.ASGC_GENERAL && cta.colorGradient.isNotEmpty() -> {
                    root.background = GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        cta.colorGradient.map {
                            it.color.replace(HASH, INT_COLOR_PREFIX).toIntSafely()
                        }.toIntArray()
                    ).apply {
                        cornerRadius = CORNER_RADIUS
                    }
                }
            }
        }
    }

    private fun buildRibbonBasedOnType(campaign: FeedCardCampaignModel, ctaModel: FeedCardCtaModel) {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL -> {
                    tyFeedCampaignRibbonTitle.text = "${ctaModel.text} ${ctaModel.subtitle.joinToString(" ")}"
                    renderRibbonByType(campaign.isReminderActive)
                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient(ctaModel)
                    }
                }
            }
        }
    }

    private fun renderRibbonByType(isActiveReminder: Boolean) {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL -> {
                    tyFeedCampaignRibbonTitle.show()
                    tyFeedCampaignRibbonSmall.hide()
                    pbFeedCampaignRibbon.hide()
                    timerFeedCampaignRibbon.hide()

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

        private const val HASH = "#"
        private const val INT_COLOR_PREFIX = "0xFF"
        private const val CORNER_RADIUS = 4f
    }
}
