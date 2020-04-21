package com.tokopedia.gamification.giftbox.presentation.fragments

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.device.info.DeviceConnectionInfo
import com.tokopedia.gamification.R
import com.tokopedia.gamification.data.entity.CrackBenefitEntity
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxComponent
import com.tokopedia.gamification.giftbox.data.entities.GetCouponDetail
import com.tokopedia.gamification.giftbox.presentation.helpers.CubicBezierInterpolator
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.helpers.doOnLayout
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxTapTapViewModel
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxDailyView
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxTapTapView
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer
import com.tokopedia.gamification.giftbox.presentation.views.RewardSummaryView
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.image.ImageUtils
import javax.inject.Inject

class GiftBoxTapTapFragment : GiftBoxBaseFragment() {

    lateinit var tvTimer: Typography
    lateinit var progressBarTimer: ProgressBar
    lateinit var tvProgressCount: Typography
    lateinit var imageWaktu: AppCompatImageView
    lateinit var imageHabis: AppCompatImageView
    lateinit var fmWaktuHabis: FrameLayout
    lateinit var rewardSummary: RewardSummaryView

    var colorDim: Int = 0
    var colorBlackTransParent: Int = 0
    var hourCountDownTimer: CountDownTimer? = null
    var minuteCountDownTimer: CountDownTimer? = null
    var isTimeOut = false
    @RewardContainer.RewardState
    var rewardState: Int = RewardContainer.RewardState.COUPON_ONLY

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GiftBoxTapTapViewModel


    override fun getLayout() = R.layout.fragment_gift_tap_tap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val component = DaggerGiftBoxComponent.builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
            component.inject(this)

            if (it is AppCompatActivity) {
                val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
                viewModel = viewModelProvider[GiftBoxTapTapViewModel::class.java]
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)

        colorDim = ContextCompat.getColor(activity!!, R.color.gf_dim)
        colorBlackTransParent = ContextCompat.getColor(activity!!, R.color.gf_black_transparent)
//        showLoader()
//        v.postDelayed({
//            hideLoader()
//            renderHourTimerState()
//            giftBoxDailyView.startInitialAnimation()?.start()
//        }, 1000L)
        viewModel.getGiftBoxHome()
        return v
    }

    private fun setListeners() {
        giftBoxDailyView.boxCallback = object : GiftBoxDailyView.BoxCallback {

            override fun onBoxOpenAnimationStart(startDelay: Long) {


                val stageLightAnim = giftBoxDailyView.stageGlowAnimation()
                stageLightAnim.startDelay = startDelay
                val greenGlowAnim = rewardContainer.greenGlowAlphaAnimation(true)
                greenGlowAnim.startDelay = startDelay

                when (rewardState) {
                    RewardContainer.RewardState.COUPON_WITH_POINTS -> {

                        val anim1 = rewardContainer.showCouponAndRewardAnimationFadeOut(startDelay)

                        val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimationFadeOut()
                        ovoPointsTextAnim.startDelay = startDelay + 100L

                        val animatorSet = AnimatorSet()
                        animatorSet.playTogether(stageLightAnim, greenGlowAnim, anim1, ovoPointsTextAnim)
                        ovoPointsTextAnim.addListener(onEnd = { afterRewardAnimationEnds() })
                        animatorSet.start()
                    }
                    RewardContainer.RewardState.POINTS_ONLY -> {
//
//
                        //todo Rahul temp values
//                rewardContainer.imageCircleReward.alpha = 1f
//                rewardContainer.imageGlowCircleLarge.alpha = 1f
//                rewardContainer.imageGlowCircleSmall.alpha = 1f
//                rewardContainer.imageGreenGlow.alpha = 1f

                        val anim = rewardContainer.showSingleLargeRewardAnimationFadeOut(startDelay)
                        anim.addListener(onEnd = { afterRewardAnimationEnds() })

                        val animatorSet = AnimatorSet()
                        animatorSet.playTogether(stageLightAnim, greenGlowAnim, anim)
                        animatorSet.start()
                    }
                    RewardContainer.RewardState.COUPON_ONLY -> {
                        val rewardAnim = rewardContainer.showCouponAndRewardAnimationFadeOut(startDelay)

                        val animatorSet = AnimatorSet()
                        animatorSet.playTogether(rewardAnim, stageLightAnim, greenGlowAnim)
                        animatorSet.startDelay = startDelay
                        animatorSet.start()
                    }
                }
            }

            override fun onBoxScaleDownAnimationStart() {
                fadeOutViews()
            }

            override fun onBoxOpened() {
                val startsAnimatorList = starsContainer.getStarsAnimationList(giftBoxDailyView.fmGiftBox.top)
                startsAnimatorList.forEach {
                    it.start()
                }
            }
        }

        (giftBoxDailyView as GiftBoxTapTapView).boxRewardCallback = object : GiftBoxDailyView.BoxRewardCallback {
            override fun showPoints(): Animator {
                val anim1 = rewardContainer.showSingleLargeRewardAnimationFadeOut()

                val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimationFadeOut()
                ovoPointsTextAnim.startDelay = 100L

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(anim1, ovoPointsTextAnim)
                animatorSet.addListener(onEnd = { afterRewardAnimationEnds() })
                animatorSet.start()
                return animatorSet
            }

            override fun showPointsWithCoupons(): Animator {

                val anim1 = rewardContainer.showCouponAndRewardAnimationFadeOut()

                val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimationFadeOut()
                ovoPointsTextAnim.startDelay = 100L

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(anim1, ovoPointsTextAnim)
                animatorSet.addListener(onEnd = { afterRewardAnimationEnds() })
                animatorSet.start()
                return animatorSet
            }

            override fun showCoupons(): Animator {
                val anim1 = rewardContainer.showCouponAndRewardAnimationFadeOut()

                anim1.addListener(onEnd = { afterRewardAnimationEnds() })
                anim1.start()
                return anim1
            }
        }

        viewModel.giftHomeLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    showLoader()
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    //todo add check for 200 all over api calls
                    if (it.data != null) {

                        //toolbar
                        val toolbarTitle = it.data?.gamiTapEggHome?.tokensUser?.title
                        toolbarTitle?.let { title ->
                            tvTapHint.text = title
                        }


                        //timer
                        val timeLeftHours = it.data?.gamiTapEggHome?.timeRemaining?.unixFetch
                        val timeLeftSeconds = it.data?.gamiTapEggHome?.timeRemaining?.seconds
                        val showTimer = it.data?.gamiTapEggHome?.timeRemaining?.isShow

                        //glowing mode
                        val tokenAsset = it.data?.gamiTapEggHome?.tokenAsset
                        val glowImageUrl = tokenAsset?.glowImgURL
                        val glowShadowImageUrl = tokenAsset?.glowShadowImgURL

                        val imageUrlList = tokenAsset?.imageV2URLs
                        var imageFrontUrl = ""
                        val bgImageUrl = tokenAsset?.glowShadowImgURL
                        if (imageUrlList != null && imageUrlList.isNotEmpty()) {
                            imageFrontUrl = imageUrlList[0]
                        }


                        val shouldGlow = (!glowImageUrl.isNullOrEmpty() && glowShadowImageUrl.isNullOrEmpty())
                        val tokenUserState = it.data?.gamiTapEggHome?.tokensUser?.state

                        if (showTimer != null && showTimer && timeLeftHours != null && timeLeftSeconds != null) {
                            renderBottomHourTimer(timeLeftSeconds)

                            getTapTapView().loadFilesForTapTap(tokenUserState!!,
                                    glowImageUrl,
                                    glowShadowImageUrl,
                                    imageFrontUrl,
                                    bgImageUrl!!,
                                    imageCallback = { it ->
                                        setPositionOfViewsAtBoxOpen(tokenUserState)
                                        hideLoader()
                                        fadeInActiveStateViews()
                                        getTapTapView().startInitialAnimation()?.start()
                                    }
                            )
                        }

                        getTapTapView().fmGiftBox.setOnClickListener {
                            if (isTimeOut) {
                                //Do nothing
                            } else if (getTapTapView().isGiftTapAble) {
                                getTapTapView().isGiftTapAble = false
                                if (getTapTapView().tapCount == getTapTapView().targetTapCount) {
                                    crackGiftBox()
                                    getTapTapView().targetTapCount = getTapTapView().getRandomNumber()
                                } else {
                                    getTapTapView().showConfettiAnimation()
                                }
                                getTapTapView().incrementTapCount()
                            }
                        }
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    hideLoader()
                    renderGiftBoxError("Yaah, ada gangguan koneksi. Refresh lagi untuk buka hadiahmu.", "Oke")
                }
            }
        })
        viewModel.giftCrackLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    val responseCrackResultEntity = it.data?.second
                    val couponDetailEntity = it.data?.first
                    if (it.data != null) {
                        val resultCode = responseCrackResultEntity?.crackResultEntity?.resultStatus?.code
                        if (!resultCode.isNullOrEmpty() && resultCode == "200") {

                            getTapTapView().disableConfettiAnimation = true
                            getTapTapView().resetTapCount()

                            val benefits = responseCrackResultEntity?.crackResultEntity?.benefits

                            updateRewardStateAndRewards(couponDetailEntity?.couponList, benefits)

                            if (!isTimeOut) {
                                if (!getTapTapView().isBoxAlreadyOpened) {
                                    getTapTapView().firstTimeBoxOpenAnimation()
                                    getTapTapView().isBoxAlreadyOpened = true
                                } else {
                                    getTapTapView().showRewardAnimation()
                                }

                            }

                        } else {
                            //todo Rahul show error
                        }
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                }
            }
        })
    }

    fun setPositionOfViewsAtBoxOpen(@TokenUserState state: String) {

        giftBoxDailyView.fmGiftBox.doOnLayout { fmGiftBox ->
            val heightOfRvCoupons = fmGiftBox.context.resources.getDimension(R.dimen.gami_rv_coupons_height)
            val lidTop = fmGiftBox.top
            val translationY = lidTop - heightOfRvCoupons + fmGiftBox.dpToPx(3)

            rewardContainer.rvCoupons.translationY = translationY
            val distanceFromLidTop = fmGiftBox.dpToPx(29)
            rewardContainer.llRewardTextLayout.translationY = lidTop + distanceFromLidTop

        }

        giftBoxDailyView.imageGiftBoxLid.doOnLayout { lid ->
            //todo Rahul check later, it is replaced with getStatusBarHeight
            val top = lid.top + giftBoxDailyView.fmGiftBox.top
            rewardContainer.setFinalTranslationOfCirclesTap(top)
        }

        giftBoxDailyView.imageBoxFront.doOnLayout { imageBoxFront ->
            val imageFrontTop = imageBoxFront.top + giftBoxDailyView.fmGiftBox.top
            val translationY = imageFrontTop - imageBoxFront.dpToPx(40)
            starsContainer.setStartPositionOfStars(starsContainer.width / 2f, translationY)

            //todo Rahul have to refacor this below code
            giftBoxDailyView.adjustGlowImagePosition()

        }
    }

    fun crackGiftBox() {
        viewModel.crackGiftBox()
    }


    fun renderGiftBoxError(message: String, actionText: String) {
        if (context != null) {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(viewModel::getGiftBoxHome, context!!)
            } else {
                showRedError(fmParent, message, actionText, viewModel::getGiftBoxHome)
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
        setShadows()
        setDynamicSize()
        setListeners()
    }

    fun setDynamicSize() {
        tvProgressCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40.toPx().toFloat())
        tvTimer.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24.toPx().toFloat())
    }

    fun setShadows() {
        context?.let {
            val shadowColor = ContextCompat.getColor(it, R.color.gf_box_text_shadow)
            val shadowRadius = tvProgressCount.dpToPx(5)
            val shadowOffset = tvProgressCount.dpToPx(4)

            tvProgressCount.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
            tvTimer.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
        }
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

//    fun renderHourTimerState() {
//
//    }

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

    fun renderBottomHourTimer(timeLeftSeconds: Long) {
        //todo this time should come from backend
        val time = timeLeftSeconds * 1000L
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

        tvTimer.postDelayed({ hourCountDownTimer?.onFinish() }, 8 * 1000L)
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

//        animatorSet.addListener(onEnd = {
//            //            giftBoxDailyView.startInitialAnimation()
//        })
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
        val distance = imageWaktu.dpToPx(6)
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

    fun getTapTapView(): GiftBoxTapTapView {
        return giftBoxDailyView as GiftBoxTapTapView
    }

    fun updateRewardStateAndRewards(couponDetailList: ArrayList<GetCouponDetail>?, benefits: List<CrackBenefitEntity>?) {
        var hasPoints = false
        var hasCoupons = false

        if (!couponDetailList.isNullOrEmpty()) {
            hasCoupons = true
            rewardContainer.couponList.clear()
            rewardContainer.couponList.addAll(couponDetailList)
            rewardContainer.couponAdapter.notifyDataSetChanged()
        }

        //set coins
        var iconUrl: String? = ""
        benefits?.let {
            it.forEach { benefit ->
                if (benefit.benefitType != "coupons") {
                    hasPoints = true
                    iconUrl = benefit.imageUrl
                }
            }
        }

        if (hasPoints && hasCoupons) {
            rewardState = RewardContainer.RewardState.COUPON_WITH_POINTS
            if (!iconUrl.isNullOrEmpty()) {
                ImageUtils.loadImage(rewardContainer.imageSmallReward, iconUrl!!)
            }
        } else if (hasPoints) {
            //only points
            rewardState = RewardContainer.RewardState.POINTS_ONLY
            if (!iconUrl.isNullOrEmpty()) {
                ImageUtils.loadImage(rewardContainer.imageSmallReward, iconUrl!!)
                ImageUtils.loadImage(rewardContainer.imageCircleReward, iconUrl!!)
            }
        } else if (hasCoupons) {
            rewardState = RewardContainer.RewardState.COUPON_ONLY
        }
    }

}