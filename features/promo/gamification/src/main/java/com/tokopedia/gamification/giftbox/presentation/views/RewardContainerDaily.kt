package com.tokopedia.gamification.giftbox.presentation.views

import android.animation.*
import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.data.entities.CouponType
import com.tokopedia.gamification.giftbox.data.entities.GiftBoxRewardEntity
import com.tokopedia.gamification.giftbox.data.entities.OvoListItem
import com.tokopedia.gamification.giftbox.presentation.adapter.CouponAdapter
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType
import com.tokopedia.gamification.giftbox.presentation.helpers.CouponItemDecoration
import com.tokopedia.gamification.giftbox.presentation.helpers.CubicBezierInterpolator
import com.tokopedia.user.session.UserSession

open class RewardContainerDaily @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    lateinit var rvCoupons: RecyclerView
    lateinit var imageGreenGlow: AppCompatImageView

    val couponList = ArrayList<CouponType>()
    lateinit var couponAdapter: CouponAdapter

    @RewardContainer.RewardState
    var rewardState: Int = RewardContainer.RewardState.COUPON_ONLY

    @RewardContainer.RewardSourceType
    var sourceType = RewardContainer.RewardSourceType.DAILY

    var userSession: UserSession? = null
    val FADE_IN_REWARDS_DURATION_TAP_TAP = 400L

    open val LAYOUT_ID = R.layout.view_reward_container_daily

    init {
        setup(attrs)
    }

    fun setup(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(LAYOUT_ID, this, true)
        readAttrs(attrs)
        initViews()
    }

    open fun readAttrs(attrs: AttributeSet?) {}

    open fun initViews() {
        rvCoupons = findViewById(R.id.rv_coupons)
        imageGreenGlow = findViewById(R.id.image_green_glow)

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

        couponAdapter = CouponAdapter(sourceType, couponList, isTablet)
        rvCoupons.adapter = couponAdapter

        userSession = UserSession(context)

    }

    open fun showCouponAndRewardAnimation(giftBoxTop: Int): Animator {
        val anim2 = rvCouponsAnimations()
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(anim2)
        return animatorSet
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

    fun greenGlowAlphaAnimation(isInfinite: Boolean): ObjectAnimator {
        val duration = 1400L
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(imageGreenGlow, alphaProp)
        alphaAnim.repeatCount = if (isInfinite) ValueAnimator.INFINITE else 5
        alphaAnim.repeatMode = ValueAnimator.REVERSE
        alphaAnim.duration = duration
        return alphaAnim
    }

    open fun setFinalTranslationOfCirclesTap(giftBoxTop: Int) {

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
        var hasPoints = false
        var hasCoupons = false

        //set coupons if available
        val list = rewardEntity.couponDetailResponse?.couponMap?.values
        if (list != null && list.isNotEmpty()) {
            hasCoupons = true
            couponList.clear()
            couponList.addAll(list)
            //todo Rahul remove duplicate bottom (only used for testing)
            couponList.addAll(list)
        }

        //set coins
//        var iconUrl: String? = ""
        rewardEntity.gamiCrack.benefits?.let {
            it.forEach { benefit ->
                if (benefit.benefitType == BenefitType.OVO) {
                    hasPoints = true
//                    tvSmallReward.text = benefit.text
//                    if (!benefit.color.isNullOrEmpty()) {
//                        tvSmallReward.setTextColor(Color.parseColor(benefit.color))
//                    }
                    couponList.add(OvoListItem(benefit.imageUrl,benefit.text))
                    GtmEvents.viewRewardsPoints(benefit.text, userSession?.userId)
//                    iconUrl = benefit.imageUrl
                } else if (benefit.benefitType == BenefitType.COUPON) {
                    benefit.referenceID?.let {
                        GtmEvents.viewRewards(it.toString(), userSession?.userId)
                    }
                }
            }
        }

        if (hasPoints && hasCoupons) {
            rewardState = RewardContainer.RewardState.COUPON_WITH_POINTS
//            if (!iconUrl.isNullOrEmpty()) {
//                ImageUtils.loadImage(imageSmallReward, iconUrl!!)
//            }
        } else if (hasPoints) {
            //only points
            rewardState = RewardContainer.RewardState.POINTS_ONLY
//            if (!iconUrl.isNullOrEmpty()) {
//                imageSmallReward.loadRemoteImageDrawable("ic_ovo.png", ImageDensityType.SUPPORT_MULTIPLE_DPI)
//                imageCircleReward.loadRemoteImageDrawable("ic_ovo.png", ImageDensityType.SUPPORT_MULTIPLE_DPI)
//            }
        } else if (hasCoupons) {
            rewardState = RewardContainer.RewardState.COUPON_ONLY
        }
        couponAdapter.notifyDataSetChanged()
        asyncCallback.invoke(rewardState)
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