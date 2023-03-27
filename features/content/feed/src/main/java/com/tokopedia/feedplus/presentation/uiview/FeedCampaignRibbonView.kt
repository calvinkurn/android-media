package com.tokopedia.feedplus.presentation.uiview

import android.graphics.drawable.GradientDrawable
import android.widget.Toast
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_NEW_PRODUCTS
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_RESTOCK
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_SHOP_DISCOUNT
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_SHOP_FLASH_SALE
import com.tokopedia.feedplus.databinding.LayoutFeedCampaignRibbonMotionBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaModel
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
    ASGC_GENERAL, ASGC_DISCOUNT,
    ASGC_FLASH_SALE_UPCOMING, ASGC_FLASH_SALE_ONGOING,
    TITLE_ONLY
}

class FeedCampaignRibbonView(
    private val binding: LayoutFeedCampaignRibbonMotionBinding,
    private val listener: FeedListener
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    var type: FeedCampaignRibbonType = FeedCampaignRibbonType.TITLE_ONLY

    fun bindData(
        modelType: String,
        campaign: FeedCardCampaignModel,
        ctaModel: FeedCardCtaModel,
        hasVoucher: Boolean,
        isTypeHighlight: Boolean
    ) {
        with(binding) {
            if (!isTypeHighlight) {
                root.hide()
            } else {
                root.show()
            }

            type = getRibbonType(modelType, campaign.isOngoing)
            buildRibbonBasedOnType(campaign, ctaModel)
        }
    }

    private fun getRibbonType(type: String, isOngoing: Boolean) = when {
        type == TYPE_FEED_ASGC_RESTOCK || type == TYPE_FEED_ASGC_NEW_PRODUCTS -> FeedCampaignRibbonType.ASGC_GENERAL
        type == TYPE_FEED_ASGC_SHOP_DISCOUNT -> FeedCampaignRibbonType.ASGC_DISCOUNT
        type == TYPE_FEED_ASGC_SHOP_FLASH_SALE && isOngoing -> FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING
        type == TYPE_FEED_ASGC_SHOP_FLASH_SALE && !isOngoing -> FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING
        else -> FeedCampaignRibbonType.TITLE_ONLY
    }

    private fun setBackgroundGradient(cta: FeedCardCtaModel) {
        with(binding) {
            when {
                type == FeedCampaignRibbonType.ASGC_GENERAL && cta.colorGradient.isEmpty() -> {
//                    root.background =
//                        GradientDrawable(
//                            GradientDrawable.Orientation.LEFT_RIGHT,
//                            intArrayOf(
//                                cta.color.replace(HASH, INT_COLOR_PREFIX).toIntSafely(),
//                                cta.color.replace(HASH, INT_COLOR_PREFIX).toIntSafely()
//                            )
//                        ).apply {
//                            cornerRadius = CORNER_RADIUS
//                        }
                }
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
                type == FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING || type == FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING && cta.colorGradient.isNotEmpty() -> {
                    root.background = GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        cta.colorGradient.map {
                            it.color.replace(HASH, INT_COLOR_PREFIX).toIntSafely()
                        }.toIntArray()
                    ).apply {
                        cornerRadius = CORNER_RADIUS
                    }
                }
                type == FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING || type == FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING && cta.colorGradient.isEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_flashsale_gradient
                    )
                }
            }
        }
    }

    private fun buildRibbonBasedOnType(
        campaign: FeedCardCampaignModel,
        ctaModel: FeedCardCtaModel
    ) {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL -> {
                    Toast.makeText(
                        root.context,
                        "${ctaModel.text} ${ctaModel.subtitle.joinToString(" ")}",
                        Toast.LENGTH_SHORT
                    ).show()
                    tyFeedCampaignRibbonTitle.text =
                        "${ctaModel.text} ${ctaModel.subtitle.joinToString(" ")}"
//                    renderRibbonByType(campaign.isReminderActive)
//                    startDelayProcess(TWO_SECOND) {
//                        setBackgroundGradient(ctaModel)
//                    }
                }
                FeedCampaignRibbonType.ASGC_DISCOUNT -> {
                    Toast.makeText(
                        root.context,
                        "${ctaModel.text} ${ctaModel.subtitle.joinToString(" ")}",
                        Toast.LENGTH_SHORT
                    ).show()
                    tyFeedCampaignRibbonTitle.text = ctaModel.text
//                    renderRibbonByType(campaign.isReminderActive)
//                    startDelayProcess(TWO_SECOND) {
//                        setBackgroundGradient(ctaModel)
//                    }
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING -> {
                    Toast.makeText(
                        root.context,
                        "Flash Sale Ongoing",
                        Toast.LENGTH_SHORT
                    ).show()

                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient(ctaModel)

                        startDelayProcess(THREE_SECOND) {
                            // TODO
                        }
                    }
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING -> {
                    Toast.makeText(
                        root.context,
                        "Flash Sale Upcoming",
                        Toast.LENGTH_SHORT
                    ).show()
                    root.setTransition(root.currentState, R.id.initial_title_with_icon)
                    root.setTransitionDuration(SIX_MILISECOND.toInt())
                    root.transitionToEnd()
                    root.progress = 1f

                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient(ctaModel)

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(R.id.initial_title_with_icon, R.id.start_in_state)
                            root.setTransitionDuration(SIX_MILISECOND.toInt())
                            root.transitionToEnd()
                        }
                    }
                }
                else -> {}
            }
        }
    }

//    private fun renderRibbonByType(isActiveReminder: Boolean) {
//        with(binding) {
//            when (type) {
//                FeedCampaignRibbonType.ASGC_GENERAL -> {
//                    tyFeedCampaignRibbonTitle.show()
//                    tyFeedCampaignRibbonTitleSecond.hide()
//                    tyFeedCampaignRibbonSmall.hide()
//                    pbFeedCampaignRibbon.hide()
//                    timerFeedCampaignRibbon.hide()
//
//                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)
//                    Toast.makeText(root.context, "ASGC General", Toast.LENGTH_SHORT).show()
//                }
//                FeedCampaignRibbonType.ASGC_DISCOUNT -> {
//                    tyFeedCampaignRibbonTitle.show()
//                    tyFeedCampaignRibbonTitleSecond.show()
//                    tyFeedCampaignRibbonSmall.hide()
//                    pbFeedCampaignRibbon.hide()
//                    timerFeedCampaignRibbon.hide()
//
//                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)
//                    Toast.makeText(root.context, "ASGC Discount", Toast.LENGTH_SHORT).show()
//                }
//                FeedCampaignRibbonType.TITLE_ONLY -> {
//                    tyFeedCampaignRibbonTitle.show()
//                    tyFeedCampaignRibbonSmall.hide()
//                    pbFeedCampaignRibbon.hide()
//                    timerFeedCampaignRibbon.hide()
//
//                    if (isActiveReminder) {
//                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL_FILLED)
//                    } else {
//                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL)
//                    }
//                }
//                FeedCampaignRibbonType.TITLE_WITH_TIMER -> {
//                    tyFeedCampaignRibbonTitle.show()
//                    tyFeedCampaignRibbonSmall.hide()
//                    pbFeedCampaignRibbon.hide()
//                    timerFeedCampaignRibbon.show()
//
//                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)
//                }
//                FeedCampaignRibbonType.START_IN -> {
//                    tyFeedCampaignRibbonTitle.hide()
//                    tyFeedCampaignRibbonSmall.show()
//                    pbFeedCampaignRibbon.hide()
//                    timerFeedCampaignRibbon.show()
//
//                    if (isActiveReminder) {
//                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL_FILLED)
//                    } else {
//                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL)
//                    }
//                }
//                FeedCampaignRibbonType.ON_GOING -> {
//                    tyFeedCampaignRibbonTitle.hide()
//                    tyFeedCampaignRibbonSmall.show()
//                    pbFeedCampaignRibbon.show()
//                    timerFeedCampaignRibbon.show()
//
//                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)
//                }
//            }
//        }
//    }

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
//            listener.setReminder()
//            listener.removeReminder()
        }
    }

    companion object {
        private const val SIX_MILISECOND = 600L
        private const val TWO_SECOND = 2000L
        private const val THREE_SECOND = 3000L

        private const val HASH = "#"
        private const val INT_COLOR_PREFIX = "0xFF"
        private const val CORNER_RADIUS = 4f

        private const val ONE = 1f
        private const val ZERO = 0f
        private const val MINUS_ONE = -1f
    }
}
