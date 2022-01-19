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
import androidx.lifecycle.lifecycleScope
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.coroutines.asyncCatchError
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.CastAnalyticHelper
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.analytic.PlayPiPAnalytic
import com.tokopedia.play.analytic.ProductAnalyticHelper
import com.tokopedia.play.animation.PlayDelayFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeInAnimation
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play.extensions.*
import com.tokopedia.play.gesture.PlayClickTouchListener
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.util.measureWithTimeout
import com.tokopedia.play.util.observer.DistinctEventObserver
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.util.video.state.BufferSource
import com.tokopedia.play.util.video.state.PlayViewerVideoState
import com.tokopedia.play.util.withCache
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayFullscreenManager
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.custom.dialog.InteractiveWinningDialogFragment
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.ChatHeightMapKey
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.ChatHeightMapValue
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.ChatListHeightManager
import com.tokopedia.play.view.measurement.bounds.manager.chatlistheight.PlayChatListHeightManager
import com.tokopedia.play.view.measurement.bounds.provider.videobounds.PlayVideoBoundsProvider
import com.tokopedia.play.view.measurement.bounds.provider.videobounds.VideoBoundsProvider
import com.tokopedia.play.view.measurement.layout.DynamicLayoutManager
import com.tokopedia.play.view.measurement.layout.PlayDynamicLayoutManager
import com.tokopedia.play.view.storage.multiplelikes.MultipleLikesIconCacheStorage
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.uimodel.MerchantVoucherUiModel
import com.tokopedia.play.view.uimodel.OpenApplinkUiModel
import com.tokopedia.play.view.uimodel.PlayProductUiModel
import com.tokopedia.play.view.uimodel.action.*
import com.tokopedia.play.view.uimodel.event.*
import com.tokopedia.play.view.uimodel.recom.*
import com.tokopedia.play.view.uimodel.state.*
import com.tokopedia.play.view.viewcomponent.*
import com.tokopedia.play.view.viewcomponent.interactive.*
import com.tokopedia.play.view.viewcomponent.partnerinfo.PartnerInfoViewComponent
import com.tokopedia.play.view.viewcomponent.realtimenotif.RealTimeNotificationViewComponent
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play.view.wrapper.PlayResult
import com.tokopedia.play_common.R as commonR
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.util.event.EventObserver
import com.tokopedia.play_common.util.extension.*
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.play_common.viewcomponent.viewComponentOrNull
import com.tokopedia.unifycomponents.Toaster
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayUserInteractionFragment @Inject constructor(
        private val viewModelFactory: ViewModelProvider.Factory,
        private val dispatchers: CoroutineDispatchers,
        private val pipAnalytic: PlayPiPAnalytic,
        private val analytic: PlayAnalytic,
        private val multipleLikesIconCacheStorage: MultipleLikesIconCacheStorage,
        private val castAnalyticHelper: CastAnalyticHelper
) :
        TkpdBaseV4Fragment(),
        PlayMoreActionBottomSheet.Listener,
        PlayFragmentContract,
        ToolbarRoomViewComponent.Listener,
        PartnerInfoViewComponent.Listener,
        VideoControlViewComponent.Listener,
        LikeViewComponent.Listener,
        ShareLinkViewComponent.Listener,
        SendChatViewComponent.Listener,
        QuickReplyViewComponent.Listener,
        PinnedViewComponent.Listener,
        VideoSettingsViewComponent.Listener,
        ImmersiveBoxViewComponent.Listener,
        PlayButtonViewComponent.Listener,
        PiPViewComponent.Listener,
        ProductFeaturedViewComponent.Listener,
        PinnedVoucherViewComponent.Listener,
        InteractiveViewComponent.Listener,
        InteractiveWinnerBadgeViewComponent.Listener,
        RealTimeNotificationViewComponent.Listener,
        CastViewComponent.Listener,
        ProductSeeMoreViewComponent.Listener,
        KebabMenuViewComponent.Listener
{
    private val viewSize by viewComponent { EmptyViewComponent(it, R.id.view_size) }
    private val gradientBackgroundView by viewComponent { EmptyViewComponent(it, R.id.view_gradient_background) }
    private val toolbarView by viewComponent { ToolbarRoomViewComponent(it, R.id.view_toolbar_room, this) }
    private val partnerInfoView by viewComponentOrNull { PartnerInfoViewComponent(it, this) }
    private val statsInfoView by viewComponent { StatsInfoViewComponent(it, R.id.view_stats_info) }
    private val videoControlView by viewComponent { VideoControlViewComponent(it, R.id.pcv_video, this) }
    private val likeView by viewComponent { LikeViewComponent(it, this) }
    private val likeCountView by viewComponent { LikeCountViewComponent(it) }
    private val shareLinkView by viewComponentOrNull { ShareLinkViewComponent(it, R.id.view_share_link, this) }
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
    private val castView by viewComponentOrNull(isEagerInit = true) { CastViewComponent(it, this) }
    private val topmostLikeView by viewComponentOrNull(isEagerInit = true) { EmptyViewComponent(it, R.id.view_topmost_like) }
    private val rtnView by viewComponentOrNull { RealTimeNotificationViewComponent(it, this) }
    private val likeBubbleView by viewComponent { LikeBubbleViewComponent(
        it, R.id.view_like_bubble, viewLifecycleOwner.lifecycleScope, multipleLikesIconCacheStorage) }
    private val productSeeMoreView by viewComponentOrNull(isEagerInit = true) { ProductSeeMoreViewComponent(it, R.id.view_product_see_more, this) }
    private val kebabMenuView by viewComponentOrNull(isEagerInit = true) { KebabMenuViewComponent(it, R.id.view_kebab_menu, this) }

    /**
     * Interactive
     */
    private val interactiveView by viewComponentOrNull { InteractiveViewComponent(it, this) }
    private val interactiveWinnerBadgeView by viewComponentOrNull(isEagerInit = true) { InteractiveWinnerBadgeViewComponent(it, this) }

    private val offset8 by lazy { requireContext().resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3) }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayInteractionViewModel

    private lateinit var bottomSheet: PlayMoreActionBottomSheet

    private val container: View
        get() = requireView()

    private val bottomSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_BOTTOMSHEET_HEIGHT).toInt()

    private val bottomSheetMenuMaxHeight: Int
        get() = (requireView().height * PERCENT_MENU_BOTTOMSHEET_HEIGHT).toInt()

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

    private val chatListHeightMap = mutableMapOf<ChatHeightMapKey, ChatHeightMapValue>()

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
        viewSize.rootView.requestApplyInsetsWhenAttached()
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

    }

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val lastAction = viewModel.observableLoggedInInteractionEvent.value?.peekContent()
            if (lastAction != null) handleInteractionEvent(lastAction.event)
        } else {
            playViewModel.submitAction(
                    OpenPageResultAction(isSuccess = resultCode == Activity.RESULT_OK, requestCode = requestCode)
            )
            super.onActivityResult(requestCode, resultCode, data)
        }
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
    }

    //region ComponentListener
    /**
     * Toolbar View Component Listener
     */
    override fun onBackButtonClicked(view: ToolbarRoomViewComponent) {
        doLeaveRoom()
    }

    override fun onShareIconClick(view: ShareLinkViewComponent) {
        playViewModel.submitAction(ClickShareAction)

        analytic.clickCopyLink()
    }

    override fun onPartnerNameClicked(view: PartnerInfoViewComponent) {
        playViewModel.submitAction(ClickPartnerNameAction)
    }

    override fun onFollowButtonClicked(view: PartnerInfoViewComponent) {
        playViewModel.submitAction(ClickFollowAction)
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
    override fun onLikeClicked(view: LikeViewComponent) {
        playViewModel.submitAction(ClickLikeAction)
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
     * Pinned View Component Listener
     */
    override fun onPinnedMessageActionClicked(view: PinnedViewComponent, appLink: String, message: String) {
        analytic.clickPinnedMessage(message, appLink)
        openPageByApplink(appLink, pipMode = true)
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

    /**
     * Interactive View Component Listener
     */
    override fun onFollowButtonClicked(view: InteractiveViewComponent) {
        playViewModel.submitAction(ClickFollowInteractiveAction)
    }

    override fun onTapTapClicked(view: InteractiveViewComponent) {
        playViewModel.submitAction(InteractiveTapTapAction)
    }

    override fun onRetryButtonClicked(view: InteractiveViewComponent) {
        playViewModel.submitAction(ClickRetryInteractiveAction)
    }

    override fun onTapAnimationLoaded(view: InteractiveViewComponent) {
        /**
         * Connect to different anchor because Lottie increase the height of interactive view in a significant way
         * and because of that, the distance between interactive view and winner badge increase significantly
         */
        val winnerBadgeView = interactiveWinnerBadgeView?.rootView
        if (winnerBadgeView != null) {
            this.view?.changeConstraint {
                connect(winnerBadgeView.id, ConstraintSet.BOTTOM, R.id.v_winner_badge_bottom, ConstraintSet.TOP)
            }
        }
    }

    /**
     * InteractiveWinnerBadge View Component Listener
     */
    override fun onBadgeClicked(view: InteractiveWinnerBadgeViewComponent) {
        playViewModel.submitAction(InteractiveWinnerBadgeClickedAction(bottomSheetMaxHeight))
    }

    /**
     * RealTimeNotification View Component Listener
     */
    override fun onShowNotification(view: RealTimeNotificationViewComponent, height: Float) {
        chatListView?.setMask(height + offset8, true)
    }

    override fun onHideNotification(view: RealTimeNotificationViewComponent) {
        chatListView?.setMask(MASK_NO_CUT_HEIGHT, true)
    }

    /**
     * Cast View Component Listener
     */
    override fun onCastClicked() {
        analytic.clickCast()
    }

    /**
     * Product See More Listener
     */
    override fun onProductSeeMoreClick(view: ProductSeeMoreViewComponent) {
        openProductSheet()
        analytic.clickFeaturedProductSeeMore()
    }

    //endregion

    fun maxTopOnChatMode(maxTopPosition: Int) {
        mMaxTopChatMode = maxTopPosition
        if (!playViewModel.bottomInsets.isKeyboardShown) return
        viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) {
             invalidateChatListBounds(maxTopPosition = maxTopPosition)
        }
    }

    fun onStartAnimateInsets(isHidingInsets: Boolean) {
        view?.hide()
    }

    //TODO("Find better logic to improve this code")
    fun onFinishAnimateInsets(isHidingInsets: Boolean) {
        /**
         * The first one is to handle fast changes when insets transition from show to hide
         */
        if (isHidingInsets) viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) { invalidateChatListBounds() }
        view?.show()
        /**
         * The second one is to handle edge cases when somehow any interaction has changed while insets is shown
         */
        if (isHidingInsets) viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) { invalidateChatListBounds() }

        if (isHidingInsets && rtnView?.isAnimating() == true && rtnView?.isAnimatingHide() != true) {
            val height = rtnView?.getRtnHeight() ?: return
            chatListView?.setMask(height.toFloat() + offset8, false)
        } else {
            chatListView?.setMask(MASK_NO_CUT_HEIGHT, false)
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

        videoSettingsView.setFullscreen(orientation.isLandscape)

        if (orientation.isLandscape) setupLandscapeView()
        else setupPortraitView()

        if (playViewModel.isPiPAllowed) pipView?.show()
        else pipView?.hide()

        setupFeaturedProductsFadingEdge(view)
    }

    private fun setupInsets(view: View) {
        /**
         * This is a temporary workaround for when insets not working as intended inside viewpager
         */
        val realBottomMargin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                val layoutParams = viewSize.rootView.layoutParams as ViewGroup.MarginLayoutParams
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

        viewSize.rootView.doOnApplyWindowInsets { v, insets, _, recordedMargin ->
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
        observeNewChat()
        observeChatList()
        observePinnedMessage()
        observePinnedProduct()
        observeBottomInsetsState()
        observeStatusInfo()
        observeProductContent()

        observeUiState()
        observeUiEvent()

        observeLoggedInInteractionEvent()
        observeCastState()
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
        viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) {
            val toolbarMeasure = asyncCatchError(block = {
                measureWithTimeout { toolbarView.rootView.awaitMeasured() }
             }, onError = {})
            val statsInfoMeasure = asyncCatchError(block = {
                measureWithTimeout { statsInfoView.rootView.awaitMeasured() }
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
        playViewModel.observableVideoMeta.observe(viewLifecycleOwner) { meta ->
            changeLayoutBasedOnVideoOrientation(meta.videoStream.orientation)
            triggerImmersive(false)

            viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) {
                playFragment.setCurrentVideoTopBounds(meta.videoStream.orientation, getVideoTopBounds(meta.videoStream.orientation))
                if (playViewModel.channelType.isLive) invalidateChatListBounds(videoOrientation = meta.videoStream.orientation, videoPlayer = meta.videoPlayer)
            }

            videoSettingsViewOnStateChanged(videoOrientation = meta.videoStream.orientation)
            gradientBackgroundViewOnStateChanged(videoOrientation = meta.videoStream.orientation)
            pipViewOnStateChanged(videoPlayer = meta.videoPlayer)
            playButtonViewOnStateChanged(videoPlayer = meta.videoPlayer)

            pinnedViewOnStateChanged()
            quickReplyViewOnStateChanged()

            changeLayoutBasedOnVideoType(meta.videoPlayer, playViewModel.channelType)
            if (meta.videoPlayer is PlayVideoPlayerUiModel.General.Complete) videoControlView.setPlayer(meta.videoPlayer.exoPlayer)
        }
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
            quickReplyViewOnStateChanged(channelType = it.channelType)

            pinnedViewOnStateChanged()
        })
    }

    private fun observeQuickReply() {
        playViewModel.observableQuickReply.observe(viewLifecycleOwner, DistinctObserver {
            quickReplyView?.setQuickReply(it)
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

    private fun observePinnedMessage() {
        playViewModel.observablePinnedMessage.observe(viewLifecycleOwner, DistinctObserver {
            pinnedViewOnStateChanged(pinnedModel = it, shouldTriggerChatHeightCalculation = true)

            /**
             * To trigger bottom bounds for product featured
             */
            quickReplyViewOnStateChanged()
        })
    }

    private fun observePinnedProduct() {
        playViewModel.observablePinnedProduct.observe(viewLifecycleOwner, DistinctObserver {
            productFeaturedViewOnStateChanged(pinnedModel = it, shouldTriggerChatHeightCalculation = true)
        })
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver { map ->
            if (playViewModel.videoOrientation.isVertical) triggerImmersive(false)

            val keyboardState = map[BottomInsetsType.Keyboard]
                if (keyboardState != null && !keyboardState.isPreviousStateSame) {
                    when (keyboardState) {
                        is BottomInsetsState.Shown -> {
                            pushParentPlayByKeyboardHeight(keyboardState.estimatedInsetsHeight)
                        }
                    }
                }

            if (map.isKeyboardShown) dismissToaster()

            gradientBackgroundViewOnStateChanged(bottomInsets = map)
            toolbarViewOnStateChanged(bottomInsets = map)
            partnerInfoViewOnStateChanged(bottomInsets = map)
            statsInfoViewOnStateChanged(bottomInsets = map)
            videoControlViewOnStateChanged(bottomInsets = map)
            sendChatViewOnStateChanged(bottomInsets = map)
            quickReplyViewOnStateChanged(bottomInsets = map)
            chatListViewOnStateChanged(bottomInsets = map)
            pinnedViewOnStateChanged(bottomInsets = map)
            productFeaturedViewOnStateChanged(bottomInsets = map)
            videoSettingsViewOnStateChanged(bottomInsets = map)
            immersiveBoxViewOnStateChanged(bottomInsets = map)
            pipViewOnStateChanged(bottomInsets = map)
            castViewOnStateChanged(bottomInsets = map)
        })
    }

    private fun observeStatusInfo() {
        playViewModel.observableStatusInfo.observe(viewLifecycleOwner, DistinctObserver {
            getBottomSheetInstance().setState(it.statusType.isFreeze)

            if (it.statusType.isFreeze || it.statusType.isBanned) {
                gradientBackgroundView.hide()
                likeCountView.hide()
                likeView.hide()
                quickReplyView?.hide()
                chatListView?.hide()
                pinnedView?.hide()
                immersiveBoxView.hide()
                playButtonView.hide()
                shareLinkView?.hide()

                videoControlViewOnStateChanged(isFreezeOrBanned = true)

                sendChatView?.focusChatForm(false)
                sendChatView?.invisible()

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

    private fun observeUiState() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiState.withCache().collectLatest { cachedState ->
                val state = cachedState.value
                val prevState = cachedState.prevValue

                renderInteractiveView(prevState?.interactiveView, state.interactiveView, state.partner)
                renderWinnerBadgeView(state.winnerBadge)
                renderToolbarView(state.title, state.share)
                renderPartnerInfoView(prevState?.partner, state.partner)
                renderLikeView(prevState?.like, state.like)
                renderLikeBubbleView(state.like)
                renderStatsInfoView(state.totalView)
                renderRealTimeNotificationView(state.rtn)
                renderViewAllProductView(state.viewAllProduct)
                renderKebabMenuView(state.kebabMenu)
            }
        }
    }

    private fun observeUiEvent() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            playViewModel.uiEvent.collect { event ->
                when (event) {
                    is ShowWinningDialogEvent -> {
                        if (container.alpha != VISIBLE_ALPHA) return@collect
                        getInteractiveWinningDialog().apply {
                            setData(imageUrl = event.userImageUrl, title = event.dialogTitle, subtitle = event.dialogSubtitle)
                        }.show(childFragmentManager)
                    }
                    is ShowCoachMarkWinnerEvent -> {
                        if (interactiveWinnerBadgeView?.isHidden() == true || container.alpha != VISIBLE_ALPHA) return@collect
                        interactiveWinnerBadgeView?.showCoachMark(event.title, event.subtitle)
                    }
                    HideCoachMarkWinnerEvent -> {
                        interactiveWinnerBadgeView?.hideCoachMark()
                    }
                    is OpenPageEvent -> {
                        openPageByApplink(
                            applink = event.applink,
                            params = event.params.toTypedArray(),
                            requestCode = event.requestCode,
                            pipMode = event.pipMode
                        )
                    }
                    is ShowInfoEvent -> {
                        doShowToaster(
                            toasterType = Toaster.TYPE_NORMAL,
                            message = getTextFromUiString(event.message)
                        )
                    }
                    is ShowErrorEvent -> {
                        val errMessage = if (event.errMessage == null) {
                            ErrorHandler.getErrorMessage(
                                context, event.error, ErrorHandler.Builder()
                                    .className(PlayViewModel::class.java.simpleName)
                                    .build()
                            )
                        } else {
                            val (_, errCode) = ErrorHandler.getErrorMessagePair(
                                context, event.error, ErrorHandler.Builder()
                                    .className(PlayViewModel::class.java.simpleName)
                                    .build()
                            )
                            getString(
                                commonR.string.play_custom_error_handler_msg,
                                getTextFromUiString(event.errMessage),
                                errCode
                            )
                        }
                        doShowToaster(
                            toasterType = Toaster.TYPE_ERROR,
                            message = errMessage
                        )
                    }
                    is CopyToClipboardEvent -> copyToClipboard(event.content)
                    is ShowRealTimeNotificationEvent -> {
                        rtnView?.queueNotification(event.notification)
                    }
                    is AnimateLikeEvent -> {
                        likeView.playLikeAnimation()
                    }
                    is ShowLikeBubbleEvent -> {
                        if (event is ShowLikeBubbleEvent.Burst) {
                            likeBubbleView.shotBurst(event.count, event.reduceOpacity, event.config)
                        }
                        else if (event is ShowLikeBubbleEvent.Single) {
                            likeBubbleView.shot(event.count, event.reduceOpacity, event.config)
                        }
                    }
                    RemindToLikeEvent -> likeView.playReminderAnimation()
                    is PreloadLikeBubbleIconEvent -> likeBubbleView.preloadIcons(event.urls)
                }
            }
        }
    }

    private fun observeCastState() {
        playViewModel.observableCastState.observe(viewLifecycleOwner) {
            castViewOnStateChanged()
            pipViewOnStateChanged()
            sendCastAnalytic(it)
        }
    }
    //endregion

    private fun setupFeaturedProductsFadingEdge(view: View) {
        view.doOnLayout {
            productFeaturedView?.setFadingEndBounds(
                (FADING_EDGE_PRODUCT_FEATURED_WIDTH_MULTIPLIER * it.width).toInt()
            )
        }
    }

    private fun sendCastAnalytic(cast: PlayCastUiModel) {
        when {
            cast.connectFailed() -> {
                analytic.connectCast(false)
            }
            cast.currentState == PlayCastState.CONNECTED -> {
                val channelData = playViewModel.latestCompleteChannelData
                analytic.connectCast(true, channelData.id, channelData.channelDetail.channelInfo.channelType)
                castAnalyticHelper.startRecording()
            }
            cast.previousState == PlayCastState.CONNECTED -> {
                castAnalyticHelper.stopRecording()
            }
        }
    }

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
                if (shouldImmersive) {
                    interactiveWinnerBadgeView?.hideCoachMark()
                    playFullscreenManager.onEnterFullscreen()
                }
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
                    if (isAdded) {
                        playFragment.stopRenderMonitoring()
                        statsInfoView.rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    }
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

    private fun doClickChatBox() {
        viewModel.doInteractionEvent(InteractionEvent.SendChat)
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
            InteractionEvent.SendChat -> shouldComposeChat()
            is InteractionEvent.OpenProductDetail -> doOpenProductDetail(event.product, event.position)
        }
    }

    private fun shouldComposeChat() {
        sendChatView?.focusChatForm(shouldFocus = true, forceChangeKeyboardState = true)
    }

    private fun openLoginPage() {
        openPageByApplink(ApplinkConst.LOGIN, requestCode = REQUEST_CODE_LOGIN)
    }

    private fun openPageByApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false, pipMode: Boolean = false) {
        if (pipMode && playViewModel.isPiPAllowed && !playViewModel.isFreezeOrBanned) {
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
        playViewModel.onShowProductSheet(bottomSheetMaxHeight)
    }

    private fun pushParentPlayByKeyboardHeight(estimatedKeyboardHeight: Int) {
        val hasQuickReply = !playViewModel.observableQuickReply.value?.quickReplyList.isNullOrEmpty()

        viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) {
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
            maxTopPosition: Int = mMaxTopChatMode ?: 0,
            shouldForceInvalidate: Boolean = false,
    ) {
        if (!playViewModel.channelType.isLive) return

        val hasQuickReply = !playViewModel.observableQuickReply.value?.quickReplyList.isNullOrEmpty()

        val hasProductFeatured = productFeaturedView?.isShown() == true
        val hasPinnedVoucher = pinnedVoucherView?.isShown() == true

        if (bottomInsets.isKeyboardShown) getChatListHeightManager().invalidateHeightChatMode(videoOrientation, videoPlayer, maxTopPosition, hasQuickReply)
        else getChatListHeightManager().invalidateHeightNonChatMode(videoOrientation, videoPlayer, shouldForceInvalidate, hasProductFeatured, hasPinnedVoucher)
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

    private fun doShowToaster(
            toasterType: Int = Toaster.TYPE_NORMAL,
            actionText: String = "",
            message: String,
    ) {
        if (toasterBottomMargin == 0) {
            val likeAreaView = likeView.rootView
            val likeAreaBottomMargin = (likeAreaView.layoutParams as? ViewGroup.MarginLayoutParams)
                ?.bottomMargin ?: 0
            toasterBottomMargin = likeAreaView.height + likeAreaBottomMargin
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
        viewLifecycleOwner.lifecycleScope.launch(dispatchers.main) {
            delay(AUTO_SWIPE_DELAY)
            playNavigation.navigateToNextPage()
        }
    }

    //region OnStateChanged
    private fun playButtonViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            state: PlayViewerVideoState = playViewModel.observableVideoProperty.value?.state ?: PlayViewerVideoState.Unknown
    ) {
        if (!channelType.isVod || (videoPlayer is PlayVideoPlayerUiModel.General.Complete && videoPlayer.playerType != PlayerType.Client)) {
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
        else if (channelType.isVod && videoPlayer.isGeneral() && !bottomInsets.isAnyShown) videoControlView.show()
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
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned,
    ) {
        if (isFreezeOrBanned) videoSettingsView.hide()
        else if (videoOrientation.isHorizontal && videoPlayer.isGeneral() && !bottomInsets.isAnyShown) videoSettingsView.show()
        else videoSettingsView.hide()
    }

    private fun pinnedViewOnStateChanged(
            pinnedModel: PinnedMessageUiModel? = playViewModel.observablePinnedMessage.value,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned,
            shouldTriggerChatHeightCalculation: Boolean = false,
    ) {
        if (isFreezeOrBanned) {
            pinnedView?.hide()
            return
        }

        if (pinnedModel != null) {
            pinnedView?.setPinnedMessage(pinnedModel)
            if (pinnedModel.shouldShow && !bottomInsets.isAnyShown) pinnedView?.show()
            else pinnedView?.hide()
        } else pinnedView?.hide()

        changePinnedMessageConstraint()

        if (shouldTriggerChatHeightCalculation) viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) { invalidateChatListBounds(shouldForceInvalidate = true) }

        changeQuickReplyConstraint()
    }

    private fun productFeaturedViewOnStateChanged(
            pinnedModel: PinnedProductUiModel? = playViewModel.observablePinnedProduct.value,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned,
            shouldTriggerChatHeightCalculation: Boolean = false,
    ) {
        if (isFreezeOrBanned) {
            pinnedVoucherView?.hide()
            productFeaturedView?.hide()
            return
        }

        if (pinnedModel != null && pinnedModel.productTags is PlayProductTagsUiModel.Complete) {
            pinnedVoucherView?.setVoucher(pinnedModel.productTags.voucherList)
            productFeaturedView?.setFeaturedProducts(pinnedModel.productTags.productList, pinnedModel.productTags.basicInfo.maxFeaturedProducts)
            productSeeMoreView?.setTotalProduct(pinnedModel.productTags.productList.size)
        }

        if (!bottomInsets.isAnyShown && pinnedModel?.shouldShow == true) {
            pinnedVoucherView?.showIfNotEmpty()
            productFeaturedView?.showIfNotEmpty()
        } else {
            pinnedVoucherView?.hide()
            productFeaturedView?.hide()
        }

        changePinnedMessageConstraint()

        if (shouldTriggerChatHeightCalculation) viewLifecycleOwner.lifecycleScope.launch(dispatchers.immediate) { invalidateChatListBounds() }
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

    private fun partnerInfoViewOnStateChanged(
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
    ) {
        if (!bottomInsets.isAnyShown && orientation.isPortrait) partnerInfoView?.show()
        else partnerInfoView?.hide()
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

    private fun sendChatViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (channelType.isLive &&
                bottomInsets[BottomInsetsType.ProductSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.VariantSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.CouponSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.LeaderboardSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.KebabMenuSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.UserReportSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.UserReportSubmissionSheet]?.isShown == false) {
            sendChatView?.show()
        } else sendChatView?.invisible()

        sendChatView?.focusChatForm(channelType.isLive && bottomInsets[BottomInsetsType.Keyboard] is BottomInsetsState.Shown
                && bottomInsets[BottomInsetsType.UserReportSubmissionSheet] is BottomInsetsState.Hidden)
    }

    private fun quickReplyViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
    ) {
        if (channelType.isLive &&
                bottomInsets[BottomInsetsType.ProductSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.VariantSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.CouponSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.LeaderboardSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.KebabMenuSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.UserReportSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.UserReportSubmissionSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.Keyboard]?.isShown == true) {
            quickReplyView?.showIfNotEmpty()
        } else quickReplyView?.hide()

        changeQuickReplyConstraint()
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
        val isCastVisible = castView?.isShown() ?: false
        if (!playViewModel.isPiPAllowed || !videoPlayer.isGeneral() || isFreezeOrBanned || playViewModel.isCastAllowed || isCastVisible) {
            pipView?.hide()
            return
        }

        if (!bottomInsets.isAnyShown) pipView?.show()
        else pipView?.hide()
    }

    private fun renderInteractiveView(
        prevState: PlayInteractiveViewUiState?,
        state: PlayInteractiveViewUiState,
        partner: PlayPartnerUiState,
    ) {
        if (prevState?.interactive != state.interactive) {
            when (val interactive = state.interactive) {
                PlayInteractiveUiState.Loading -> {
                    interactiveView?.setLoading()
                }
                PlayInteractiveUiState.Error -> {
                    interactiveView?.setError()
                }
                is PlayInteractiveUiState.PreStart -> {
                    interactiveView?.setPreStart(
                        title = interactive.title,
                        timeToStartInMs = interactive.timeToStartInMs
                    ) { playViewModel.submitAction(InteractivePreStartFinishedAction) }
                }
                is PlayInteractiveUiState.Ongoing -> {
                    interactiveView?.setTapTap(durationInMs = interactive.timeRemainingInMs) {
                        playViewModel.submitAction(InteractiveOngoingFinishedAction)
                    }
                }
                is PlayInteractiveUiState.Finished -> {
                    interactiveView?.setFinish(info = getString(interactive.info))
                }
                else -> {}
            }

            /**
             * Connect to different anchor because Lottie increase the height of interactive view in a significant way
             * and because of that, the distance between interactive view and winner badge increase significantly
             */
            val winnerBadgeView = interactiveWinnerBadgeView?.rootView
            if (winnerBadgeView != null && state.interactive !is PlayInteractiveUiState.Ongoing) {
                view?.changeConstraint {
                    val bottomAnchor = interactiveView?.id ?: return@changeConstraint
                    connect(winnerBadgeView.id, ConstraintSet.BOTTOM, bottomAnchor, ConstraintSet.TOP)
                }
            }

        }

        interactiveView?.showFollowMode(
            partner.followStatus is PlayPartnerFollowStatus.Followable &&
                    !partner.followStatus.isFollowing
        )

        when (state.visibility) {
            ViewVisibility.Visible -> interactiveView?.show()
            ViewVisibility.Invisible -> interactiveView?.invisible()
            ViewVisibility.Gone -> interactiveView?.hide()
        }
    }

    private fun renderWinnerBadgeView(state: PlayWinnerBadgeUiState) {
        if (state.shouldShow) interactiveWinnerBadgeView?.show()
        else interactiveWinnerBadgeView?.hide()
    }

    private fun renderToolbarView(
        title: PlayTitleUiState,
        share: PlayShareUiState
    ) {
        toolbarView.setTitle(title.title)

        if(share.shouldShow) shareLinkView?.show()
        else shareLinkView?.hide()
    }

    private fun renderPartnerInfoView(prevState: PlayPartnerUiState?, state: PlayPartnerUiState) {
        if (prevState == state) return
        partnerInfoView?.setInfo(state)
    }

    private fun renderLikeView(
            prevState: PlayLikeUiState?,
            likeState: PlayLikeUiState,
    ) {
        if (prevState?.canLike != likeState.canLike) likeView.setEnabled(isEnabled = likeState.canLike)

        if (prevState?.isLiked != likeState.isLiked) {
            likeView.setIsLiked(likeState.isLiked)
        }

        likeCountView.setTotalLikes(likeState.totalLike)

        if (likeState.shouldShow) {
            likeView.show()
            likeCountView.show()
        }
        else {
            likeView.hide()
            likeCountView.hide()
        }
    }

    private fun renderLikeBubbleView(likeState: PlayLikeUiState) {
        if (likeState.canShowBubble) likeBubbleView.show()
        else likeBubbleView.hide()
    }

    private fun renderStatsInfoView(totalView: PlayTotalViewUiState) {
        statsInfoView.setTotalViews(totalView.viewCountStr)
    }

    private fun renderRealTimeNotificationView(rtn: PlayRtnUiState) {
        rtnView?.setLifespan(rtn.lifespanInMs)
        if (rtn.shouldShow) rtnView?.show()
        else rtnView?.invisible()
    }

    private fun renderViewAllProductView(viewAllProduct: PlayViewAllProductUiState) {
        if(viewAllProduct.shouldShow) productSeeMoreView?.show()
        else productSeeMoreView?.hide()
    }

    private fun renderKebabMenuView(kebabMenuUiState: PlayKebabMenuUiState) {
        if(kebabMenuUiState.shouldShow) kebabMenuView?.show()
        else kebabMenuView?.hide()
    }

    private fun castViewOnStateChanged(
        bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if(playViewModel.isCastAllowed && !bottomInsets.isAnyShown) {
            val currentVisibility = castView?.isHidden() ?: true
            if(currentVisibility)
                analytic.impressCast(playViewModel.latestCompleteChannelData.channelDetail.channelInfo.id, playViewModel.channelType)

            castView?.show()
        }
        else castView?.hide()
    }
    //endregion

    private fun getTextFromUiString(uiString: UiString): String {
        return when (uiString) {
            is UiString.Text -> uiString.text
            is UiString.Resource -> getString(uiString.resource)
        }
    }

    /**
     * Change constraint
     */
    private fun changePinnedMessageConstraint() {
        val pinnedMessageId = pinnedView?.id ?: return
        val pinnedVoucherId = pinnedVoucherView?.id ?: return
        view?.changeConstraint {
            connect(pinnedMessageId, ConstraintSet.BOTTOM, pinnedVoucherId, ConstraintSet.TOP, offset8)
            setGoneMargin(pinnedMessageId, ConstraintSet.BOTTOM, offset8)
        }
    }

    private fun changeQuickReplyConstraint(
            pinnedMessage: PinnedMessageUiModel? = playViewModel.observablePinnedMessage.value,
            videoPlayer: PlayVideoPlayerUiModel = playViewModel.videoPlayer,
            channelType: PlayChannelType = playViewModel.channelType
    ) {
        /**
         * This can be solved by a simple barrier, but the barrier only work on testapp, not customerapp
         * and I don't know why arghhh
         */
        val quickReplyViewId = quickReplyView?.id ?: return
        val topmostLikeView = this.topmostLikeView ?: return
        view?.changeConstraint {
            if(quickReplyView?.isShown() == true) {
                sendChatView?.let {
                    connect(quickReplyViewId, ConstraintSet.BOTTOM, it.id, ConstraintSet.TOP, offset8)
                }
            } else {
                connect(quickReplyViewId, ConstraintSet.BOTTOM, topmostLikeView.id, ConstraintSet.TOP)
            }
        }
    }

    /**
     * Dialog
     */
    private fun getInteractiveWinningDialog(): InteractiveWinningDialogFragment {
        val existing = InteractiveWinningDialogFragment.get(childFragmentManager)
        return existing ?: childFragmentManager.fragmentFactory.instantiate(requireActivity().classLoader, InteractiveWinningDialogFragment::class.java.name) as InteractiveWinningDialogFragment
    }

    override fun onKebabMenuClick(view: KebabMenuViewComponent) {
        analytic.clickKebabMenu()
        playViewModel.onShowKebabMenuSheet(bottomSheetMenuMaxHeight)
    }

    companion object {
        private const val INTERACTION_TOUCH_CLICK_TOLERANCE = 25

        private const val REQUEST_CODE_LOGIN = 192

        private const val PERCENT_BOTTOMSHEET_HEIGHT = 0.6
        private const val PERCENT_MENU_BOTTOMSHEET_HEIGHT = 0.2

        private const val VISIBLE_ALPHA = 1f

        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L

        private const val AUTO_SWIPE_DELAY = 500L

        private const val MASK_NO_CUT_HEIGHT = 0f

        private const val FADING_EDGE_PRODUCT_FEATURED_WIDTH_MULTIPLIER = 0.125f
    }
}