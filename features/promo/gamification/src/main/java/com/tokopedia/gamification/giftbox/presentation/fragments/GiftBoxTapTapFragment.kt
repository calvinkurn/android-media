package com.tokopedia.gamification.giftbox.presentation.fragments

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.activities.GiftLauncherActivity
import com.tokopedia.gamification.giftbox.presentation.helpers.CubicBezierInterpolator
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener

import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxDailyView
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxTapTapView
import com.tokopedia.gamification.giftbox.presentation.views.RewardSummaryView

class GiftBoxTapTapFragment : GiftBoxBaseFragment() {

    lateinit var tvTimer: AppCompatTextView
    lateinit var progressBarTimer: ProgressBar
    lateinit var tvProgressCount: AppCompatTextView
    lateinit var imageWaktu: AppCompatImageView
    lateinit var imageHabis: AppCompatImageView
    lateinit var fmWaktuHabis: FrameLayout
    lateinit var rewardSummary: RewardSummaryView

    var colorDim: Int = 0
    var colorBlackTransParent: Int = 0
    var hourCountDownTimer: CountDownTimer? = null
    var minuteCountDownTimer: CountDownTimer? = null


    override fun getLayout() = R.layout.fragment_gift_tap_tap

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        colorDim = ContextCompat.getColor(activity!!, R.color.gf_dim)
        colorBlackTransParent = ContextCompat.getColor(activity!!, R.color.gf_black_transparent)
        showLoader()
        v.postDelayed({
            hideLoader()
            renderHourTimerState()
            giftBoxDailyView.startInitialAnimation()?.start()
        }, 1000L)
        return v
    }

    private fun setListeners() {
        giftBoxDailyView.boxCallback = object : GiftBoxDailyView.BoxCallback {

            override fun onBoxOpenAnimationStart(startDelay: Long) {
                rewardContainer.setFinalTranslationOfCircles(giftBoxDailyView.fmGiftBox.top)

                val stageLightAnim = giftBoxDailyView.stageGlowAnimation()
                stageLightAnim.startDelay = startDelay
                val greenGlowAnim = rewardContainer.greenGlowAlphaAnimation(true)
                greenGlowAnim.startDelay = startDelay

                when (GiftLauncherActivity.uiType) {
                    GiftLauncherActivity.UiType.COUPON_POINTS -> {

                        val anim1 = rewardContainer.showCouponAndRewardAnimationFadeOut(startDelay)

                        val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimationFadeOut()
                        ovoPointsTextAnim.startDelay = startDelay + 100L

                        val animatorSet = AnimatorSet()
                        animatorSet.playTogether(stageLightAnim, greenGlowAnim, anim1, ovoPointsTextAnim)
                        ovoPointsTextAnim.addListener(onEnd = { afterRewardAnimationEnds() })
                        animatorSet.start()
                    }
                    GiftLauncherActivity.UiType.POINTS -> {


                        val anim = rewardContainer.showSingleLargeRewardAnimationFadeOut(startDelay)
                        anim.addListener(onEnd = { afterRewardAnimationEnds() })

                        val animatorSet = AnimatorSet()
                        animatorSet.playTogether(stageLightAnim, greenGlowAnim, anim)
                        animatorSet.start()
                    }
                }
            }

            override fun onBoxScaleDownAnimationStart() {
                fadeOutViews()
            }

            override fun onBoxOpened() {
                val startsAnimatorList = starsContainer.getStarsAnimationList()
                startsAnimatorList.forEach {
                    it.start()
                }
            }
        }

        (giftBoxDailyView as GiftBoxTapTapView).boxRewardCallback = object : GiftBoxDailyView.BoxRewardCallback {
            override fun showPoints() {
                val anim1 = rewardContainer.showSingleLargeRewardAnimationFadeOut()

                val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimationFadeOut()
                ovoPointsTextAnim.startDelay = 100L

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(anim1, ovoPointsTextAnim)
                animatorSet.addListener(onEnd = { afterRewardAnimationEnds() })
                animatorSet.start()
            }

            override fun showPointsWithCoupons() {
                val anim1 = rewardContainer.showCouponAndRewardAnimationFadeOut()

                val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimationFadeOut()
                ovoPointsTextAnim.startDelay = 100L

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(anim1, ovoPointsTextAnim)
                animatorSet.addListener(onEnd = { afterRewardAnimationEnds() })
                animatorSet.start()
            }

            override fun showCoupons() {
                val anim1 = rewardContainer.showCouponAndRewardAnimationFadeOut()

                anim1.addListener(onEnd = { afterRewardAnimationEnds() })
                anim1.start()
            }
        }
    }

    fun afterRewardAnimationEnds() {
        (giftBoxDailyView as GiftBoxTapTapView).isGiftTapAble = true
    }

    override fun initialViewSetup() {
        super.initialViewSetup()
        tvTimer.alpha = 0f
        progressBarTimer.alpha = 0f
        tvProgressCount.alpha = 0f
        imageHabis.alpha = 0f
        imageWaktu.alpha = 0f

    }

    override fun initViews(v: View) {
        tvTimer = v.findViewById(R.id.tv_timer)
        progressBarTimer = v.findViewById(R.id.progress_bar_timer)
        tvProgressCount = v.findViewById(R.id.tv_progress_count)
        imageHabis = v.findViewById(R.id.image_habis)
        imageWaktu = v.findViewById(R.id.image_waktu)
        fmWaktuHabis = v.findViewById(R.id.fm_waktu_habis)
        rewardSummary = v.findViewById(R.id.rewardSummary)
        super.initViews(v)
        setListeners()
    }

    fun showRewardSummary() {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(rewardSummary, alphaProp)

        alphaAnim.duration = 700L
        alphaAnim.startDelay = 3000L

        alphaAnim.addListener(onEnd = {
            rewardSummary.playRewardItemAnimation()
        })
        alphaAnim.start()
    }

    fun renderHourTimerState() {
        renderBottomHourTimer()
        fadeInActiveStateViews()
    }

    fun startOneMinuteCounter() {
        //todo this time should come from backend
        val time = 10 * 1000L
        minuteCountDownTimer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                timeUpAnimation()
                (giftBoxDailyView as GiftBoxTapTapView).isTimeOut = true
            }

            override fun onTick(millisUntilFinished: Long) {
                var seconds = millisUntilFinished / 1000
                tvProgressCount.text = "$seconds"
                progressBarTimer.progress = 100 - (seconds / 60f * 100).toInt()
            }
        }

        minuteCountDownTimer?.start()
    }

    fun renderBottomHourTimer() {
        //todo this time should come from backend
        val time = 60 * 60 * 1000L
        hourCountDownTimer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                (giftBoxDailyView as GiftBoxTapTapView).glowingAnimator?.end()
                val anim1 = (giftBoxDailyView as GiftBoxTapTapView).fadeOutGlowingAndFadeInGiftBoxAnimation()
                anim1.addListener(onEnd = {
                    (giftBoxDailyView as GiftBoxTapTapView).isGiftTapAble = true
                })
                anim1.start()
                animateTvTimerAndProgressBar()
                startOneMinuteCounter()
            }

            override fun onTick(millisUntilFinished: Long) {
                var seconds = millisUntilFinished / 1000
                var minutes = seconds / 60
                val hour = minutes / 60
                minutes %= 60
                seconds %= 60
                tvTimer.text = "${hour}:${minutes}:${seconds}"
            }
        }

        hourCountDownTimer?.start()
        //show hour timer

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(tvTimer, alphaProp)
        alphaAnim.duration = 500L
        alphaAnim.start()

        tvTimer.postDelayed(Runnable { hourCountDownTimer?.onFinish() }, 3 * 1000L)
    }

    fun animateTvTimerAndProgressBar() {
        tvTimer.animate().alpha(0f).setDuration(600L).start()
        progressBarTimer.animate().alpha(1f).setStartDelay(300L).setDuration(600L).start()
        tvProgressCount.animate().alpha(1f).setStartDelay(300L).setDuration(600L).start()

    }


    fun fadeInActiveStateViews() {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)
        val giftBoxAnim = ObjectAnimator.ofPropertyValuesHolder(giftBoxDailyView, alphaProp)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapHintAnim, giftBoxAnim)
        animatorSet.duration = FADE_IN_DURATION

        animatorSet.addListener(onEnd = {
            //            giftBoxDailyView.startInitialAnimation()
        })
        animatorSet.start()
    }

    fun timeUpAnimation() {
        val height = screenHeight
        val width = screenWidth

        val translateAnimationDuration = 250L
//        val translateAnimationDuration = 850L

        //waktu 30% from top and right
        val waktuStartX = width.toFloat()
        val waktuStartY = height * 0.3f

        val waktuFinalX = width * 0.225f
        val waktuFinalY = height * 0.4234375f


        //habis 70% from top and left
        val habisStartX = -imageHabis.width.toFloat()
        val habisStartY = height * 0.7f

        val habisFinalX = width * 0.199f
        val habisFinalY = height * 0.4921875f

        val waktuPropX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, waktuStartX, waktuFinalX)
        val waktuPropY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, waktuStartY, waktuFinalY)

        val propAlpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)

        val translateWaktuAnim = ObjectAnimator.ofPropertyValuesHolder(imageWaktu, waktuPropX, waktuPropY, propAlpha)
        translateWaktuAnim.duration = translateAnimationDuration

        val habisPropX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, habisStartX, habisFinalX)
        val habisPropY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, habisStartY, habisFinalY)

        val translateHabisAnim = ObjectAnimator.ofPropertyValuesHolder(imageHabis, habisPropX, habisPropY, propAlpha)
        translateHabisAnim.duration = translateAnimationDuration

        //set background to dim

        val colorAnimator = ObjectAnimator.ofObject(fmWaktuHabis, "backgroundColor", ArgbEvaluator(), Color.BLACK, colorDim)
        colorAnimator.duration = 250L

        //intial yellow to white
        val waktuImageAnimation = ValueAnimator.ofInt(1)
        waktuImageAnimation.addUpdateListener {
            imageWaktu.setImageResource(R.drawable.gf_ic_waktu_white)
        }
        waktuImageAnimation.startDelay = translateAnimationDuration - 100L

        val habisImageAnimation = ValueAnimator.ofInt(1)
        habisImageAnimation.addUpdateListener {
            imageHabis.setImageResource(R.drawable.gf_ic_habis_white)
        }
        habisImageAnimation.startDelay = translateAnimationDuration - 100L

        //translation for 45 degree from cx and cy
        val distance = dpToPx(6f)
        val habisPropX45 = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, habisFinalX, habisFinalX - distance, habisFinalX)
        val habisPropY45 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, habisFinalY, habisFinalY + distance, habisFinalY)
        val habisTranslateAt45 = ObjectAnimator.ofPropertyValuesHolder(imageHabis, habisPropX45, habisPropY45)
        habisTranslateAt45.duration = 100L

        val waktuPropX45 = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, waktuFinalX, waktuFinalX + distance, waktuFinalX)
        val waktuPropY45 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, waktuFinalY, waktuFinalY - distance, waktuFinalY)
        val waktuTranslateAt45 = ObjectAnimator.ofPropertyValuesHolder(imageWaktu, waktuPropX45, waktuPropY45)
        waktuImageAnimation.duration = 100L

        val animatorSetAfterCollision = AnimatorSet()
        animatorSetAfterCollision.playTogether(habisTranslateAt45, waktuTranslateAt45)

        //last image animation
        val waktuFinalImages = arrayOf(R.drawable.gf_ic_waktu, R.drawable.gf_ic_waktu_white, R.drawable.gf_ic_waktu)
        val waktuFinalImageAnimation = ValueAnimator.ofInt(0, 1, 2)
        waktuFinalImageAnimation.addUpdateListener {
            val index = it.animatedValue as Int
            imageWaktu.setImageResource(waktuFinalImages[index])
        }

        val habisFinalImages = arrayOf(R.drawable.gf_ic_habis, R.drawable.gf_ic_habis_white, R.drawable.gf_ic_habis)
        val habisFinalImageAnimation = ValueAnimator.ofInt(0, 1, 2)
        habisFinalImageAnimation.addUpdateListener {
            val index = it.animatedValue as Int
            imageHabis.setImageResource(habisFinalImages[index])
        }

        val finalImageAnimatorSet = AnimatorSet()
        finalImageAnimatorSet.playTogether(waktuFinalImageAnimation, habisFinalImageAnimation)
        finalImageAnimatorSet.duration = 100L


        val animatorSet = AnimatorSet()
        animatorSet.playTogether(translateHabisAnim, translateWaktuAnim, colorAnimator, waktuImageAnimation, habisImageAnimation)
        animatorSet.playSequentially(translateHabisAnim, animatorSetAfterCollision, finalImageAnimatorSet)
        animatorSet.interpolator = CubicBezierInterpolator(0.22, 1.0, 0.36, 1.0)
        animatorSet.start()
    }

    fun fadeOutViews() {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapHintAnim)
        animatorSet.duration = 300L

        animatorSet.start()
    }

    enum class GiftBoxTapTapState {
        HOUR_TIMER, MINUTE_TIMER, ERROR, NO_INTERNET
    }

}