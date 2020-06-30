package com.tokopedia.gamification.giftbox.presentation.fragments

import android.animation.*
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.gamification.R
import com.tokopedia.gamification.data.entity.CrackBenefitEntity
import com.tokopedia.gamification.data.entity.CrackButtonEntity
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.InactiveImageLoader
import com.tokopedia.gamification.giftbox.analytics.GtmGiftTapTap
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxComponent
import com.tokopedia.gamification.giftbox.data.entities.CouponTapTap
import com.tokopedia.gamification.giftbox.presentation.entities.RewardSummaryItem
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType.Companion.COUPON
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType.Companion.OVO
import com.tokopedia.gamification.giftbox.presentation.fragments.BoxState.Companion.CLOSE
import com.tokopedia.gamification.giftbox.presentation.fragments.BoxState.Companion.OPEN
import com.tokopedia.gamification.giftbox.presentation.fragments.MinuteTimerState.Companion.FINISHED
import com.tokopedia.gamification.giftbox.presentation.fragments.MinuteTimerState.Companion.NOT_STARTED
import com.tokopedia.gamification.giftbox.presentation.fragments.MinuteTimerState.Companion.STARTED
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserStateTapTap.Companion.CRACK_UNLIMITED
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserStateTapTap.Companion.EMPTY
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserStateTapTap.Companion.LOBBY
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.helpers.doOnLayout
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxTapTapViewModel
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxDailyView
import com.tokopedia.gamification.giftbox.presentation.views.GiftBoxTapTapView
import com.tokopedia.gamification.giftbox.presentation.views.RewardContainer
import com.tokopedia.gamification.giftbox.presentation.views.RewardSummaryView
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.taptap.data.entiity.BackButton
import com.tokopedia.gamification.taptap.data.entiity.GamiTapEggHome
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject
import kotlin.math.ceil

class GiftBoxTapTapFragment : GiftBoxBaseFragment() {

    lateinit var tvTimer: Typography
    lateinit var progressBarTimer: ProgressBar
    lateinit var tvProgressCount: Typography
    lateinit var rewardSummary: RewardSummaryView
    lateinit var lottieTimeUp: LottieAnimationView
    lateinit var viewDim: View
    var fmCoupons: FrameLayout? = null

    //Inactive views
    lateinit var tvInactiveTitle: Typography
    lateinit var tvInactiveMessage: Typography
    lateinit var imageInactive: AppCompatImageView
    lateinit var btnInactiveFirst: Typography
    lateinit var imageInactiveBg: AppCompatImageView

    var colorDim: Int = 0
    var colorBlackTransParent: Int = 0
    var hourCountDownTimer: CountDownTimer? = null
    var minuteCountDownTimer: CountDownTimer? = null
    var isTimeOut = false
    val rewardItems = arrayListOf<RewardSummaryItem>()
    val benefitItems = arrayListOf<Pair<CrackBenefitEntity, CrackButtonEntity?>>()
    var backButton: BackButton? = null

    @RewardContainer.RewardState
    var rewardState: Int = RewardContainer.RewardState.COUPON_ONLY

    @BoxState
    var boxState: Int = CLOSE

    @MinuteTimerState
    var minuteTimerState: Int = NOT_STARTED

    val CONTAINER_INACTIVE = 2
    val SERVER_LIMIT_REACHED = "47004"
    val STATUS_OK = "200"
    var MAX_REWARD_LIMIT = -1
    val CAPPING = 3
    var clientLimitReached = MAX_REWARD_LIMIT == benefitItems.size

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GiftBoxTapTapViewModel


    override fun getLayout() = R.layout.fragment_gift_tap_tap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val component = DaggerGiftBoxComponent.builder()
                    .activityContextModule(ActivityContextModule(it))
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

        colorDim = ContextCompat.getColor(activity!!, com.tokopedia.gamification.R.color.gf_dim)
        colorBlackTransParent = ContextCompat.getColor(activity!!, com.tokopedia.gamification.R.color.gf_black_transparent)
        viewModel.getGiftBoxHome()
        return v
    }

    private fun showRewardAnimation(@RewardContainer.RewardState rewardState: Int, startDelay: Long, isFirstTime: Boolean) {
        var stageLightAnim: Animator? = null

        var soundDelay = 100L
        if (isFirstTime) {
            stageLightAnim = giftBoxDailyView.stageGlowAnimation()
            stageLightAnim.startDelay = startDelay
            soundDelay = 700L
        }
        val NEGATIVE_DURATION = -250L

        giftBoxDailyView.postDelayed({ playPrizeSound() }, soundDelay)

        when (rewardState) {
            RewardContainer.RewardState.COUPON_WITH_POINTS -> {

                val pairAnim1 = rewardContainer.showCouponAndRewardAnimationFadeOut(startDelay) // 1 second

                val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimationFadeOut()
                ovoPointsTextAnim.startDelay = startDelay + 100L

                val animatorSet = AnimatorSet()
                if (stageLightAnim != null) {
                    animatorSet.playTogether(stageLightAnim, pairAnim1.first, ovoPointsTextAnim)
                } else {
                    animatorSet.playTogether(pairAnim1.first, ovoPointsTextAnim)
                }
                ovoPointsTextAnim.addListener(onEnd = { toggleInActiveHint(true) })
                animatorSet.start()
                getTapTapView().postDelayed({
                    afterRewardAnimationEnds()
                }, startDelay + pairAnim1.second + NEGATIVE_DURATION)
            }
            RewardContainer.RewardState.POINTS_ONLY -> {

                val pairAnim = rewardContainer.showSingleLargeRewardAnimationFadeOut(startDelay) // 1second

                val animatorSet = AnimatorSet()
                if (stageLightAnim != null) {
                    animatorSet.playTogether(stageLightAnim, pairAnim.first)
                } else {
                    animatorSet.play(pairAnim.first)
                }

                animatorSet.start()

                val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimationFadeOut()
                ovoPointsTextAnim.startDelay = startDelay + 100L
                ovoPointsTextAnim.addListener(onEnd = { toggleInActiveHint(true) })
                ovoPointsTextAnim.start()
                getTapTapView().postDelayed({
                    afterRewardAnimationEnds()
                    GtmGiftTapTap.viewRewards(OVO)
                }, startDelay + pairAnim.second + NEGATIVE_DURATION)
            }
            RewardContainer.RewardState.COUPON_ONLY -> {
                val pairAnim = rewardContainer.showCouponAndRewardAnimationFadeOut(startDelay) // 1 second

                val animatorSet = AnimatorSet()
                if (stageLightAnim != null) {
                    animatorSet.playTogether(pairAnim.first, stageLightAnim)
                } else {
                    animatorSet.play(pairAnim.first)
                }
                pairAnim.first.addListener(onEnd = { toggleInActiveHint(true) })
                animatorSet.startDelay = startDelay
                animatorSet.start()
                getTapTapView().postDelayed({
                    afterRewardAnimationEnds()
                    GtmGiftTapTap.viewRewards(COUPON)
                }, startDelay + pairAnim.second + NEGATIVE_DURATION)
            }
        }
    }

    private fun setListeners() {
        giftBoxDailyView.boxCallback = object : GiftBoxDailyView.BoxCallback {

            override fun onBoxOpenAnimationStart(startDelay: Long) {
                showRewardAnimation(rewardState, startDelay, true)
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

        viewModel.giftHomeLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    showLoader()
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    if (it.data != null) {

                        backButton = it.data.gamiTapEggHome?.backButton

                        //toolbar
                        val toolbarTitle = it.data.gamiTapEggHome?.tokensUser?.title
                        toolbarTitle?.let { title ->
                            tvTapHint.text = title
                        }
                        val tokenUser = it.data.gamiTapEggHome?.tokensUser
                        tokenUser?.apply {
                            viewModel.campaignId = campaignID

                            if (!tokenUserID.isNullOrEmpty())
                                viewModel.tokenId = tokenUserID
                        }

                        rewardSummary.apply {
                            setButtons(it.data.gamiTapEggHome?.rewardButton)
                            imageBoxUrl = it.data.gamiTapEggHome?.tokenAsset?.rewardImgURL
                        }

                        //for empty state
                        val state = it.data.gamiTapEggHome?.tokensUser?.state
                        state?.let { tokenUserState ->
                            when (tokenUserState) {
                                EMPTY -> {
                                    val title = it.data.gamiTapEggHome?.tokensUser?.text
                                    val desc = it.data.gamiTapEggHome?.tokensUser?.desc

                                    tvInactiveTitle.text = title
                                    tvInactiveMessage.text = desc
                                    val inactiveImageLoader = InactiveImageLoader()

                                    val tokenAsset = it.data.gamiTapEggHome?.tokenAsset
                                    if (tokenAsset != null) {
                                        inactiveImageLoader.loadImages(imageInactive, imageInactiveBg, tokenAsset) {
                                            loadInactiveContainer()
                                        }
                                    } else {
                                        loadInactiveContainer()
                                    }


                                    it.data.gamiTapEggHome?.actionButton?.let { items ->
                                        if (!items.isNullOrEmpty())
                                            btnInactiveFirst.text = items[0].text

                                        btnInactiveFirst.setOnClickListener {
                                            GtmGiftTapTap.clickHomePageButton()
                                        }
                                    }
                                    GtmGiftTapTap.campaignOver()
                                }
                                LOBBY -> {
                                    it.data.gamiTapEggHome?.let { gamiTapEggHome ->
                                        setupLobbyUi(gamiTapEggHome)
                                        GtmGiftTapTap.impressionGiftBox()
                                    }

                                }
                                CRACK_UNLIMITED -> {
                                    it.data.gamiTapEggHome?.let { gamiTapEggHome ->
                                        setupCrackUnlimitedUi(gamiTapEggHome)
                                        GtmGiftTapTap.impressionGiftBox()
                                    }
                                }
                                else -> {
                                    activity?.finish()
                                }
                            }
                        }

                        getTapTapView().fmGiftBox.setOnClickListener {
                            handleGiftBoxTap()
                            if (fmCoupons != null) {
                                fmParent.removeView(fmCoupons)
                                fmCoupons = null
                            }
                        }

                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    hideLoader()
                    renderGiftBoxError(defaultErrorMessage, getString(R.string.gami_oke))
                }
            }
        })
        viewModel.giftCrackLiveData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    val responseCrackResultEntity = it.data
                    if (it.data != null) {
                        val resultCode = responseCrackResultEntity?.crackResultEntity?.resultStatus?.code
                        if (!resultCode.isNullOrEmpty()) {
                            when (resultCode) {
                                STATUS_OK -> {
                                    boxState = OPEN
                                    toggleInActiveHint(false)
                                    getTapTapView().disableConfettiAnimation = true
                                    getTapTapView().resetTapCount()

                                    val benefits = responseCrackResultEntity.crackResultEntity?.benefits

                                    if (responseCrackResultEntity.crackResultEntity != null) {
                                        updateRewardStateAndRewards(benefits, responseCrackResultEntity.crackResultEntity.imageUrl, responseCrackResultEntity.crackResultEntity.returnButton)

                                        if (!isTimeOut) {
                                            if (!getTapTapView().isBoxAlreadyOpened) {
                                                getTapTapView().firstTimeBoxOpenAnimation()
                                                getTapTapView().isBoxAlreadyOpened = true
                                            } else {
                                                getTapTapView().boxBounceAnimation().start()
                                                showRewardAnimation(rewardState, 0L, false)
                                            }
                                        }
                                    }

                                }
                                SERVER_LIMIT_REACHED -> {
                                    //todo Rahul how to show the ui - how to
                                    // show you have won all the rewards

                                    viewModel.waitingForCrackResult = false
                                    getTapTapView().isGiftTapAble = false
                                    minuteCountDownTimer?.cancel()
                                    minuteTimerState = FINISHED
                                    rewardSummary.visibility = View.VISIBLE
                                    toggleInActiveHint(false)
                                    dimBackground()
                                    showRewardSummary()
                                    return@Observer
                                }
                                else -> {
                                    getTapTapView().isGiftTapAble = true
                                    val status = responseCrackResultEntity?.crackResultEntity?.resultStatus
                                    val messageList = status?.message
                                    if (!messageList.isNullOrEmpty()) {
                                        renderGiftBoxOpenError(messageList[0], getString(R.string.gami_oke))
                                    } else {
                                        renderGiftBoxOpenError(defaultErrorMessage, getString(R.string.gami_oke))
                                    }
                                }
                            }
                        }
                    }
                    viewModel.waitingForCrackResult = false

                    if (isTimeOut) {
                        dimBackground()
                        showRewardSummary()
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    viewModel.waitingForCrackResult = false

                    if (isTimeOut) {
                        dimBackground()
                        showRewardSummary()
                    } else {
                        getTapTapView().isGiftTapAble = true
                        renderGiftBoxOpenError(defaultErrorMessage, getString(R.string.gami_oke))
                    }
                }
            }
        })

        viewModel.couponLiveData.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    if (result.data?.couponMap != null) {
                        benefitItems.forEach {
                            if (it.first.benefitType == BenefitType.COUPON && !it.first.referenceID.isNullOrEmpty()) {
                                val couponDetail = result.data.couponMap["id_${it.first.referenceID}"]
                                rewardItems.add(RewardSummaryItem(couponDetail, it.first, it.second))
                            } else if (it.first.benefitType == OVO) {
                                rewardItems.add(RewardSummaryItem(null, it.first))
                            }
                        }
                    }
                    fadeOutWaktuHabisAndShowReward()
                }

                LiveDataResult.STATUS.ERROR -> {
                    fadeOutWaktuHabisAndShowReward()
                }
            }
        })
    }

    fun toggleInActiveHint(show: Boolean) {
        tvTapHint.animate().alpha(if (show) 1f else 0f).setDuration(300L).start()
    }

    private fun handleGiftBoxTap() {

        if (isTimeOut) {
            //Do nothing
        } else if (getTapTapView().isGiftTapAble) {
            playTapSound()
            getTapTapView().isGiftTapAble = false
            if (getTapTapView().tapCount == getTapTapView().targetTapCount) {
                crackGiftBox()
                getTapTapView().targetTapCount = getTapTapView().getRandomNumber()
            } else {
                if (boxState == OPEN)
                    getTapTapView().showConfettiAnimation()
                else
                    getTapTapView().isGiftTapAble = true
            }
            getTapTapView().incrementTapCount()
            GtmGiftTapTap.clickGiftBox()
        }
    }

    private fun setupCrackUnlimitedUi(gamiTapEggHome: GamiTapEggHome) {
        val timeLeftSeconds = gamiTapEggHome.timeRemaining?.seconds
        val showTimer = gamiTapEggHome.timeRemaining?.isShow


        val tokenAsset = gamiTapEggHome.tokenAsset
        var imageFrontUrl = ""
        val bgImageUrl = tokenAsset?.backgroundImgURL
        val imageUrlList = tokenAsset?.imageV2URLs
        val lidImages = arrayListOf<String>()

        if (imageUrlList != null && imageUrlList.size > 2) {
            imageFrontUrl = imageUrlList[0]
            lidImages.addAll(imageUrlList.subList(2, imageUrlList.size))
        }
        if (!bgImageUrl.isNullOrEmpty() && imageFrontUrl.isNotEmpty()) {
            getTapTapView().loadFilesForTapTap(
                    null,
                    null,
                    imageFrontUrl,
                    bgImageUrl,
                    lidImages,
                    imageCallback = { isLoaded ->
                        setPositionOfViewsAtBoxOpen()
                        hideLoader()
                        fadeInActiveStateViews()
                    }
            )
        }


        if (showTimer != null && timeLeftSeconds != null) {
            (giftBoxDailyView as GiftBoxTapTapView).apply {
                imageBoxGlow.alpha = 0f
                imageBoxWhite.alpha = 0f
                fmGiftBox.alpha = 1f
                isGiftTapAble = true
            }
            animateTvTimerAndProgressBar()
            startOneMinuteCounter(timeLeftSeconds)
        }
    }

    private fun setupLobbyUi(gamiTapEggHome: GamiTapEggHome) {
        //timer
        val timeLeftHours = gamiTapEggHome.timeRemaining?.unixFetch
        val timeLeftSeconds = gamiTapEggHome.timeRemaining?.seconds
        val showTimer = gamiTapEggHome.timeRemaining?.isShow

        //glowing mode
        val tokenAsset = gamiTapEggHome.tokenAsset
        val glowImageUrl = tokenAsset?.glowImgURL
        val glowShadowImageUrl = tokenAsset?.glowShadowImgURL

        val imageUrlList = tokenAsset?.imageV2URLs
        val lidImages = arrayListOf<String>()
        var imageFrontUrl = ""
        val bgImageUrl = tokenAsset?.backgroundImgURL
        if (!glowShadowImageUrl.isNullOrEmpty() && !bgImageUrl.isNullOrEmpty()) {

            if (imageUrlList != null && imageUrlList.isNotEmpty()) {
                imageFrontUrl = imageUrlList[0]
                lidImages.addAll(imageUrlList.subList(2, imageUrlList.size))
            }

            if (showTimer != null && showTimer && timeLeftHours != null && timeLeftSeconds != null) {
                renderBottomHourTimer(timeLeftSeconds)

                getTapTapView().loadFilesForTapTap(
                        glowImageUrl,
                        glowShadowImageUrl,
                        imageFrontUrl,
                        bgImageUrl,
                        lidImages,
                        imageCallback = { isLoaded ->

                            setPositionOfViewsAtBoxOpen()
                            hideLoader()
                            fadeInActiveStateViews()
                            getTapTapView().startInitialAnimation()?.start()
                        }
                )
            }
        }
    }

    private fun loadInactiveContainer() {
        viewFlipper.displayedChild = CONTAINER_INACTIVE
        loader.visibility = View.GONE
    }

    fun setPositionOfViewsAtBoxOpen() {

        fmCoupons?.doOnLayout { fmCoupon ->
            giftBoxDailyView.fmGiftBox.doOnLayout { fmGiftBox ->

                val heightOfRvCoupons = if (isTablet)
                    fmGiftBox.context.resources.getDimension(R.dimen.gami_rv_coupons_height).toInt()
                else
                    fmCoupon.height

                val lidTop = fmGiftBox.top
                val clTransactionHeight = if (isTablet)
                    0
                else fmGiftBox.context.resources.getDimension(R.dimen.gami_cl_transaction_height).toInt()
                val translationY = lidTop - heightOfRvCoupons + clTransactionHeight + fmGiftBox.dpToPx(3)

                val sideMargin = fmGiftBox.context.resources.getDimension(R.dimen.gami_rv_coupons_top_margin).toInt()
                val ratio = 3 //coming from R.layout.list_item_coupons
                rewardContainer.rvCoupons.translationY = translationY + (2 * sideMargin / ratio)

                val distanceFromLidTop = fmGiftBox.context.resources.getDimension(R.dimen.gami_lid_top_distance_for_reward_text)
                rewardContainer.llRewardTextLayout.translationY = lidTop + distanceFromLidTop

            }
        }

        giftBoxDailyView.imageGiftBoxLid.doOnLayout { lid ->
            val top = lid.top + giftBoxDailyView.fmGiftBox.top
            rewardContainer.setFinalTranslationOfCirclesTap(top)
        }

        giftBoxDailyView.imageBoxFront.doOnLayout { imageBoxFront ->
            val imageFrontTop = imageBoxFront.top + giftBoxDailyView.fmGiftBox.top
            val translationY = imageFrontTop - imageBoxFront.dpToPx(40)
            starsContainer.setStartPositionOfStars(starsContainer.width / 2f, translationY)

        }
    }

    private fun crackGiftBox() {
        if (!clientLimitReached)
            viewModel.crackGiftBox()
    }

    private fun renderGiftBoxOpenError(message: String, actionText: String) {
        if (context != null) {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(::handleGiftBoxTap, context!!)
            } else {
                showRedError(fmParent, message, actionText, ::handleGiftBoxTap)
            }
            GtmGiftTapTap.viewError()
        }
    }

    private fun renderGiftBoxError(message: String, actionText: String) {
        if (context != null) {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(viewModel::getGiftBoxHome, context!!)
            } else {
                showRedError(fmParent, message, actionText, viewModel::getGiftBoxHome)
            }
            GtmGiftTapTap.viewError()
        }
    }

    private fun afterRewardAnimationEnds() {
        (giftBoxDailyView as GiftBoxTapTapView).isGiftTapAble = true
    }

    override fun initialViewSetup() {
        super.initialViewSetup()
        tvToolbarTitle.text = activity?.getString(com.tokopedia.gamification.R.string.gami_gift_60_toolbar_title)
        ImageViewCompat.setImageTintList(imageToolbarIcon, ColorStateList.valueOf(ContextCompat.getColor(imageToolbarIcon.context, android.R.color.white)))
        tvToolbarTitle.setTextColor(ContextCompat.getColor(tvToolbarTitle.context, android.R.color.white))

        tvTimer.alpha = 0f
        progressBarTimer.alpha = 0f
        tvProgressCount.alpha = 0f
        lottieTimeUp.gone()

    }

    override fun initViews(v: View) {
        tvTimer = v.findViewById(R.id.tv_timer)
        progressBarTimer = v.findViewById(R.id.progress_bar_timer)
        tvProgressCount = v.findViewById(R.id.tv_progress_count)
        lottieTimeUp = v.findViewById(R.id.lottie_timeup)
        rewardSummary = v.findViewById(R.id.rewardSummary)
        tvInactiveTitle = v.findViewById(R.id.tvInactiveTitle)
        tvInactiveMessage = v.findViewById(R.id.tvInactiveMessage)
        imageInactive = v.findViewById(R.id.imageInactive)
        btnInactiveFirst = v.findViewById(R.id.btnInactiveFirst)
        imageInactiveBg = v.findViewById(R.id.imageInactiveBg)
        fmCoupons = v.findViewById(R.id.fmCoupons)
        viewDim = v.findViewById(R.id.view_dim)

        rewardSummary.visibility = View.GONE

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
            val shadowColor = ContextCompat.getColor(it, com.tokopedia.gamification.R.color.gf_box_text_shadow)
            val shadowRadius = tvProgressCount.dpToPx(5)
            val shadowOffset = tvProgressCount.dpToPx(4)

            tvProgressCount.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
            tvTimer.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
            tvInactiveMessage.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
            tvInactiveTitle.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
        }
    }

    fun rewardSummaryAnimation(): Animator {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(rewardSummary, alphaProp)

        alphaAnim.duration = 700L

        alphaAnim.addListener(onEnd = {
            rewardSummary.playRewardItemAnimation()
            rewardSummary.setRewardData(rewardItems)
        })
        return alphaAnim
    }

    private fun showRewardSummary() {
        val notWaitingForApi = !viewModel.waitingForCrackResult
        if (notWaitingForApi) {
            giftBoxDailyView.postDelayed({
                val item = benefitItems.find { it.first.isBigPrize }
                if (item != null) {
                    viewModel.getCouponDetails(benefitItems.map { it.first })
                } else {
                    rewardItems.addAll(benefitItems.map { RewardSummaryItem(null, it.first) })
                    fadeOutWaktuHabisAndShowReward()
                }
            }, 2000L)
        }
    }

    fun dimBackground() {
        val colorAnimator = ObjectAnimator.ofObject(viewDim, "backgroundColor", ArgbEvaluator(), colorBlackTransParent, colorDim)
        colorAnimator.duration = 250L
        colorAnimator.start()
    }

    fun startOneMinuteCounter(totalSeconds: Long) {
        val time = (5 + 1) * 1000L
        MAX_REWARD_LIMIT = ceil(5.toInt() / CAPPING.toFloat()).toInt()

        fun onTimeUp() {
            (giftBoxDailyView as GiftBoxTapTapView).isTimeOut = true
            minuteTimerState = FINISHED
            rewardSummary.visibility = View.VISIBLE
            lottieTimeUp.visible()
            lottieTimeUp.playAnimation()
            dimBackground()
            playTimeOutSound()
            toggleInActiveHint(false)
            showRewardSummary()

            bgSoundManager?.mPlayer?.setVolume(0.3f, 0.3f)
        }
        //todo Rahul uncomment this
//        val time = totalSeconds * 1000L

        minuteCountDownTimer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {}

            override fun onTick(millisUntilFinished: Long) {

                val seconds = (millisUntilFinished / 1000)
                tvProgressCount.text = "$seconds"
                progressBarTimer.progress = 100 - (seconds / 60f * 100).toInt()
                if (seconds == 3L) {
                    playCountDownSound()
                } else if (seconds == 0L) {
                    onTimeUp()
                }
            }
        }

        minuteCountDownTimer?.start()
        minuteTimerState = STARTED
        if (!isBackgroundSoundPlaying()) {
            playLoopSound()
        }

    }

    private fun renderBottomHourTimer(timeLeftSeconds: Long) {
        fun onTimeUp() {
            (giftBoxDailyView as GiftBoxTapTapView).glowingAnimator?.end()
            val anim1 = (giftBoxDailyView as GiftBoxTapTapView).fadeOutGlowingAndFadeInGiftBoxAnimation()
            anim1.addListener(onEnd = {
                (giftBoxDailyView as GiftBoxTapTapView).isGiftTapAble = true
            })
            anim1.start()
            animateTvTimerAndProgressBar()
            startOneMinuteCounter(60)
        }

        val time = (timeLeftSeconds + 1) * 1000L
        hourCountDownTimer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {}

            override fun onTick(millisUntilFinished: Long) {
                var seconds = millisUntilFinished / 1000
                var minutes = seconds / 60
                val hour = minutes / 60
                minutes %= 60
                seconds %= 60

                val hourText = if (hour < 10) "0$hour" else hour.toString()
                val minuteText = if (minutes < 10) "0$minutes" else minutes.toString()
                val secondText = if (seconds < 10) "0$seconds" else seconds.toString()
                tvTimer.text = "${hourText}:${minuteText}:${secondText}"

                if (seconds == 0L) {
                    onTimeUp()
                }
            }
        }

        hourCountDownTimer?.start()

        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(tvTimer, alphaProp)
        alphaAnim.duration = 500L
        alphaAnim.start()

        playLoopSound()
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

        animatorSet.start()
    }

    private fun fadeOutWaktuHabisAndShowReward() {
        val animatorSet = AnimatorSet()
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 1f, 0f)
        val fadeOutAnim = ObjectAnimator.ofPropertyValuesHolder(lottieTimeUp, alphaProp)
        fadeOutAnim.duration = 300L
        fadeOutAnim.startDelay = 300L

        animatorSet.playSequentially(fadeOutAnim, rewardSummaryAnimation())
        fadeOutAnim.addListener(onEnd = {
            lottieTimeUp.cancelAnimation()
            lottieTimeUp.gone()
            bgSoundManager?.mPlayer?.setVolume(1f, 1f)
        })
        animatorSet.start()
    }

    fun fadeOutViews() {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)
        tapHintAnim.duration = 300L
        tapHintAnim.addListener(onEnd = { tvTapHint.text = getString(R.string.gami_ayo_tap) })
        tapHintAnim.start()
    }


    fun getTapTapView(): GiftBoxTapTapView {
        return giftBoxDailyView as GiftBoxTapTapView
    }

    fun updateRewardStateAndRewards(benefits: List<CrackBenefitEntity>?, imageUrl: String?, button: CrackButtonEntity?) {
        var hasPoints = false
        var hasCoupons = false

        rewardContainer.couponList.clear()

        var iconUrl: String? = ""
        benefits?.let {
            it.forEach { benefit ->
                if (benefit.benefitType != COUPON) {
                    hasPoints = true
                    iconUrl = imageUrl

                    rewardContainer.tvSmallReward.text = benefit.text
                    if (!benefit.color.isNullOrEmpty()) {
                        rewardContainer.tvSmallReward.setTextColor(Color.parseColor(benefit.color))
                    }
//                    GtmEvents.viewRewardsPoints(benefit.text, userSession?.userId)

                } else if (benefit.benefitType == COUPON) {
                    hasCoupons = true
                    benefit.referenceID?.let { refId ->
//                        GtmEvents.viewRewards(refId, userSession?.userId)
                    }
                    rewardContainer.couponList.add(CouponTapTap(imageUrl))
                }
                benefitItems.add(Pair(benefit, button))
            }
        }
        rewardContainer.couponAdapter.notifyDataSetChanged()

        if (hasPoints && hasCoupons) {
            rewardState = RewardContainer.RewardState.COUPON_WITH_POINTS
            rewardContainer.imageSmallReward.setImageResource(R.drawable.gami_ic_ovo)

        } else if (hasPoints) {
            //only points

            rewardState = RewardContainer.RewardState.POINTS_ONLY
            rewardContainer.imageSmallReward.setImageResource(R.drawable.gami_ic_ovo)
            rewardContainer.imageCircleReward.setImageResource(R.drawable.gami_ic_ovo)

        } else if (hasCoupons) {
            rewardState = RewardContainer.RewardState.COUPON_ONLY
        }
    }

    fun onBackPressed(): Boolean {
        if (minuteTimerState == STARTED && backButton != null && backButton!!.isShow) {
            showBackDialog(backButton!!)
            return false
        }
        return true
    }

    private fun showBackDialog(backButton: BackButton) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE)
            dialog.setTitle(backButton.title)
            dialog.setDescription(backButton.text)
            dialog.setPrimaryCTAText(backButton.yesText)
            dialog.setPrimaryCTAClickListener {
                GtmGiftTapTap.clickContinueButton()
                activity?.finish()
            }
            dialog.setSecondaryCTAText(backButton.cancelText)
            dialog.setSecondaryCTAClickListener {
                GtmGiftTapTap.clickExitButton()
                dialog.cancel()
            }
            dialog.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        val drawable = menu.getItem(0).icon
        drawable.mutate()
        context?.let {
            drawable.setColorFilter(ContextCompat.getColor(it, android.R.color.white), PorterDuff.Mode.SRC_IN)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        hourCountDownTimer?.cancel()
        minuteCountDownTimer?.cancel()
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(STARTED, FINISHED, NOT_STARTED)
annotation class MinuteTimerState {
    companion object {
        const val STARTED = 0
        const val FINISHED = 1
        const val NOT_STARTED = 2
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(COUPON, OVO)
annotation class BenefitType {
    companion object {
        const val COUPON = "coupon"
        const val OVO = "ovo_points"
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(EMPTY, CRACK_UNLIMITED, LOBBY)
annotation class TokenUserStateTapTap {
    companion object {
        const val EMPTY = "empty"
        const val CRACK_UNLIMITED = "crackunlimited"
        const val LOBBY = "lobby"
    }
}

@Retention(AnnotationRetention.SOURCE)
@IntDef(OPEN, CLOSE)
annotation class BoxState {
    companion object {
        const val OPEN = 0
        const val CLOSE = 1
    }
}