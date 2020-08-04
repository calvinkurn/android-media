package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.TkpdBaseV4Fragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.animation.PlayDelayFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeInAnimation
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play.extensions.*
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.util.PlayFullScreenHelper
import com.tokopedia.play.util.changeConstraint
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.DistinctEventObserver
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.layout.parent.PlayParentLayoutManagerImpl
import com.tokopedia.play.view.type.*
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.viewcomponent.*
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play_common.model.ui.PlayChatUiModel
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.play_common.view.doOnApplyWindowInsets
import com.tokopedia.play_common.view.requestApplyInsetsWhenAttached
import com.tokopedia.play_common.view.updateMargins
import com.tokopedia.play_common.view.updatePadding
import com.tokopedia.play_common.viewcomponent.viewComponent
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

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

    companion object {
        private const val REQUEST_CODE_LOGIN = 192

        private const val PERCENT_PRODUCT_SHEET_HEIGHT = 0.6

        private const val VISIBLE_ALPHA = 1f

        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L
    }

    private val spaceSize by viewComponent { EmptyViewComponent(it, R.id.space_size) }
    private val gradientBackgroundView by viewComponent { EmptyViewComponent(it, R.id.view_gradient_background) }
    private val toolbarView by viewComponent { ToolbarViewComponent(it, R.id.view_toolbar, this) }
    private val statsInfoView by viewComponent { StatsInfoViewComponent(it, R.id.view_stats_info) }
    private val videoControlView by viewComponent { VideoControlViewComponent(it, R.id.pcv_video, this) }
    private val likeView by viewComponent { LikeViewComponent(it, R.id.view_like, this) }
    private val sendChatView by viewComponent { SendChatViewComponent(it, R.id.view_send_chat, this) }
    private val quickReplyView by viewComponent { QuickReplyViewComponent(it, R.id.rv_quick_reply, this) }
    private val chatListView by viewComponent { ChatListViewComponent(it, R.id.view_chat_list) }
    private val pinnedView by viewComponent { PinnedViewComponent(it, R.id.view_pinned, this) }
    private val videoSettingsView by viewComponent { VideoSettingsViewComponent(it, R.id.view_video_settings, this) }
    private val immersiveBoxView by viewComponent { ImmersiveBoxViewComponent(it, R.id.v_immersive_box, this) }
    private val playButtonView by viewComponent { PlayButtonViewComponent(it, R.id.view_play_button, this) }
    private val endLiveInfoView by viewComponent { EndLiveInfoViewComponent(it, R.id.view_end_live_info, this) }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayInteractionViewModel

    private lateinit var bottomSheet: PlayMoreActionBottomSheet

    private val container: View
        get() = requireView()

    private val offset12 by lazy { container.resources.getDimensionPixelOffset(R.dimen.play_offset_12) }
    private val offset16 by lazy { container.resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4) }

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

    override fun onInterceptSystemUiVisibilityChanged(): Boolean {
        return if (!orientation.isLandscape) {
            systemUiVisibility = if (!playViewModel.videoOrientation.isHorizontal && container.isFullAlpha)
                PlayFullScreenHelper.getHideSystemUiVisibility()
            else
                PlayFullScreenHelper.getShowSystemUiVisibility()

            true
        } else false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val lastAction = viewModel.observableLoggedInInteractionEvent.value?.peekContent()
            if (lastAction != null) handleInteractionEvent(lastAction.event)
        } else super.onActivityResult(requestCode, resultCode, data)
    }

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

            if (isMarginChanged) v.parent.requestLayout()
        }

        endLiveInfoView.rootView.doOnApplyWindowInsets { v, insets, padding, _ ->
            v.updatePadding(bottom = padding.bottom + insets.systemWindowInsetBottom)
        }
    }

    private fun setupObserve() {
        observeVideoPlayer()
        observeVideoProperty()
        observeTitleChannel()
        observeQuickReply()
        observeVideoStream()
        observeToolbarInfo()
        observeTotalLikes()
        observeTotalViews()
        observeNewChat()
        observeChatList()
        observePinned()
        observeCartInfo()
        observeFollowShop()
        observeLikeContent()
        observeBottomInsetsState()
        observeEventUserInfo()

        observeLoggedInInteractionEvent()
    }

    //region observe
    private fun observeVideoPlayer() {
        playViewModel.observableVideoPlayer.observe(viewLifecycleOwner, Observer {
            changeLayoutBasedOnVideoType(it, playViewModel.channelType)

            /**
             * New
             */
            if (it is General) videoControlView.setPlayer(it.exoPlayer)
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, DistinctObserver {
            if (it.state == PlayVideoState.Playing) {
                PlayAnalytics.clickPlayVideo(channelId, playViewModel.channelType)
            }
            if (it.state == PlayVideoState.Ended) showInteractionIfWatchMode()

            /**
             * New
             */
            if (!playViewModel.channelType.isVod) {
                playButtonView.hide()
                return@DistinctObserver
            }
            when (it.state) {
                PlayVideoState.Pause -> playButtonView.showPlayButton()
                PlayVideoState.Ended -> playButtonView.showRepeatButton()
                else -> playButtonView.hide()
            }
        })
    }

    private fun observeTitleChannel() {
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, DistinctObserver {
            triggerStartMonitoring()
        })
    }

    private fun observeQuickReply() {
        playViewModel.observableQuickReply.observe(viewLifecycleOwner, DistinctObserver {
            quickReplyView.setQuickReply(it)
        })
    }

    private fun observeVideoStream() {
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, DistinctObserver {
            changeLayoutBasedOnVideoOrientation(it.orientation)
            triggerImmersive(false)
            playFragment.setVideoTopBounds(playViewModel.videoPlayer, it.orientation, getVideoTopBounds(it.orientation))

            /**
             * New
             */
            statsInfoView.setLiveBadgeVisibility(it.channelType.isLive)

            if (it.channelType.isVod && playViewModel.videoPlayer.isGeneral && !playViewModel.bottomInsets.isAnyShown) videoControlView.show()
            else videoControlView.hide()

            if (it.channelType.isLive) sendChatView.show() else sendChatView.hide()

            if (it.channelType.isLive && !playViewModel.bottomInsets.isAnyBottomSheetsShown) chatListView.show() else chatListView.hide()

            if (it.orientation.isHorizontal &&
                    playViewModel.videoPlayer.isGeneral &&
                    !playViewModel.bottomInsets.isAnyShown
            ) videoSettingsView.show() else videoSettingsView.hide()
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
            chatListView.showNewChat(it)
        })
    }

    private fun observeChatList() {
        playViewModel.observableChatList.observe(viewLifecycleOwner, object : Observer<List<PlayChatUiModel>> {
            override fun onChanged(chatList: List<PlayChatUiModel>) {
                playViewModel.observableChatList.removeObserver(this)
                chatListView.setChatList(chatList)
            }
        })
    }

    private fun observePinned() {
        playViewModel.observablePinned.observe(viewLifecycleOwner, DistinctObserver {
            when (it) {
                is PinnedMessageUiModel -> {
                    pinnedView.setPinnedMessage(it)

                    if (!playViewModel.bottomInsets.isAnyShown) pinnedView.show()
                    else pinnedView.hide()
                }
                is PinnedProductUiModel -> {
                    pinnedView.setPinnedProduct(it)

                    if (!playViewModel.bottomInsets.isAnyShown) pinnedView.show()
                    else pinnedView.hide()
                }
                is PinnedRemoveUiModel -> {
                    pinnedView.hide()
                }
            }
        })
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeFollowShop() {
        viewModel.observableFollowPartner.observe(viewLifecycleOwner, DistinctObserver {
            if (it is Fail) {
                showToast(it.throwable.message.orEmpty())
            }
        })
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
            }, PlayParentLayoutManagerImpl.ANIMATION_DURATION)

            /**
             * New
             */
            if (map.isAnyShown) gradientBackgroundView.hide() else gradientBackgroundView.show()
            if (!map.isAnyShown) toolbarView.show() else toolbarView.hide()
            if (!map.isAnyShown) statsInfoView.show() else statsInfoView.hide()
            if (!map.isAnyShown && playViewModel.channelType.isVod && playViewModel.videoPlayer.isGeneral) videoControlView.show()
            else videoControlView.hide()
            if (map.isAnyShown) likeView.hide() else likeView.show()

            if (playViewModel.channelType.isLive &&
                    map[BottomInsetsType.ProductSheet]?.isShown == false &&
                    map[BottomInsetsType.VariantSheet]?.isShown == false) {
                sendChatView.show()
            } else sendChatView.hide()

            sendChatView.focusChatForm(playViewModel.channelType.isLive && map[BottomInsetsType.Keyboard] is BottomInsetsState.Shown)

            if (playViewModel.channelType.isLive &&
                    map[BottomInsetsType.ProductSheet]?.isShown == false &&
                    map[BottomInsetsType.VariantSheet]?.isShown == false &&
                    map[BottomInsetsType.Keyboard]?.isShown == true) {
                quickReplyView.show()
            } else quickReplyView.hide()

            if (playViewModel.channelType.isLive &&
                    map[BottomInsetsType.ProductSheet]?.isShown == false &&
                    map[BottomInsetsType.VariantSheet]?.isShown == false) {
                chatListView.show()
            } else chatListView.hide()

            val pinned = playViewModel.observablePinned.value
            if (!map.isAnyShown &&
                    (pinned is PinnedMessageUiModel || pinned is PinnedProductUiModel)
            ) pinnedView.show() else pinnedView.hide()

            if (!map.isAnyShown && playViewModel.videoOrientation.isHorizontal && playViewModel.videoPlayer.isGeneral) videoSettingsView.show()
            else videoSettingsView.hide()

            if (map.isAnyShown) immersiveBoxView.hide() else immersiveBoxView.show()
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, DistinctObserver {
            getBottomSheetInstance().setState(it.isFreeze)

            if (it.isFreeze) hideBottomSheet()

            /**
             * New
             */
            if(it.isFreeze || it.isBanned) gradientBackgroundView.hide()
            if(it.isFreeze || it.isBanned) toolbarView.show()
            if(it.isFreeze || it.isBanned) statsInfoView.show()
            if(it.isFreeze || it.isBanned) {
                videoControlView.hide()
                videoControlView.setPlayer(null)
            }
            if(it.isFreeze || it.isBanned) likeView.hide()
            if(it.isFreeze || it.isBanned) {
                sendChatView.focusChatForm(false)
                sendChatView.hide()
            }
            if(it.isFreeze || it.isBanned) quickReplyView.hide()
            if(it.isFreeze || it.isBanned) chatListView.hide()
            if(it.isFreeze || it.isBanned) pinnedView.hide()
            if(it.isFreeze || it.isBanned) immersiveBoxView.hide()
            if(it.isFreeze || it.isBanned) playButtonView.hide()
            if(it.isFreeze) {
                endLiveInfoView.setInfo(
                        title = it.freezeTitle,
                        message = it.freezeMessage,
                        btnTitle = it.freezeButtonTitle,
                        btnUrl = it.freezeButtonUrl
                )
                endLiveInfoView.show()
            } else endLiveInfoView.hide()
        })
    }

    private fun observeCartInfo() {
        playViewModel.observableBadgeCart.observe(viewLifecycleOwner, DistinctObserver {
            toolbarView.setCartInfo(it)
        })
    }
    //endregion

    private fun setupView(view: View) {
        likeView.setEnabled(false)
        videoSettingsView.setFullscreen(false)

        container.setOnClickListener {
            if (!playViewModel.videoOrientation.isHorizontal && container.hasAlpha) triggerImmersive(it.isFullSolid)
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

        when {
            orientation.isLandscape -> triggerFullImmersive(shouldImmersive, true)
            playViewModel.videoOrientation.isHorizontal -> handleVideoHorizontalImmersive(shouldImmersive)
            else -> {
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

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun doActionFollowPartner(partnerId: Long, action: PartnerFollowAction) {
        PlayAnalytics.clickFollowShop(channelId, partnerId.toString(), action.value, playViewModel.channelType)
        viewModel.doFollow(partnerId, action)

        toolbarView.setFollowStatus(action == PartnerFollowAction.Follow)
    }

    private fun handleVideoHorizontalImmersive(shouldImmersive: Boolean) {
        if (shouldImmersive) videoSettingsView.fadeOut() else videoSettingsView.fadeIn()
        if (shouldImmersive) immersiveBoxView.fadeOut() else immersiveBoxView.fadeIn()
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
        sendChatView.focusChatForm(shouldFocus = true, forceChangeKeyboardState = true)
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

    private fun invalidateInsets(view: View) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) view.requestApplyInsets()
            else view.requestFitSystemWindows()
        } catch (e: Exception) {}
    }

    private fun openProductSheet() {
        PlayAnalytics.clickPinnedProduct(channelId)
        playViewModel.onShowProductSheet(productSheetMaxHeight)
    }

    private fun pushParentPlayByKeyboardHeight(estimatedKeyboardHeight: Int) {
        val hasQuickReply = !playViewModel.observableQuickReply.value?.quickReplyList.isNullOrEmpty()
        playFragment.onBottomInsetsViewShown(getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight, hasQuickReply))
    }

    private fun cancelAllAnimations() {
        fadeAnimationList.forEach { it.cancel() }
    }

    private fun getVideoTopBounds(videoOrientation: VideoOrientation): Int {
        return if (videoOrientation.isHorizontal) {
            val toolbarViewTotalHeight = run {
                val height = if (toolbarView.rootView.height <= 0) {
                    toolbarView.rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    toolbarView.rootView.measuredHeight
                } else toolbarView.rootView.height
                val marginLp = toolbarView.rootView.layoutParams as ViewGroup.MarginLayoutParams
                height + marginLp.bottomMargin + marginLp.topMargin
            }

            val statsInfoTotalHeight = run {
                val height = if (statsInfoView.rootView.height <= 0) {
                    statsInfoView.rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    statsInfoView.rootView.measuredHeight
                } else statsInfoView.rootView.height
                val marginLp = statsInfoView.rootView.layoutParams as ViewGroup.MarginLayoutParams
                height + marginLp.bottomMargin + marginLp.topMargin
            }

            val statusBarHeight = container.let { DisplayMetricUtils.getStatusBarHeight(it.context) }.orZero()

            toolbarViewTotalHeight + statsInfoTotalHeight + statusBarHeight
        } else 0
    }

    private fun getVideoBottomBoundsOnKeyboardShown(estimatedKeyboardHeight: Int, hasQuickReply: Boolean): Int {
        val sendChatViewTotalHeight = run {
            val height = sendChatView.rootView.height
            val marginLp = sendChatView.rootView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val quickReplyViewTotalHeight = run {
            val height = if (hasQuickReply) {
                if (quickReplyView.rootView.height <= 0) {
                    quickReplyView.rootView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    quickReplyView.rootView.measuredHeight
                } else quickReplyView.rootView.height
            } else 0
            val marginLp = quickReplyView.rootView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val chatListViewTotalHeight = run {
            val height = container.resources.getDimensionPixelSize(R.dimen.play_chat_max_height)
            val marginLp = chatListView.rootView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val statusBarHeight = DisplayMetricUtils.getStatusBarHeight(container.context)
        val requiredMargin = offset16

        val interactionTopmostY = getScreenHeight() - (estimatedKeyboardHeight + sendChatViewTotalHeight + chatListViewTotalHeight + quickReplyViewTotalHeight + statusBarHeight + requiredMargin)

        return interactionTopmostY
    }

    private fun changeLayoutBasedOnVideoOrientation(videoOrientation: VideoOrientation) {
        container.changeConstraint {

            connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
            connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

            if (videoOrientation is VideoOrientation.Horizontal) {
                connect(id, ConstraintSet.TOP, statsInfoView.id, ConstraintSet.BOTTOM, offset16)
                clear(id, ConstraintSet.BOTTOM)
                setDimensionRatio(id, "H, ${videoOrientation.aspectRatio}")
            } else {
                connect(id, ConstraintSet.TOP, statsInfoView.id, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.BOTTOM, pinnedView.id, ConstraintSet.TOP, offset16)
            }
        }

        container.changeConstraint {
            val componentAnchor = if (videoOrientation.isHorizontal) immersiveBoxView.id else ConstraintSet.PARENT_ID

            connect(id, ConstraintSet.START, componentAnchor, ConstraintSet.START)
            connect(id, ConstraintSet.END, componentAnchor, ConstraintSet.END)
            connect(id, ConstraintSet.TOP, componentAnchor, ConstraintSet.TOP)
            connect(id, ConstraintSet.BOTTOM, componentAnchor, ConstraintSet.BOTTOM)
        }
    }

    private fun changeLayoutBasedOnVideoType(videoPlayerUiModel: VideoPlayerUiModel, channelType: PlayChannelType) {
        val bottomMargin = if (videoPlayerUiModel.isYouTube && channelType.isVod) 0 else offset12
        changePinnedBottomMarginGone(bottomMargin)
    }

    private fun changePinnedBottomMarginGone(bottomMargin: Int) {
        val layoutParams = pinnedView.rootView.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.goneBottomMargin = bottomMargin
        pinnedView.rootView.layoutParams = layoutParams
    }
}