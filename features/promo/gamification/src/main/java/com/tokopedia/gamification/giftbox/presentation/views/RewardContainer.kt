package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tkpd.remoteresourcerequest.view.ImageDensityType
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType
import com.tokopedia.gamification.giftbox.presentation.helpers.doOnLayout
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.gamification.giftbox.presentation.helpers.updateLayoutParams
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.AdapterType.Companion.AdapterTypeDaily
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.AdapterType.Companion.AdapterTypeOvo
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.AdapterType.Companion.AdapterTypeTapTap
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardSourceType.Companion.DAILY
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardSourceType.Companion.TAP_TAP
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.COUPON_ONLY
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.COUPON_WITH_POINTS
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.POINTS_ONLY
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer.RewardState.Companion.RP_0_ONLY
import com.tokopedia.utils.image.ImageUtils

class RewardContainer @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RewardContainerDaily(context, attrs, defStyleAttr) {

    lateinit var tvSmallReward: AppCompatTextView
    lateinit var imageSmallReward: DeferredImageView
    lateinit var llRewardTextLayout: RelativeLayout
    lateinit var imageGlowCircleSmall: AppCompatImageView
    lateinit var imageGlowCircleLarge: AppCompatImageView
    lateinit var imageGreenGlow: AppCompatImageView

    val FADE_OUT_REWARDS_DURATION_TAP_TAP = 600L
    override fun getLayoutId() = R.layout.view_reward_container

    companion object {
        const val NEGATIVE_DELAY_FOR_LARGE_REWARD_ANIM = 250L
    }

    override fun initViews() {
        super.initViews()
        tvSmallReward = findViewById(R.id.tv_small_reward)
        imageSmallReward = findViewById(R.id.image_small_reward)
        llRewardTextLayout = findViewById(R.id.ll_reward_text)
        imageGlowCircleSmall = findViewById(R.id.image_glow_circle_small)
        imageGlowCircleLarge = findViewById(R.id.image_glow_circle_large)
        imageGreenGlow = findViewById(R.id.image_green_glow)
        llRewardTextLayout.alpha = 0f

        doOnLayout {
            setGreenGlowImagePosition(imageGreenGlow)
            setImageGlowCircle(imageGlowCircleSmall, imageGlowCircleLarge, imageCircleReward)
        }
    }

    override fun readAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray =
                    context.theme.obtainStyledAttributes(it, R.styleable.RewardContainer, 0, 0)
            sourceType = typedArray.getInt(R.styleable.RewardContainer_source, RewardSourceType.TAP_TAP)
            typedArray.recycle()
        }
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

    override fun setRewards(rewardEntity: GiftBoxRewardEntity, asyncCallback: ((rewardState: Int) -> Unit)) {
        var hasPoints = false
        var hasCoupons = false

        //set coupons if available
        val list = rewardEntity.couponDetailResponse?.couponDetailList
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
                    GtmEvents.viewRewardsPoints(benefit.benefitType, benefit.text, userSession?.userId)
                    iconUrl = benefit.imageUrl
                } else if (benefit.benefitType == BenefitType.COUPON) {
                    benefit.referenceID?.let {
                        GtmEvents.viewRewards(benefit.benefitType, it.toString(), userSession?.userId)
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
                imageSmallReward.loadRemoteImageDrawable("ic_ovo.png", ImageDensityType.SUPPORT_MULTIPLE_DPI)
                imageCircleReward.loadRemoteImageDrawable("ic_ovo.png", ImageDensityType.SUPPORT_MULTIPLE_DPI)
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

    fun setImageGlowCircle(smallImage: View?, largeImage: View?, circleReward: View?) {
        smallImage?.alpha = 0f
        largeImage?.alpha = 0f
        circleReward?.alpha = 0f
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

    override fun setFinalTranslationOfCirclesTap(giftBoxTop: Int) {
        val largeY = giftBoxTop + dpToPx(30) - imageGlowCircleLarge.height.toFloat()
        val smallY = largeY + (imageGlowCircleLarge.height - imageGlowCircleSmall.height) / 2f
        val circleRewardY = smallY + (imageGlowCircleSmall.height - imageCircleReward.height) / 2f
        imageGlowCircleLarge.translationY = largeY
        imageGlowCircleSmall.translationY = smallY
        imageCircleReward.translationY = circleRewardY
    }

    override fun showCouponAndRewardAnimation(giftBoxTop: Int): Animator {
        val anim1 = getGreenGlowingAndTwoCirclesAnimation(giftBoxTop)
        val anim2 = rvCouponsAnimations()

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(anim1, anim2)
        animatorSet.playTogether(anim1)
        return animatorSet
    }

    fun getGreenGlowingAndTwoCirclesAnimation(giftBoxTop: Int, isInfinite: Boolean = false): Animator {
        val greenAnimator = greenGlowAlphaAnimation(isInfinite)

        val circleAnimator = concentricCircleAnimation(imageGlowCircleSmall, imageGlowCircleLarge, giftBoxTop, isInfinite)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(greenAnimator, circleAnimator)
        return animatorSet
    }

    fun ovoPointsTextAnimation(duration: Long = 800L): Animator {
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

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(COUPON_ONLY, POINTS_ONLY, COUPON_WITH_POINTS,RP_0_ONLY)
    annotation class RewardState {
        companion object {
            const val COUPON_ONLY = 1
            const val POINTS_ONLY = 2
            const val COUPON_WITH_POINTS = 3
            const val RP_0_ONLY = 4
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(DAILY, TAP_TAP)
    annotation class RewardSourceType {
        companion object {
            const val DAILY = 1
            const val TAP_TAP = 2
        }
    }

    @Retention(AnnotationRetention.SOURCE)
    @IntDef(AdapterTypeDaily, AdapterTypeTapTap, AdapterTypeOvo)
    annotation class AdapterType {
        companion object {
            const val AdapterTypeDaily = 10
            const val AdapterTypeTapTap = 20
            const val AdapterTypeOvo = 30
        }
    }
}