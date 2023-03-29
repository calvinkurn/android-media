package com.tokopedia.feedplus.presentation.uiview

import android.graphics.drawable.GradientDrawable
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.feedcomponent.util.TimeConverter
import com.tokopedia.feedplus.R
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_NEW_PRODUCTS
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_RESTOCK
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_SHOP_DISCOUNT
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_SHOP_FLASH_SALE
import com.tokopedia.feedplus.data.FeedXCard.Companion.TYPE_FEED_ASGC_SPECIAL_RELEASE
import com.tokopedia.feedplus.databinding.LayoutFeedCampaignRibbonMotionBinding
import com.tokopedia.feedplus.presentation.adapter.listener.FeedListener
import com.tokopedia.feedplus.presentation.model.FeedCardCampaignModel
import com.tokopedia.feedplus.presentation.model.FeedCardCtaModel
import com.tokopedia.feedplus.presentation.model.FeedCardProductModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

/**
 * Created By : Muhammad Furqan on 16/03/23
 */
enum class FeedCampaignRibbonType {
    ASGC_GENERAL, ASGC_DISCOUNT,
    ASGC_FLASH_SALE_UPCOMING,
    ASGC_FLASH_SALE_ONGOING,
    ASGC_SPECIAL_RELEASE
}

class FeedCampaignRibbonView(
    private val binding: LayoutFeedCampaignRibbonMotionBinding,
    private val listener: FeedListener
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    var type: FeedCampaignRibbonType = FeedCampaignRibbonType.ASGC_GENERAL

    fun bindData(
        modelType: String,
        campaign: FeedCardCampaignModel,
        ctaModel: FeedCardCtaModel,
        product: FeedCardProductModel?,
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
            buildRibbonBasedOnType(product, campaign, ctaModel)
        }
    }

    private fun getRibbonType(type: String, isOngoing: Boolean) = when {
        type == TYPE_FEED_ASGC_RESTOCK || type == TYPE_FEED_ASGC_NEW_PRODUCTS -> FeedCampaignRibbonType.ASGC_GENERAL
        type == TYPE_FEED_ASGC_SHOP_DISCOUNT -> FeedCampaignRibbonType.ASGC_DISCOUNT
        type == TYPE_FEED_ASGC_SHOP_FLASH_SALE && isOngoing -> FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING
        type == TYPE_FEED_ASGC_SHOP_FLASH_SALE && !isOngoing -> FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING
        type == TYPE_FEED_ASGC_SPECIAL_RELEASE -> FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE
        else -> FeedCampaignRibbonType.ASGC_GENERAL
    }

    private fun setBackgroundGradient(cta: FeedCardCtaModel) {
        with(binding) {
            when {
                type == FeedCampaignRibbonType.ASGC_GENERAL && cta.colorGradient.isEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_general_gradient
                    )
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
                (type == FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING || type == FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING) && cta.colorGradient.isNotEmpty() -> {
                    root.background = GradientDrawable(
                        GradientDrawable.Orientation.LEFT_RIGHT,
                        cta.colorGradient.map {
                            it.color.replace(HASH, INT_COLOR_PREFIX).toIntSafely()
                        }.toIntArray()
                    ).apply {
                        cornerRadius = CORNER_RADIUS
                    }
                }
                (type == FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING || type == FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING) && cta.colorGradient.isEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_flashsale_gradient
                    )
                }
                type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE && cta.colorGradient.isEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_special_release_gradient
                    )
                }
                type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE && cta.colorGradient.isNotEmpty() -> {
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

    private fun buildRibbonBasedOnType(
        product: FeedCardProductModel?,
        campaign: FeedCardCampaignModel,
        ctaModel: FeedCardCtaModel
    ) {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL -> {
                    tyFeedCampaignRibbonTitle.text =
                        "${ctaModel.text} ${ctaModel.subtitle.joinToString(" ")}"

                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient(ctaModel)
                    }
                }
                FeedCampaignRibbonType.ASGC_DISCOUNT -> {
                    tyFeedCampaignRibbonTitle.text = ctaModel.text
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING -> {
                    tyFeedCampaignRibbonTitle.text = campaign.shortName
                    tyFeedCampaignRibbonSubtitle.text = product?.stockWording
                    val value = (((product?.stockSoldPercentage ?: 0.75f) * 100) / 100).roundToInt()
                    pbFeedCampaignRibbon.setValue(value, true)

                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient(ctaModel)

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(root.currentState, R.id.availability_state)
                            root.transitionToEnd()
                        }
                    }
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING -> {
                    root.setTransition(
                        R.id.initial_title_with_timer_and_icon,
                        R.id.initial_title_with_timer_and_icon
                    )
                    root.transitionToEnd()
                    root.progress = 1f

                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient(ctaModel)

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(root.currentState, R.id.start_in_state)
                            root.transitionToEnd()
                        }
                    }
                }
                FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE -> {
                    tyFeedCampaignRibbonTitle.text =
                        "${ctaModel.text} ${ctaModel.subtitle.joinToString(" ")}"

                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient(ctaModel)
                    }
                }
                else -> {}
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
