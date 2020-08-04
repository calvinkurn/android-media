package com.tokopedia.play.view.fragment

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
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
import com.tokopedia.play.extensions.isFullSolid
import com.tokopedia.play.gesture.PlayClickTouchListener
import com.tokopedia.play.ui.endliveinfo.EndLiveInfoComponent
import com.tokopedia.play.ui.endliveinfo.interaction.EndLiveInfoInteractionEvent
import com.tokopedia.play.ui.gradientbg.GradientBackgroundComponent
import com.tokopedia.play.ui.immersivebox.ImmersiveBoxComponent
import com.tokopedia.play.ui.immersivebox.interaction.ImmersiveBoxInteractionEvent
import com.tokopedia.play.ui.like.LikeComponent
import com.tokopedia.play.ui.like.interaction.LikeInteractionEvent
import com.tokopedia.play.ui.playbutton.PlayButtonComponent
import com.tokopedia.play.ui.playbutton.interaction.PlayButtonInteractionEvent
import com.tokopedia.play.ui.sizecontainer.SizeContainerComponent
import com.tokopedia.play.ui.statsinfo.StatsInfoMiniComponent
import com.tokopedia.play.ui.toolbar.ToolbarMiniComponent
import com.tokopedia.play.ui.toolbar.interaction.PlayToolbarInteractionEvent
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.ui.videocontrol.VideoControlMiniComponent
import com.tokopedia.play.ui.videocontrol.interaction.VideoControlInteractionEvent
import com.tokopedia.play.ui.videosettings.VideoSettingsMiniComponent
import com.tokopedia.play.ui.videosettings.interaction.VideoSettingsInteractionEvent
import com.tokopedia.play.util.PlayFullScreenHelper
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.util.observer.DistinctObserver
import com.tokopedia.play.view.contract.PlayFragmentContract
import com.tokopedia.play.view.contract.PlayNavigation
import com.tokopedia.play.view.contract.PlayOrientationListener
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.layout.interaction.PlayInteractionLayoutManager
import com.tokopedia.play.view.layout.interaction.PlayInteractionViewInitializer
import com.tokopedia.play.view.layout.interaction.miniinteraction.PlayMiniInteractionLayoutManager
import com.tokopedia.play.view.type.PlayRoomEvent
import com.tokopedia.play.view.type.ScreenOrientation
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
import com.tokopedia.play_common.state.PlayVideoState
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 06/05/20
 */
class PlayMiniInteractionFragment : BaseDaggerFragment(), PlayInteractionViewInitializer, PlayFragmentContract {

    companion object {

        private const val INTERACTION_TOUCH_CLICK_TOLERANCE = 25

        private const val REQUEST_CODE_LOGIN = 192

        private const val VISIBLE_ALPHA = 1f
        private const val FADE_DURATION = 200L
        private const val FADE_TRANSITION_DELAY = 3000L

        fun newInstance(channelId: String): PlayMiniInteractionFragment {
            return PlayMiniInteractionFragment().apply {
                val bundle = Bundle()
                bundle.putString(PLAY_KEY_CHANNEL_ID, channelId)
                arguments = bundle
            }
        }
    }

    private val scope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext
            get() = job + dispatchers.main
    }
    private val job: Job = SupervisorJob()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var dispatchers: CoroutineDispatcherProvider

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayInteractionViewModel

    private lateinit var layoutManager: PlayInteractionLayoutManager

    private val container: View
        get() = requireView()

    private val orientationListener: PlayOrientationListener
        get() = requireParentFragment() as PlayOrientationListener

    private val playNavigation: PlayNavigation
        get() = requireActivity() as PlayNavigation

    private var channelId: String = ""

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

    override fun getScreenName(): String = "Play Mini Interaction"

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

        observeVideoPlayer()
        observeLikeContent()
        observeLoggedInInteractionEvent()
        observeTotalLikes()
        observeVideoProperty()
        observeVideoStream()
        observeEventUserInfo()
        observeTitleChannel()
        observeToolbarInfo()
        observeTotalViews()
        observeCartInfo()
    }

    private fun setupView(view: View) {
        container.setOnTouchListener(PlayClickTouchListener(INTERACTION_TOUCH_CLICK_TOLERANCE))
        container.setOnClickListener {
            val event = playViewModel.observableEvent.value
            if (event?.isFreeze == true || event?.isBanned == true) return@setOnClickListener

            if (container.hasAlpha) triggerImmersive(it.isFullSolid)
        }
    }

    private fun setInsets(view: View) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, insets ->
            layoutManager.setupInsets(view, insets)
            insets
        }

        invalidateInsets(view)
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

        triggerFullImmersive(shouldImmersive, true)
    }

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        layoutManager = PlayMiniInteractionLayoutManager(
                container = container,
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
        val toolbarComponent = ToolbarMiniComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)

        scope.launch {
            toolbarComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            PlayToolbarInteractionEvent.BackButtonClicked -> doLeaveRoom()
                            is PlayToolbarInteractionEvent.FollowButtonClicked -> doClickFollow(it.partnerId, it.action)
                            is PlayToolbarInteractionEvent.PartnerNameClicked -> openPartnerPage(it.partnerId, it.type)
                            PlayToolbarInteractionEvent.CartButtonClicked -> shouldOpenCartPage()
                        }
                    }
        }

        return toolbarComponent.getContainerId()
    }

    override fun onInitVideoControl(container: ViewGroup): Int {
        val videoControlComponent = VideoControlMiniComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)

        scope.launch {
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
        val likeComponent = LikeComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)

        scope.launch {
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
        throw IllegalStateException("Component is not created for this fragment")
    }

    override fun onInitChatList(container: ViewGroup): Int {
        throw IllegalStateException("Component is not created for this fragment")
    }

    override fun onInitPinned(container: ViewGroup): Int {
        throw IllegalStateException("Component is not created for this fragment")
    }

    override fun onInitPlayButton(container: ViewGroup): Int {
        val playButtonComponent = PlayButtonComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)

        scope.launch {
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
        val immersiveBoxComponent = ImmersiveBoxComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)

        scope.launch {
            immersiveBoxComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is ImmersiveBoxInteractionEvent.BoxClicked -> {
                                PlayAnalytics.clickWatchArea(
                                        channelId = channelId,
                                        userId = userSession.userId,
                                        channelType = playViewModel.channelType,
                                        screenOrientation = orientation
                                )
                                if (this@PlayMiniInteractionFragment.container.hasAlpha) this@PlayMiniInteractionFragment.container.performClick()
                                else triggerImmersive(it.currentAlpha == VISIBLE_ALPHA)
                            }
                        }
                    }
        }

        return immersiveBoxComponent.getContainerId()
    }

    override fun onInitQuickReply(container: ViewGroup): Int {
        throw IllegalStateException("Component is not created for this fragment")
    }

    override fun onInitGradientBackground(container: ViewGroup): Int {
        return GradientBackgroundComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .getContainerId()
    }

    override fun onInitEndLiveComponent(container: ViewGroup): Int {
        val endLiveInfoComponent = EndLiveInfoComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)

        scope.launch {
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
        return StatsInfoMiniComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)
                .getContainerId()
    }

    override fun onInitVideoSettings(container: ViewGroup): Int {
        val videoSettingsComponent =  VideoSettingsMiniComponent(container, EventBusFactory.get(viewLifecycleOwner), scope, dispatchers)

        scope.launch {
            videoSettingsComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            VideoSettingsInteractionEvent.EnterFullscreenClicked -> {
                                PlayAnalytics.clickCtaFullScreenFromPortraitToLandscape(
                                        userId = userSession.userId,
                                        channelId = channelId,
                                        channelType = playViewModel.channelType
                                )
                                enterFullscreen()
                            }
                            VideoSettingsInteractionEvent.ExitFullscreenClicked -> exitFullscreen()
                        }
                    }
        }

        return videoSettingsComponent.getContainerId()
    }
    //endregion

    override fun onInterceptOrientationChangedEvent(newOrientation: ScreenOrientation): Boolean {
        return false
    }

    override fun onInterceptSystemUiVisibilityChanged(): Boolean {
        return if (playViewModel.isFreezeOrBanned && orientation.isLandscape) {
            systemUiVisibility = PlayFullScreenHelper.getHideNavigationBarVisibility()
            true
        }
        else false
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val orientation = ScreenOrientation.getByInt(newConfig.orientation)
        if (orientation.isLandscape && !playViewModel.isFreezeOrBanned) triggerImmersive(false)
        onInterceptSystemUiVisibilityChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        job.cancelChildren()
    }

    //region observe
    private fun observeVideoPlayer() {
        playViewModel.observableVideoPlayer.observe(viewLifecycleOwner, Observer {
            layoutManager.onVideoPlayerChanged(container, it, playViewModel.channelType)
            scope.launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVideo(it)
                        )
            }
        })
    }

    private fun observeLikeContent() {
        playViewModel.observableLikeState.observe(viewLifecycleOwner, object : Observer<LikeStateUiModel> {
            private var isFirstTime = true
            override fun onChanged(likeModel: LikeStateUiModel) {
                scope.launch {
                    EventBusFactory.get(viewLifecycleOwner)
                            .emit(
                                    ScreenStateEvent::class.java,
                                    ScreenStateEvent.LikeContent(likeModel, isFirstTime)
                            )
                    isFirstTime = false
                }
            }
        })
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(viewLifecycleOwner, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeTotalLikes() {
        playViewModel.observableTotalLikes.observe(viewLifecycleOwner, DistinctObserver(::setTotalLikes))
    }

    private fun observeVideoProperty() {
        playViewModel.observableVideoProperty.observe(viewLifecycleOwner, DistinctObserver {
            if (it.state == PlayVideoState.Playing) {
                PlayAnalytics.clickPlayVideo(channelId, playViewModel.channelType)
            }
            scope.launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.VideoPropertyChanged(it, playViewModel.getStateHelper(orientation))
                        )
            }
        })
    }

    private fun observeVideoStream() {
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, DistinctObserver {
            setVideoStream(it)
        })
    }

    private fun observeEventUserInfo() {
        playViewModel.observableEvent.observe(viewLifecycleOwner, DistinctObserver {
            onInterceptSystemUiVisibilityChanged()
            if (it.isFreeze || it.isBanned) {
                cancelAllAnimations()
                container.alpha = VISIBLE_ALPHA
            }
            scope.launch {
                if (it.isBanned) sendEventBanned(it)
                else if(it.isFreeze) sendEventFreeze(it)
            }
        })
    }

    private fun observeTitleChannel() {
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, DistinctObserver {
            if (it is Success) setChannelTitle(it.data.title)
        })
    }

    private fun observeToolbarInfo() {
        playViewModel.observablePartnerInfo.observe(viewLifecycleOwner, DistinctObserver(::setPartnerInfo))
    }

    private fun observeTotalViews() {
        playViewModel.observableTotalViews.observe(viewLifecycleOwner, DistinctObserver(::setTotalView))
    }

    private fun observeCartInfo() {
        playViewModel.observableBadgeCart.observe(viewLifecycleOwner, DistinctObserver(::setCartInfo))
    }
    //endregion

    private fun enterFullscreen() {
        orientationListener.onOrientationChanged(ScreenOrientation.Landscape, isTilting = false)
    }

    private fun exitFullscreen() {
        orientationListener.onOrientationChanged(ScreenOrientation.Portrait, isTilting = false)
    }

    private fun sendInitState() {
        scope.launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.Init(orientation, playViewModel.getStateHelper(orientation))
            )
        }
    }

    private fun setVideoStream(videoStream: VideoStreamUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.VideoStreamChanged(videoStream, playViewModel.getStateHelper(orientation))
                    )
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

    private fun setTotalLikes(totalLikes: TotalLikeUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetTotalLikes(totalLikes)
                    )
        }
    }

    private fun sendEventBanned(eventUiModel: EventUiModel) {
        scope.launch {
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
        scope.launch {
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

    private fun setChannelTitle(title: String) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetChannelTitle(title)
                    )
        }
    }

    private fun setPartnerInfo(partnerInfo: PartnerInfoUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetPartnerInfo(partnerInfo)
                    )
        }
    }

    private fun setTotalView(totalView: TotalViewUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetTotalViews(totalView)
                    )
        }
    }

    private fun setCartInfo(cartUiModel: CartUiModel) {
        scope.launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetTotalCart(cartUiModel)
                    )
        }
    }

    private fun doClickLike(shouldLike: Boolean) {
        viewModel.doInteractionEvent(InteractionEvent.Like(shouldLike))
    }

    private fun doClickFollow(partnerId: Long, followAction: PartnerFollowAction) {
        viewModel.doInteractionEvent(InteractionEvent.Follow(partnerId, followAction))
    }

    private fun doLeaveRoom() {
        playNavigation.onBackPressed(isSystemBack = false)
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

    private fun shouldOpenCartPage() {
        PlayAnalytics.clickCartIcon(channelId, playViewModel.channelType)
        viewModel.doInteractionEvent(InteractionEvent.CartPage)
    }

    private fun handleLoginInteractionEvent(loginInteractionEvent: LoginStateEvent) {
        when (loginInteractionEvent) {
            is LoginStateEvent.InteractionAllowed -> handleInteractionEvent(loginInteractionEvent.event)
            is LoginStateEvent.NeedLoggedIn -> openLoginPage()
        }
    }

    private fun handleInteractionEvent(event: InteractionEvent) {
        when (event) {
            is InteractionEvent.Like -> doLikeUnlike(event.shouldLike)
        }
    }

    private fun openLoginPage() {
        openPageByApplink(ApplinkConst.LOGIN, requestCode = REQUEST_CODE_LOGIN)
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

    private fun invalidateInsets(view: View) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) view.requestApplyInsets()
            else view.requestFitSystemWindows()
        } catch (e: Exception) {}
    }

    private fun cancelAllAnimations() {
        fadeAnimationList.forEach { it.cancel() }
    }
}