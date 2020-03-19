package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
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
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.presentation.adapter.CouponAdapter
import com.tokopedia.gamification.giftbox.presentation.helpers.*
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.COUPON_ONLY
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.COUPON_WITH_POINTS
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.POINTS_ONLY
import com.tokopedia.utils.image.ImageUtils

class RewardContainer : FrameLayout {

    lateinit var tvSmallReward: AppCompatTextView
    lateinit var imageSmallReward: AppCompatImageView
    lateinit var llRewardTextLayout: RelativeLayout
    lateinit var rvCoupons: RecyclerView
    lateinit var imageGreenGlow: AppCompatImageView
    lateinit var imageGlowCircleSmall: AppCompatImageView
    lateinit var imageGlowCircleLarge: AppCompatImageView
    lateinit var imageCircleReward: AppCompatImageView

    lateinit var couponAdapter: CouponAdapter

    val couponList = ArrayList<GetCouponDetail>()
    val FADE_OUT_REWARDS_DURATION = 1000L


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
        LayoutInflater.from(context).inflate(R.layout.view_reward_container, this, true)

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

        rvCoupons.layoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            val millsPerInch = 150f
            override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
                val linearSmoothScroller = object : LinearSmoothScroller(context) {
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                        return millsPerInch / displayMetrics.densityDpi
                    }
                }
                linearSmoothScroller.targetPosition = position
                startSmoothScroll(linearSmoothScroller)
            }
        }

        rvCoupons.addItemDecoration(CouponItemDecoration())
        couponAdapter = CouponAdapter(couponList)
        rvCoupons.adapter = couponAdapter


        doOnLayout {
            setGreenGlowImagePosition(imageGreenGlow)
            setImageGlowCircle(imageGlowCircleSmall, imageGlowCircleLarge, imageCircleReward)
        }
    }

    fun setRewards(rewardEntity: GiftBoxRewardEntity, asyncCallback: ((rewardState:  Int) -> Unit)) {
        var hasPoints = false
        var hasCoupons = false

        //set coupons if available
        val list = rewardEntity.couponDetailResponse?.couponList
        if (list != null && list.isNotEmpty()) {
            hasCoupons = true
            couponList.clear()
            couponList.addAll(list)
            couponAdapter.notifyDataSetChanged()
        }

        //set coins
        var iconUrl: String? = ""
        rewardEntity.crackResult.benefits?.let {
            it.forEach { benefit ->
                if (benefit.benefitType != "coupons") {
                    hasPoints = true
                    tvSmallReward.text = benefit.text
                    if (!benefit.color.isNullOrEmpty()) {
                        tvSmallReward.setTextColor(Color.parseColor(benefit.color))
                    }
                    iconUrl = benefit.imageUrl
                }
            }
        }

        if (hasPoints && hasCoupons) {
            rewardState = RewardState.COUPON_WITH_POINTS
            ImageUtils.loadImage(imageSmallReward, iconUrl!!)
        } else if (hasPoints) {
            //only points
            rewardState = RewardState.POINTS_ONLY
            if(!iconUrl.isNullOrEmpty()){
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

        val largeDimen = (this@RewardContainer.width * 0.6).toInt()
        val smallDimen = (this@RewardContainer.width * 0.4).toInt()
        val circleRewardDimen = (this@RewardContainer.width * 0.25).toInt()
        largeImage.updateLayoutParams<FrameLayout.LayoutParams> {
            this.width = largeDimen
            this.height = largeDimen
        }

        smallImage.updateLayoutParams<FrameLayout.LayoutParams> {
            this.width = smallDimen
            this.height = smallDimen
        }

        circleReward.updateLayoutParams<FrameLayout.LayoutParams> {
            this.width = circleRewardDimen
            this.height = circleRewardDimen
        }
    }

    fun showSingleLargeRewardAnimation(giftBoxTop: Int): Animator {
        val anim1 = getGreenGlowingAndTwoCirclesAnimation(giftBoxTop)
        val anim2 = largeImageRewardAnimation(imageCircleReward)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(anim1, anim2)
        return animatorSet
    }

    fun showSingleLargeRewardAnimationFadeOut(): Animator {

        val anim2 = largeImageRewardAnimation(imageCircleReward)

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(imageCircleReward, alphaProp)
        alphaAnim.duration = FADE_OUT_REWARDS_DURATION

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(anim2, alphaAnim)
        return animatorSet
    }

    fun showSingleLargeRewardAnimationFadeOut(startDelay: Long): Animator {

        val anim2 = largeImageRewardAnimation(imageCircleReward)
        anim2.startDelay = startDelay

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(imageCircleReward, alphaProp)
        alphaAnim.duration = FADE_OUT_REWARDS_DURATION

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(anim2, alphaAnim)
        return animatorSet
    }

    fun showCouponAndRewardAnimation(giftBoxTop: Int): Animator {
        val anim1 = getGreenGlowingAndTwoCirclesAnimation(giftBoxTop)
        val anim2 = rvCouponsAnimations()

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(anim1, anim2)
        animatorSet.playTogether(anim1)
        return animatorSet
    }

    fun showCouponAndRewardAnimationFadeOut(startDelay: Long): Animator {
        val anim2 = rvCouponsAnimations()
        anim2.startDelay = startDelay

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(rvCoupons, alphaProp)
        alphaAnim.duration = FADE_OUT_REWARDS_DURATION

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(anim2, alphaAnim)
        return animatorSet
    }

    fun showCouponAndRewardAnimationFadeOut(): Animator {
        val anim2 = rvCouponsAnimations()

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(rvCoupons, alphaProp)
        alphaAnim.duration = FADE_OUT_REWARDS_DURATION

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(anim2, alphaAnim)
        return animatorSet
    }

    private fun largeImageRewardAnimation(view: View): Animator {

        val duration = 800L
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

    fun setFinalTranslationOfCircles(giftBoxTop: Int) {
        val largeY = giftBoxTop - imageGlowCircleLarge.height.toFloat()
        imageGlowCircleLarge.translationY = largeY
        imageGlowCircleSmall.translationY = largeY + (imageGlowCircleLarge.height - imageGlowCircleSmall.height) / 2f
        imageCircleReward.translationY = largeY + (imageGlowCircleLarge.height - imageCircleReward.height) / 3f
    }

    fun ovoPointsTextAnimation(): Animator {
        return scaleAndAlphaAnimation(llRewardTextLayout)
    }

    fun ovoPointsTextAnimationFadeOut(): Animator {
        val anim1 = ovoPointsTextAnimation()

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(llRewardTextLayout, alphaProp)
        alphaAnim.duration = FADE_OUT_REWARDS_DURATION

        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(anim1, alphaAnim)
        return animatorSet
    }

    private fun rvCouponsAnimations(): Animator {
        val couponAnimator = scaleAndAlphaAnimation(rvCoupons)
        couponAnimator.addListener(onEnd = {
            rvCoupons.smoothScrollToPosition(couponAdapter.itemCount - 1)
        })
        return couponAnimator
    }

    private fun concentricCircleAnimation(smallImage: View, largeImage: View, giftboxTop: Int, isInfinite: Boolean): Animator {

        val largeY = giftboxTop + dpToPx(30f) - largeImage.height.toFloat()
        val smallY = largeY + (largeImage.height - smallImage.height) / 2f
        val circleRewardY = smallY + (smallImage.height - imageCircleReward.height) / 2f
        largeImage.translationY = largeY
        smallImage.translationY = smallY
        imageCircleReward.translationY = circleRewardY


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

    private fun scaleAndAlphaAnimation(view: View): Animator {
        val duration = 800L
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

    fun setPositionOfViews(height: Float, statusBarHeight: Float) {
        llRewardTextLayout.translationY = height * 0.385f
//        rvCoupons.translationY = height * 0.1281125f
//        rvCoupons.translationY = (height * 0.0969f) + statusBarHeight
    }

    private fun dpToPx(dp: Float): Float {
        return dp * (context.resources.displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
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
}