package com.tokopedia.gamification.giftbox.presentation.fragments

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.tokopedia.analytics.performance.util.PageLoadTimePerformanceInterface
import com.tokopedia.applink.RouteManager
import com.tokopedia.dialog.DialogUnify
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
import com.tokopedia.gamification.giftbox.presentation.viewmodels.AutoApplyCallback
import com.tokopedia.gamification.giftbox.presentation.viewmodels.GiftBoxDailyViewModel
import com.tokopedia.gamification.giftbox.presentation.views.*
import com.tokopedia.gamification.pdp.data.LiveDataResult
import com.tokopedia.gamification.pdp.presentation.views.PdpErrorListener
import com.tokopedia.gamification.pdp.presentation.views.PdpGamificationView
import com.tokopedia.gamification.pdp.presentation.views.Wishlist
import com.tokopedia.gamification.taptap.data.entiity.BackButton
import com.tokopedia.kotlin.extensions.view.setMargin
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.unifycomponents.toPx
import kotlinx.android.synthetic.main.fragment_gift_box_daily.*
import timber.log.Timber
import javax.inject.Inject

class GiftBoxDailyFragment : GiftBoxBaseFragment() {

    lateinit var rewardContainer: RewardContainerDaily
    lateinit var llRewardMessage: LinearLayout
    lateinit var tvRewardFirstLine: AppCompatTextView
    lateinit var tvRewardSecondLine: AppCompatTextView
    lateinit var tokoButtonContainer: CekTokoButtonContainer
    lateinit var directGiftView: GamiDirectGiftView
    lateinit var pdpGamificationView: PdpGamificationView

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: GiftBoxDailyViewModel
    var giftBoxRewardEntity: GiftBoxRewardEntity? = null
    var isReminderSet = false
    var reminder: Reminder? = null
    var gameRemindMeCheck: GameRemindMeCheck? = null
    var pltPerf: PageLoadTimePerformanceInterface? = null
    lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    @TokenUserState
    var tokenUserState: String = TokenUserState.DEFAULT
    var disableGiftBoxTap = false
    var autoApplyMessage = ""
    var infoUrl: String? = null
    var backButtonData: BackButton? = null
    val APPLNK_REWARD_HISTORY_KEY = "app_flag_gami_reward_history"
    val APPLNK_HOME = "tokopedia://home"

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
        try {
            pltPerf = PltModule().providePerfInterface()
            pltPerf?.startMonitoring(GAMI_GIFT_DAILY_TRACE_PAGE)
            pltPerf?.startPreparePagePerformanceMonitoring()
        } catch (ex: Throwable) {
            Timber.e(ex)
        }

    }

    override fun onDestroy() {
        super.onDestroy()

        pltPerf?.stopMonitoring()

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
        pltPerf?.stopPreparePagePerformanceMonitoring()
        pltPerf?.startNetworkRequestPerformanceMonitoring()
        viewModel.getGiftBox()
        return v
    }

    override fun initViews(v: View) {
        llRewardMessage = v.findViewById(R.id.ll_reward_message)
        rewardContainer = v.findViewById(R.id.reward_container)
        tvRewardFirstLine = v.findViewById(R.id.tvRewardFirstLine)
        tvRewardSecondLine = v.findViewById(R.id.tvRewardSecondLine)
        tokoButtonContainer = v.findViewById(R.id.tokoBtnContainer)
        tvLoaderMessage = v.findViewById(R.id.tvLoaderMessage)
        directGiftView = v.findViewById(R.id.direct_gift_view)
        pdpGamificationView = v.findViewById(R.id.pdpGamificationView)
        bottomSheetBehavior = BottomSheetBehavior.from<ViewGroup>(pdpGamificationView)
        super.initViews(v)
        pdpGamificationView.userId = userSession?.userId
        setTabletConfigurations()
        setShadows()
        setListeners()
        setupBottomSheet(false)
        preloadAssets()
    }

    fun preloadAssets() {
        context?.let {
            val w = directGiftView.dpToPx(56).toInt()
            Glide.with(it)
                    .load(R.drawable.gf_glowing_ovo)
                    .preload()
        }
    }

    private fun setupBottomSheet(show: Boolean) {
        if (show) {
            pdpGamificationView.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            bottomSheetBehavior.isHideable = false
        } else {
            val peekHeight = resources.getDimension(R.dimen.gami_peek_height).toInt()
            bottomSheetBehavior.peekHeight = peekHeight
            bottomSheetBehavior.isHideable = true
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            pdpGamificationView.fragment = this
        }
    }

    fun setShadows() {
        context?.let {
            val shadowColor = ContextCompat.getColor(it, com.tokopedia.gamification.R.color.gf_box_text_shadow)
            val shadowRadius = tvRewardFirstLine.dpToPx(5)
            val shadowOffset = tvRewardFirstLine.dpToPx(4)
            tvRewardFirstLine.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
            tvRewardSecondLine.setShadowLayer(shadowRadius, 0f, shadowOffset, shadowColor)
        }

    }

    fun setTabletConfigurations() {
        //Do nothing
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
                            RewardContainer.RewardState.COUPON_ONLY -> {
                                performRewardAnimation(startDelay, stageLightAnim)
                            }

                            RewardContainer.RewardState.RP_0_ONLY -> {
                                backButtonData = BackButton(requireContext().getString(R.string.gami_back_button_cancel),
                                        null, true,
                                        requireContext().getString(R.string.gami_back_button_message),
                                        requireContext().getString(R.string.gami_back_button_title),
                                        requireContext().getString(R.string.gami_back_button_ok))

                                performRp0Animation(startDelay)
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
                    pltPerf?.stopNetworkRequestPerformanceMonitoring()
                    pltPerf?.startRenderPerformanceMonitoring()
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
                                    tokoButtonContainer.toggleReminderVisibility(true)
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

                                    setInitialUiForReminder()
                                    setClickEventOnReminder()
                                }
                                TokenUserState.EMPTY -> {
                                    tokoButtonContainer.toggleReminderVisibility(true)
                                    directGiftView.visibility = View.GONE
                                    renderGiftBoxActive(giftBoxEntity)
                                    tvRewardFirstLine.visibility = View.GONE
                                    tokoButtonContainer.btnSecond.visibility = View.GONE
                                    setInitialUiForReminder()
                                    setClickEventOnReminder()
                                    GtmEvents.emptyBoxImpression(userSession?.userId)
                                }
                                else -> {
                                    hideLoader()
                                    val messageList = giftBoxEntity.gamiLuckyHome.resultStatus.message
                                    if (!messageList.isNullOrEmpty()) {
                                        renderGiftBoxError(messageList[0], getString(R.string.gami_oke))
                                    }
                                    setInitialUiForReminder()

                                }
                            }

                            renderUiForReminderCheck(remindMeCheckEntity, reminder?.isShow ?: false)
                        } else {
                            tokoButtonContainer.toggleReminderVisibility(false)

                            if (remindMeCheckStatusCode != HTTP_STATUS_OK) {

                                val messageList = remindMeCheckEntity?.gameRemindMeCheck?.resultStatus?.message
                                if (!messageList.isNullOrEmpty()) {
                                    renderGiftBoxError(messageList[0], getString(R.string.gami_oke))
                                }

                            } else if (giftBoxStatusCode != HTTP_STATUS_OK) {
                                val messageList = giftBoxEntity?.gamiLuckyHome?.resultStatus?.message
                                if (!messageList.isNullOrEmpty()) {
                                    renderGiftBoxError(messageList[0], getString(R.string.gami_oke))
                                }
                            }
                        }
                        handleInfoIcon(giftBoxStatusCode, giftBoxEntity.gamiLuckyHome?.infoUrl)
                    }
                    pltPerf?.stopRenderPerformanceMonitoring()
                }

                LiveDataResult.STATUS.LOADING -> showLoader()

                LiveDataResult.STATUS.ERROR -> {
                    pltPerf?.stopNetworkRequestPerformanceMonitoring()
                    pltPerf?.startRenderPerformanceMonitoring()
                    hideLoader()
                    tokoButtonContainer.toggleReminderVisibility(false)
                    renderGiftBoxError(defaultErrorMessage, getString(R.string.gami_oke))
                    pltPerf?.stopRenderPerformanceMonitoring()
                }
            }
        })
        viewModel.rewardLiveData.observe(viewLifecycleOwner, Observer
        {
            when (it.status) {
                LiveDataResult.STATUS.SUCCESS -> {

                    if (it.data == null) {
                        renderOpenBoxError(defaultErrorMessage, getString(R.string.gami_oke))
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
                                        var i = 0
                                        while (i < array.size) {
                                            sb.append(array[i])
                                            if (i != array.size - 1) {
                                                sb.append(" & ")
                                            }
                                            i += 1
                                        }
                                        tvRewardSecondLine.text = sb.toString()
                                    } else {
                                        tvRewardSecondLine.text = benefitText[1]
                                    }

                                }
                            }

                            val actionButtonList = giftBoxRewardEntity?.gamiCrack?.actionButton
                            if (actionButtonList != null
                                    && actionButtonList.isNotEmpty()
                                    && !actionButtonList[0].type.isNullOrEmpty()
                                    && actionButtonList[0].type == "redirect"
                            ) {
                                tokoBtnContainer.setSecondButtonText(actionButtonList[0].text)
                                tokoBtnContainer.btnSecond.setOnClickListener {
                                    checkInternetOnButtonActionAndRedirect()
                                }
                            } else {
                                tokoButtonContainer.btnSecond.visibility = View.GONE
                            }

                            handleRecomPage(it.data?.gamiCrack?.recommendation)

                        } else {
                            disableGiftBoxTap = false
                            val messageList = it.data?.gamiCrack?.resultStatus?.message
                            if (!messageList.isNullOrEmpty()) {
                                renderOpenBoxError(messageList[0], getString(R.string.gami_oke))
                            } else {
                                renderOpenBoxError(defaultErrorMessage, getString(R.string.gami_oke))
                            }
                        }
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    disableGiftBoxTap = false
                    renderOpenBoxError(defaultErrorMessage, getString(R.string.gami_oke))
                }
            }
        })

        viewModel.reminderSetLiveData.observe(viewLifecycleOwner, Observer
        {
            when (it.status) {
                LiveDataResult.STATUS.LOADING -> {
                    tokoButtonContainer.btnReminder.performLoading()
                }
                LiveDataResult.STATUS.SUCCESS -> {
                    tokoButtonContainer.btnReminder.stopLoading()

                    val code = it.data?.gameRemindMe?.resultStatus?.code
                    val reason = it.data?.gameRemindMe?.resultStatus?.reason

                    if (code == HTTP_STATUS_OK) {
                        renderReminderButton(it.data.gameRemindMe.requestToSetReminder, true)
                    } else {
                        val messageList = it.data?.gameRemindMe?.resultStatus?.message
                        if (!messageList.isNullOrEmpty()) {
                            showRemindMeError(messageList[0], getString(R.string.gami_oke))
                        }
                        GtmEvents.clickReminderButton(userSession?.userId, null)
                    }
                }
                LiveDataResult.STATUS.ERROR -> {
                    tokoButtonContainer.btnReminder.stopLoading()
                    showRemindMeError(defaultErrorMessage, getString(R.string.gami_oke))
                    GtmEvents.clickReminderButton(userSession?.userId, null)
                }
            }
        })

        viewModel.autoApplycallback = object : AutoApplyCallback {
            override fun success(response: AutoApplyResponse?) {
                try {
                    val code = response?.tokopointsSetAutoApply?.resultStatus?.code
                    val messageList = response?.tokopointsSetAutoApply?.resultStatus?.message
                    if (code == HTTP_STATUS_OK) {
                        if (autoApplyMessage.isNotEmpty() && context != null) {
                            CustomToast.show(context!!, autoApplyMessage)
                        }
                    } else {
                        if (!messageList.isNullOrEmpty()) {
                            CustomToast.show(context!!, messageList[0], isError = true)
                        }
                    }
                } catch (th: Throwable) {
                    Timber.e(th)
                }
            }
        }

        pdpGamificationView.errorListener = object : PdpErrorListener {
            override fun onError() {
                setupBottomSheet(false)
            }

        }
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    GtmEvents.clickProductRecom(userSession?.userId)
                    pdpGamificationView.recyclerView.scrollBy(0, 1.toPx())
                }
            }
        })
    }

    fun handleRecomPage(recommendation: Recommendation?) {
        recommendation?.isShow?.let { _ ->
            if (canShowRecomPage(recommendation)) {
                rewardContainer.postDelayed({
                    setupBottomSheet(true)
                    var shopId = ""
                    if (!recommendation.shopId.isNullOrEmpty()) {
                        shopId = recommendation.shopId
                    }
                    pdpGamificationView.getRecommendationParams(recommendation.pageName ?: "", shopId, shopId.isEmpty())
                }, 1000L)

            }
        }
    }

    fun canShowRecomPage(recommendation: Recommendation?): Boolean {
        if (recommendation != null) {
            return recommendation.isShow == true && !recommendation.pageName.isNullOrEmpty()
        }
        return false
    }

    fun performRewardAnimation(startDelay: Long, stageLightAnim: Animator) {
        val rewardAnim = rewardContainer.showCouponAndRewardAnimation(giftBoxDailyView.fmGiftBox.top)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(rewardAnim, stageLightAnim)
        animatorSet.startDelay = startDelay
        animatorSet.start()
    }

    fun performRp0Animation(startDelay: Long) {
        val rewardAnim = rewardContainer.showSingleLargeRewardAnimation()
        rewardAnim.startDelay = startDelay
        rewardAnim.start()
    }

    private fun handleInfoIcon(statusCode: String?, infoUrl: String?) {
        if (statusCode == HTTP_STATUS_OK && !infoUrl.isNullOrEmpty()) {
            this.infoUrl = infoUrl
            activity?.invalidateOptionsMenu()
        }
    }

    override fun handleInfoIconClick() {
        RouteManager.route(context, infoUrl)
        GtmEvents.clickInfoButton(userSession?.userId)
    }

    private fun handleButtonAction() {
        val actionButtonList = giftBoxRewardEntity?.gamiCrack?.actionButton
        if (actionButtonList != null && actionButtonList.isNotEmpty()) {
            val applink = actionButtonList[0].applink
            if (!applink.isNullOrEmpty()) {
                performAutoApply()
                RouteManager.route(context, applink)
                GtmEvents.clickClaimButton(tokoButtonContainer.btnSecond.btn.text.toString(), userSession?.userId)
            }
        }
    }

    fun performAutoApply() {
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
    }

    fun setClickEventOnReminder() {
        tokoButtonContainer.btnReminder.setOnClickListener {
            if (!isReminderSet) {
                viewModel.setReminder()
            } else {
                viewModel.unSetReminder()
            }
        }
    }

    override fun playLoopSound() {
        // Don't want to play sound
    }

    fun renderUiForReminderCheck(remindMeCheckEntity: RemindMeCheckEntity, showReminder: Boolean) {
        this.gameRemindMeCheck = remindMeCheckEntity?.gameRemindMeCheck
        tokoButtonContainer.btnReminder.stopLoading()
        val isRemindMe = gameRemindMeCheck?.isRemindMe
        if (isRemindMe != null && showReminder) {
            tokoButtonContainer.toggleReminderVisibility(true)
            renderReminderButton(isRemindMe, false)
        } else {
            tokoButtonContainer.toggleReminderVisibility(false)
        }
    }

    fun setInitialUiForReminder() {
        if (gameRemindMeCheck != null) {
            val isRemindMe = gameRemindMeCheck?.isRemindMe
            if (isRemindMe != null)
                renderReminderButton(isRemindMe, false)
        }
    }

    fun renderOpenBoxError(message: String, actionText: String) {
        context?.let {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(viewModel::getRewards, it)
            } else {
                showRedError(fmParent, message, actionText, viewModel::getRewards)
            }
        }
    }

    fun showRemindMeError(message: String, actionText: String) {
        context?.let {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(viewModel::setReminder, it)
            } else {
                showRedError(fmParent, message, actionText, viewModel::setReminder)
            }
        }
    }

    fun renderReminderButton(isUserReminded: Boolean, showToast: Boolean) {
        context?.let {
            if (isUserReminded) {
                tokoButtonContainer.btnReminder.setText(reminder?.buttonUnset)
                isReminderSet = true
                if (showToast && !reminder?.textSet.isNullOrEmpty()) {
                    CustomToast.show(context, reminder?.textSet!!)
                    GtmEvents.clickReminderButton(userSession?.userId, reminder?.textSet!!)
                }
            } else {
                tokoButtonContainer.btnReminder.setText(reminder?.buttonSet)
                isReminderSet = false
                if (showToast && !reminder?.textUnset.isNullOrEmpty()) {
                    CustomToast.show(context, reminder?.textUnset!!)
                    GtmEvents.clickReminderButton(userSession?.userId, reminder?.textUnset!!)
                }
            }
            tokoButtonContainer.btnReminder.setIcon(isUserReminded)
        }
    }


    fun setPositionOfViewsAtBoxOpen(@TokenUserState state: String) {
        rewardContainer.setFinalTranslationOfCirclesTap(giftBoxDailyView.fmGiftBox.top)

        giftBoxDailyView.fmGiftBox.doOnLayout { fmGiftBox ->
            val heightOfRvCoupons = fmGiftBox.context.resources.getDimension(com.tokopedia.gamification.R.dimen.gami_rv_coupons_height_daily)
            val lidTop = fmGiftBox.top
            var translationY = lidTop - heightOfRvCoupons + fmGiftBox.dpToPx(3)
            if (isTablet) {
                translationY -= fmGiftBox.dpToPx(8)
            }

            rewardContainer.rvCoupons.translationY = translationY
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
        val alphaAnimReminder = ObjectAnimator.ofPropertyValuesHolder(tokoButtonContainer.btnReminder, alphaProp)
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(alphaAnim, alphaAnimReminder)
        animatorSet.duration = 200L
        return animatorSet
    }

    override fun initialViewSetup() {
        super.initialViewSetup()

        tvTapHint.setBackgroundResource(R.drawable.gami_bg_text_hint_box)
        tvTapHint.setTextColor(ContextCompat.getColor(tvTapHint.context, R.color.gf_tap_hint))
        directGiftView.alpha = 0f
        llRewardMessage.alpha = 0f
        tokoButtonContainer.btnReminder.alpha = 0f

        tokoButtonContainer.btnReminder.loaderReminder.visibility = View.GONE
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
        if (tokenUserState == TokenUserState.ACTIVE) {
            directGiftView.setData(entity.gamiLuckyHome.prizeList,
                    entity.gamiLuckyHome.bottomSheetButtonText,
                    entity.gamiLuckyHome.prizeDetailList,
                    entity.gamiLuckyHome.prizeDetailListButton,
                    userSession?.userId
            )
            if (isTablet) {
                tvRewardFirstLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22f)
                tvRewardSecondLine.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26f)
                tokoButtonContainer.wrapButtons()
            }
        }

        if (tokenUserState == TokenUserState.EMPTY) {
            tokoButtonContainer.wrapButtons()
            val sideMargin = 8.toPx()
            val topMargin = 32.toPx()
            tvRewardSecondLine.setMargin(sideMargin, topMargin, sideMargin, topMargin)
            tvRewardSecondLine.text = entity.gamiLuckyHome.tokensUser.text
            tvRewardSecondLine.setTextSize(TypedValue.COMPLEX_UNIT_PX, 20.toPx().toFloat())
        }

        if (tvTapHint.text.isNullOrEmpty()) {
            tvTapHint.visibility = View.GONE
        }

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

    fun fadeOutViews() {
        val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)
        val prizeListContainerAnim = ObjectAnimator.ofPropertyValuesHolder(directGiftView, alphaProp)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(tapHintAnim, prizeListContainerAnim)
        animatorSet.duration = 300L
        animatorSet.addListener(onEnd = { directGiftView.visibility = View.GONE })
        animatorSet.start()
    }

    private fun fadeInActiveStateViews(frontImageUrl: String, imageBgUrl: String, lidImages: List<String>) {
        giftBoxDailyView.loadFiles(tokenUserState, frontImageUrl, imageBgUrl, lidImages, viewLifecycleOwner, imageCallback = {
            giftBoxDailyView.imagesLoaded.lazySet(0)
            if (it) {
                setPositionOfViewsAtBoxOpen(tokenUserState)
                hideLoader()

                val alphaProp = PropertyValuesHolder.ofFloat(View.ALPHA, 0f, 1f)
                val tapHintAnim = ObjectAnimator.ofPropertyValuesHolder(tvTapHint, alphaProp)
                val giftBoxAnim = ObjectAnimator.ofPropertyValuesHolder(giftBoxDailyView, alphaProp)

                val animatorSet = AnimatorSet()
                if (tokenUserState == TokenUserState.EMPTY) {
                    val rewardAlphaAnim = ObjectAnimator.ofPropertyValuesHolder(llRewardMessage, alphaProp)
                    val reminderAlphaAnim = ObjectAnimator.ofPropertyValuesHolder(tokoButtonContainer.btnReminder, alphaProp)
                    animatorSet.playTogether(tapHintAnim, giftBoxAnim, rewardAlphaAnim, reminderAlphaAnim)
                } else {
                    val prizeListContainerAnim = ObjectAnimator.ofPropertyValuesHolder(directGiftView, alphaProp)
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
                renderGiftBoxError(defaultErrorMessage, getString(R.string.gami_oke))
            }
        })
    }

    fun checkInternetOnButtonActionAndRedirect() {
        context?.let {
            var internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(this::checkInternetOnButtonActionAndRedirect, it)
            } else {
                handleButtonAction()
            }
        }
    }

    fun renderGiftBoxError(message: String, actionText: String) {
        context?.let {
            val internetAvailable = isConnectedToInternet()
            if (!internetAvailable) {
                showNoInterNetDialog(viewModel::getGiftBox, it)
            } else {
                showRedError(fmParent, message, actionText, viewModel::getGiftBox)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            Wishlist.REQUEST_FROM_PDP -> {
                if (data != null) {
                    val wishlistStatusFromPdp = data.getBooleanExtra(Wishlist.PDP_WIHSLIST_STATUS_IS_WISHLIST, false)
                    val position = data.getIntExtra(Wishlist.PDP_EXTRA_UPDATED_POSITION, -1)
                    pdpGamificationView.onActivityResult(position, wishlistStatusFromPdp)
                }
            }
        }
    }

    override fun getMenu() = if (infoUrl.isNullOrEmpty()) R.menu.gami_menu_share else R.menu.gami_menu_daily

    private fun showBackDialog(backButton: BackButton, appLinkForReward: String) {
        context?.let {
            val dialog = DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.WITH_ICON)
            dialog.setTitle(backButton.title)
            dialog.dialogImageContainer.layoutParams.apply {
                height = 180.toPx()
                width = 180.toPx()
            }
            dialog.dialogImageContainer.setBackgroundColor(ContextCompat.getColor(it, R.color.gf_black_transparent))
            dialog.dialogImageContainer.outlineProvider = null
            dialog.setImageDrawable(R.drawable.gami_exit_icon)
            dialog.setDescription(backButton.text)
            dialog.setPrimaryCTAText(backButton.yesText)
            dialog.setPrimaryCTAClickListener {
                try {
                    dialog.dismiss()
                    backButtonData = null
                    RouteManager.route(context, appLinkForReward)
                    GtmEvents.clickGreenCtaOnBackButtonDialog(userSession?.userId)
                } catch (ex: Exception) {
                }
            }
            dialog.setSecondaryCTAText(backButton.cancelText)
            dialog.setSecondaryCTAClickListener {
                try {
                    dialog.dismiss()
                    RouteManager.route(context, APPLNK_HOME)
                    GtmEvents.clickCancelCtaOnBackButtonDialog(userSession?.userId)
                } catch (ex: Exception) {
                }

            }
            if (isTablet) {
                val layoutParams = dialog.findViewById<View>(com.tokopedia.dialog.R.id.dialog_bg).layoutParams
                layoutParams.width = (giftBoxDailyView.width - giftBoxDailyView.width * 0.3).toInt()
            }
            dialog.show()
            GtmEvents.impressionRpZeroDialog(userSession?.userId)
        }
    }

    private fun getAppLinkForBackButtonDialog(): String? {
        try {
            val remoteConfig = FirebaseRemoteConfigImpl(requireContext())
            return remoteConfig.getString(APPLNK_REWARD_HISTORY_KEY, "")
        } catch (e: Exception) {
            return ""
        }
    }

    fun onBackPressed(): Boolean {
        if (backButtonData != null) {
            val appLinkForReward = getAppLinkForBackButtonDialog()
            if (!appLinkForReward.isNullOrEmpty()) {
                showBackDialog(backButtonData!!, appLinkForReward)
                return false
            }
        }
        return true
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