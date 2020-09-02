package com.tokopedia.gamification.giftbox.presentation.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.gamification.R
import com.tokopedia.gamification.audio.AudioFactory
import com.tokopedia.gamification.di.ActivityContextModule
import com.tokopedia.gamification.giftbox.analytics.GtmEvents
import com.tokopedia.gamification.giftbox.data.di.GAMI_GIFT_DAILY_TRACE_PAGE
import com.tokopedia.gamification.giftbox.data.di.component.DaggerGiftBoxComponent
import com.tokopedia.gamification.giftbox.data.di.modules.AppModule
import com.tokopedia.gamification.giftbox.data.di.modules.PltModule
import com.tokopedia.gamification.giftbox.data.entities.*
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.ACTIVE
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.DEFAULT
import com.tokopedia.gamification.giftbox.presentation.fragments.TokenUserState.Companion.EMPTY
import com.tokopedia.gamification.giftbox.presentation.helpers.addListener
import com.tokopedia.gamification.giftbox.presentation.helpers.doOnLayout
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.gamification.giftbox.presentation.helpers.updateLayoutParams
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxDailyViewModel
import com.tokopedia.gamification.giftbox.presentation.views.*
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import javax.inject.Inject

class GiftBoxDailyFragment : GiftBoxBaseFragment() {

    lateinit var tvBenefits: Typography
    lateinit var llBenefits: LinearLayout
    lateinit var prizeViewSmallFirst: GiftPrizeSmallView
    lateinit var prizeViewSmallSecond: GiftPrizeSmallView
    lateinit var prizeViewLarge: GiftPrizeLargeView
    lateinit var llRewardMessage: LinearLayout
    lateinit var tvRewardFirstLine: AppCompatTextView
    lateinit var tvRewardSecondLine: AppCompatTextView
    lateinit var btnAction: AppCompatTextView
    lateinit var tvReminderBtn: AppCompatTextView
    lateinit var tvReminderMessage: AppCompatTextView
    lateinit var loaderReminder: LoaderUnify
    lateinit var reminderLayout: RelativeLayout
    lateinit var fmReminder: FrameLayout

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GiftBoxDailyViewModel
    var giftBoxRewardEntity: GiftBoxRewardEntity? = null
    var isReminderSet = false
    var reminder: Reminder? = null
    var gameRemindMeCheck: GameRemindMeCheck? = null
    lateinit var pltPerf: PageLoadTimePerformanceInterface

    @TokenUserState
    var tokenUserState: String = TokenUserState.DEFAULT
    var disableGiftBoxTap = false
    var autoApplyMessage = ""
    var totalPrizeImagesCount = 0
    var loadedPrizeImagesCount = 0
    private val HTTP_STATUS_OK = "200"

    override fun getLayout() = com.tokopedia.gamification.R.layout.fragment_gift_box_daily

    override fun onCreate(savedInstanceState: Bundle?) {
        setupPlt()
        super.onCreate(savedInstanceState)
        context?.let {
            val component = DaggerGiftBoxComponent.builder()
                    .activityContextModule(ActivityContextModule(it))
                    .appModule(AppModule((context as AppCompatActivity).application))
                    .build()
            component.inject(this)

            if (it is AppCompatActivity) {
                val viewModelProvider = ViewModelProviders.of(context as AppCompatActivity, viewModelFactory)
                viewModel = viewModelProvider[GiftBoxDailyViewModel::class.java]
            }

            mAudiosManager = AudioFactory.createAudio(it)
        }
    }

    private fun setupPlt() {
        pltPerf = PltModule().providePerfInterface()
        pltPerf.startMonitoring(GAMI_GIFT_DAILY_TRACE_PAGE)
        pltPerf.startPreparePagePerformanceMonitoring()
    }

    override fun onDestroy() {
        super.onDestroy()

        pltPerf.stopMonitoring()

        mAudiosManager?.let {
            it.destroy()
            mAudiosManager = null
        }

        bgSoundManager?.let {
            it.destroy()
            bgSoundManager = null
        }

        rewardSoundManager?.let {
            it.destroy()
            rewardSoundManager = null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val v = super.onCreateView(inflater, container, savedInstanceState)
        pltPerf.stopPreparePagePerformanceMonitoring()
        pltPerf.startNetworkRequestPerformanceMonitoring()
        viewModel.getGiftBox()
        return v
    }

    override fun initViews(v: View) {
        tvBenefits = v.findViewById(R.id.tvBenefits)
        llBenefits = v.findViewById(R.id.ll_benefits)
        llRewardMessage = v.findViewById(R.id.ll_reward_message)
        prizeViewSmallFirst = v.findViewById(R.id.giftPrizeSmallViewFirst)
        prizeViewSmallSecond = v.findViewById(R.id.giftPrizeSmallViewSecond)
        prizeViewLarge = v.findViewById(R.id.giftPrizeLargeView)
        tvRewardFirstLine = v.findViewById(R.id.tvRewardFirstLine)
        tvRewardSecondLine = v.findViewById(R.id.tvRewardSecondLine)
        btnAction = v.findViewById(R.id.btnAction)
        tvLoaderMessage = v.findViewById(R.id.tvLoaderMessage)
        tvReminderBtn = v.findViewById(R.id.tvReminderBtn)
        tvReminderMessage = v.findViewById(R.id.tvReminderMessage)
        loaderReminder = v.findViewById(R.id.loaderReminder)
        reminderLayout = v.findViewById(R.id.reminderLayout)
        fmReminder = v.findViewById(R.id.fmReminder)
        super.initViews(v)
        setTextSize()
        setShadows()
        setListeners()
    }

    fun setShadows() {
        context?.let {
            val shadowColor = ContextCompat.getColor(it, com.tokopedia.gamification.R.color.gf_box_text_shadow)
            val shadowRadius = tvRewardFirstLine.dpToPx(5)
            val shadowOffset = tvRewardFirstLine.dpToPx(4)
            tvRewardFirstLine.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
            tvRewardSecondLine.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
            tvBenefits.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
        }

    }

    fun setTextSize() {
        if (isTablet) {
            tvBenefits.setTextSize(TypedValue.COMPLEX_UNIT_PX, 24.toPx().toFloat())
        }
    }

    private fun setListeners() {
        giftBoxDailyView.boxCallback = object : GiftBoxDailyView.BoxCallback {

            override fun onBoxOpenAnimationStart(startDelay: Long) {
                giftBoxDailyView.adjustGlowImagePosition()

                rewardContainer.setFinalTranslationOfCirclesTap(giftBoxDailyView.fmGiftBox.top)
                val stageLightAnim = giftBoxDailyView.stageGlowAnimation()

                giftBoxRewardEntity?.let {
                    rewardContainer.setRewards(it, asyncCallback = { rewardState ->
                        var soundDelay = 700L
                        giftBoxDailyView.postDelayed({ playPrizeSound() }, soundDelay)

                        when (rewardState) {
                            RewardContainer.RewardState.COUPON_WITH_POINTS -> {

                                val rewardAnim = rewardContainer.showCouponAndRewardAnimation(giftBoxDailyView.fmGiftBox.top)

                                val animatorSet = AnimatorSet()
                                animatorSet.playTogether(rewardAnim, stageLightAnim)
                                animatorSet.startDelay = startDelay
                                animatorSet.start()

                                val ovoPointsTextAnim = rewardContainer.ovoPointsTextAnimation()
                                ovoPointsTextAnim.startDelay = startDelay + 100L
                                ovoPointsTextAnim.start()
                            }
                            RewardContainer.RewardState.POINTS_ONLY -> {
                                val rewardAnim = rewardContainer.showSingleLargeRewardAnimation(giftBoxDailyView.fmGiftBox.top)

                                val animatorSet = AnimatorSet()
                                animatorSet.playTogether(stageLightAnim, rewardAnim)
                                animatorSet.startDelay = startDelay
                                animatorSet.start()
                            }

                            RewardContainer.RewardState.COUPON_ONLY -> {
                                val rewardAnim = rewardContainer.showCouponAndRewardAnimation(giftBoxDailyView.fmGiftBox.top)

                                val animatorSet = AnimatorSet()
                                animatorSet.playTogether(rewardAnim, stageLightAnim)
                                animatorSet.startDelay = startDelay
                                animatorSet.start()
                            }
                        }
                    })

                }
            }

            override fun onBoxScaleDownAnimationStart() {
                //Do nothing
            }

            override fun onBoxOpened() {

                val startsAnimatorList = starsContainer.getStarsAnimationList(giftBoxDailyView.fmGiftBox.top)
                startsAnimatorList.forEach {
                    it.start()
                }
                showRewardMessageDescription().start()
            }
        }

        viewModel.giftBoxLiveData.observe(viewLifecycleOwner, Observer { it ->
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    pltPerf.stopNetworkRequestPerformanceMonitoring()
                    pltPerf.startRenderPerformanceMonitoring()
                    if (it.data != null) {
                        val giftBoxEntity = it.data.first
                        val remindMeCheckEntity = it.data.second

                        val giftBoxStatusCode = giftBoxEntity.gamiLuckyHome?.resultStatus?.code
                        val remindMeCheckStatusCode = remindMeCheckEntity?.gameRemindMeCheck?.resultStatus?.code
                        if (giftBoxStatusCode == HTTP_STATUS_OK && remindMeCheckStatusCode == HTTP_STATUS_OK) {

                            tokenUserState = giftBoxEntity.gamiLuckyHome.tokensUser.state
                            reminder = giftBoxEntity.gamiLuckyHome.reminder
                            when (tokenUserState) {
                                TokenUserState.ACTIVE -> {
                                    if (!viewModel.campaignSlug.isNullOrEmpty()) {
                                        GtmEvents.viewGiftBoxPage(viewModel.campaignSlug!!, userSession?.userId)
                                    }
                                    reminderLayout.visibility = View.VISIBLE
                                    renderGiftBoxActive(giftBoxEntity)
                                    giftBoxDailyView.fmGiftBox.setOnClickListener {
                                        if (!disableGiftBoxTap) {
                                            if (!viewModel.campaignSlug.isNullOrEmpty()) {
                                                GtmEvents.clickGiftBox(viewModel.campaignSlug!!, userSession?.userId)
                                            }
                                            viewModel.getRewards()
                                            disableGiftBoxTap = true
                                        }
                                        playTapSound()
                                    }

                                    tvReminderMessage.text = reminder?.text
                                    setInitialUiForReminder()
                                    setClickEventOnReminder()
                                }
                                TokenUserState.EMPTY -> {
                                    reminderLayout.visibility = View.VISIBLE
                                    renderGiftBoxActive(giftBoxEntity)
                                    tvRewardFirstLine.visibility = View.GONE
                                    tvRewardSecondLine.visibility = View.GONE
                                    btnAction.visibility = View.GONE

                                    tvReminderMessage.text = reminder?.text
                                    setInitialUiForReminder()
                                    setClickEventOnReminder()
                                    GtmEvents.emptyBoxImpression(userSession?.userId)
                                }
                                else -> {
                                    hideLoader()
                                    val messageList = giftBoxEntity.gamiLuckyHome.resultStatus.message
                                    if (!messageList.isNullOrEmpty()) {
                                        renderGiftBoxError(messageList[0], "Oke", "${GiftBoxErrorCause.TOKEN_USER_STATE} = $tokenUserState")
                                    }

                                    tvReminderMessage.text = reminder?.text
                                    setInitialUiForReminder()

                                }
                            }

                            renderUiForReminderCheck(remindMeCheckEntity)
                        } else {
                            reminderLayout.visibility = View.GONE

                            if (remindMeCheckStatusCode != HTTP_STATUS_OK) {

                                val messageList = remindMeCheckEntity?.gameRemindMeCheck?.resultStatus?.message
                                if (!messageList.isNullOrEmpty()) {
                                    renderGiftBoxError(messageList[0], "Oke","${GiftBoxErrorCause.GAMI_REMIND_ME_CHECK_RESULT_STATUS_CODE} = $remindMeCheckStatusCode" )
                                }

                            } else if (giftBoxStatusCode != HTTP_STATUS_OK) {
                                val messageList = giftBoxEntity?.gamiLuckyHome?.resultStatus?.message
                                if (!messageList.isNullOrEmpty()) {
                                    renderGiftBoxError(messageList[0], "Oke", "${GiftBoxErrorCause.GAMI_LUCKYHOME_RESULT_STATUS_CODE} = $giftBoxStatusCode")
                                }
                            }
                        }
                    }
                    pltPerf.stopRenderPerformanceMonitoring()
                }

                LiveDataResult.STATUS.LOADING -> showLoader()

                LiveDataResult.STATUS.ERROR -> {
                    pltPerf.stopNetworkRequestPerformanceMonitoring()
                    pltPerf.startRenderPerformanceMonitoring()
                    hideLoader()
                    reminderLayout.visibility = View.GONE
                    renderGiftBoxError(defaultErrorMessage, "Oke", "GamiLuckHome - ${it.error?.message}")
                    pltPerf.stopRenderPerformanceMonitoring()
                }
            }
        })
        viewModel.rewardLiveData.observe(viewLifecycleOwner, Observer
        {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {

                    if (it.data == null) {
                        renderOpenBoxError(defaultErrorMessage, "Oke", GiftBoxErrorCause.DATA_NULL)
                    } else {
                        val code = it.data?.gamiCrack.resultStatus.code
                        if (code == HTTP_STATUS_OK) {
                            //set data in rewards first and then animate
                            disableGiftBoxTap = true
                            giftBoxRewardEntity = it.data
                            giftBoxDailyView.handleTapOnGiftBox()
                            fadeOutViews()

                            val benefitText = giftBoxRewardEntity?.gamiCrack?.benefitText
                            if (benefitText != null && benefitText.isNotEmpty()) {
                                tvRewardFirstLine.text = benefitText[0]
                                if (benefitText.size > 1) {
                                    val indexOfAnd = benefitText[1].indexOf("&")
                                    if (indexOfAnd > 0) {
                                        val array = benefitText[1].split("&")
                                        val sb = StringBuilder()
                                        sb.append(array[0])
                                        sb.append(" &")
                                        sb.append("\n")
                                        sb.append(array[1])
                                        tvRewardSecondLine.text = sb.toString()
                                    } else {
                                        tvRewardSecondLine.text = benefitText[1]
                                    }

                                }
                            }

                            val actionButtonList = giftBoxRewardEntity?.gamiCrack?.actionButton
                            if (actionButtonList != null && actionButtonList.isNotEmpty()) {
                                btnAction.text = actionButtonList[0].text
                                btnAction.setOnClickListener {
                                    checkInternetOnButtonActionAndRedirect()
                                }
                            }

                        } else {
                            disableGiftBoxTap = false
                            val messageList = it.data?.gamiCrack?.resultStatus?.message
                            if (!messageList.isNullOrEmpty()) {
                                renderOpenBoxError(messageList[0], "Oke", "${GiftBoxErrorCause.GAMI_CRACK_RESULT_STATUS_CODE} = $code")
                            }
                        }
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    disableGiftBoxTap = false
                    renderOpenBoxError(defaultErrorMessage, "Oke", "Reward Error - ${it.error?.message}")
                }
            }
        })

        viewModel.reminderSetLiveData.observe(viewLifecycleOwner, Observer
        {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    loaderReminder.visibility = View.VISIBLE
                    tvReminderBtn.visibility = View.INVISIBLE
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    loaderReminder.visibility = View.GONE
                    tvReminderBtn.visibility = View.VISIBLE

                    val code = it.data?.gameRemindMe?.resultStatus?.code
                    val reason = it.data?.gameRemindMe?.resultStatus?.reason

                    if (code == HTTP_STATUS_OK) {
                        renderReminderButton(true)
                    } else {
                        val messageList = it.data?.gameRemindMe?.resultStatus?.message
                        if (!messageList.isNullOrEmpty()) {
                            showRemindMeError(messageList[0], "Oke", "${GiftBoxErrorCause.GAMI_REMIND_ME_RESULT_STATUS_CODE} = $code")
                        }
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    loaderReminder.visibility = View.GONE
                    tvReminderBtn.visibility = View.VISIBLE
                    showRemindMeError(defaultErrorMessage, "Oke", "Remind Me - ${it.error?.message}" )
                }
            }
        })

        viewModel.autoApplyLiveData.observe(viewLifecycleOwner, Observer
        {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {
                    val code = it.data?.tokopointsSetAutoApply?.resultStatus?.code
                    val messageList = it.data?.tokopointsSetAutoApply?.resultStatus?.message
                    if (code == HTTP_STATUS_OK) {
                        if (autoApplyMessage.isNotEmpty() && context != null) {
                            CustomToast.show(context!!, autoApplyMessage)
                        }
                    } else {
                        if (!messageList.isNullOrEmpty()) {
                            CustomToast.show(context!!, messageList[0], isError = true)
                        }
                    }
                }
            }

        })
    }

    fun handleButtonAction() {
        val actionButtonList = giftBoxRewardEntity?.gamiCrack?.actionButton
        if (actionButtonList != null && actionButtonList.isNotEmpty()) {
            val applink = actionButtonList[0].applink
            if (!applink.isNullOrEmpty()) {

                //check for auto-apply
                var isAutoApply = false
                var dummyCode = ""
                val list = giftBoxRewardEntity?.gamiCrack?.benefits
                if (!list.isNullOrEmpty()) {
                    for (item in list) {
                        if (item.isAutoApply) {
                            isAutoApply = true
                            autoApplyMessage = item.autoApplyMsg
                            dummyCode = item.dummyCode
                            break
                        }
                    }
                }

                if (isAutoApply && !autoApplyMessage.isNullOrEmpty() && !dummyCode.isNullOrEmpty()) {
                    viewModel.autoApply(dummyCode)
                }
                RouteManager.route(context, applink)
                GtmEvents.clickClaimButton(btnAction.text.toString(), userSession?.userId)
            }
        }


    }

    fun setClickEventOnReminder() {
        tvReminderBtn.setOnClickListener {
            if (!isReminderSet) {
                viewModel.setReminder()
                GtmEvents.clickReminderButton(userSession?.userId)
            }
        }
    }

    override fun playLoopSound() {
        // Don't want to play sound
    }

    fun renderUiForReminderCheck(remindMeCheckEntity: RemindMeCheckEntity) {
        this.gameRemindMeCheck = remindMeCheckEntity?.gameRemindMeCheck
        loaderReminder.visibility = View.GONE
        tvReminderBtn.visibility = View.VISIBLE
        val isRemindMe = gameRemindMeCheck?.isRemindMe
        if (isRemindMe != null) {
            reminderLayout.visibility = View.VISIBLE
            renderReminderButton(isRemindMe)
        } else {
            reminderLayout.visibility = View.GONE
        }
    }

    fun setInitialUiForReminder() {
        if (gameRemindMeCheck != null) {
            val isRemindMe = gameRemindMeCheck?.isRemindMe
            if (isRemindMe != null)
                renderReminderButton(isRemindMe)
        }
    }

    fun renderOpenBoxError(message: String, actionText: String, cause:String?) {
        if (context != null) {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(viewModel::getRewards, context!!)
            } else {
                showRedError(fmParent, message, actionText, cause, viewModel::getRewards)
            }
        }
    }

    fun showRemindMeError(message: String, actionText: String, cause:String?) {
        if (context != null) {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(viewModel::setReminder, context!!)
            } else {
                showRedError(fmParent, message, actionText, cause, viewModel::setReminder)
            }
        }
    }

    fun renderReminderButton(isUserReminded: Boolean) {
        context?.let {
            if (isUserReminded) {
                fmReminder.background = ContextCompat.getDrawable(it, com.tokopedia.gamification.R.drawable.gf_bg_disabled_3d)
                tvReminderBtn.text = reminder?.disableText
                isReminderSet = true
            } else {
                tvReminderBtn.text = reminder?.enableText
                fmReminder.background = ContextCompat.getDrawable(it, com.tokopedia.gamification.R.drawable.gf_bg_green_3d)
                isReminderSet = false
            }
            tvReminderMessage.text = reminder?.text
        }
    }


    fun setPositionOfViewsAtBoxOpen(@TokenUserState state: String) {
        rewardContainer.setFinalTranslationOfCirclesTap(giftBoxDailyView.fmGiftBox.top)

        giftBoxDailyView.fmGiftBox.doOnLayout { fmGiftBox ->
            val heightOfRvCoupons = fmGiftBox.context.resources.getDimension(com.tokopedia.gamification.R.dimen.gami_rv_coupons_height)
            val lidTop = fmGiftBox.top
            var translationY = lidTop - heightOfRvCoupons + fmGiftBox.dpToPx(3)
            if (isTablet) {
                translationY -= fmGiftBox.dpToPx(8)
            }

            rewardContainer.rvCoupons.translationY = translationY
            val distanceFromLidTop = fmGiftBox.dpToPx(29)
            rewardContainer.llRewardTextLayout.translationY = lidTop + distanceFromLidTop

            tvTapHint.doOnLayout { tapHint ->
                if (giftBoxDailyView.height > LARGE_PHONE_HEIGHT) {
                    tapHint.translationY = lidTop - fmGiftBox.context.resources.getDimension(R.dimen.gami_tap_hint_margin) - tapHint.height
                }

                if (isTablet) {
                    tapHint.translationY = lidTop - fmGiftBox.context.resources.getDimension(R.dimen.gami_tap_hint_margin_tablet) - tapHint.height
                }
            }

        }
        giftBoxDailyView.imageBoxFront.doOnLayout { imageBoxFront ->

            val imageFrontTop = imageBoxFront.top + giftBoxDailyView.fmGiftBox.top
            val translationY = imageFrontTop - imageBoxFront.dpToPx(40)
            starsContainer.setStartPositionOfStars(starsContainer.width / 2f, translationY)

            if (state == TokenUserState.EMPTY) {
                llBenefits.translationY = imageFrontTop + imageBoxFront.height + imageBoxFront.dpToPx(18)
            } else {
                llBenefits.updateLayoutParams<FrameLayout.LayoutParams> {
                    this.gravity = Gravity.BOTTOM
                }
            }

            llRewardMessage.translationY = imageFrontTop + imageBoxFront.height + imageBoxFront.dpToPx(16)

        }

        giftBoxDailyView.fmGiftBox.doOnLayout { fm ->
            if (isTablet) {
                var offsetY = fm.dpToPx(62)
                giftBoxDailyView.imageShadow.translationX = 0f
                giftBoxDailyView.imageShadow.translationY = fm.bottom.toFloat() - offsetY
            } else {
                giftBoxDailyView.imageShadow.translationX = fm.left.toFloat()
                giftBoxDailyView.imageShadow.translationY = fm.bottom.toFloat() - fm.dpToPx(50)

            }
        }
    }

    fun showRewardMessageDescription(): Animator {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
        val alphaAnim = ObjectAnimator.ofPropertyValuesHolder(llRewardMessage, alphaProp)

        val alphaAnimReminder = ObjectAnimator.ofPropertyValuesHolder(reminderLayout, alphaProp)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(alphaAnim, alphaAnimReminder)
        animatorSet.duration = 200L
        return animatorSet
    }

    override fun initialViewSetup() {
        super.initialViewSetup()

        tvTapHint.setBackgroundResource(R.drawable.gami_bg_text_hint_box)
        tvTapHint.setTextColor(ContextCompat.getColor(tvTapHint.context, R.color.gf_tap_hint))
        llBenefits.alpha = 0f
        llRewardMessage.alpha = 0f
        reminderLayout.alpha = 0f
        loaderReminder.visibility = View.GONE
        setDynamicMargin()
    }

    fun setDynamicMargin() {
        val dpi = resources.displayMetrics.densityDpi
        when (dpi) {
            DisplayMetrics.DENSITY_MEDIUM, DisplayMetrics.DENSITY_HIGH -> {
                tvTapHint.updateLayoutParams<FrameLayout.LayoutParams> {
                    val margin = tvTapHint.dpToPx(40).toInt()
                    setMargins(margin, 0, margin, 0)
                }
            }
            else -> {
            }
        }
    }


    fun renderGiftBoxActive(entity: GiftBoxEntity) {

        tvTapHint.text = entity.gamiLuckyHome.tokensUser.title
        tvBenefits.text = entity.gamiLuckyHome.tokensUser.text

        if (tokenUserState == TokenUserState.EMPTY) {
            tvBenefits.setType(Typography.HEADING_2)
            tvBenefits.setWeight(Typography.BOLD)
            tvBenefits.translationY = tvBenefits.dpToPx(5)
        }

        if (tvTapHint.text.isNullOrEmpty()) {
            tvTapHint.visibility = View.GONE
        }

        //set prize list
        loadPrizeImagesAsync(entity) {
            val imageUrlList = entity.gamiLuckyHome.tokenAsset.imageV2URLs
            var frontImageUrl = ""
            var bgUrl = entity.gamiLuckyHome.tokenAsset.backgroundImgURL
            if (!imageUrlList.isNullOrEmpty()) {
                frontImageUrl = imageUrlList[0]
                if (frontImageUrl.isEmpty()) {
                    frontImageUrl = ""
                }
            }
            val lidImages = arrayListOf<String>()

            if (imageUrlList != null && imageUrlList.size > 2) {
                lidImages.addAll(imageUrlList.subList(2, imageUrlList.size))
            }

            if (!bgUrl.isNullOrEmpty())
                fadeInActiveStateViews(frontImageUrl, bgUrl, lidImages)
        }
    }

    fun loadPrizeImagesAsync(entity: GiftBoxEntity, imageCallback: (() -> Unit)) {
        loadedPrizeImagesCount = 0
        totalPrizeImagesCount = 0

        fun checkImageLoadStatus() {
            loadedPrizeImagesCount += 1
            if (loadedPrizeImagesCount == totalPrizeImagesCount) {
                imageCallback.invoke()
            }
        }
        entity.gamiLuckyHome.prizeList?.forEach {
            if (it.isSpecial) {
                totalPrizeImagesCount += 1
                prizeViewLarge.setData(it.imageURL, it.text) {
                    checkImageLoadStatus()
                }
                prizeViewLarge.visibility = View.VISIBLE
            } else {
                if (prizeViewSmallFirst.tvTitle.text.isNullOrEmpty()) {
                    prizeViewSmallFirst.setData(it.imageURL, it.text) {
                        checkImageLoadStatus()
                    }
                    prizeViewSmallFirst.visibility = View.VISIBLE
                    totalPrizeImagesCount += 1
                } else {
                    prizeViewSmallSecond.setData(it.imageURL, it.text) {
                        checkImageLoadStatus()
                    }
                    prizeViewSmallSecond.visibility = View.VISIBLE
                    totalPrizeImagesCount += 1
                }
            }
        }
        if (totalPrizeImagesCount == loadedPrizeImagesCount) {
            imageCallback.invoke()
        }
    }

    fun fadeOutViews() {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)
        val tvBenefitsAnim = ObjectAnimator.ofPropertyValuesHolder(tvBenefits, alphaProp)
        val prizeListContainerAnim = ObjectAnimator.ofPropertyValuesHolder(llBenefits, alphaProp)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapHintAnim, prizeListContainerAnim, tvBenefitsAnim)
        animatorSet.duration = 300L

        animatorSet.start()
    }

    private fun fadeInActiveStateViews(frontImageUrl: String, imageBgUrl: String, lidImages: List<String>) {
        giftBoxDailyView.loadFiles(tokenUserState, frontImageUrl, imageBgUrl, lidImages,viewLifecycleOwner, imageCallback = {
            giftBoxDailyView.imagesLoaded.lazySet(0)
            if (it) {
                setPositionOfViewsAtBoxOpen(tokenUserState)
                hideLoader()

                val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)
                val giftBoxAnim = ObjectAnimator.ofPropertyValuesHolder(giftBoxDailyView, alphaProp)
                val prizeListContainerAnim = ObjectAnimator.ofPropertyValuesHolder(llBenefits, alphaProp)

                val animatorSet = AnimatorSet()
                if (tokenUserState == TokenUserState.EMPTY) {
                    val rewardAlphaAnim = ObjectAnimator.ofPropertyValuesHolder(llRewardMessage, alphaProp)
                    val reminderAlphaAnim = ObjectAnimator.ofPropertyValuesHolder(reminderLayout, alphaProp)
                    animatorSet.playTogether(tapHintAnim, giftBoxAnim, prizeListContainerAnim, rewardAlphaAnim, reminderAlphaAnim)
                } else {
                    animatorSet.playTogether(tapHintAnim, giftBoxAnim, prizeListContainerAnim)
                }

                animatorSet.duration = FADE_IN_DURATION

                animatorSet.addListener(onEnd = {
                    if (tokenUserState == TokenUserState.ACTIVE) {
                        giftBoxDailyView.startInitialAnimation()
                    }
                })
                animatorSet.start()
            } else {
                //Do nothing
                hideLoader()
                renderGiftBoxError(defaultErrorMessage, "Oke", GiftBoxErrorCause.IMAGE_LOADING_ERROR)
            }
        })
    }

    fun checkInternetOnButtonActionAndRedirect() {
        if (context != null) {
            var internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(this::checkInternetOnButtonActionAndRedirect, context!!)
            } else {
                handleButtonAction()
            }
        }
    }

    fun renderGiftBoxError(message: String, actionText: String, cause:String?) {
        if (context != null) {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(viewModel::getGiftBox, context!!)
            } else {
                showRedError(fmParent, message, actionText, cause, viewModel::getGiftBox)
            }
        }
    }

}

@Retention(AnnotationRetention.SOURCE)
@StringDef(ACTIVE, EMPTY, DEFAULT)
annotation class TokenUserState {
    companion object {
        const val ACTIVE = "active"
        const val EMPTY = "empty"
        const val DEFAULT = "default"
    }
}