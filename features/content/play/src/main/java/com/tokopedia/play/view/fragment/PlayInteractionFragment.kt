package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.animation.PlayDelayFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeInAnimation
import com.tokopedia.play.animation.PlayFadeInFadeOutAnimation
import com.tokopedia.play.animation.PlayFadeOutAnimation
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.extensions.hasAlpha
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.gesture.PlayClickTouchListener
import com.tokopedia.play.ui.chatlist.ChatListComponent
import com.tokopedia.play.ui.endliveinfo.EndLiveInfoComponent
import com.tokopedia.play.ui.endliveinfo.interaction.EndLiveInfoInteractionEvent
import com.tokopedia.play.ui.gradientbg.GradientBackgroundComponent
import com.tokopedia.play.ui.immersivebox.ImmersiveBoxComponent
import com.tokopedia.play.ui.immersivebox.interaction.ImmersiveBoxInteractionEvent
import com.tokopedia.play.ui.like.LikeComponent
import com.tokopedia.play.ui.like.interaction.LikeInteractionEvent
import com.tokopedia.play.ui.pinned.PinnedComponent
import com.tokopedia.play.ui.pinned.interaction.PinnedInteractionEvent
import com.tokopedia.play.ui.playbutton.PlayButtonComponent
import com.tokopedia.play.ui.playbutton.interaction.PlayButtonInteractionEvent
import com.tokopedia.play.ui.quickreply.QuickReplyComponent
import com.tokopedia.play.ui.quickreply.interaction.QuickReplyInteractionEvent
import com.tokopedia.play.ui.sendchat.SendChatComponent
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent
import com.tokopedia.play.ui.sizecontainer.SizeContainerComponent
import com.tokopedia.play.ui.statsinfo.StatsInfoComponent
import com.tokopedia.play.ui.toolbar.ToolbarComponent
import com.tokopedia.play.ui.toolbar.interaction.PlayToolbarInteractionEvent
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.ui.videocontrol.VideoControlComponent
import com.tokopedia.play.ui.videocontrol.interaction.VideoControlInteractionEvent
import com.tokopedia.play.ui.videosettings.VideoSettingsComponent
import com.tokopedia.play.ui.videosettings.interaction.VideoSettingsInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.layout.interaction.PlayInteractionLayoutManager
import com.tokopedia.play.view.layout.interaction.PlayInteractionLayoutManagerImpl
import com.tokopedia.play.view.layout.interaction.PlayInteractionViewInitializer
import com.tokopedia.play.view.layout.parent.PlayParentLayoutManagerImpl
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.trackingoptimizer.TrackingQueue
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayInteractionFragment :
        BaseDaggerFragment(),
        PlayInteractionViewInitializer,
        CoroutineScope,
        PlayMoreActionBottomSheet.Listener {

    companion object {
        private const val INTERACTION_TOUCH_CLICK_TOLERANCE = 25

        private const val REQUEST_CODE_LOGIN = 192

        private const val PERCENT_PRODUCT_SHEET_HEIGHT = 0.6

        private const val INVISIBLE_ALPHA = 0f
        private const val VISIBLE_ALPHA = 1f

        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L

        fun newInstance(channelId: String): PlayInteractionFragment {
            return PlayInteractionFragment().apply {
                val bundle = Bundle()
                bundle.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = bundle
            }
        }
    }

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + dispatchers.main

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    @Inject
    lateinit var trackingQueue: TrackingQueue

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayInteractionViewModel

    private lateinit var layoutManager: PlayInteractionLayoutManager

    private lateinit var bottomSheet: PlayMoreActionBottomSheet

    private val container: View
        get() = requireView()

    private val productSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_PRODUCT_SHEET_HEIGHT).toInt()

    private var channelId: String = ""

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

    private var systemUiVisibility: Int
        get() = requireActivity().window.decorView.systemUiVisibility
        set(value) {
            requireActivity().window.decorView.systemUiVisibility = value
        }

    private var requestedOrientation: Int
        get() = requireActivity().requestedOrientation
        set(value) {
            requireActivity().requestedOrientation = value
        }

    /**
     * Animation
     */
    private val fadeInAnimation = PlayFadeInAnimation(FADE_DURATION)
    private val fadeOutAnimation = PlayFadeOutAnimation(FADE_DURATION)
    private val fadeInFadeOutAnimation = PlayFadeInFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY)
    private val delayFadeOutAnimation = PlayDelayFadeOutAnimation(FADE_DURATION, FADE_TRANSITION_DELAY)
    private val fadeAnimationList = arrayOf(fadeInAnimation, fadeOutAnimation, fadeInFadeOutAnimation, delayFadeOutAnimation)

    override fun getScreenName(): String = "Play Interaction"

    override fun initInjector() {
        DaggerPlayComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .playModule(PlayModule(requireContext()))
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playViewModel = ViewModelProvider(requireParentFragment(), viewModelFactory).get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayInteractionViewModel::class.java)
        channelId  = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_interaction, container, false)
        initComponents(view as ViewGroup)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
        setInsets(view)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeVOD()
        observeVideoProperty()
        observeTitleChannel()
        observeQuickReply()
        observeVideoStream()
        observeToolbarInfo()
        observeTotalLikes()
        observeTotalViews()
        observeChatList()
        observePinned()
        observeCartInfo()
        observeFollowShop()
        observeLikeContent()
        observeBottomInsetsState()
        observeEventUserInfo()

        observeLoggedInInteractionEvent()
    }

    override fun onPause() {
        super.onPause()
        trackingQueue.sendAll()
    }

    override fun onDestroyView() {
        destroyInsets(requireView())
        super.onDestroyView()
        layoutManager.onDestroy()
        job.cancel()
    }

    override fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet) {
        PlayAnalytics.clickWatchMode(channelId, playViewModel.channelType)
        triggerImmersive(container.alpha == VISIBLE_ALPHA)
        bottomSheet.dismiss()
    }

    override fun onNoAction(bottomSheet: PlayMoreActionBottomSheet) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(ScreenStateEvent::class.java, ScreenStateEvent.OnNoMoreAction)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_LOGIN && resultCode == Activity.RESULT_OK) {
            val lastAction = viewModel.observableLoggedInInteractionEvent.value?.peekContent()
            if (lastAction != null) handleInteractionEvent(lastAction.event)
        } else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun setInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            layoutManager.setupInsets(view, insets)
            insets
        }

        invalidateInsets(view)
    }

    private fun destroyInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view, null)
    }

    //region observe
    private fun observeVOD() {
        playViewModel.observableVOD.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVideo(it)
                        )
            }
        })
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, Observer {
            if (it.state == PlayVideoState.Playing) PlayAnalytics.clickPlayVideo(channelId, playViewModel.channelType)
            if (it.state == PlayVideoState.Ended) showInteractionIfWatchMode()
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.VideoPropertyChanged(it, playViewModel.stateHelper)
                        )
            }
        })
    }

    private fun observeTitleChannel() {
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    setChannelTitle(it.data.title)
                }
            }
        })
    }

    private fun observeQuickReply() {
        playViewModel.observableQuickReply.observe(viewLifecycleOwner, Observer(::setQuickReply))
    }

    private fun observeVideoStream() {
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, Observer {
            layoutManager.onVideoOrientationChanged(container, it.orientation)
            triggerImmersive(false)
            playFragment.setVideoTopBounds(it.orientation, layoutManager.getVideoTopBounds(container, it.orientation))

            setVideoStream(it)
        })
    }

    private fun observeToolbarInfo() {
        playViewModel.observablePartnerInfo.observe(viewLifecycleOwner, Observer(::setPartnerInfo))
    }

    private fun observeTotalLikes() {
        playViewModel.observableTotalLikes.observe(viewLifecycleOwner, Observer(::setTotalLikes))
    }

    private fun observeTotalViews() {
        playViewModel.observableTotalViews.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeChatList() {
        playViewModel.observableNewChat.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.IncomingChat(it)
                        )
            }
        })
    }

    private fun observePinned() {
        playViewModel.observablePinned.observe(viewLifecycleOwner, Observer(::setPinned))
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeFollowShop() {
        viewModel.observableFollowPartner.observe(viewLifecycleOwner, Observer {
            if (it is Fail) {
                showToast(it.throwable.message.orEmpty())
            }
        })
    }

    private fun observeLikeContent() {
        playViewModel.observableIsLikeContent.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.LikeContent(it, false)
                        )
            }
        })
    }

    private fun observeBottomInsetsState() {
        playViewModel.observableBottomInsetsState.observe(viewLifecycleOwner, Observer {
            if (it.isAnyShown) requireView().gone()

            launch {
                val keyboardState = it[BottomInsetsType.Keyboard]
                if (keyboardState != null && !keyboardState.isPreviousStateSame) {
                    when (keyboardState) {
                        is BottomInsetsState.Hidden -> if (!it.isAnyShown) playFragment.onBottomInsetsViewHidden()
                        is BottomInsetsState.Shown -> {
                            pushParentPlayByKeyboardHeight(keyboardState.estimatedInsetsHeight)
                        }
                    }
                }

                if (keyboardState?.isHidden == true) delay(PlayParentLayoutManagerImpl.ANIMATION_DURATION)
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.BottomInsetsChanged(it, it.isAnyShown, it.isAnyHidden, playViewModel.stateHelper)
                        )

            }.apply {
                invokeOnCompletion {
                    view?.visible()
                }
            }
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, Observer {
            launch {
                getBottomSheetInstance().setState(it.isFreeze)

                if (it.isBanned) sendEventBanned(it)
                else if(it.isFreeze) {
                    sendEventFreeze(it)
                    hideBottomSheet()
                }
            }
        })
    }

    private fun observeCartInfo() {
        playViewModel.observableBadgeCart.observe(viewLifecycleOwner, Observer(::setCartInfo))
    }
    //endregion

    private fun setupView(view: View) {
        container.setOnTouchListener(PlayClickTouchListener(INTERACTION_TOUCH_CLICK_TOLERANCE))
        container.setOnClickListener {
            if (
                    (playViewModel.screenOrientation.isLandscape && container.hasAlpha) ||
                    (!playViewModel.screenOrientation.isLandscape && !playViewModel.videoOrientation.isHorizontal && container.hasAlpha)
            ) triggerImmersive(it.alpha == VISIBLE_ALPHA)
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
            playViewModel.screenOrientation.isLandscape -> triggerFullImmersive(shouldImmersive, true)
            playViewModel.videoOrientation.isHorizontal -> sendImmersiveEvent(shouldImmersive)
            else -> {
                systemUiVisibility = if (shouldImmersive) layoutManager.onEnterImmersive() else layoutManager.onExitImmersive()
                triggerFullImmersive(shouldImmersive, false)
            }
        }
    }

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        layoutManager = PlayInteractionLayoutManagerImpl(
                container = container,
                orientation = playViewModel.screenOrientation,
                videoOrientation = playViewModel.videoOrientation,
                viewInitializer = this
        )

        sendInitState()

        layoutManager.layoutView(container)
    }

    override fun onInitSizeContainer(container: ViewGroup): Int {
        return SizeContainerComponent(container)
                .getContainerId()
    }

    override fun onInitToolbar(container: ViewGroup): Int {
        val toolbarComponent = ToolbarComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            toolbarComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            PlayToolbarInteractionEvent.BackButtonClicked -> doLeaveRoom()
                            is PlayToolbarInteractionEvent.FollowButtonClicked -> doClickFollow(it.partnerId, it.action)
                            PlayToolbarInteractionEvent.MoreButtonClicked -> showMoreActionBottomSheet()
                            is PlayToolbarInteractionEvent.PartnerNameClicked -> openPartnerPage(it.partnerId, it.type)
                            PlayToolbarInteractionEvent.CartButtonClicked -> shouldOpenCartPage()
                        }
                    }
        }

        return toolbarComponent.getContainerId()
    }

    override fun onInitVideoControl(container: ViewGroup): Int {
        val videoControlComponent = VideoControlComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)

        launch {
            videoControlComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            VideoControlInteractionEvent.VideoScrubStarted -> onScrubStarted()
                            VideoControlInteractionEvent.VideoScrubEnded -> onScrubEnded()
                        }
                    }
        }

        return videoControlComponent.getContainerId()
    }

    override fun onInitLike(container: ViewGroup): Int {
        val likeComponent = LikeComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            likeComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is LikeInteractionEvent.LikeClicked -> doClickLike(it.shouldLike)
                        }
                    }
        }

        return likeComponent.getContainerId()
    }

    override fun onInitChat(container: ViewGroup): Int {
        val sendChatComponent = SendChatComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)

        launch {
            sendChatComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            SendChatInteractionEvent.FormClicked -> doClickChatBox()
                            is SendChatInteractionEvent.SendClicked -> {
                                PlayAnalytics.clickSendChat(channelId)
                                doSendChat(it.message)
                            }
                        }
                    }
        }

        return sendChatComponent.getContainerId()
    }

    override fun onInitChatList(container: ViewGroup): Int {
        return ChatListComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)
                .getContainerId()
    }

    override fun onInitPinned(container: ViewGroup): Int {
        val pinnedComponent = PinnedComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)

        launch {
            pinnedComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is PinnedInteractionEvent.PinnedMessageClicked -> {
                                PlayAnalytics.clickPinnedMessage(channelId, it.message, it.applink, playViewModel.channelType)
                                openPageByApplink(it.applink)
                            }
                            PinnedInteractionEvent.PinnedProductClicked -> doClickPinnedProduct()
                        }
                    }
        }

        return pinnedComponent.getContainerId()
    }

    override fun onInitPlayButton(container: ViewGroup): Int {
        val playButtonComponent = PlayButtonComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            playButtonComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            PlayButtonInteractionEvent.PlayClicked -> playViewModel.startCurrentVideo()
                        }
                    }
        }

        return playButtonComponent.getContainerId()
    }

    override fun onInitImmersiveBox(container: ViewGroup): Int {
        val immersiveBoxComponent = ImmersiveBoxComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            immersiveBoxComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is ImmersiveBoxInteractionEvent.BoxClicked -> {
                                PlayAnalytics.clickWatchArea(
                                        channelId = channelId,
                                        userId = userSession.userId,
                                        channelType = playViewModel.channelType,
                                        screenOrientation = playViewModel.screenOrientation
                                )
                                if (playViewModel.screenOrientation.isLandscape && this@PlayInteractionFragment.container.hasAlpha) this@PlayInteractionFragment.container.performClick()
                                else triggerImmersive(it.currentAlpha == VISIBLE_ALPHA)
                            }
                        }
                    }
        }

        return immersiveBoxComponent.getContainerId()
    }

    override fun onInitQuickReply(container: ViewGroup): Int {
        val quickReplyComponent = QuickReplyComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            quickReplyComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is QuickReplyInteractionEvent.ReplyClicked -> {
                                PlayAnalytics.clickQuickReply(channelId)
                                doSendChat(it.replyString)
                            }
                        }
                    }
        }

        return quickReplyComponent.getContainerId()
    }

    override fun onInitGradientBackground(container: ViewGroup): Int {
        return GradientBackgroundComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .getContainerId()
    }

    override fun onInitEndLiveComponent(container: ViewGroup): Int {
        val endLiveInfoComponent = EndLiveInfoComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            endLiveInfoComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is EndLiveInfoInteractionEvent.ButtonActionClicked -> {
                                openPageByApplink(
                                        applink = it.buttonUrl,
                                        shouldFinish = true
                                )
                            }
                        }
                    }
        }

        return endLiveInfoComponent.getContainerId()
    }

    override fun onInitStatsInfo(container: ViewGroup): Int {
        return StatsInfoComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .getContainerId()
    }

    override fun onInitVideoSettings(container: ViewGroup): Int {
        val videoSettingsComponent =  VideoSettingsComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            videoSettingsComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            VideoSettingsInteractionEvent.EnterFullScreenClicked -> {
                                PlayAnalytics.clickCtaFullScreenFromPortraitToLandscape(
                                        userId = userSession.userId,
                                        channelId = channelId,
                                        channelType = playViewModel.channelType
                                )
                                enterFullscreen()
                            }
                            VideoSettingsInteractionEvent.ExitFullScreenClicked -> exitFullscreen()
                        }
                    }
        }

        return videoSettingsComponent.getContainerId()
    }
    //endregion

    private fun sendInitState() {
        launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.Init(playViewModel.screenOrientation, playViewModel.stateHelper)
            )
        }
    }

    //region set data
    /**
     * Emit data to ui component
     */
    private fun setChannelTitle(title: String) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetChannelTitle(title)
                    )
        }
    }

    private fun setPartnerInfo(partnerInfo: PartnerInfoUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetPartnerInfo(partnerInfo)
                    )
        }
    }

    private fun setCartInfo(cartUiModel: CartUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetTotalCart(cartUiModel)
                    )
        }
    }

    private fun setTotalView(totalView: TotalViewUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetTotalViews(totalView)
                    )
        }
    }


    private fun setTotalLikes(totalLikes: TotalLikeUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetTotalLikes(totalLikes)
                    )
        }
    }

    private fun setQuickReply(quickReply: QuickReplyUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetQuickReply(quickReply)
                    )
        }
    }

    private fun setPinned(pinnedMessage: PinnedUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetPinned(pinnedMessage, playViewModel.stateHelper)
                    )
        }
    }

    private fun setVideoStream(videoStream: VideoStreamUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.VideoStreamChanged(videoStream, playViewModel.stateHelper)
                    )
        }
    }
    //endregion

    private fun onScrubStarted() {
        if (!playViewModel.screenOrientation.isLandscape) return

        cancelAllAnimations()
        fadeInAnimation.start(container)
    }

    private fun onScrubEnded() {
        if (!playViewModel.screenOrientation.isLandscape) return

        cancelAllAnimations()
        delayFadeOutAnimation.start(container)
    }

    private fun enterFullscreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    }

    private fun exitFullscreen() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun doLeaveRoom() {
        PlayAnalytics.clickLeaveRoom(channelId, playViewModel.getDurationCurrentVideo(), playViewModel.channelType)
        activity?.onBackPressed()
    }

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun doActionFollowPartner(partnerId: Long, action: PartnerFollowAction) {
        PlayAnalytics.clickFollowShop(channelId, partnerId.toString(), action.value, playViewModel.channelType)
        viewModel.doFollow(partnerId, action)

        sendEventFollowPartner(action == PartnerFollowAction.Follow)
    }

    private fun sendEventFollowPartner(shouldFollow: Boolean) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.FollowPartner(shouldFollow)
                    )
        }
    }

    private fun sendImmersiveEvent(shouldImmersive: Boolean) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.ImmersiveStateChanged(shouldImmersive)
                    )
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
            InteractionEvent.SendChat -> sendEventComposeChat()
            InteractionEvent.ClickPinnedProduct -> openProductSheet()
            is InteractionEvent.Like -> doLikeUnlike(event.shouldLike)
            is InteractionEvent.Follow -> doActionFollowPartner(event.partnerId, event.partnerAction)
        }
    }

    private fun sendEventComposeChat() {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.ComposeChat
                    )
        }
    }

    private fun doLikeUnlike(shouldLike: Boolean) {
        //Used to show mock like when user click like
        playViewModel.changeLikeCount(shouldLike)

        viewModel.doLikeUnlike(playViewModel.contentId,
                playViewModel.contentType,
                playViewModel.likeType,
                shouldLike)

        sendEventLikeContent(shouldLike)
        PlayAnalytics.clickLike(channelId, shouldLike, playViewModel.channelType)
    }

    private fun sendEventLikeContent(shouldLike: Boolean) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.LikeContent(shouldLike, true)
                    )
        }
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

    private fun sendEventBanned(eventUiModel: EventUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.OnNewPlayRoomEvent(
                                    PlayRoomEvent.Banned(
                                            title = eventUiModel.bannedTitle,
                                            message = eventUiModel.bannedMessage,
                                            btnTitle = eventUiModel.bannedButtonTitle
                                    )
                            )
                    )
        }
    }

    private fun sendEventFreeze(eventUiModel: EventUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.OnNewPlayRoomEvent(
                                    PlayRoomEvent.Freeze(
                                            title = eventUiModel.freezeTitle,
                                            message = eventUiModel.freezeMessage,
                                            btnTitle = eventUiModel.freezeButtonTitle,
                                            btnUrl = eventUiModel.freezeButtonUrl
                                    )
                            )
                    )
        }
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
        playFragment.onBottomInsetsViewShown(layoutManager.getVideoBottomBoundsOnKeyboardShown(container, estimatedKeyboardHeight, hasQuickReply))
    }

    private fun cancelAllAnimations() {
        fadeAnimationList.forEach { it.cancel() }
    }
}