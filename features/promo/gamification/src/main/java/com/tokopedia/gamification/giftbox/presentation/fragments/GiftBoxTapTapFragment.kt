package com.tokopedia.gamification.giftbox.presentation.fragments

import android.animation.*
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieTask
import com.tkpd.remoteresourcerequest.view.ImageDensityType
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.gamification.R
import com.tokopedia.gamification.data.entity.CrackBenefitEntity
import com.tokopedia.gamification.data.entity.CrackButtonEntity
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.Constants
import com.tokopedia.gamification.giftbox.InactiveImageLoader
import com.tokopedia.gamification.giftbox.analytics.GtmGiftTapTap
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxComponent
import com.tokopedia.gamification.giftbox.data.di.modules.AppModule
import com.tokopedia.gamification.giftbox.data.entities.CouponTapTap
import com.tokopedia.gamification.giftbox.presentation.entities.RewardSummaryItem
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType.Companion.COUPON
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType.Companion.COUPON_RP_0
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType.Companion.OVO
import com.tokopedia.gamification.giftbox.presentation.fragments.BenefitType.Companion.REWARD_POINT
import com.tokopedia.gamification.giftbox.presentation.fragments.BoxState.Companion.CLOSE
import com.tokopedia.gamification.giftbox.presentation.fragments.BoxState.Companion.OPEN
import com.tokopedia.gamification.giftbox.presentation.fragments.DisplayType.Companion.CARD
import com.tokopedia.gamification.giftbox.presentation.fragments.DisplayType.Companion.CATALOG
import com.tokopedia.gamification.giftbox.presentation.fragments.DisplayType.Companion.IMAGE
import com.tokopedia.gamification.giftbox.presentation.fragments.DisplayType.Companion.UNDEFINED
import com.tokopedia.gamification.giftbox.presentation.fragments.MinuteTimerState.Companion.FINISHED
import com.tokopedia.gamification.giftbox.presentation.fragments.MinuteTimerState.Companion.NOT_STARTED
import com.tokopedia.gamification.giftbox.presentation.fragments.MinuteTimerState.Companion.STARTED
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserStateTapTap.Companion.CRACK_UNLIMITED
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserStateTapTap.Companion.DEFAULT
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserStateTapTap.Companion.EMPTY
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserStateTapTap.Companion.LOBBY
import com.tokopedia.gamification.giftbox.presentation.helpers.BgSoundDownloader
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.helpers.doOnLayout
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxTapTapViewModel
import com.tokopedia.gamification.giftbox.presentation.views.*
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.taptap.data.entiity.BackButton
import com.tokopedia.gamification.taptap.data.entiity.GamiTapEggHome
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import java.util.zip.ZipInputStream
import javax.inject.Inject
import kotlin.math.roundToInt

class GiftBoxTapTapFragment : GiftBoxBaseFragment() {

    lateinit var tvTimer: Typography
    lateinit var progressBarTimer: ProgressBar
    lateinit var tvProgressCount: Typography
    lateinit var rewardSummary: RewardSummaryView
    lateinit var lottieTimeUp: LottieAnimationView
    lateinit var rewardContainer: RewardContainer
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
    val bgSoundDownloader = BgSoundDownloader()

    @RewardContainer.RewardState
    var rewardState: Int = RewardContainer.RewardState.COUPON_ONLY

    @BoxState
    var boxState: Int = CLOSE

    @MinuteTimerState
    var minuteTimerState: Int = NOT_STARTED

    private val CONTAINER_INACTIVE = 2
    private val SERVER_LIMIT_REACHED_CODE = "47004"
    private val STATUS_OK = "200"
    private var MAX_REWARD_LIMIT = -1
    private val CAPPING = 3
    lateinit var endTime: String
    lateinit var endTimeMinute: String
    private val LOW_VOLUME = 0.3f
    private val NORMAL_VOLUME = 1f

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GiftBoxTapTapViewModel

    @TokenUserStateTapTap
    var oldTokenUserState = DEFAULT
    var forceOpenGiftBox = false
    var serverLimitReached = false
    var isRewardAnimationGoingOn = false

    override fun getLayout() = R.layout.fragment_gift_tap_tap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            val component = DaggerGiftBoxComponent.builder()
                    .activityContextModule(ActivityContextModule(it))
                    .appModule(AppModule((context as AppCompatActivity).application))
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

        downloadAudios()
        colorDim = ContextCompat.getColor(requireActivity(), com.tokopedia.gamification.R.color.gf_dim)
        colorBlackTransParent = ContextCompat.getColor(requireActivity(), com.tokopedia.gamification.R.color.gf_black_transparent)
        viewModel.getGiftBoxHome()
        return v
    }

    private fun downloadAudios() {
        bgSoundDownloader.downloadAndPlay("gf_giftbox_bg_tap_60.mp3", {
            giftBoxTapTapAudio.bgSoundPath = it
        }, 1000L)

        bgSoundDownloader.downloadAndPlay("gami_count_down.mp3", {
            giftBoxTapTapAudio.countDownSoundPath = it
        }, 10000L)

        bgSoundDownloader.downloadAndPlay("gami_timeout.mp3", {
            giftBoxTapTapAudio.timeUpSoundPath = it
        }, 10000L)
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
        isRewardAnimationGoingOn = true

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
                    GtmGiftTapTap.viewRewards(OVO, userSession?.userId)
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
                    GtmGiftTapTap.viewRewards(COUPON, userSession?.userId)
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

                        fun renderUi(state: String?, gamiTapEggHome: GamiTapEggHome) {
                            state?.let { tokenUserState ->
                                oldTokenUserState = tokenUserState
                                viewModel.canShowLoader = false

                                when (tokenUserState) {
                                    EMPTY -> {
                                        renderEmptyState(gamiTapEggHome)
                                    }
                                    LOBBY -> {
                                        setupLobbyUi(gamiTapEggHome)
                                        GtmGiftTapTap.impressionGiftBox(userSession?.userId)

                                    }
                                    CRACK_UNLIMITED -> {
                                        setupCrackUnlimitedUi(gamiTapEggHome)
                                        GtmGiftTapTap.impressionGiftBox(userSession?.userId)
                                    }
                                    else -> {
                                        activity?.finish()
                                    }
                                }
                            }
                        }

                        if (oldTokenUserState == DEFAULT) {
                            val gamiTapEggHome = it.data.gamiTapEggHome

                            if (gamiTapEggHome != null) {
                                backButton = gamiTapEggHome.backButton

                                //toolbar
                                val toolbarTitle = gamiTapEggHome.tokensUser?.title
                                toolbarTitle?.let { title ->
                                    tvTapHint.text = title
                                }
                                val tokenUser = gamiTapEggHome.tokensUser
                                tokenUser?.apply {
                                    viewModel.campaignId = campaignID

                                    if (!tokenUserID.isNullOrEmpty())
                                        viewModel.tokenId = tokenUserID
                                }

                                rewardSummary.apply {
                                    setButtons(gamiTapEggHome.rewardButton)
                                    imageBoxUrl = gamiTapEggHome.tokenAsset?.rewardImgURL
                                }

                                //for empty state
                                val state = gamiTapEggHome.tokensUser?.state
                                renderUi(state, gamiTapEggHome)
                            }

                            getTapTapView().fmGiftBox.setOnClickListener {
                                handleGiftBoxTap()
                                if (fmCoupons != null) {
                                    fmParent.removeView(fmCoupons)
                                    fmCoupons = null
                                }
                            }
                        } else {
                            //2nd time api call only check for state
                            val state = it.data.gamiTapEggHome?.tokensUser?.state
                            state?.let { tokenUserState ->
                                when (tokenUserState) {
                                    EMPTY -> {
                                        renderEmptyState(it.data.gamiTapEggHome)
                                    }
                                    LOBBY -> {
                                        setupLobbyUiSecondTime(it.data.gamiTapEggHome)
                                        GtmGiftTapTap.impressionGiftBox(userSession?.userId)
                                    }
                                    CRACK_UNLIMITED -> {
                                        setupCrackUnlimitedUiSecondTime(it.data.gamiTapEggHome)
                                        GtmGiftTapTap.impressionGiftBox(userSession?.userId)
                                    }
                                }
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
                                SERVER_LIMIT_REACHED_CODE -> {
                                    serverLimitReached = true

                                    viewModel.waitingForCrackResult = false
                                    getTapTapView().isGiftTapAble = true
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
                    if (result.data?.couponDetailList != null) {
                        benefitItems.forEach {
                            if (it.first.benefitType == BenefitType.COUPON && !it.first.referenceID.isNullOrEmpty()) {
                                val refId = it.first.referenceID
                                val couponDetail = result.data.couponDetailList.find { coupon->coupon.referenceId == refId }
                                if(couponDetail!=null) {
                                    rewardItems.add(RewardSummaryItem(couponDetail, it.first, it.second))
                                }
                            } else if (it.first.benefitType == OVO) {
                                rewardItems.add(RewardSummaryItem(null, it.first))
                            }
                        }
                    }

                    fadeOutWaktuHabisAndShowReward()
                }

                LiveDataResult.STATUS.ERROR -> {
                    rewardItems.addAll(benefitItems.map { RewardSummaryItem(null, it.first) })
                    fadeOutWaktuHabisAndShowReward()
                }
            }
        })
    }

    private fun renderEmptyState(gamiTapEggHome: GamiTapEggHome) {
        hourCountDownTimer?.cancel()

        val title = gamiTapEggHome.tokensUser?.text
        val desc = gamiTapEggHome.tokensUser?.desc

        tvInactiveTitle.text = title
        tvInactiveMessage.text = desc
        val inactiveImageLoader = InactiveImageLoader()

        val tokenAsset = gamiTapEggHome.tokenAsset
        if (tokenAsset != null) {
            inactiveImageLoader.loadImages(imageInactive, imageInactiveBg, tokenAsset) {
                loadInactiveContainer()
            }
        } else {
            loadInactiveContainer()
        }


        gamiTapEggHome.actionButton?.let { items ->
            if (!items.isNullOrEmpty()) {
                btnInactiveFirst.text = items[0].text

                btnInactiveFirst.setOnClickListener {
                    RouteManager.route(btnInactiveFirst.context, items[0].applink)
                    GtmGiftTapTap.clickHomePageButton(userSession?.userId)
                }
            }
        }
        GtmGiftTapTap.campaignOver(userSession?.userId)
    }

    fun toggleInActiveHint(show: Boolean) {
        tvTapHint.animate().alpha(if (show) 1f else 0f).setDuration(300L).start()
    }

    private fun handleGiftBoxTap() {
        if (isTimeOut) {
            //Do nothing
        } else if (getTapTapView().isGiftTapAble && !isRewardAnimationGoingOn) {
            playTapSound()
            getTapTapView().isGiftTapAble = false
            if (getTapTapView().tapCount == getTapTapView().targetTapCount || forceOpenGiftBox) {
                forceOpenGiftBox = false
                crackGiftBox()
                getTapTapView().resetTapCount()
                getTapTapView().targetTapCount = getTapTapView().getRandomNumber()
                if (boxState == OPEN)
                    getTapTapView().showConfettiAnimation()
            } else {
                if (boxState == OPEN)
                    getTapTapView().showConfettiAnimation()
                else
                    getTapTapView().isGiftTapAble = true
            }
            getTapTapView().incrementTapCount()
            GtmGiftTapTap.clickGiftBox(userSession?.userId)
        }
    }

    private fun setupCrackUnlimitedUiSecondTime(gamiTapEggHome: GamiTapEggHome) {

        (giftBoxDailyView as GiftBoxTapTapView).glowingAnimator?.end()
        val anim1 = (giftBoxDailyView as GiftBoxTapTapView).fadeOutGlowingAndFadeInGiftBoxAnimation()
        anim1.addListener(onEnd = {
            (giftBoxDailyView as GiftBoxTapTapView).isGiftTapAble = true
        })
        anim1.start()
        animateTvTimerAndProgressBar()

        val timeLeftSeconds = gamiTapEggHome.timeRemaining?.seconds
        val showTimer = gamiTapEggHome.timeRemaining?.isShow
        if (showTimer != null && timeLeftSeconds != null) {
            startOneMinuteCounter(timeLeftSeconds)
        }
    }

    private fun setupCrackUnlimitedUi(gamiTapEggHome: GamiTapEggHome) {

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
                    viewLifecycleOwner,
                    imageCallback = { isLoaded ->
                        giftBoxDailyView.imagesLoaded.lazySet(0)
                        setPositionOfViewsAtBoxOpen()
                        hideLoader()
                        fadeInActiveStateViews()
                        showMinuteTimer(gamiTapEggHome)
                    }
            )
        }
    }

    private fun showMinuteTimer(gamiTapEggHome: GamiTapEggHome) {
        val timeLeftSeconds = gamiTapEggHome.timeRemaining?.seconds
        val showTimer = gamiTapEggHome.timeRemaining?.isShow

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

    private fun setupLobbyUiSecondTime(gamiTapEggHome: GamiTapEggHome) {
        val timeLeftHours = gamiTapEggHome.timeRemaining?.unixFetch
        val timeLeftSeconds = gamiTapEggHome.timeRemaining?.seconds
        val showTimer = gamiTapEggHome.timeRemaining?.isShow

        if (showTimer != null && showTimer && timeLeftHours != null && timeLeftSeconds != null) {
            renderBottomHourTimer(timeLeftSeconds)
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

                tvTimer.animate().setDuration(500L).alpha(1f).start()
                playLoopSound()

                getTapTapView().loadFilesForTapTap(
                        glowImageUrl,
                        glowShadowImageUrl,
                        imageFrontUrl,
                        bgImageUrl,
                        lidImages,
                        viewLifecycleOwner,
                        imageCallback = { isLoaded ->
                            giftBoxDailyView.imagesLoaded.lazySet(0)
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

                tvTapHint.doOnLayout { tapHint ->
                    if (giftBoxDailyView.height > LARGE_PHONE_HEIGHT) {
                        tapHint.translationY = lidTop - fmGiftBox.context.resources.getDimension(R.dimen.gami_tap_hint_margin) - tapHint.height
                    }

                    if (isTablet) {
                        tapHint.translationY = lidTop - fmGiftBox.context.resources.getDimension(R.dimen.gami_tap_hint_margin_tablet) - tapHint.height
                    }
                }
                if (giftBoxDailyView.height > LARGE_PHONE_HEIGHT) {
                    rewardContainer.rvCoupons.translationY = (translationY + (2 * sideMargin / ratio)) - fmGiftBox.context.resources.getDimension(R.dimen.gami_box_coupon_padding)

                } else {
                    rewardContainer.rvCoupons.translationY = translationY + (2 * sideMargin / ratio)
                }

                val distanceFromLidTop = fmGiftBox.context.resources.getDimension(R.dimen.gami_lid_top_distance_for_reward_text)
                rewardContainer.llRewardTextLayout.translationY = lidTop + distanceFromLidTop

                if (isTablet) {
                    val rewardContainer = rewardSummary.findViewById<FrameLayout>(R.id.container_gami_gift_result)
                    val REWARD_TABLET_DIMEN_PERCENT = 0.3
                    val lp = rewardContainer.layoutParams as FrameLayout.LayoutParams
                    lp.height = (giftBoxDailyView.height - giftBoxDailyView.height * REWARD_TABLET_DIMEN_PERCENT).toInt()
                    lp.width = (giftBoxDailyView.width - giftBoxDailyView.width * REWARD_TABLET_DIMEN_PERCENT).toInt()
                }

            }
        }

        giftBoxDailyView.imageGiftBoxLid.doOnLayout { lid ->
            val top = lid.top + giftBoxDailyView.fmGiftBox.top
            rewardContainer.setFinalTranslationOfCirclesTap(top)
        }

        giftBoxDailyView.imageBoxFront.doOnLayout { imageBoxFront ->
            val imageFrontTop = imageBoxFront.top + giftBoxDailyView.fmGiftBox.top
            val translationY = imageFrontTop - imageBoxFront.context.resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_40)
            starsContainer.setStartPositionOfStars(starsContainer.width / 2f, translationY)

        }
    }

    private fun clientLimitReached() = MAX_REWARD_LIMIT == benefitItems.size || serverLimitReached

    private fun crackGiftBox() {
        if (!clientLimitReached())
            viewModel.crackGiftBox()
    }

    private fun renderGiftBoxOpenError(message: String, actionText: String) {
        forceOpenGiftBox = true
        if (context != null) {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(::handleGiftBoxTap, requireContext())
            } else {
                showRedError(fmParent, message, actionText, ::handleGiftBoxTap)
            }
        }
    }

    private fun renderGiftBoxError(message: String, actionText: String) {
        if (context != null) {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(viewModel::getGiftBoxHome, requireContext())
            } else {
                showRedError(fmParent, message, actionText, viewModel::getGiftBoxHome)
            }
        }
    }

    private fun afterRewardAnimationEnds() {
        isRewardAnimationGoingOn = false
        (giftBoxDailyView as GiftBoxTapTapView).isGiftTapAble = true
    }

    override fun initialViewSetup() {
        super.initialViewSetup()
        endTime = context?.getString(R.string.gami_end_time) ?: ""
        endTimeMinute = context?.getString(R.string.gami_end_time_minute) ?: ""
        tvTimer.alpha = 0f
        progressBarTimer.alpha = 0f
        tvProgressCount.alpha = 0f
        lottieTimeUp.gone()

    }

    override fun initViews(v: View) {
        rewardContainer = v.findViewById(R.id.reward_container)
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

    private fun setDynamicSize() {
        tvProgressCount.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40.toPx().toFloat())
        tvTimer.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24.toPx().toFloat())
        if (isTablet) {
            rewardSummary.tvSummary.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24.toPx().toFloat())
        }
    }

    private fun setShadows() {
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

    private fun startOneMinuteCounter(totalSeconds: Long) {

        MAX_REWARD_LIMIT = (totalSeconds.toInt() / CAPPING.toFloat()).roundToInt()

        fun onTimeUp() {
            isTimeOut = true
            minuteTimerState = FINISHED
            rewardSummary.visibility = View.VISIBLE
            lottieTimeUp.visible()
            showTimeupAnimation()
            dimBackground()
            playTimeOutSound()
            toggleInActiveHint(false)
            showRewardSummary()

            bgSoundManager?.mPlayer?.setVolume(LOW_VOLUME, LOW_VOLUME)
        }

        val time = totalSeconds * 1000L

        minuteCountDownTimer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                tvProgressCount.text = endTimeMinute
                progressBarTimer.progress = 100
                onTimeUp()
            }

            override fun onTick(millisUntilFinished: Long) {

                var seconds = (millisUntilFinished / 1000f).roundToInt()
                tvProgressCount.text = "$seconds"
                progressBarTimer.progress = 100 - (seconds / totalSeconds.toFloat() * 100).toInt()
                if (seconds == 3) {
                    playCountDownSound()
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
        hourCountDownTimer?.cancel()

        fun callApiAgain() {
            val delayArrayList = arrayListOf(0L, 300L, 600L)
            val delay = delayArrayList[(0 until delayArrayList.size).random()]
            val handler = Handler()
            handler.postDelayed({ viewModel.getGiftBoxHome() }, delay)
        }

        val time = (timeLeftSeconds) * 1000L

        hourCountDownTimer = object : CountDownTimer(time, 1000) {
            override fun onFinish() {
                tvTimer.text = endTime
                callApiAgain()
            }

            override fun onTick(millisUntilFinished: Long) {
                var seconds = (millisUntilFinished / 1000f).roundToInt()
                var minutes = seconds / 60
                val hour = minutes / 60
                minutes %= 60
                seconds %= 60

                val hourText = if (hour < 10) "0$hour" else hour.toString()
                val minuteText = if (minutes < 10) "0$minutes" else minutes.toString()
                val secondText = if (seconds < 10) "0$seconds" else seconds.toString()
                tvTimer.text = "${hourText}:${minuteText}:${secondText}"
            }
        }
        hourCountDownTimer?.start()
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
            bgSoundManager?.mPlayer?.setVolume(NORMAL_VOLUME, NORMAL_VOLUME)
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
                    GtmGiftTapTap.viewRewards(benefit.text, userSession?.userId)

                } else if (benefit.benefitType == COUPON) {
                    hasCoupons = true
                    benefit.referenceID?.let { refId ->
                        GtmGiftTapTap.viewRewards(benefit.text, userSession?.userId)
                    }
                    rewardContainer.couponList.add(CouponTapTap(imageUrl))
                }
                benefitItems.add(Pair(benefit, button))
            }
        }
        rewardContainer.couponAdapter.notifyDataSetChanged()

        if (hasPoints && hasCoupons) {
            rewardState = RewardContainer.RewardState.COUPON_WITH_POINTS
            rewardContainer.imageSmallReward.loadRemoteImageDrawable("ic_ovo.png", ImageDensityType.SUPPORT_MULTIPLE_DPI)

        } else if (hasPoints) {
            //only points

            rewardState = RewardContainer.RewardState.POINTS_ONLY
            rewardContainer.imageSmallReward.loadRemoteImageDrawable("ic_ovo.png", ImageDensityType.SUPPORT_MULTIPLE_DPI)
            rewardContainer.imageCircleReward.loadRemoteImageDrawable("ic_ovo.png", ImageDensityType.SUPPORT_MULTIPLE_DPI)

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
                GtmGiftTapTap.clickContinueButton(userSession?.userId)
                dialog.dismiss()
                activity?.finish()
            }
            dialog.setSecondaryCTAText(backButton.cancelText)
            dialog.setSecondaryCTAClickListener {
                GtmGiftTapTap.clickExitButton(userSession?.userId)
                dialog.dismiss()
            }
            if (isTablet) {
                val layoutParams = dialog.findViewById<View>(com.tokopedia.dialog.R.id.dialog_bg).layoutParams
                layoutParams.width = (giftBoxDailyView.width - giftBoxDailyView.width * 0.3).toInt()
            }
            dialog.show()
        }
    }

    fun showTimeupAnimation() {
        val task = prepareLoaderLottieTask(Constants.TIME_UP_ZIP_FILE)
        if (task != null)
            addLottieAnimationToView(task)
    }

    private fun prepareLoaderLottieTask(fileName: String): LottieTask<LottieComposition>? {
        context?.let { c ->
            val lottieFileZipStream = ZipInputStream(c.assets.open(fileName))
            return LottieCompositionFactory.fromZipStream(lottieFileZipStream, null)
        }
        return null
    }

    private fun addLottieAnimationToView(task: LottieTask<LottieComposition>) {
        task.addListener { result: LottieComposition? ->
            result?.let {
                lottieTimeUp.setComposition(result)
                lottieTimeUp.playAnimation()
            }
        }
    }

    fun forceStopAll() {
        countDownAudioManager?.destroy()
        bgSoundManager?.destroy()
        hourCountDownTimer?.cancel()
        minuteCountDownTimer?.cancel()
        bgSoundDownloader.removeAll()
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
@StringDef(COUPON, OVO, REWARD_POINT, COUPON_RP_0)
annotation class BenefitType {
    companion object {
        const val COUPON = "coupon"
        const val OVO = "ovo_points"
        const val REWARD_POINT = "reward_point"
        const val COUPON_RP_0 = "coupon_rp0"
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(CARD, CATALOG, IMAGE, UNDEFINED)
annotation class DisplayType {
    companion object {
        const val CARD = "card"
        const val CATALOG = "catalog"
        const val IMAGE = "image"
        const val UNDEFINED = ""
    }
}

@Retention(AnnotationRetention.SOURCE)
@StringDef(EMPTY, CRACK_UNLIMITED, LOBBY, DEFAULT)
annotation class TokenUserStateTapTap {
    companion object {
        const val EMPTY = "empty"
        const val CRACK_UNLIMITED = "crackunlimited"
        const val LOBBY = "lobby"
        const val DEFAULT = "default"
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