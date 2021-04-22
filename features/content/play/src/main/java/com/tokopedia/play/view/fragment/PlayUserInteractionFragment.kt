package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.PlayPiPAnalytic
import com.tokopedia.play.analytic.ProductAnalyticHelper
import com.tokopedia.play.animation.PlayDelayFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeInAnimation
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play.extensions.*
import com.tokopedia.play.gesture.PlayClickTouchListener
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.observer.DistinctEventObserver
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayFullscreenManager
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.ChatHeightMapKey
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.ChatListHeightManager
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.PlayChatListHeightManager
import com.tokopedia.play.view.measurement.bounds.provider.videobounds.PlayVideoBoundsProvider
import com.tokopedia.play.view.measurement.bounds.provider.videobounds.VideoBoundsProvider
import com.tokopedia.play.view.measurement.layout.DynamicLayoutManager
import com.tokopedia.play.view.measurement.layout.PlayDynamicLayoutManager
import com.tokopedia.play.view.measurement.scaling.PlayVideoScalingManager
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.viewcomponent.*
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.util.extension.awaitMeasured
import com.tokopedia.play_common.util.extension.changeConstraint
import com.tokopedia.play_common.util.extension.recreateView
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.play_common.viewcomponent.viewComponentOrNull
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayUserInteractionFragment @Inject constructor(
        private val viewModelFactory: ViewModelProvider.Factory,
        private val dispatchers: CoroutineDispatchers,
        private val pipAnalytic: PlayPiPAnalytic,
        private val analytic: PlayAnalytic
) :
        TkpdBaseV4Fragment(),
        PlayMoreActionBottomSheet.Listener,
        PlayFragmentContract,
        ToolbarViewComponent.Listener,
        VideoControlViewComponent.Listener,
        LikeViewComponent.Listener,
        SendChatViewComponent.Listener,
        QuickReplyViewComponent.Listener,
        PinnedViewComponent.Listener,
        VideoSettingsViewComponent.Listener,
        ImmersiveBoxViewComponent.Listener,
        PlayButtonViewComponent.Listener,
        PiPViewComponent.Listener,
        ProductFeaturedViewComponent.Listener,
        PinnedVoucherViewComponent.Listener
{
    private val job = SupervisorJob()
    private val scope = CoroutineScope(dispatchers.main + job)

    private val spaceSize by viewComponent { EmptyViewComponent(it, R.id.space_size) }
    private val gradientBackgroundView by viewComponent { EmptyViewComponent(it, R.id.view_gradient_background) }
    private val toolbarView by viewComponent { ToolbarViewComponent(it, R.id.view_toolbar, this) }
    private val statsInfoView by viewComponent { StatsInfoViewComponent(it, R.id.view_stats_info) }
    private val videoControlView by viewComponent { VideoControlViewComponent(it, R.id.pcv_video, this) }
    private val likeView by viewComponent { LikeViewComponent(it, R.id.view_like, this) }
    private val sendChatView by viewComponentOrNull { SendChatViewComponent(it, R.id.view_send_chat, this) }
    private val quickReplyView by viewComponentOrNull { QuickReplyViewComponent(it, R.id.rv_quick_reply, this) }
    private val chatListView by viewComponentOrNull { ChatListViewComponent(it, R.id.view_chat_list) }
    private val pinnedView by viewComponentOrNull { PinnedViewComponent(it, R.id.view_pinned, this) }
    private val pinnedVoucherView by viewComponentOrNull { PinnedVoucherViewComponent(it, R.id.view_pinned_voucher, this) }
    private val productFeaturedView by viewComponentOrNull { ProductFeaturedViewComponent(it, this) }
    private val videoSettingsView by viewComponent { VideoSettingsViewComponent(it, R.id.view_video_settings, this) }
    private val immersiveBoxView by viewComponent { ImmersiveBoxViewComponent(it, R.id.v_immersive_box, this) }
    private val playButtonView by viewComponent { PlayButtonViewComponent(it, R.id.view_play_button, this) }
    private val endLiveInfoView by viewComponent { EndLiveInfoViewComponent(it, R.id.view_end_live_info) }
    private val pipView by viewComponentOrNull(isEagerInit = true) { PiPViewComponent(it, R.id.view_pip_control, this) }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayInteractionViewModel

    private lateinit var bottomSheet: PlayMoreActionBottomSheet

    private val container: View
        get() = requireView()

    private val productSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_PRODUCT_SHEET_HEIGHT).toInt()

    private val channelId: String
        get() = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

    private val orientationListener: PlayOrientationListener
        get() = requireActivity() as PlayOrientationListener

    private val playNavigation: PlayNavigation
        get() = requireActivity() as PlayNavigation

    private val playFullscreenManager: PlayFullscreenManager
        get() = requireActivity() as PlayFullscreenManager

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    private val screenOrientationDataSource = object : ScreenOrientationDataSource {
        override fun getScreenOrientation(): ScreenOrientation {
            return orientation
        }
    }

    private var isOpened = false
    private var portraitInsets: WindowInsets? = null

    private var videoBoundsProvider: VideoBoundsProvider? = null
    private var dynamicLayoutManager: DynamicLayoutManager? = null
    private var chatListHeightManager: ChatListHeightManager? = null

    private val chatListHeightMap = mutableMapOf<ChatHeightMapKey, Float>()

    private var mMaxTopChatMode: Int? = null
    private var toasterBottomMargin = 0

    private lateinit var onStatsInfoGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    private lateinit var productAnalyticHelper: ProductAnalyticHelper

    /**
     * Animation
     */
    private val fadeInAnimation = PlayFadeInAnimation(FADE_DURATION)
    private val fadeOutAnimation = PlayFadeOutAnimation(FADE_DURATION)
    private val fadeInFadeOutAnimation = PlayFadeInFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY)
    private val delayFadeOutAnimation = PlayDelayFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY)
    private val fadeAnimationList = arrayOf(fadeInAnimation, fadeOutAnimation, fadeInFadeOutAnimation, delayFadeOutAnimation)

    override fun getScreenName(): String = "Play User Interaction"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayInteractionViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_interaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAnalytic()
        setupView(view)
        setupInsets(view)
        setupObserve()

        invalidateSystemUiVisibility()
    }

    override fun onStart() {
        super.onStart()
        spaceSize.rootView.requestApplyInsetsWhenAttached()
    }

    override fun onPause() {
        super.onPause()
        isOpened = false
        productAnalyticHelper.sendImpressedFeaturedProducts()
        analytic.getTrackingQueue().sendAll()
    }

    override fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet) {
        analytic.clickWatchMode()
        triggerImmersive(container.isFullSolid)
        bottomSheet.dismiss()
    }

    override fun onNoAction(bottomSheet: PlayMoreActionBottomSheet) {
        toolbarView.hideActionMore()
    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val lastAction = viewModel.observableLoggedInInteractionEvent.value?.peekContent()
            if (lastAction != null) handleInteractionEvent(lastAction.event)
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        isOpened = true
        invalidateSystemUiVisibility()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreateView()
    }

    override fun onDestroyView() {
        videoBoundsProvider = null
        dynamicLayoutManager = null
        chatListHeightManager = null

        cancelAllAnimations()

        super.onDestroyView()
        job.cancelChildren()
    }

    //region ComponentListener
    /**
     * Toolbar View Component Listener
     */
    override fun onBackButtonClicked(view: ToolbarViewComponent) {
        doLeaveRoom()
    }

    override fun onMoreButtonClicked(view: ToolbarViewComponent) {
        showMoreActionBottomSheet()
    }

    override fun onFollowButtonClicked(view: ToolbarViewComponent, partnerId: Long, action: PartnerFollowAction) {
        doClickFollow(partnerId, action)
    }

    override fun onPartnerNameClicked(view: ToolbarViewComponent, partnerId: Long, type: PartnerType) {
        openPartnerPage(partnerId, type)
    }

    override fun onCartButtonClicked(view: ToolbarViewComponent) {
        shouldOpenCartPage()
    }

    override fun onCopyButtonClicked(view: ToolbarViewComponent, content: String) {
        copyToClipboard(content)
        showLinkCopiedToaster()

        analytic.clickCopyLink()
    }

    /**
     * VideoControl View Component Listener
     */
    override fun onStartSeeking(view: VideoControlViewComponent) {
        onScrubStarted()
    }

    override fun onEndSeeking(view: VideoControlViewComponent) {
        onScrubEnded()
    }

    /**
     * Like View Component Listener
     */
    override fun onLikeClicked(view: LikeViewComponent, shouldLike: Boolean) {
        doClickLike(shouldLike)
    }

    /**
     * SendChat View Component Listener
     */
    override fun onChatFormClicked(view: SendChatViewComponent) {
        doClickChatBox()
    }

    override fun onSendChatClicked(view: SendChatViewComponent, message: String) {
        analytic.clickSendChat()
        doSendChat(message)
    }

    /**
     * QuickReply View Component Listener
     */
    override fun onQuickReplyClicked(view: QuickReplyViewComponent, replyString: String) {
        analytic.clickQuickReply()
        doSendChat(replyString)
    }

    /**
     * QuickReply View Component Listener
     */
    override fun onPinnedMessageActionClicked(view: PinnedViewComponent, applink: String, message: String) {
        analytic.clickPinnedMessage(message, applink)
        openPageByApplink(applink, pipMode = true)
    }

    /**
     * VideoSettings View Component Listener
     */
    override fun onEnterFullscreen(view: VideoSettingsViewComponent) {
        analytic.clickCtaFullScreenFromPortraitToLandscape()
        enterFullscreen()
    }

    override fun onExitFullscreen(view: VideoSettingsViewComponent) {
        exitFullscreen()
    }

    /**
     * ImmersiveBox View Component Listener
     */
    override fun onImmersiveBoxClicked(view: ImmersiveBoxViewComponent, currentAlpha: Float) {
        analytic.clickWatchArea(
                screenOrientation = orientation
        )
        triggerImmersive(currentAlpha == VISIBLE_ALPHA)
    }

    /**
     * PlayButton View Component Listener
     */
    override fun onButtonClicked(view: PlayButtonViewComponent) {
        playViewModel.startCurrentVideo()
    }

    /**
     * PIP View Component Listener
     */
    override fun onPiPButtonClicked(view: PiPViewComponent) {
        playViewModel.requestWatchInPiP()
        pipAnalytic.clickPiPIcon(
                channelId = channelId,
                shopId = playViewModel.partnerId,
                channelType = playViewModel.channelType
        )
    }

    /**
     * Product Featured View Component Listener
     */
    override fun onProductFeaturedImpressed(view: ProductFeaturedViewComponent, products: List<Pair<PlayProductUiModel.Product, Int>>) {
        productAnalyticHelper.trackImpressedProducts(products)
    }

    override fun onProductFeaturedClicked(view: ProductFeaturedViewComponent, product: PlayProductUiModel.Product, position: Int) {
        viewModel.doInteractionEvent(InteractionEvent.OpenProductDetail(product, position))
        analytic.clickFeaturedProduct(product, position)
    }

    override fun onSeeMoreClicked(view: ProductFeaturedViewComponent) {
        openProductSheet()
        analytic.clickFeaturedProductSeeMore()
    }

    /**
     * Pinned Voucher View Component Listener
     */
    override fun onVoucherImpressed(view: PinnedVoucherViewComponent, voucher: MerchantVoucherUiModel, position: Int) {
        analytic.impressionHighlightedVoucher(voucher)
    }

    override fun onVoucherClicked(view: PinnedVoucherViewComponent, voucher: MerchantVoucherUiModel) {
        if (voucher.code.isBlank() || voucher.code.isEmpty()) return

        copyToClipboard(content = voucher.code)
        doShowToaster(message = getString(R.string.play_voucher_code_copied), actionText = getString(R.string.play_action_ok))
        analytic.clickHighlightedVoucher(voucher)
    }
    //endregion

    fun maxTopOnChatMode(maxTopPosition: Int) {
        mMaxTopChatMode = maxTopPosition
        scope.launch(dispatchers.immediate) {
            invalidateChatListBounds(maxTopPosition = maxTopPosition)
        }
    }

    private fun initAnalytic() {
        productAnalyticHelper = ProductAnalyticHelper(analytic)
    }

    private fun setupView(view: View) {

        fun setupLandscapeView() {
            container.setOnTouchListener(PlayClickTouchListener(INTERACTION_TOUCH_CLICK_TOLERANCE))
            container.setOnClickListener {
                if (playViewModel.isFreezeOrBanned) return@setOnClickListener
                if (container.hasAlpha) triggerImmersive(it.isFullSolid)
            }
        }

        fun setupPortraitView() {
            container.setOnClickListener {
                if (playViewModel.isFreezeOrBanned) return@setOnClickListener
                if (!playViewModel.videoOrientation.isHorizontal && container.hasAlpha) triggerImmersive(it.isFullSolid)
            }
        }

        handleVideoHorizontalTopBounds()

        likeView.setEnabled(false)
        videoSettingsView.setFullscreen(orientation.isLandscape)

        if (orientation.isLandscape) setupLandscapeView()
        else setupPortraitView()

        if (playViewModel.isPiPAllowed) pipView?.show()
        else pipView?.hide()
    }

    private fun setupInsets(view: View) {
        /**
         * This is a temporary workaround for when insets not working as intended inside viewpager
         */
        val realBottomMargin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val layoutParams = spaceSize.rootView.layoutParams as ViewGroup.MarginLayoutParams
                val initialBottomMargin = layoutParams.bottomMargin
                val rootInsets = activity?.window?.decorView?.rootWindowInsets
                if (portraitInsets == null && orientation.isPortrait) portraitInsets = rootInsets
                val insets = if (orientation.isPortrait) portraitInsets else rootInsets
                if (insets != null) {
                    layoutParams.updateMargins(top = insets.systemWindowInsetTop, bottom = initialBottomMargin + insets.systemWindowInsetBottom)
                    initialBottomMargin
                } else error("Insets not supported")
            } catch (e: Throwable) { 0 }
        } else 0

        spaceSize.rootView.doOnApplyWindowInsets { v, insets, _, recordedMargin ->
            val skipTop = !isOpened && insets.systemWindowInsetTop == 0
            val skipBottom = !isOpened && insets.systemWindowInsetBottom == 0

            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams

            /**
             * Reduce margin by the first added value
             */
            val margin = recordedMargin.copy(bottom = realBottomMargin)

            var isMarginChanged = false

            val newTopMargin = insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin && !skipTop) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                isMarginChanged = true
            }

            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin && !skipBottom) {
                marginLayoutParams.updateMargins(bottom = newBottomMargin)
                isMarginChanged = true
            }

            val newLeftMargin = insets.systemWindowInsetLeft
            if (marginLayoutParams.leftMargin != newLeftMargin) {
                marginLayoutParams.updateMargins(left = newLeftMargin)
                isMarginChanged = true
            }

            val newRightMargin = insets.systemWindowInsetRight
            if (marginLayoutParams.rightMargin != newRightMargin) {
                marginLayoutParams.updateMargins(right = newRightMargin)
                isMarginChanged = true
            }

            if (isMarginChanged) v.parent.requestLayout()
        }
    }

    private fun setupObserve() {
        observeVideoMeta()
        observeVideoProperty()
        observeChannelInfo()
        observeQuickReply()
        observeToolbarInfo()
        observeLikeStatus()
        observeTotalViews()
        observeNewChat()
        observeChatList()
        observePinned()
        observeCartInfo()
        observeBottomInsetsState()
        observeStatusInfo()
        observeShareInfo()
        observeProductContent()

        observeLoggedInInteractionEvent()
    }

    private fun invalidateSystemUiVisibility() {
        when {
            playViewModel.isFreezeOrBanned -> playFullscreenManager.onExitFullscreen()
            orientation.isLandscape -> playFullscreenManager.onEnterFullscreen()
            !playViewModel.videoOrientation.isHorizontal && container.isFullAlpha -> playFullscreenManager.onEnterFullscreen()
            else -> playFullscreenManager.onExitFullscreen()
        }
    }

    private fun handleVideoHorizontalTopBounds() {
        scope.launch {
            val toolbarMeasure = asyncCatchError(block = {
                toolbarView.rootView.awaitMeasured()
             }, onError = {})
            val statsInfoMeasure = asyncCatchError(block = {
                statsInfoView.rootView.awaitMeasured()
            }, onError = {})

            awaitAll(toolbarMeasure, statsInfoMeasure)
            playFragment.onFirstTopBoundsCalculated()
        }
    }

    //region observe
    /**
     * Observe
     */
    private fun observeVideoMeta() {
        playViewModel.observableVideoMeta.observe(viewLifecycleOwner, Observer { meta ->
            changeLayoutBasedOnVideoOrientation(meta.videoStream.orientation)
            triggerImmersive(false)

            scope.launch(dispatchers.immediate) {
                playFragment.setCurrentVideoTopBounds(meta.videoStream.orientation, getVideoTopBounds(meta.videoStream.orientation))
                if (playViewModel.channelType.isLive) invalidateChatListBounds(videoOrientation = meta.videoStream.orientation, videoPlayer = meta.videoPlayer)
            }

            videoSettingsViewOnStateChanged(videoOrientation = meta.videoStream.orientation)
            gradientBackgroundViewOnStateChanged(videoOrientation = meta.videoStream.orientation)
            pipViewOnStateChanged(videoPlayer = meta.videoPlayer)

            changeLayoutBasedOnVideoType(meta.videoPlayer, playViewModel.channelType)
            if (meta.videoPlayer is PlayVideoPlayerUiModel.General.Complete) videoControlView.setPlayer(meta.videoPlayer.exoPlayer)
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, DistinctObserver {
            if (it.state == PlayViewerVideoState.Waiting ||
                    (it.state is PlayViewerVideoState.Buffer && it.state.bufferSource == BufferSource.Broadcaster)) {
                triggerImmersive(false)
            } else if (it.state == PlayViewerVideoState.Play) {
                analytic.clickPlayVideo()
            } else if (it.state == PlayViewerVideoState.End) showInteractionIfWatchMode()

            playButtonViewOnStateChanged(state = it.state)
        })
    }

    private fun observeChannelInfo() {
        playViewModel.observableChannelInfo.observe(viewLifecycleOwner, DistinctObserver {
            triggerStartMonitoring()
            statsInfoViewOnStateChanged(channelType = it.channelType)
            videoControlViewOnStateChanged(channelType = it.channelType)
            sendChatViewOnStateChanged(channelType = it.channelType)
            chatListViewOnStateChanged(channelType = it.channelType)
        })
    }

    private fun observeQuickReply() {
        playViewModel.observableQuickReply.observe(viewLifecycleOwner, DistinctObserver {
            quickReplyView?.setQuickReply(it)
        })
    }

    private fun observeToolbarInfo() {
        playViewModel.observablePartnerInfo.observe(viewLifecycleOwner, DistinctObserver {
            toolbarView.setPartnerInfo(it)
        })
    }

    private fun observeLikeStatus() {
        playViewModel.observableLikeStatusInfo.observe(viewLifecycleOwner, object : Observer<PlayLikeStatusInfoUiModel> {
            private var isFirstTime = true

            override fun onChanged(it: PlayLikeStatusInfoUiModel) {
                if (isFirstTime) likeView.setEnabled(true)

                likeView.playLikeAnimation(it.isLiked, it.source == LikeSource.UserAction && !isFirstTime)
                isFirstTime = false

                likeView.setTotalLikes(it)
            }
        })
    }

    private fun observeTotalViews() {
        playViewModel.observableTotalViews.observe(viewLifecycleOwner, DistinctObserver {
            statsInfoView.setTotalViews(it)
        })
    }

    private fun observeNewChat() {
        playViewModel.observableNewChat.observe(viewLifecycleOwner, DistinctEventObserver {
            chatListView?.showNewChat(it)
        })
    }

    private fun observeChatList() {
        playViewModel.observableChatList.observe(viewLifecycleOwner, object : Observer<List<PlayChatUiModel>> {
            override fun onChanged(chatList: List<PlayChatUiModel>) {
                playViewModel.observableChatList.removeObserver(this)
                chatListView?.setChatList(chatList)
            }
        })
    }

    private fun observePinned() {
        playViewModel.observablePinned.observe(viewLifecycleOwner, Observer {
            pinnedViewOnStateChanged(pinnedModel = it)
            productFeaturedViewOnStateChanged(pinnedModel = it)
            quickReplyViewOnStateChanged(pinnedModel = it)

        })
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver { map ->
            if (!playViewModel.isFreezeOrBanned) view?.hide()

            if (playViewModel.videoOrientation.isVertical) triggerImmersive(false)

            scope.launch(dispatchers.immediate) {
                if (playViewModel.channelType.isLive && !map.isKeyboardShown) invalidateChatListBounds(bottomInsets = map)
            }

            val keyboardState = map[BottomInsetsType.Keyboard]
                if (keyboardState != null && !keyboardState.isPreviousStateSame) {
                    when (keyboardState) {
                        is BottomInsetsState.Hidden -> if (!map.isAnyShown) playFragment.onBottomInsetsViewHidden()
                        is BottomInsetsState.Shown -> {
                            pushParentPlayByKeyboardHeight(keyboardState.estimatedInsetsHeight)
                        }
                    }
                }

            view?.postDelayed({
                view?.show()
            }, PlayVideoScalingManager.ANIMATION_DURATION)

            gradientBackgroundViewOnStateChanged(bottomInsets = map)
            toolbarViewOnStateChanged(bottomInsets = map)
            statsInfoViewOnStateChanged(bottomInsets = map)
            videoControlViewOnStateChanged(bottomInsets = map)
            likeViewOnStateChanged(bottomInsets = map)
            sendChatViewOnStateChanged(bottomInsets = map)
            quickReplyViewOnStateChanged(bottomInsets = map)
            chatListViewOnStateChanged(bottomInsets = map)
            pinnedViewOnStateChanged(bottomInsets = map)
            productFeaturedViewOnStateChanged(bottomInsets = map)
            videoSettingsViewOnStateChanged(bottomInsets = map)
            immersiveBoxViewOnStateChanged(bottomInsets = map)
            pipViewOnStateChanged(bottomInsets = map)
        })
    }

    private fun observeStatusInfo() {
        playViewModel.observableStatusInfo.observe(viewLifecycleOwner, DistinctObserver {
            getBottomSheetInstance().setState(it.statusType.isFreeze)

            if (it.statusType.isFreeze || it.statusType.isBanned) {
                gradientBackgroundView.hide()
                likeView.hide()
                quickReplyView?.hide()
                chatListView?.hide()
                pinnedView?.hide()
                immersiveBoxView.hide()
                playButtonView.hide()
                toolbarView.setIsShareable(false)

                videoControlViewOnStateChanged(isFreezeOrBanned = true)

                sendChatView?.focusChatForm(false)
                sendChatView?.hide()

                videoSettingsViewOnStateChanged(isFreezeOrBanned = true)
                toolbarViewOnStateChanged(isFreezeOrBanned = true)
                statsInfoViewOnStateChanged(isFreezeOrBanned = true)
                pipViewOnStateChanged(isFreezeOrBanned = true)
                pinnedViewOnStateChanged(isFreezeOrBanned = true)
                productFeaturedViewOnStateChanged(isFreezeOrBanned = true)

                /**
                 * Non view component
                 */
                hideBottomSheet()
                cancelAllAnimations()

                triggerImmersive(false)
            }

            endLiveInfoViewOnStateChanged(event = it)
        })
    }

    private fun observeCartInfo() {
        playViewModel.observableCartInfo.observe(viewLifecycleOwner, DistinctObserver {
            toolbarView.setCartInfo(it)
        })
    }

    private fun observeShareInfo() {
        playViewModel.observableShareInfo.observe(viewLifecycleOwner, DistinctObserver {
            toolbarView.setShareInfo(it)
        })
    }

    private fun observeProductContent() {
        playViewModel.observableProductSheetContent.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PlayResult.Loading -> {
                    if (it.showPlaceholder) {
                        pinnedVoucherView?.showPlaceholder()
                        productFeaturedView?.showPlaceholder()
                    }
                }
                is PlayResult.Failure -> {
                    pinnedVoucherView?.hide()
                    productFeaturedView?.hide()
                }
            }
        })
    }
    //endregion

    private fun triggerImmersive(shouldImmersive: Boolean) {
        cancelAllAnimations()

        fun triggerFullImmersive(shouldImmersive: Boolean, fadeOutAfterFadeIn: Boolean) {
            val animation = when {
                shouldImmersive -> fadeOutAnimation
                fadeOutAfterFadeIn -> fadeInFadeOutAnimation
                else -> fadeInAnimation
            }

            animation.start(container)
        }

        fun isBroadcasterLoading(): Boolean {
            val videoState = playViewModel.viewerVideoState
            return (videoState == PlayViewerVideoState.Waiting ||
                    (videoState is PlayViewerVideoState.Buffer && videoState.bufferSource == BufferSource.Broadcaster))
        }

        when {
            playViewModel.isFreezeOrBanned || isBroadcasterLoading() -> {
                container.alpha = VISIBLE_ALPHA
                playFullscreenManager.onExitFullscreen()
            }
            orientation.isLandscape -> triggerFullImmersive(shouldImmersive, true)
            playViewModel.videoOrientation.isHorizontal -> handleVideoHorizontalImmersive(shouldImmersive)
            playViewModel.videoOrientation.isVertical -> {
                if (shouldImmersive) playFullscreenManager.onEnterFullscreen()
                else playFullscreenManager.onExitFullscreen()
                triggerFullImmersive(shouldImmersive, false)
            }
        }
    }

    private fun triggerStartMonitoring() {
        playFragment.startRenderMonitoring()

        if (!this::onStatsInfoGlobalLayoutListener.isInitialized) {
            onStatsInfoGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    playFragment.stopRenderMonitoring()
                    statsInfoView.rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
            statsInfoView.rootView.viewTreeObserver.addOnGlobalLayoutListener(onStatsInfoGlobalLayoutListener)
        }
    }

    private fun onScrubStarted() {
        if (!orientation.isLandscape) return

        cancelAllAnimations()
        fadeInAnimation.start(container)
    }

    private fun onScrubEnded() {
        if (!orientation.isLandscape) return

        cancelAllAnimations()
        delayFadeOutAnimation.start(container)
    }

    private fun enterFullscreen() {
        orientationListener.onOrientationChanged(ScreenOrientation.Landscape, isTilting = false)
    }

    private fun exitFullscreen() {
        orientationListener.onOrientationChanged(ScreenOrientation.Portrait, isTilting = false)
    }

    private fun doLeaveRoom() {
        playNavigation.onBackPressed(isSystemBack = false)
    }

    private fun doActionFollowPartner(partnerId: Long, action: PartnerFollowAction) {
        analytic.clickFollowShop(partnerId.toString(), action.value)
        viewModel.doFollow(partnerId, action)

        toolbarView.setFollowStatus(action == PartnerFollowAction.Follow)
    }

    //TODO("This action is duplicated with the one in PlayBottomSheetFragment, find a way to prevent duplication")
    private fun doOpenProductDetail(product: PlayProductUiModel.Product, position: Int) {
        if (product.applink != null && product.applink.isNotEmpty()) {
            analytic.clickProduct(product, position)
            openPageByApplink(product.applink, pipMode = true)
        }
    }

    private fun handleVideoHorizontalImmersive(shouldImmersive: Boolean) {
        if (shouldImmersive) {
            videoSettingsView.fadeOut()
            immersiveBoxView.fadeOut()
        } else {
            videoSettingsView.fadeIn()
            immersiveBoxView.fadeIn()
        }
    }

    private fun showMoreActionBottomSheet() {
        getBottomSheetInstance().show(childFragmentManager)
    }

    private fun openPartnerPage(partnerId: Long, partnerType: PartnerType) {
        if (partnerType == PartnerType.Shop) openShopPage(partnerId)
        else if (partnerType == PartnerType.Buyer) openProfilePage(partnerId)
    }

    private fun openShopPage(partnerId: Long) {
        analytic.clickShop(partnerId.toString())
        openPageByApplink(ApplinkConst.SHOP, partnerId.toString(), pipMode = true)
    }

    private fun openProfilePage(partnerId: Long) {
        openPageByApplink(ApplinkConst.PROFILE, partnerId.toString())
    }

    private fun doClickChatBox() {
        viewModel.doInteractionEvent(InteractionEvent.SendChat)
    }

    private fun doClickLike(shouldLike: Boolean) {
        viewModel.doInteractionEvent(InteractionEvent.Like(shouldLike))
    }

    private fun doClickFollow(partnerId: Long, followAction: PartnerFollowAction) {
        viewModel.doInteractionEvent(InteractionEvent.Follow(partnerId, followAction))
    }

    private fun shouldOpenCartPage() {
        analytic.clickCartIcon()
        viewModel.doInteractionEvent(InteractionEvent.CartPage)
    }

    private fun doSendChat(message: String) {
        playViewModel.sendChat(message)
    }

    private fun handleLoginInteractionEvent(loginInteractionEvent: LoginStateEvent) {
        when (loginInteractionEvent) {
            is LoginStateEvent.InteractionAllowed -> handleInteractionEvent(loginInteractionEvent.event)
            is LoginStateEvent.NeedLoggedIn -> openLoginPage()
        }
    }

    private fun handleInteractionEvent(event: InteractionEvent) {
        when (event) {
            InteractionEvent.CartPage -> openPageByApplink(ApplinkConst.CART)
            InteractionEvent.SendChat -> shouldComposeChat()
            is InteractionEvent.OpenProductDetail -> doOpenProductDetail(event.product, event.position)
            is InteractionEvent.Like -> doLikeUnlike(event.shouldLike)
            is InteractionEvent.Follow -> doActionFollowPartner(event.partnerId, event.partnerAction)
        }
    }

    private fun shouldComposeChat() {
        sendChatView?.focusChatForm(shouldFocus = true, forceChangeKeyboardState = true)
    }

    private fun doLikeUnlike(shouldLike: Boolean) {
        //Used to show mock like when user click like
        playViewModel.changeLikeCount(shouldLike)

        viewModel.doLikeUnlike(
                likeParamInfo = playViewModel.likeParamInfo,
                shouldLike = shouldLike
        )

        analytic.clickLike(shouldLike)
    }

    private fun openLoginPage() {
        openPageByApplink(ApplinkConst.LOGIN, requestCode = REQUEST_CODE_LOGIN)
    }

    private fun openPageByApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false, pipMode: Boolean = false) {
        if (pipMode && !playViewModel.isFreezeOrBanned) {
            playViewModel.requestPiPBrowsingPage(
                    OpenApplinkUiModel(applink = applink, params = params.toList(), requestCode, shouldFinish)
            )
        } else {
            openApplink(applink, *params, requestCode = requestCode, shouldFinish = shouldFinish)
        }
    }

    private fun openApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false) {
        if (requestCode == null) {
            RouteManager.route(context, applink, *params)
        } else {
            val intent = RouteManager.getIntent(context, applink, *params)
            startActivityForResult(intent, requestCode)
        }
        activity?.overridePendingTransition(R.anim.anim_play_enter_page, R.anim.anim_play_exit_page)

        if (shouldFinish) activity?.finish()
    }

    private fun getBottomSheetInstance() : PlayMoreActionBottomSheet {
        if (!::bottomSheet.isInitialized) {
            bottomSheet = PlayMoreActionBottomSheet.newInstance(requireContext(), this)
        }
        return bottomSheet
    }

    private fun hideBottomSheet() {
        val bottomSheet = getBottomSheetInstance()
        if (bottomSheet.isVisible) bottomSheet.dismiss()
    }

    private fun showInteractionIfWatchMode() {
        view?.performClick()
    }

    private fun openProductSheet() {
        playViewModel.onShowProductSheet(productSheetMaxHeight)
    }

    private fun pushParentPlayByKeyboardHeight(estimatedKeyboardHeight: Int) {
        val hasQuickReply = !playViewModel.observableQuickReply.value?.quickReplyList.isNullOrEmpty()

        scope.launch(dispatchers.immediate) {
            playFragment.onBottomInsetsViewShown(getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight, hasQuickReply))
        }
    }

    private fun cancelAllAnimations() {
        fadeAnimationList.forEach { it.cancel() }
    }

    private suspend fun getVideoTopBounds(videoOrientation: VideoOrientation): Int {
        return getVideoBoundsProvider().getVideoTopBounds(videoOrientation)
    }

    private suspend fun getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int {
        return try {
            getVideoBoundsProvider().getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight, hasQuickReply)
        } catch (e: Throwable) { getScreenHeight() }
    }

    private suspend fun invalidateChatListBounds(
            videoOrientation: VideoOrientation = playViewModel.videoOrientation,
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            maxTopPosition: Int = mMaxTopChatMode ?: 0
    ) {
        val hasQuickReply = !playViewModel.observableQuickReply.value?.quickReplyList.isNullOrEmpty()

        if (bottomInsets.isKeyboardShown) getChatListHeightManager().invalidateHeightChatMode(videoOrientation, videoPlayer, maxTopPosition, hasQuickReply)
        else getChatListHeightManager().invalidateHeightNonChatMode(videoOrientation, videoPlayer)
    }

    private fun changeLayoutBasedOnVideoOrientation(videoOrientation: VideoOrientation) {
        getDynamicLayoutManager().onVideoOrientationChanged(videoOrientation)
    }

    private fun changeLayoutBasedOnVideoType(videoPlayer: PlayVideoPlayerUiModel, channelType: PlayChannelType) {
        getDynamicLayoutManager().onVideoPlayerChanged(videoPlayer, channelType)
    }

    private fun getDynamicLayoutManager(): DynamicLayoutManager = synchronized(this) {
        if (dynamicLayoutManager == null) {
            dynamicLayoutManager = PlayDynamicLayoutManager(container as ViewGroup, screenOrientationDataSource)
        }
        return dynamicLayoutManager!!
    }

    private fun getVideoBoundsProvider(): VideoBoundsProvider = synchronized(this) {
        if (videoBoundsProvider == null) {
            videoBoundsProvider = PlayVideoBoundsProvider(container as ViewGroup, screenOrientationDataSource)
        }
        return videoBoundsProvider!!
    }

    private fun getChatListHeightManager(): ChatListHeightManager = synchronized(this) {
        if (chatListHeightManager == null) {
            chatListHeightManager = PlayChatListHeightManager(requireView() as ViewGroup, screenOrientationDataSource, chatListHeightMap)
        }
        return chatListHeightManager!!
    }

    private fun copyToClipboard(content: String) {
        (requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(ClipData.newPlainText("play-room", content))
    }

    private fun showLinkCopiedToaster() {
        doShowToaster(message = getString(R.string.play_link_copied))
    }

    private fun doShowToaster(
            toasterType: Int = Toaster.TYPE_NORMAL,
            actionText: String = "",
            message: String,
    ) {
        if (toasterBottomMargin == 0) {
            val likeAreaBottomMargin = (likeView.clickAreaView.layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin?:0
            toasterBottomMargin = likeView.clickAreaView.height + likeAreaBottomMargin
        }
        Toaster.toasterCustomBottomHeight = toasterBottomMargin
        Toaster.build(
                container,
                message,
                type = toasterType,
                actionText = actionText
        ).show()
    }

    private fun doAutoSwipe() {
        scope.launch {
            delay(AUTO_SWIPE_DELAY)
            playNavigation.navigateToNextPage()
        }
    }

    //region OnStateChanged
    private fun playButtonViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            state: PlayViewerVideoState
    ) {
        if (!channelType.isVod) {
            playButtonView.hide()
            return
        }
        when (state) {
            PlayViewerVideoState.Pause -> playButtonView.showPlayButton()
            PlayViewerVideoState.End -> {
                if (playViewModel.bottomInsets.isAnyShown || !playNavigation.canNavigateNextPage()) playButtonView.showRepeatButton()
                else doAutoSwipe()
            }
            else -> playButtonView.hide()
        }
    }

    private fun videoControlViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (isFreezeOrBanned) {
            videoControlView.setPlayer(null)
            videoControlView.hide()
        }
        else if (channelType.isVod && videoPlayer.isGeneral && !bottomInsets.isAnyShown) videoControlView.show()
        else videoControlView.hide()
    }

    private fun chatListViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
    ) {
        if (channelType.isLive && !bottomInsets.isAnyBottomSheetsShown) chatListView?.show() else chatListView?.hide()
    }

    private fun videoSettingsViewOnStateChanged(
            videoOrientation: VideoOrientation = playViewModel.videoOrientation,
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (isFreezeOrBanned) videoSettingsView.hide()
        else if (videoOrientation.isHorizontal && videoPlayer.isGeneral && !bottomInsets.isAnyShown) videoSettingsView.show()
        else videoSettingsView.hide()
    }

    private fun pinnedViewOnStateChanged(
            pinnedModel: PlayPinnedUiModel? = playViewModel.observablePinned.value,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned,
    ) {
        if (isFreezeOrBanned) {
            pinnedView?.hide()
            return
        }

        when (pinnedModel) {
            is PlayPinnedUiModel.PinnedMessage -> {
                pinnedView?.setPinnedMessage(pinnedModel)

                if (!bottomInsets.isAnyShown) pinnedView?.show()
                else pinnedView?.hide()
            }
            is PlayPinnedUiModel.PinnedProduct -> {
                pinnedView?.hide()
            }
            PlayPinnedUiModel.NoPinned -> {
                pinnedView?.hide()
            }
        }
    }

    private fun productFeaturedViewOnStateChanged(
            pinnedModel: PlayPinnedUiModel? = playViewModel.observablePinned.value,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned,
    ) {
        if (isFreezeOrBanned) {
            pinnedVoucherView?.hide()
            productFeaturedView?.hide()
            return
        }

        when (pinnedModel) {
            is PlayPinnedUiModel.PinnedProduct -> {
                if (pinnedModel.productTags is PlayProductTagsUiModel.Complete) {
                    pinnedVoucherView?.setVoucher(pinnedModel.productTags.voucherList)
                    productFeaturedView?.setFeaturedProducts(pinnedModel.productTags.productList, pinnedModel.productTags.basicInfo.maxFeaturedProducts)
                }

                if (!bottomInsets.isAnyShown) {
                    pinnedVoucherView?.showIfNotEmpty()
                    productFeaturedView?.showIfNotEmpty()
                } else {
                    pinnedVoucherView?.hide()
                    productFeaturedView?.hide()
                }
            } else -> {
                pinnedVoucherView?.hide()
                productFeaturedView?.hide()
            }
        }
    }

    private fun gradientBackgroundViewOnStateChanged(
            videoOrientation: VideoOrientation = playViewModel.videoOrientation,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (bottomInsets.isAnyShown ||
                (videoOrientation.isHorizontal && orientation.isPortrait)
        ) gradientBackgroundView.hide()
        else gradientBackgroundView.show()
    }

    private fun toolbarViewOnStateChanged(
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (isFreezeOrBanned) toolbarView.show()
        else if (!bottomInsets.isAnyShown && orientation.isPortrait) toolbarView.show()
        else toolbarView.hide()
    }

    private fun statsInfoViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        statsInfoView.setLiveBadgeVisibility(channelType.isLive)

        if (isFreezeOrBanned) statsInfoView.hide()
        else if (!bottomInsets.isAnyShown && orientation.isPortrait) statsInfoView.show()
        else statsInfoView.hide()
    }

    private fun likeViewOnStateChanged(
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (bottomInsets.isAnyShown) likeView.hide() else likeView.show()
    }

    private fun sendChatViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (channelType.isLive &&
                bottomInsets[BottomInsetsType.ProductSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.VariantSheet]?.isShown == false) {
            sendChatView?.show()
        } else sendChatView?.hide()

        sendChatView?.focusChatForm(channelType.isLive && bottomInsets[BottomInsetsType.Keyboard] is BottomInsetsState.Shown)
    }

    private fun quickReplyViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            pinnedModel: PlayPinnedUiModel? = playViewModel.observablePinned.value
    ) {
        if (channelType.isLive &&
                bottomInsets[BottomInsetsType.ProductSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.VariantSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.Keyboard]?.isShown == true) {
            quickReplyView?.showIfNotEmpty()
        } else quickReplyView?.hide()

        val quickReplyViewId = quickReplyView?.id ?: return
        when (pinnedModel) {
            is PlayPinnedUiModel.PinnedProduct -> {
                val pinnedVoucherViewId = pinnedVoucherView?.id
                if (pinnedVoucherViewId != null) {
                    view?.changeConstraint {
                        connect(quickReplyViewId, ConstraintSet.BOTTOM, R.id.view_topmost_like, ConstraintSet.TOP)
                    }
                }
            }
            is PlayPinnedUiModel.PinnedMessage -> {
                val sendChatViewId = sendChatView?.id
                if (sendChatViewId != null) {
                    view?.changeConstraint {
                        connect(quickReplyViewId, ConstraintSet.BOTTOM, sendChatViewId, ConstraintSet.TOP)
                    }
                }
            }
            else -> {}
        }
    }

    private fun immersiveBoxViewOnStateChanged(
            bottomInsets: Map<BottomInsetsType, BottomInsetsState>
    ) {
        if (bottomInsets.isAnyShown) immersiveBoxView.hide() else immersiveBoxView.show()
    }

    private fun endLiveInfoViewOnStateChanged(
            event: PlayStatusInfoUiModel
    ) {
        if(event.statusType.isFreeze) {
            endLiveInfoView.setInfo(title = event.freezeModel.title)
            endLiveInfoView.show()
        } else endLiveInfoView.hide()
    }

    private fun pipViewOnStateChanged(
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (!playViewModel.isPiPAllowed || !videoPlayer.isGeneral || isFreezeOrBanned) {
            pipView?.hide()
            return
        }

        if (!bottomInsets.isAnyShown) pipView?.show()
        else pipView?.hide()
    }
    //endregion

    companion object {
        private const val INTERACTION_TOUCH_CLICK_TOLERANCE = 25

        private const val REQUEST_CODE_LOGIN = 192

        private const val PERCENT_PRODUCT_SHEET_HEIGHT = 0.6

        private const val VISIBLE_ALPHA = 1f

        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L

        private const val AUTO_SWIPE_DELAY = 500L
    }
}