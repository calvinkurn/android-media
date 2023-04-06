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
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
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
    ASGC_FLASH_SALE_UPCOMING,
    ASGC_FLASH_SALE_ONGOING,
    ASGC_SPECIAL_RELEASE_ONGOING,
    ASGC_SPECIAL_RELEASE_UPCOMING
}

class FeedCampaignRibbonView(
    private val binding: LayoutFeedCampaignRibbonMotionBinding,
    private val listener: FeedListener
) {
    private val scope = CoroutineScope(Dispatchers.Main)

    private var type: FeedCampaignRibbonType = FeedCampaignRibbonType.ASGC_GENERAL
    private var mProduct: FeedCardProductModel? = null
    private var mCampaign: FeedCardCampaignModel? = null
    private var mCta: FeedCardCtaModel? = null
    private var mHasVoucher: Boolean = false

    fun bindData(
        modelType: String,
        campaign: FeedCardCampaignModel,
        ctaModel: FeedCardCtaModel,
        product: FeedCardProductModel?,
        hasVoucher: Boolean,
        isTypeHighlight: Boolean,
    ) {
        with(binding) {
            type = getRibbonType(modelType, campaign.isOngoing)
            mProduct = product
            mCampaign = campaign
            mCta = ctaModel
            mHasVoucher = hasVoucher

            val shouldHideRibbon =
                campaign.shortName.isEmpty() && ctaModel.text.isEmpty() && ctaModel.subtitle.isEmpty()

            if (!isTypeHighlight || shouldHideRibbon) {
                root.hide()
            } else {
                root.show()
            }

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
                        setBackgroundGradient()

                        runRecursiveDelayDiscount(START_ANIMATION_INDEX)
                    }
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING, FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_ONGOING -> {
                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient()

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(root.currentState, R.id.availability_state)
                            root.transitionToEnd()
                        }
                    }
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING, FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING -> {
                    startDelayProcess(TWO_SECOND) {
                        setBackgroundGradient()

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(root.currentState, R.id.start_in_state)
                            root.transitionToEnd()
                        }
                    }
                }
            }
        }
    }

    private fun runRecursiveDelayDiscount(index: Int) {
        if ((mCta?.texts?.size ?: 0) > index) {
            with(binding) {
                when (root.currentState) {
                    R.id.initial_title_with_icon -> {
                        tyFeedCampaignRibbonTitleSecond.text = mCta?.texts?.get(index) ?: ""

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(root.currentState, R.id.second_title_with_icon)
                            root.transitionToEnd()
                            runRecursiveDelayDiscount(index + ONE)
                        }
                    }
                    R.id.second_title_with_icon -> {
                        tyFeedCampaignRibbonTitle.text = mCta?.texts?.get(index) ?: ""

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(root.currentState, R.id.initial_title_with_icon)
                            root.transitionToEnd()
                            runRecursiveDelayDiscount(index + ONE)
                        }
                    }
                    else -> {
                        tyFeedCampaignRibbonTitleSecond.text = mCta?.texts?.get(index) ?: ""

                        startDelayProcess(THREE_SECOND) {
                            root.setTransition(
                                root.currentState,
                                R.id.second_title_with_icon
                            )
                            root.transitionToEnd()
                            runRecursiveDelayDiscount(index + ONE)
                        }
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
        type == TYPE_FEED_ASGC_SPECIAL_RELEASE && isOngoing -> FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_ONGOING
        type == TYPE_FEED_ASGC_SPECIAL_RELEASE && !isOngoing -> FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING
        else -> FeedCampaignRibbonType.ASGC_GENERAL
    }

    private fun setBackgroundGradient() {
        with(binding) {
            when {
                !mCta?.colorGradient.isNullOrEmpty() -> {
                    mCta?.let { cta ->
                        root.background = GradientDrawable(
                            GradientDrawable.Orientation.LEFT_RIGHT,
                            cta.colorGradient.map {
                                Color.parseColor(it.color)
                            }.toIntArray()
                        ).apply {
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = CORNER_RADIUS
                        }
                    }
                }
                type == FeedCampaignRibbonType.ASGC_GENERAL && mCta?.colorGradient.isNullOrEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_general_gradient
                    )
                }
                (type == FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING || type == FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING) && mCta?.colorGradient.isNullOrEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_flashsale_gradient
                    )
                }
                (type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_ONGOING || type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING) && mCta?.colorGradient.isNullOrEmpty() -> {
                    root.background = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_special_release_gradient
                    )
                }
                else -> {}
            }
        }
    }

    private fun buildRibbonBasedOnType() {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL,
                FeedCampaignRibbonType.ASGC_DISCOUNT -> {
                    if (!mCta?.texts.isNullOrEmpty()) {
                        tyFeedCampaignRibbonTitle.text = mCta?.texts!![0]
                    } else {
                        tyFeedCampaignRibbonTitle.text = mCta?.text
                    }

                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)
                    icFeedCampaignRibbonIcon.setOnClickListener { }
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING,
                FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_ONGOING -> {
                    tyFeedCampaignRibbonTitle.text = mCampaign?.shortName
                    tyFeedCampaignRibbonSubtitle.text = mProduct?.stockWording
                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)

                    val value = getProgressValue()
                    pbFeedCampaignRibbon.setValue(value, true)

                    setupTimer(mCampaign?.endTime ?: "") {}
                    icFeedCampaignRibbonIcon.setOnClickListener { }
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING,
                FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING -> {
                    tyFeedCampaignRibbonTitle.text = mCampaign?.shortName
                    setupTimer(mCampaign?.startTime ?: "") {}

                    if (mCampaign?.isReminderActive == true) {
                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL_FILLED)
                    } else {
                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL)
                    }

                    icFeedCampaignRibbonIcon.setOnClickListener {
                        listener.onReminderClicked(
                            mCampaign?.id.toLongOrZero(),
                            !(mCampaign?.isReminderActive ?: false)
                        )
                    }
                }
            }
        }
    }

    private fun getProgressValue(): Int = if (mProduct == null) {
        SEVENTY_FIVE_PERCENT
    } else if (mProduct!!.stockSoldPercentage > 1) {
        mProduct!!.stockSoldPercentage.toIntSafely()
    } else {
        (mProduct!!.stockSoldPercentage * 100).toIntSafely()
    }

    private fun resetAnimationBasedOnType() {
        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL,
                FeedCampaignRibbonType.ASGC_DISCOUNT,
                FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING,
                FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING -> {
                    root.setTransition(
                        root.currentState,
                        R.id.initial_title_with_icon
                    )
                    root.transitionToEnd()
                    root.progress = 1f
                }
                FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING,
                FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_ONGOING -> {
                    root.setTransition(
                        root.currentState,
                        R.id.initial_title_with_timer_and_icon
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

    companion object {
        private const val TWO_SECOND = 2000L
        private const val THREE_SECOND = 3000L

        private const val SEVENTY_FIVE_PERCENT = 75

        private const val CORNER_RADIUS = 20f
        private const val START_ANIMATION_INDEX = 1
        private const val ONE = 1
    }
}
