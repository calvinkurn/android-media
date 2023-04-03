package com.tokopedia.feedplus.presentation.uiview

import android.graphics.Color
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
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
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
    var product: FeedCardProductModel? = null
    var mCampaign: FeedCardCampaignModel? = null
    var mCta: FeedCardCtaModel? = null
    var mHasVoucher: Boolean = false

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
            mCampaign = campaign
            mCta = ctaModel
            mHasVoucher = hasVoucher

            buildRibbonBasedOnType()
        }
    }

    fun resetView() {
        with(binding) {
            root.background = MethodChecker.getDrawable(
                root.context,
                R.drawable.feed_tag_product_background
            )
            resetAnimationBasedOnType()
        }
    }

    fun startAnimation() {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL -> {
                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient()
                    }
                }
                FeedCampaignRibbonType.ASGC_DISCOUNT -> {
                    startDelayProcess(TWO_SECOND) {
                    }
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING -> {
                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient()

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(root.currentState, R.id.availability_state)
                            root.transitionToEnd()
                        }
                    }
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING -> {
                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient()

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(root.currentState, R.id.start_in_state)
                            root.transitionToEnd()
                        }
                    }
                }
                FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE -> {
                    tyFeedCampaignRibbonTitle.text =
                        "${mCta?.text} ${mCta?.subtitle?.joinToString(" ")}"

                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient()
                    }
                }
            }
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

    private fun setBackgroundGradient() {
        with(binding) {
            when {
                type == FeedCampaignRibbonType.ASGC_GENERAL && mCta?.colorGradient.isNullOrEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_general_gradient
                    )
                }
                type == FeedCampaignRibbonType.ASGC_GENERAL && !mCta?.colorGradient.isNullOrEmpty() -> {
                    mCta?.let { cta ->
                        root.background = GradientDrawable(
                            GradientDrawable.Orientation.LEFT_RIGHT,
                            cta.colorGradient.map {
                                Color.parseColor(it.color)
                            }.toIntArray()
                        ).apply {
                            cornerRadius = CORNER_RADIUS
                        }
                    }
                }
                (type == FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING || type == FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING) && !mCta?.colorGradient.isNullOrEmpty() -> {
                    mCta?.let { cta ->
                        root.background = GradientDrawable(
                            GradientDrawable.Orientation.LEFT_RIGHT,
                            cta.colorGradient.map {
                                Color.parseColor(it.color)
                            }.toIntArray()
                        ).apply {
                            cornerRadius = CORNER_RADIUS
                        }
                    }
                }
                (type == FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING || type == FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING) && mCta?.colorGradient.isNullOrEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_flashsale_gradient
                    )
                }
                type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE && mCta?.colorGradient.isNullOrEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_special_release_gradient
                    )
                }
                type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE && !mCta?.colorGradient.isNullOrEmpty() -> {
                    mCta?.let { cta ->
                        root.background = GradientDrawable(
                            GradientDrawable.Orientation.LEFT_RIGHT,
                            cta.colorGradient.map {
                                Color.parseColor(it.color)
                            }.toIntArray()
                        ).apply {
                            cornerRadius = CORNER_RADIUS
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun buildRibbonBasedOnType() {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL -> {
                    tyFeedCampaignRibbonTitle.text =
                        "${mCta?.text} ${mCta?.subtitle?.joinToString(" ")}"
                }
                FeedCampaignRibbonType.ASGC_DISCOUNT -> {
                    tyFeedCampaignRibbonTitle.text = mCta?.text
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING -> {
                    tyFeedCampaignRibbonTitle.text = mCampaign?.shortName
                    tyFeedCampaignRibbonSubtitle.text = product?.stockWording
                    val value = (((product?.stockSoldPercentage ?: 0.75f) * 100) / 100).roundToInt()
                    pbFeedCampaignRibbon.setValue(value, true)

                    setupTimer(mCampaign?.endTime ?: "") {}
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING -> {
                    tyFeedCampaignRibbonTitle.text = mCampaign?.shortName
                }
                FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE -> {
                    tyFeedCampaignRibbonTitle.text =
                        "${mCta?.text} ${mCta?.subtitle?.joinToString(" ")}"
                }
            }
        }
    }

    private fun resetAnimationBasedOnType() {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL -> {
                    root.setTransition(
                        root.currentState,
                        R.id.initial_title_with_icon
                    )
                    root.transitionToEnd()
                    root.progress = 1f
                }
                FeedCampaignRibbonType.ASGC_DISCOUNT -> {
                    root.setTransition(
                        root.currentState,
                        R.id.initial_title_with_icon
                    )
                    root.transitionToEnd()
                    root.progress = 1f
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING -> {
                    root.setTransition(
                        root.currentState,
                        R.id.initial_title_with_timer_and_icon
                    )
                    root.transitionToEnd()
                    root.progress = 1f
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING -> {
                    root.setTransition(
                        root.currentState,
                        R.id.initial_title_with_icon
                    )
                    root.transitionToEnd()
                    root.progress = 1f
                }
                FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE -> {
                    root.setTransition(
                        root.currentState,
                        R.id.initial_title_with_icon
                    )
                    root.transitionToEnd()
                    root.progress = 1f
                }
            }
        }
    }

    private fun setupTimer(timeTarget: String, onFinish: () -> Unit) {
        val targetCalendar = TimeConverter.convertToCalendar(timeTarget)
        targetCalendar?.let {
            if (it.timeInMillis > Calendar.getInstance().timeInMillis) {
                binding.timerFeedCampaignRibbon.apply {
                    this.timerFormat = TimerUnifySingle.FORMAT_AUTO
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

        private const val CORNER_RADIUS = 4f

        private const val ONE = 1f
        private const val ZERO = 0f
        private const val MINUS_ONE = -1f
    }
}
