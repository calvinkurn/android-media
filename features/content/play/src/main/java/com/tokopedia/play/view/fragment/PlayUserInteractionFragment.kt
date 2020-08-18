package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.animation.PlayDelayFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeInAnimation
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play.extensions.*
import com.tokopedia.play.gesture.PlayClickTouchListener
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.PlayFullScreenHelper
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.DistinctEventObserver
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.view.measurement.ScreenOrientationDataSource
import com.tokopedia.play.view.measurement.bounds.provider.PlayVideoBoundsProvider
import com.tokopedia.play.view.measurement.bounds.provider.VideoBoundsProvider
import com.tokopedia.play.view.measurement.layout.DynamicLayoutManager
import com.tokopedia.play.view.measurement.layout.PlayDynamicLayoutManager
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.measurement.scaling.PlayVideoScalingManager
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.viewcomponent.*
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play_common.model.result.NetworkResult
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.util.extension.awaitMeasured
import com.tokopedia.play_common.util.extension.recreateView
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.play_common.viewcomponent.viewComponentOrNull
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Created by jegul on 29/11/19
 */
class PlayUserInteractionFragment @Inject constructor(
        private val viewModelFactory: ViewModelProvider.Factory,
        private val dispatchers: CoroutineDispatcherProvider,
        private val trackingQueue: TrackingQueue
):
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
        EndLiveInfoViewComponent.Listener
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
    private val videoSettingsView by viewComponent { VideoSettingsViewComponent(it, R.id.view_video_settings, this) }
    private val immersiveBoxView by viewComponent { ImmersiveBoxViewComponent(it, R.id.v_immersive_box, this) }
    private val playButtonView by viewComponent { PlayButtonViewComponent(it, R.id.view_play_button, this) }
    private val endLiveInfoView by viewComponent { EndLiveInfoViewComponent(it, R.id.view_end_live_info, this) }

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
        get() = requireParentFragment() as PlayOrientationListener

    private val playNavigation: PlayNavigation
        get() = requireActivity() as PlayNavigation

    private var systemUiVisibility: Int
        get() = requireActivity().window.decorView.systemUiVisibility
        set(value) {
            requireActivity().window.decorView.systemUiVisibility = value
        }

    private val orientation: ScreenOrientation
        get() = ScreenOrientation.getByInt(resources.configuration.orientation)

    private var videoBoundsProvider: VideoBoundsProvider? = null
    private var dynamicLayoutManager: DynamicLayoutManager? = null

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
        setupView(view)
        setupInsets(view)
        setupObserve()

        invalidateSystemUiVisibility()
    }

    override fun onStart() {
        super.onStart()
        spaceSize.rootView.requestApplyInsetsWhenAttached()
        endLiveInfoView.rootView.requestApplyInsetsWhenAttached()
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
    }

    override fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet) {
        PlayAnalytics.clickWatchMode(channelId, playViewModel.channelType)
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
        invalidateSystemUiVisibility()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        recreateView()
    }

    override fun onDestroyView() {
        videoBoundsProvider = null
        dynamicLayoutManager = null
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
        PlayAnalytics.clickSendChat(channelId)
        doSendChat(message)
    }

    /**
     * QuickReply View Component Listener
     */
    override fun onQuickReplyClicked(view: QuickReplyViewComponent, replyString: String) {
        PlayAnalytics.clickQuickReply(channelId)
        doSendChat(replyString)
    }

    /**
     * QuickReply View Component Listener
     */
    override fun onPinnedMessageActionClicked(view: PinnedViewComponent, applink: String, message: String) {
        PlayAnalytics.clickPinnedMessage(channelId, message, applink, playViewModel.channelType)
        openPageByApplink(applink)
    }

    override fun onPinnedProductActionClicked(view: PinnedViewComponent) {
        doClickPinnedProduct()
    }

    /**
     * VideoSettings View Component Listener
     */
    override fun onEnterFullscreen(view: VideoSettingsViewComponent) {
        PlayAnalytics.clickCtaFullScreenFromPortraitToLandscape(
                userId = playViewModel.userId,
                channelId = channelId,
                channelType = playViewModel.channelType
        )
        enterFullscreen()
    }

    override fun onExitFullscreen(view: VideoSettingsViewComponent) {
        exitFullscreen()
    }

    /**
     * ImmersiveBox View Component Listener
     */
    override fun onImmersiveBoxClicked(view: ImmersiveBoxViewComponent, currentAlpha: Float) {
        PlayAnalytics.clickWatchArea(
                channelId = channelId,
                userId = playViewModel.userId,
                channelType = playViewModel.channelType,
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
     * EndLiveInfo View Component Listener
     */
    override fun onButtonActionClicked(view: EndLiveInfoViewComponent, btnUrl: String) {
        openPageByApplink(
                applink = btnUrl,
                shouldFinish = true
        )
    }
    //endregion

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
    }

    private fun setupInsets(view: View) {
        spaceSize.rootView.doOnApplyWindowInsets { v, insets, _, margin ->
            val marginLayoutParams = v.layoutParams as ViewGroup.MarginLayoutParams
            var isMarginChanged = false

            val newTopMargin = insets.systemWindowInsetTop
            if (marginLayoutParams.topMargin != newTopMargin) {
                marginLayoutParams.updateMargins(top = newTopMargin)
                isMarginChanged = true
            }

            val newBottomMargin = margin.bottom + insets.systemWindowInsetBottom
            if (marginLayoutParams.bottomMargin != newBottomMargin) {
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

        endLiveInfoView.rootView.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(
                    left = padding.left + insets.systemWindowInsetLeft,
                    right = padding.right + insets.systemWindowInsetRight,
                    bottom = padding.bottom + insets.systemWindowInsetBottom
            )
        }
    }

    private fun setupObserve() {
        observeVideoMeta()
        observeVideoProperty()
        observeTitleChannel()
        observeQuickReply()
        observeToolbarInfo()
        observeTotalLikes()
        observeTotalViews()
        observeNewChat()
        observeChatList()
        observePinned()
        observeCartInfo()
        observeLikeContent()
        observeBottomInsetsState()
        observeEventUserInfo()

        observeLoggedInInteractionEvent()
    }

    private fun invalidateSystemUiVisibility() {
        systemUiVisibility = when {
            playViewModel.isFreezeOrBanned -> PlayFullScreenHelper.getShowSystemUiVisibility()
            orientation.isLandscape -> PlayFullScreenHelper.getHideSystemUiVisibility()
            !playViewModel.videoOrientation.isHorizontal && container.isFullAlpha -> PlayFullScreenHelper.getHideSystemUiVisibility()
            else -> PlayFullScreenHelper.getShowSystemUiVisibility()
        }
    }

    private fun handleVideoHorizontalTopBounds() {
        scope.launch {
            val toolbarMeasure = async { toolbarView.rootView.awaitMeasured() }
            val statsInfoMeasure = async { statsInfoView.rootView.awaitMeasured() }

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
            meta.videoStream?.let {
                changeLayoutBasedOnVideoOrientation(it.orientation)
                triggerImmersive(false)

                scope.launch(dispatchers.immediate) {
                    playFragment.setCurrentVideoTopBounds(it.orientation, getVideoTopBounds(it.orientation))
                }

                statsInfoViewOnStateChanged(channelType = it.channelType)
                videoControlViewOnStateChanged(channelType = it.channelType)
                sendChatViewOnStateChanged(channelType = it.channelType)
                chatListViewOnStateChanged(channelType = it.channelType)
                videoSettingsViewOnStateChanged(videoOrientation = it.orientation)
                gradientBackgroundViewOnStateChanged(videoOrientation = it.orientation)
            }

            changeLayoutBasedOnVideoType(meta.videoPlayer, playViewModel.channelType)
            if (meta.videoPlayer is General) videoControlView.setPlayer(meta.videoPlayer.exoPlayer)
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, DistinctObserver {
            if (it.state == PlayVideoState.Playing) {
                PlayAnalytics.clickPlayVideo(channelId, playViewModel.channelType)
            }
            if (it.state == PlayVideoState.Ended) showInteractionIfWatchMode()

            playButtonViewOnStateChanged(state = it.state)
        })
    }

    private fun observeTitleChannel() {
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is NetworkResult.Success, is NetworkResult.Fail -> triggerStartMonitoring()
            }
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

    private fun observeTotalLikes() {
        playViewModel.observableTotalLikes.observe(viewLifecycleOwner, DistinctObserver {
            likeView.setTotalLikes(it)
            endLiveInfoView.setTotalLikes(it)
        })
    }

    private fun observeTotalViews() {
        playViewModel.observableTotalViews.observe(viewLifecycleOwner, DistinctObserver {
            statsInfoView.setTotalViews(it)
            endLiveInfoView.setTotalViews(it)
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
        playViewModel.observablePinned.observe(viewLifecycleOwner, DistinctObserver {
            pinnedViewOnStateChanged(pinnedModel = it)
        })
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeLikeContent() {
        playViewModel.observableLikeState.observe(viewLifecycleOwner, object : Observer<LikeStateUiModel> {
            private var isFirstTime = true
            override fun onChanged(likeModel: LikeStateUiModel) {
                likeView.setEnabled(true)
                likeView.playLikeAnimation(likeModel.isLiked, !likeModel.fromNetwork && !isFirstTime)

                isFirstTime = false
            }
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, DistinctObserver { map ->
            if (!playViewModel.isFreezeOrBanned) view?.hide()

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
            videoSettingsViewOnStateChanged(bottomInsets = map)
            immersiveBoxViewOnStateChanged(bottomInsets = map)
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, DistinctObserver {
            getBottomSheetInstance().setState(it.isFreeze)

            if (it.isFreeze || it.isBanned) {
                gradientBackgroundView.hide()
                likeView.hide()
                quickReplyView?.hide()
                chatListView?.hide()
                pinnedView?.hide()
                immersiveBoxView.hide()
                playButtonView.hide()

                videoControlViewOnStateChanged(isFreezeOrBanned = true)

                sendChatView?.focusChatForm(false)
                sendChatView?.hide()

                videoSettingsViewOnStateChanged(isFreezeOrBanned = true)
                toolbarViewOnStateChanged(isFreezeOrBanned = true)
                statsInfoViewOnStateChanged(isFreezeOrBanned = true)

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
        playViewModel.observableBadgeCart.observe(viewLifecycleOwner, DistinctObserver {
            toolbarView.setCartInfo(it)
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

        when {
            playViewModel.isFreezeOrBanned -> {
                container.alpha = VISIBLE_ALPHA
                systemUiVisibility = PlayFullScreenHelper.getShowSystemUiVisibility()
            }
            orientation.isLandscape -> triggerFullImmersive(shouldImmersive, true)
            playViewModel.videoOrientation.isHorizontal -> handleVideoHorizontalImmersive(shouldImmersive)
            playViewModel.videoOrientation.isVertical -> {
                systemUiVisibility =
                        if (shouldImmersive) PlayFullScreenHelper.getHideSystemUiVisibility()
                        else PlayFullScreenHelper.getShowSystemUiVisibility()
                triggerFullImmersive(shouldImmersive, false)
            }
        }
    }

    private lateinit var onToolbarGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    private fun triggerStartMonitoring() {
        playFragment.startRenderMonitoring()

        if (!this::onToolbarGlobalLayoutListener.isInitialized) {
            onToolbarGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener{
                override fun onGlobalLayout() {
                    playFragment.stopRenderMonitoring()
                    toolbarView.rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
            toolbarView.rootView.viewTreeObserver.addOnGlobalLayoutListener(onToolbarGlobalLayoutListener)
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
        PlayAnalytics.clickFollowShop(channelId, partnerId.toString(), action.value, playViewModel.channelType)
        viewModel.doFollow(partnerId, action)

        toolbarView.setFollowStatus(action == PartnerFollowAction.Follow)
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
        else if (partnerType == PartnerType.Influencer) openProfilePage(partnerId)
    }

    private fun openShopPage(partnerId: Long) {
        PlayAnalytics.clickShop(channelId, partnerId.toString(), playViewModel.channelType)
        openPageByApplink(ApplinkConst.SHOP, partnerId.toString())
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

    private fun doClickPinnedProduct() {
        viewModel.doInteractionEvent(InteractionEvent.ClickPinnedProduct)
    }

    private fun doClickFollow(partnerId: Long, followAction: PartnerFollowAction) {
        viewModel.doInteractionEvent(InteractionEvent.Follow(partnerId, followAction))
    }

    private fun shouldOpenCartPage() {
        PlayAnalytics.clickCartIcon(channelId, playViewModel.channelType)
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
            InteractionEvent.ClickPinnedProduct -> openProductSheet()
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

        viewModel.doLikeUnlike(playViewModel.contentId,
                playViewModel.contentType,
                playViewModel.likeType,
                shouldLike)

        PlayAnalytics.clickLike(channelId, shouldLike, playViewModel.channelType)
    }

    private fun openLoginPage() {
        openPageByApplink(ApplinkConst.LOGIN, requestCode = REQUEST_CODE_LOGIN)
    }

    private fun openPageByApplink(applink: String, vararg params: String, requestCode: Int? = null, shouldFinish: Boolean = false) {
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
        PlayAnalytics.clickPinnedProduct(channelId)
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
        return getVideoBoundsProvider().getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight, hasQuickReply)
    }

    private fun changeLayoutBasedOnVideoOrientation(videoOrientation: VideoOrientation) {
        getDynamicLayoutManager().onVideoOrientationChanged(videoOrientation)
    }

    private fun changeLayoutBasedOnVideoType(videoPlayerUiModel: VideoPlayerUiModel, channelType: PlayChannelType) {
        getDynamicLayoutManager().onVideoPlayerChanged(videoPlayerUiModel, channelType)
    }

    private fun getDynamicLayoutManager(): DynamicLayoutManager = synchronized(this) {
        if (dynamicLayoutManager == null) {
            dynamicLayoutManager = PlayDynamicLayoutManager(container as ViewGroup, object : ScreenOrientationDataSource {
                override fun getScreenOrientation(): ScreenOrientation {
                    return orientation
                }
            })
        }
        return dynamicLayoutManager!!
    }

    private fun getVideoBoundsProvider(): VideoBoundsProvider = synchronized(this) {
        if (videoBoundsProvider == null) {
            videoBoundsProvider = PlayVideoBoundsProvider(container as ViewGroup, object : ScreenOrientationDataSource {
                override fun getScreenOrientation(): ScreenOrientation {
                    return orientation
                }
            })
        }
        return videoBoundsProvider!!
    }

    //region OnStateChanged
    private fun playButtonViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            state: PlayVideoState
    ) {
        if (!channelType.isVod) {
            playButtonView.hide()
            return
        }
        when (state) {
            PlayVideoState.Pause -> playButtonView.showPlayButton()
            PlayVideoState.Ended -> playButtonView.showRepeatButton()
            else -> playButtonView.hide()
        }
    }

    private fun videoControlViewOnStateChanged(
            channelType: PlayChannelType = playViewModel.channelType,
            videoPlayer: VideoPlayerUiModel = playViewModel.videoPlayer,
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
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (channelType.isLive && !bottomInsets.isAnyBottomSheetsShown) chatListView?.show() else chatListView?.hide()
    }

    private fun videoSettingsViewOnStateChanged(
            videoOrientation: VideoOrientation = playViewModel.videoOrientation,
            videoPlayer: VideoPlayerUiModel = playViewModel.videoPlayer,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets,
            isFreezeOrBanned: Boolean = playViewModel.isFreezeOrBanned
    ) {
        if (isFreezeOrBanned) videoSettingsView.hide()
        else if (videoOrientation.isHorizontal && videoPlayer.isGeneral && !bottomInsets.isAnyShown) videoSettingsView.show()
        else videoSettingsView.hide()
    }

    private fun pinnedViewOnStateChanged(
            pinnedModel: PinnedUiModel? = playViewModel.observablePinned.value,
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        when (pinnedModel) {
            is PinnedMessageUiModel -> {
                pinnedView?.setPinnedMessage(pinnedModel)

                if (!bottomInsets.isAnyShown) pinnedView?.show()
                else pinnedView?.hide()
            }
            is PinnedProductUiModel -> {
                pinnedView?.setPinnedProduct(pinnedModel)

                if (!bottomInsets.isAnyShown) pinnedView?.show()
                else pinnedView?.hide()
            }
            is PinnedRemoveUiModel -> {
                pinnedView?.hide()
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

        if (isFreezeOrBanned) statsInfoView.show()
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
            bottomInsets: Map<BottomInsetsType, BottomInsetsState> = playViewModel.bottomInsets
    ) {
        if (channelType.isLive &&
                bottomInsets[BottomInsetsType.ProductSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.VariantSheet]?.isShown == false &&
                bottomInsets[BottomInsetsType.Keyboard]?.isShown == true) {
            quickReplyView?.show()
        } else quickReplyView?.hide()
    }

    private fun immersiveBoxViewOnStateChanged(
            bottomInsets: Map<BottomInsetsType, BottomInsetsState>
    ) {
        if (bottomInsets.isAnyShown) immersiveBoxView.hide() else immersiveBoxView.show()
    }

    private fun endLiveInfoViewOnStateChanged(
            event: EventUiModel
    ) {
        if(event.isFreeze) {
            endLiveInfoView.setInfo(
                    title = event.freezeTitle,
                    message = event.freezeMessage,
                    btnTitle = event.freezeButtonTitle,
                    btnUrl = event.freezeButtonUrl
            )
            endLiveInfoView.show()
        } else endLiveInfoView.hide()
    }
    //endregion

    companion object {
        private const val INTERACTION_TOUCH_CLICK_TOLERANCE = 25

        private const val REQUEST_CODE_LOGIN = 192

        private const val PERCENT_PRODUCT_SHEET_HEIGHT = 0.6

        private const val VISIBLE_ALPHA = 1f

        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L
    }
}