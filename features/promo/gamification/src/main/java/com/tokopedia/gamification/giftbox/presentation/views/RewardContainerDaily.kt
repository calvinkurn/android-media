package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.data.entities.CouponType
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.data.entities.OvoListItem
import com.tokopedia.gamification.giftbox.presentation.adapter.CouponAdapter
import com.tokopedia.gamification.giftbox.presentation.fragments.DisplayType
import com.tokopedia.gamification.giftbox.presentation.helpers.CouponItemDecoration
import com.tokopedia.gamification.giftbox.presentation.helpers.CubicBezierInterpolator
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.image.ImageUtils
import timber.log.Timber
import kotlin.math.min

open class RewardContainerDaily @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var rvCoupons: RecyclerView
    lateinit var imageCircleReward: DeferredImageView

    val couponList = ArrayList<CouponType>()
    lateinit var couponAdapter: CouponAdapter

    @RewardContainer.RewardState
    var rewardState: Int = RewardContainer.RewardState.COUPON_ONLY

    @RewardContainer.RewardSourceType
    var sourceType = RewardContainer.RewardSourceType.DAILY

    var userSession: UserSession? = null
    val FADE_IN_REWARDS_DURATION_TAP_TAP = 400L
    var isTablet = false
    var cancelAutoScroll = false

    open fun getLayoutId() = R.layout.view_reward_container_daily

    init {
        setup(attrs)
    }

    fun setup(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(getLayoutId(), this, true)
        readAttrs(attrs)
        initViews()
    }

    open fun readAttrs(attrs: AttributeSet?) {
        isTablet = context.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet) ?: false
    }

    @SuppressLint("ClickableViewAccessibility")
    open fun initViews() {
        rvCoupons = findViewById(R.id.rv_coupons)
        imageCircleReward = findViewById(R.id.image_circle_reward)

        val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        this.layoutParams = lp
        rvCoupons.alpha = 0f
        rvCoupons.hasFixedSize()

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        layoutManager.isItemPrefetchEnabled = true
        rvCoupons.layoutManager = layoutManager

        var isTablet = context?.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet)
        var listItemWidthInTablet = context?.resources?.getDimension(com.tokopedia.gamification.R.dimen.gami_rv_coupons_width)
        if (isTablet == null) {
            isTablet = false
        }
        if (listItemWidthInTablet == null) {
            listItemWidthInTablet = 0f
        }

        if (sourceType == RewardContainer.RewardSourceType.DAILY) {
            rvCoupons.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && !cancelAutoScroll && canAutoScroll()) {
                        performRvScrollAnimation(false)
                    }
                }
            })

            rvCoupons.setOnTouchListener { _, event ->
                when (event?.action) {
                    MotionEvent.ACTION_DOWN,
                    MotionEvent.ACTION_MOVE -> cancelAutoScroll = true
                }
                false
            }
        }

        rvCoupons.addItemDecoration(CouponItemDecoration(isTablet,
                listItemWidthInTablet.toInt(),
                getScreenWidth(),
                rvCoupons.context.resources.getDimension(R.dimen.gami_rv_coupons_top_margin).toInt(),
                rvCoupons.context.resources.getDimension(R.dimen.gami_rv_coupons_right_margin).toInt()
        ))

        couponAdapter = CouponAdapter(sourceType, couponList, isTablet)
        rvCoupons.adapter = couponAdapter

        userSession = UserSession(context)
        imageCircleReward.alpha = 0f
        imageCircleReward.scaleX = 0f
        imageCircleReward.scaleY = 0f

    }

    open fun showCouponAndRewardAnimation(giftBoxTop: Int): Animator {
        val anim2 = rvCouponsAnimations()
        anim2.addListener(onEnd = {
            if (canAutoScroll()) {
                val delay = 700L
                val handler = Handler()
                handler.postDelayed({
                    performRvScrollAnimation(true)
                }, delay)

            }
        })
        return anim2
    }

    private fun canAutoScroll(): Boolean {
        return couponList.size > 1
    }

    private fun performRvScrollAnimation(toEnd: Boolean) {
        try {
            val baseDuration = 7000
            val maxDuration = 35000
            var scrollDistance = 0
            val animDuration = min((couponList.size - 1) * baseDuration, maxDuration)
            if (isTablet) {
                scrollDistance = (couponList.size - 1) * context.resources.getDimension(R.dimen.gami_rv_coupons_width).toInt()
            } else {
                scrollDistance = (couponList.size - 1) * rvCoupons.width
            }
            scrollDistance = if (toEnd) scrollDistance else -scrollDistance
            rvCoupons.smoothScrollBy(scrollDistance, 0, null, animDuration)
        } catch (th: Throwable) {
            Timber.e(th)
        }

    }

    fun rvCouponsAnimations(): Animator {
        return scaleAndAlphaAnimation(rvCoupons, FADE_IN_REWARDS_DURATION_TAP_TAP)
    }

    fun scaleAndAlphaAnimation(view: View, duration: Long = 800L): Animator {

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(view, alphaProp)

        val scalePropX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1f)
        val scalePropY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1f)
        val scaleAnim = ObjectAnimator.ofPropertyValuesHolder(view, scalePropX, scalePropY)
        val interpolator = CubicBezierInterpolator(.56, 1.59, .49, 1.02)

        scaleAnim.interpolator = interpolator

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleAnim, alphaAnim)
        animatorSet.duration = duration
        return animatorSet
    }

    open fun setFinalTranslationOfCirclesTap(giftBoxTop: Int) {
        val translation = giftBoxTop - 124.toPx().toFloat()
        imageCircleReward.translationY = translation
    }

    fun concentricCircleAnimation(smallImage: View, largeImage: View, giftboxTop: Int, isInfinite: Boolean): Animator {

        setFinalTranslationOfCirclesTap(giftboxTop)

        fun circlesAlphaAnimator(view: View): Animator {
            val duration = 1400L
            val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
            val animator = ObjectAnimator.ofPropertyValuesHolder(view, alphaProp)
            animator.repeatCount = if (isInfinite) ValueAnimator.INFINITE else 5
            animator.duration = duration
            animator.repeatMode = ValueAnimator.REVERSE
            return animator
        }

        val smallImageAnimator = circlesAlphaAnimator(smallImage)
        smallImageAnimator.startDelay = 700L
        val largeImageAlphaAnimator = circlesAlphaAnimator(largeImage)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(smallImageAnimator, largeImageAlphaAnimator)
        return animatorSet
    }

    open fun setRewards(rewardEntity: GiftBoxRewardEntity, asyncCallback: ((rewardState: Int) -> Unit)) {
        var hasCoupons = false
        var isRp0 = false

        val list = rewardEntity.couponDetailResponse?.couponDetailList
        if (list != null && list.isNotEmpty()) {
            hasCoupons = true
            couponList.clear()
            couponList.addAll(list)
        }

        rewardEntity.gamiCrack.benefits?.let {
            it.forEach { benefit ->
                when (benefit.displayType) {
                    DisplayType.CARD -> {
                        hasCoupons = true
                        couponList.add(OvoListItem(benefit.imageUrl, benefit.text))
                        GtmEvents.viewRewardsPoints(benefit.benefitType, benefit.text, userSession?.userId)
                    }
                    DisplayType.CATALOG -> {
                        hasCoupons = true
                        benefit.referenceID?.let {refId->
                            GtmEvents.viewRewards(benefit.benefitType,refId, userSession?.userId)
                        }
                    }
                    DisplayType.IMAGE -> {
                        isRp0 = true
                        if(!benefit.imageUrl.isNullOrEmpty()){
                            ImageUtils.loadImage(imageCircleReward, benefit.imageUrl, isAnimate = false)
                            GtmEvents.viewRewards(benefit.benefitType,benefit.text, userSession?.userId)
                        }
                    }
                }
            }

            if (hasCoupons) {
                rewardState = RewardContainer.RewardState.COUPON_ONLY
                couponAdapter.notifyDataSetChanged()
            } else if(isRp0){
                rewardState = RewardContainer.RewardState.RP_0_ONLY
            }

            asyncCallback.invoke(rewardState)
        }
    }

        fun getScreenWidth(): Int {
            val displayMetrics = DisplayMetrics()
            var width = 0
            if (context is Activity) {
                (context as Activity).windowManager.defaultDisplay.getMetrics(displayMetrics)
                width = displayMetrics.widthPixels
            }
            return width
        }

        fun showSingleLargeRewardAnimation(): Animator {
            return largeImageRewardAnimation(imageCircleReward)
        }

        fun largeImageRewardAnimation(view: View, duration: Long = 800L): Animator {

            val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
            val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(view, alphaProp)

            val scalePropX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f, 1.3f, 1f)
            val scalePropY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f, 1.3f, 1f)
            val scaleAnim = ObjectAnimator.ofPropertyValuesHolder(view, scalePropX, scalePropY)

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(alphaAnim, scaleAnim)
            animatorSet.duration = duration
            return animatorSet
        }
    }