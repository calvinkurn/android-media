package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.data.entities.CouponType
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.presentation.adapter.CouponAdapter
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType
import com.tokopedia.gamification.giftbox.presentation.helpers.*
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.COUPON_ONLY
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.COUPON_WITH_POINTS
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.POINTS_ONLY
import com.tokopedia.user.session.UserSession
import com.tokopedia.utils.image.ImageUtils

class RewardContainer : FrameLayout {

    lateinit var tvSmallReward: AppCompatTextView
    lateinit var imageSmallReward: DeferredImageView
    lateinit var llRewardTextLayout: RelativeLayout
    lateinit var rvCoupons: RecyclerView
    lateinit var imageGreenGlow: AppCompatImageView
    lateinit var imageGlowCircleSmall: AppCompatImageView
    lateinit var imageGlowCircleLarge: AppCompatImageView
    lateinit var imageCircleReward: DeferredImageView

    lateinit var couponAdapter: CouponAdapter

    val couponList = ArrayList<CouponType>()
    val FADE_OUT_REWARDS_DURATION = 1000L
    val FADE_OUT_REWARDS_DURATION_TAP_TAP = 600L
    val FADE_IN_REWARDS_DURATION_TAP_TAP = 400L
    var userSession: UserSession? = null

    @RewardState
    var rewardState: Int = RewardState.COUPON_ONLY

    companion object {
        const val NEGATIVE_DELAY_FOR_LARGE_REWARD_ANIM = 250L
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
            context,
            attrs,
            defStyleAttr
    ) {
        init(attrs)
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    fun init(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(com.tokopedia.gamification.R.layout.view_reward_container, this, true)

        tvSmallReward = findViewById(R.id.tv_small_reward)
        imageSmallReward = findViewById(R.id.image_small_reward)
        rvCoupons = findViewById(R.id.rv_coupons)
        llRewardTextLayout = findViewById(R.id.ll_reward_text)
        imageGlowCircleSmall = findViewById(R.id.image_glow_circle_small)
        imageGlowCircleLarge = findViewById(R.id.image_glow_circle_large)
        imageGreenGlow = findViewById(R.id.image_green_glow)
        imageCircleReward = findViewById(R.id.image_circle_reward)

        llRewardTextLayout.alpha = 0f
        rvCoupons.alpha = 0f

        rvCoupons.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        var isTablet = context?.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet)
        var listItemWidthInTablet = context?.resources?.getDimension(com.tokopedia.gamification.R.dimen.gami_rv_coupons_width)
        if (isTablet == null) {
            isTablet = false
        }

        if (listItemWidthInTablet == null) {
            listItemWidthInTablet = 0f
        }

        rvCoupons.addItemDecoration(CouponItemDecoration(isTablet,
                listItemWidthInTablet.toInt(),
                getScreenWidth(),
                rvCoupons.context.resources.getDimension(R.dimen.gami_rv_coupons_top_margin).toInt(),
                rvCoupons.context.resources.getDimension(R.dimen.gami_rv_coupons_right_margin).toInt()
        ))
        couponAdapter = CouponAdapter(couponList, isTablet)
        rvCoupons.adapter = couponAdapter

        userSession = UserSession(context)

        doOnLayout {
            setGreenGlowImagePosition(imageGreenGlow)
            setImageGlowCircle(imageGlowCircleSmall, imageGlowCircleLarge, imageCircleReward)
        }
    }

    fun setRewards(rewardEntity: GiftBoxRewardEntity, asyncCallback: ((rewardState: Int) -> Unit)) {
        var hasPoints = false
        var hasCoupons = false

        //set coupons if available
        val list = rewardEntity.couponDetailResponse?.couponMap?.values
        if (list != null && list.isNotEmpty()) {
            hasCoupons = true
            couponList.clear()
            couponList.addAll(list)
            couponAdapter.notifyDataSetChanged()
        }

        //set coins
        var iconUrl: String? = ""
        rewardEntity.gamiCrack.benefits?.let {
            it.forEach { benefit ->
                if (benefit.benefitType != BenefitType.COUPON) {
                    hasPoints = true
                    tvSmallReward.text = benefit.text
                    if (!benefit.color.isNullOrEmpty()) {
                        tvSmallReward.setTextColor(Color.parseColor(benefit.color))
                    }
                    GtmEvents.viewRewardsPoints(benefit.text, userSession?.userId)
                    iconUrl = benefit.imageUrl
                } else if (benefit.benefitType == BenefitType.COUPON) {
                    benefit.referenceID?.let {
                        GtmEvents.viewRewards(it.toString(), userSession?.userId)
                    }
                }
            }
        }

        if (hasPoints && hasCoupons) {
            rewardState = RewardState.COUPON_WITH_POINTS
            if (!iconUrl.isNullOrEmpty()) {
                ImageUtils.loadImage(imageSmallReward, iconUrl!!)
            }
        } else if (hasPoints) {
            //only points
            rewardState = RewardState.POINTS_ONLY
            if (!iconUrl.isNullOrEmpty()) {
                ImageUtils.loadImage(imageSmallReward, iconUrl!!)
                ImageUtils.loadImage(imageCircleReward, iconUrl!!)
            }
        } else if (hasCoupons) {
            rewardState = RewardState.COUPON_ONLY
        }
        asyncCallback.invoke(rewardState)
    }

    fun setGreenGlowImagePosition(view: View) {
        view.alpha = 0f
        view.updateLayoutParams<FrameLayout.LayoutParams> {
            this.width = this@RewardContainer.width
            this.height = this@RewardContainer.height / 2
        }
    }

    fun setImageGlowCircle(smallImage: View, largeImage: View, circleReward: View) {
        smallImage.alpha = 0f
        largeImage.alpha = 0f
        circleReward.alpha = 0f
    }

    fun showSingleLargeRewardAnimation(giftBoxTop: Int): Animator {
        val anim1 = getGreenGlowingAndTwoCirclesAnimation(giftBoxTop)
        val anim2 = largeImageRewardAnimation(imageCircleReward)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(anim1, anim2)
        return animatorSet
    }

    fun showSingleLargeRewardAnimationFadeOut(startDelay: Long): Pair<Animator, Long> {

        val anim2 = largeImageRewardAnimation(imageCircleReward, FADE_IN_REWARDS_DURATION_TAP_TAP)
        anim2.startDelay = startDelay

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(imageCircleReward, alphaProp)
        alphaAnim.duration = FADE_OUT_REWARDS_DURATION_TAP_TAP

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(anim2, alphaAnim)
        return Pair(animatorSet, FADE_OUT_REWARDS_DURATION_TAP_TAP + FADE_IN_REWARDS_DURATION_TAP_TAP)
    }

    fun showCouponAndRewardAnimation(giftBoxTop: Int): Animator {
        val anim1 = getGreenGlowingAndTwoCirclesAnimation(giftBoxTop)
        val anim2 = rvCouponsAnimations()

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(anim1, anim2)
        animatorSet.playTogether(anim1)
        return animatorSet
    }

    fun showCouponAndRewardAnimationFadeOut(startDelay: Long): Pair<Animator, Long> {

        val anim2 = rvCouponsAnimations() //500
        anim2.startDelay = if (startDelay > 0)
            startDelay + GiftBoxTapTapView.REWARD_START_DELAY
        else
            0

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(rvCoupons, alphaProp)
        alphaAnim.duration = FADE_OUT_REWARDS_DURATION_TAP_TAP

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(anim2, alphaAnim)
        return Pair(animatorSet, FADE_IN_REWARDS_DURATION_TAP_TAP + FADE_OUT_REWARDS_DURATION_TAP_TAP)
    }

    private fun largeImageRewardAnimation(view: View, duration: Long = 800L): Animator {

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

    private fun getGreenGlowingAndTwoCirclesAnimation(giftBoxTop: Int, isInfinite: Boolean = false): Animator {
        val greenAnimator = greenGlowAlphaAnimation(isInfinite)

        val circleAnimator = concentricCircleAnimation(imageGlowCircleSmall, imageGlowCircleLarge, giftBoxTop, isInfinite)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(greenAnimator, circleAnimator)
        return animatorSet
    }

    fun setFinalTranslationOfCirclesTap(giftBoxTop: Int) {
        val largeY = giftBoxTop + dpToPx(30) - imageGlowCircleLarge.height.toFloat()
        val smallY = largeY + (imageGlowCircleLarge.height - imageGlowCircleSmall.height) / 2f
        val circleRewardY = smallY + (imageGlowCircleSmall.height - imageCircleReward.height) / 2f
        imageGlowCircleLarge.translationY = largeY
        imageGlowCircleSmall.translationY = smallY
        imageCircleReward.translationY = circleRewardY
    }

    fun ovoPointsTextAnimation(duration:Long = 800L): Animator {
        return scaleAndAlphaAnimation(llRewardTextLayout, duration)
    }

    fun ovoPointsTextAnimationFadeOut(): Animator {
        val anim1 = ovoPointsTextAnimation(FADE_IN_REWARDS_DURATION_TAP_TAP)

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(llRewardTextLayout, alphaProp)
        alphaAnim.duration = FADE_OUT_REWARDS_DURATION_TAP_TAP

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(anim1, alphaAnim)
        return animatorSet
    }

    private fun rvCouponsAnimations(): Animator {
        return scaleAndAlphaAnimation(rvCoupons, FADE_IN_REWARDS_DURATION_TAP_TAP)
    }

    private fun concentricCircleAnimation(smallImage: View, largeImage: View, giftboxTop: Int, isInfinite: Boolean): Animator {

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

    fun greenGlowAlphaAnimation(isInfinite: Boolean): ObjectAnimator {
        val duration = 1400L
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(imageGreenGlow, alphaProp)
        alphaAnim.repeatCount = if (isInfinite) ValueAnimator.INFINITE else 5
        alphaAnim.repeatMode = ValueAnimator.REVERSE
        alphaAnim.duration = duration
        return alphaAnim
    }

    private fun scaleAndAlphaAnimation(view: View, duration: Long = 800L): Animator {

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

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(COUPON_ONLY, POINTS_ONLY, COUPON_WITH_POINTS)
    annotation class RewardState {
        companion object {
            const val COUPON_ONLY = 1
            const val POINTS_ONLY = 2
            const val COUPON_WITH_POINTS = 3
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
}