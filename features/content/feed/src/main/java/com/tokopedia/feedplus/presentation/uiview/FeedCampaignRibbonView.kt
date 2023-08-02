package com.tokopedia.feedplus.presentation.uiview

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.TransitionDrawable
import androidx.core.content.ContextCompat
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
import com.tokopedia.feedplus.presentation.model.*
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import kotlinx.coroutines.*
import java.util.*
import com.tokopedia.unifyprinciples.R as unifyR

/**
 * Created By : Muhammad Furqan on 16/03/23
 */
enum class FeedCampaignRibbonType {
    ASGC_GENERAL, ASGC_DISCOUNT, ASGC_FLASH_SALE_UPCOMING, ASGC_FLASH_SALE_ONGOING, ASGC_SPECIAL_RELEASE_ONGOING, ASGC_SPECIAL_RELEASE_UPCOMING
}

class FeedCampaignRibbonView(
    private val binding: LayoutFeedCampaignRibbonMotionBinding,
    private val listener: FeedListener
) {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)

    private var animationJob: Job? = null

    private var type: FeedCampaignRibbonType = FeedCampaignRibbonType.ASGC_GENERAL
    private var mProduct: FeedCardProductModel? = null
    private var mAllProducts: List<FeedCardProductModel> = emptyList()
    private var mCampaign: FeedCardCampaignModel? = null
    private var mCta: FeedCardCtaModel? = null
    private var mHasVoucher: Boolean = false
    private var trackerData: FeedTrackerDataModel? = null

    private var mPostId: String = ""
    private var mAuthor: FeedAuthorModel? = null
    private var mPostType: String = ""
    private var mIsFollowing: Boolean = false
    private var feedPosition: Int = -1

    private val animationStateList = mutableListOf<Int>()

    fun bindData(
        modelType: String,
        campaign: FeedCardCampaignModel,
        ctaModel: FeedCardCtaModel,
        product: FeedCardProductModel?,
        allProducts: List<FeedCardProductModel>,
        hasVoucher: Boolean,
        isTypeHighlight: Boolean,
        trackerDataModel: FeedTrackerDataModel,
        postId: String,
        author: FeedAuthorModel,
        postType: String,
        isFollowing: Boolean,
        positionInFeed: Int
    ) {
        with(binding) {
            type = getRibbonType(modelType, campaign.isOngoing)
            mProduct = product
            mAllProducts = allProducts
            mCampaign = campaign
            mCta = ctaModel
            mHasVoucher = hasVoucher
            trackerData = trackerDataModel
            mPostId = postId
            mAuthor = author
            mPostType = postType
            mIsFollowing = isFollowing
            feedPosition = positionInFeed

            root.background = MethodChecker.getDrawable(
                root.context,
                R.drawable.feed_tag_product_background
            )

            val shouldHideRibbon =
                campaign.shortName.isEmpty() && ctaModel.texts.isEmpty()

            if (!isTypeHighlight || shouldHideRibbon) {
                root.hide()
            } else {
                root.show()
            }

            setupProgressBar()
            buildRibbonBasedOnType()
        }
    }

    fun bindProduct(
        product: FeedCardProductModel
    ) {
        mProduct = product
        if (type == FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING || type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_ONGOING) {
            setupAvailabilityProgress()
        }
    }

    fun resetView() {
        animationJob?.cancel()
        with(binding) {
            root.background = MethodChecker.getDrawable(
                root.context,
                R.drawable.feed_tag_product_background
            )
            buildRibbonBasedOnType()
        }
    }

    fun startAnimation() {
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
                    runLoopAnimation(index = START_ANIMATION_INDEX)
                }
            }
            FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING, FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING -> {
                startDelayProcess(TWO_SECOND) {
                    setBackgroundGradient()
                    runLoopAnimation(index = START_ANIMATION_INDEX)
                }
            }
        }
    }

    fun bindCampaignReminder(campaignReminderStatus: Boolean) {
        mCampaign = mCampaign?.copy(isReminderActive = campaignReminderStatus)
        if (type == FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING || type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING) {
            if (mCampaign?.isReminderActive == true) {
                binding.icFeedCampaignRibbonIcon.setImage(IconUnify.BELL_FILLED)
            } else {
                binding.icFeedCampaignRibbonIcon.setImage(IconUnify.BELL)
            }
        }
    }

    private fun setupProgressBar() {
        with(binding) {
            pbFeedCampaignRibbon.trackDrawable.setColor(
                ContextCompat.getColor(
                    root.context,
                    R.color.feed_dms_progress_bar_track_color
                )
            )
            val stockBarColor =
                ContextCompat.getColor(root.context, unifyR.color.Unify_Static_White)
            pbFeedCampaignRibbon.progressBarColor = intArrayOf(stockBarColor, stockBarColor)
        }
    }

    private fun runRecursiveDelayDiscount(index: Int) {
        mCta?.texts?.let {
            if (it.isNotEmpty()) {
                val ctaIndex = index % it.size
                if (ctaIndex < it.size) {
                    with(binding) {
                        when (root.currentState) {
                            R.id.initial_title_with_icon -> {
                                tyFeedCampaignRibbonTitleSecond.text = it[ctaIndex]

                                startDelayProcess(THREE_SECOND) {
                                    root.setTransition(
                                        root.currentState,
                                        R.id.second_title_with_icon
                                    )
                                    root.transitionToEnd()
                                    runRecursiveDelayDiscount(index + ONE)
                                }
                            }
                            R.id.second_title_with_icon -> {
                                tyFeedCampaignRibbonTitle.text = it[ctaIndex]

                                startDelayProcess(THREE_SECOND) {
                                    root.setTransition(
                                        root.currentState,
                                        R.id.initial_title_with_icon
                                    )
                                    root.transitionToEnd()
                                    runRecursiveDelayDiscount(index + ONE)
                                }
                            }
                            else -> {
                                tyFeedCampaignRibbonTitleSecond.text = it[ctaIndex]

                                startDelayProcess(THREE_SECOND) {
                                    root.setTransition(
                                        root.currentState,
                                        R.id.initial_title_with_icon
                                    )
                                    root.transitionToEnd()
                                    runRecursiveDelayDiscount(index + ONE)
                                }
                            }
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
            val currentColor = root.background
            when {
                !mCta?.colorGradient.isNullOrEmpty() -> {
                    mCta?.let { cta ->
                        val newColor = GradientDrawable(
                            GradientDrawable.Orientation.LEFT_RIGHT,
                            cta.colorGradient.map {
                                Color.parseColor(it.color)
                            }.toIntArray()
                        ).apply {
                            shape = GradientDrawable.RECTANGLE
                            cornerRadius = CORNER_RADIUS
                        }

                        val transitionDrawable = TransitionDrawable(arrayOf(currentColor, newColor))
                        root.background = transitionDrawable
                        transitionDrawable.startTransition(COLOR_TRANSITION_DURATION)
                    }
                }
                type == FeedCampaignRibbonType.ASGC_GENERAL && mCta?.colorGradient.isNullOrEmpty() -> {
                    val newColor = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_general_gradient
                    )

                    val transitionDrawable = TransitionDrawable(arrayOf(currentColor, newColor))
                    root.background = transitionDrawable
                    transitionDrawable.startTransition(COLOR_TRANSITION_DURATION)
                }
                (type == FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING || type == FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING) && mCta?.colorGradient.isNullOrEmpty() -> {
                    val newColor = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_flashsale_gradient
                    )

                    val transitionDrawable = TransitionDrawable(arrayOf(currentColor, newColor))
                    root.background = transitionDrawable
                    transitionDrawable.startTransition(COLOR_TRANSITION_DURATION)
                }
                (type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_ONGOING || type == FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING) && mCta?.colorGradient.isNullOrEmpty() -> {
                    val newColor = MethodChecker.getDrawable(
                        root.context,
                        R.drawable.bg_feed_campaign_ribbon_special_release_gradient
                    )

                    val transitionDrawable = TransitionDrawable(arrayOf(currentColor, newColor))
                    root.background = transitionDrawable
                    transitionDrawable.startTransition(COLOR_TRANSITION_DURATION)
                }
                else -> {}
            }
        }
    }

    private fun buildRibbonBasedOnType() {
        animationStateList.clear()

        with(binding) {
            when (type) {
                FeedCampaignRibbonType.ASGC_GENERAL, FeedCampaignRibbonType.ASGC_DISCOUNT -> {
                    setupAvailabilityProgress()
                    setupTimer("") {}
                    tyFeedCampaignRibbonSubtitle.text = ""

                    if (!mCta?.texts.isNullOrEmpty()) {
                        tyFeedCampaignRibbonTitle.text = mCta?.texts!![0]
                        tyFeedCampaignRibbonTitleSecond.text = mCta?.texts!![0]
                    }

                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)
                    icFeedCampaignRibbonIcon.setOnClickListener {
                        mAuthor?.let { author ->
                            mCampaign?.let { campaign ->
                                listener.onASGCGeneralClicked(
                                    mPostId,
                                    author,
                                    mPostType,
                                    mIsFollowing,
                                    campaign,
                                    mHasVoucher,
                                    mAllProducts,
                                    trackerData
                                )
                            }
                        }
                    }

                    root.setTransition(root.currentState, R.id.initial_title_with_icon)
                    root.transitionToEnd()

                    root.setOnClickListener {
                        mAuthor?.let { author ->
                            mCampaign?.let { campaign ->
                                listener.onASGCGeneralClicked(
                                    mPostId,
                                    author,
                                    mPostType,
                                    mIsFollowing,
                                    campaign,
                                    mHasVoucher,
                                    mAllProducts,
                                    trackerData
                                )
                            }
                        }
                    }
                }

                FeedCampaignRibbonType.ASGC_FLASH_SALE_ONGOING, FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_ONGOING -> {
                    setupAvailabilityProgress()

                    tyFeedCampaignRibbonTitle.text = mCampaign?.shortName.orEmpty()
                    tyFeedCampaignRibbonTitleSecond.text = mCampaign?.shortName.orEmpty()
                    icFeedCampaignRibbonIcon.setImage(IconUnify.CHEVRON_RIGHT)

                    setupTimer(mCampaign?.endTime ?: "") {}

                    icFeedCampaignRibbonIcon.setOnClickListener {
                        mAuthor?.let { author ->
                            mCampaign?.let { campaign ->
                                listener.onOngoingCampaignClicked(
                                    mPostId,
                                    author,
                                    mPostType,
                                    mIsFollowing,
                                    campaign,
                                    mHasVoucher,
                                    mAllProducts,
                                    trackerData,
                                    campaign.shortName,
                                    feedPosition
                                )
                            }
                        }
                    }

                    root.setTransition(root.currentState, R.id.initial_title_with_timer_and_icon)
                    root.transitionToEnd()

                    animationStateList.add(R.id.initial_title_with_timer_and_icon)
                    animationStateList.add(R.id.availability_state)

                    root.setOnClickListener {
                        mAuthor?.let { author ->
                            mCampaign?.let { campaign ->
                                listener.onOngoingCampaignClicked(
                                    mPostId,
                                    author,
                                    mPostType,
                                    mIsFollowing,
                                    campaign,
                                    mHasVoucher,
                                    mAllProducts,
                                    trackerData,
                                    campaign.shortName,
                                    feedPosition
                                )
                            }
                        }
                    }
                }

                FeedCampaignRibbonType.ASGC_FLASH_SALE_UPCOMING, FeedCampaignRibbonType.ASGC_SPECIAL_RELEASE_UPCOMING -> {
                    setupAvailabilityProgress()

                    tyFeedCampaignRibbonTitle.text = mCampaign?.shortName
                    tyFeedCampaignRibbonTitleSecond.text = mCampaign?.shortName
                    tyFeedCampaignRibbonSubtitle.text =
                        root.context.getString(R.string.feed_campaign_start_from_label)
                    setupTimer(mCampaign?.startTime ?: "") {}

                    if (mCampaign?.isReminderActive == true) {
                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL_FILLED)
                    } else {
                        icFeedCampaignRibbonIcon.setImage(IconUnify.BELL)
                    }

                    icFeedCampaignRibbonIcon.setOnClickListener {
                        listener.onReminderClicked(
                            mCampaign?.id.toLongOrZero(),
                            !(mCampaign?.isReminderActive ?: false),
                            trackerData,
                            type
                        )
                    }

                    root.setTransition(root.currentState, R.id.initial_title_with_icon)
                    root.transitionToEnd()

                    animationStateList.add(R.id.initial_title_with_icon)
                    animationStateList.add(R.id.start_in_state)

                    root.setOnClickListener {}
                }
            }
        }
    }

    private fun setupAvailabilityProgress() {
        with(binding) {
            val value = getProgressValue()

            tyFeedCampaignRibbonSubtitle.text = when {
                value < SEVENTY_FIVE_PERCENT -> root.context.getString(R.string.feed_cta_available_label)
                value < EIGHTY_FIVE_PERCENT -> root.context.getString(R.string.feed_cta_hyped_label)
                value < HUNDRED_PERCENT -> root.context.getString(R.string.feed_cta_almost_sold_label)
                value == HUNDRED_PERCENT -> root.context.getString(R.string.feed_cta_sold_label)
                else -> ""
            }

            pbFeedCampaignRibbon.setValue(value)
        }
    }

    private fun getProgressValue(): Int = if (mProduct == null) {
        SEVENTY_FIVE_PERCENT
    } else if (mProduct!!.stockSoldPercentage > 1) {
        mProduct!!.stockSoldPercentage.toIntSafely()
    } else {
        (mProduct!!.stockSoldPercentage * 100).toIntSafely()
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
                binding.timerFeedCampaignRibbon.show()
            }
        } ?: binding.timerFeedCampaignRibbon.hide()
    }

    private fun startDelayProcess(delayDurationInMilis: Long, block: () -> Unit) {
        animationJob?.cancel()
        animationJob = scope.launch {
            delay(delayDurationInMilis)
            block()
        }
    }

    private fun runLoopAnimation(index: Int) {
        if (animationStateList.isNotEmpty()) {
            val animationIndex = index % animationStateList.size

            with(binding) {
                startDelayProcess(THREE_SECOND) {
                    if (animationStateList.size > animationIndex) {
                        root.setTransition(root.currentState, animationStateList[animationIndex])
                        root.transitionToEnd()
                    }
                    runLoopAnimation(index + ONE)
                }
            }
        }
    }

    companion object {
        private const val TWO_SECOND = 2000L
        private const val THREE_SECOND = 3000L
        private const val COLOR_TRANSITION_DURATION = 250

        private const val SEVENTY_FIVE_PERCENT = 75
        private const val EIGHTY_FIVE_PERCENT = 85
        private const val HUNDRED_PERCENT = 100

        private const val CORNER_RADIUS = 20f
        private const val START_ANIMATION_INDEX = 1
        private const val ONE = 1
    }
}
