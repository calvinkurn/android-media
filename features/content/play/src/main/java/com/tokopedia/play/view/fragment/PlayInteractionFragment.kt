package com.tokopedia.play.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils.getStatusBarHeight
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.getScreenHeight
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.analytic.PlayAnalytics
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.di.PlayModule
import com.tokopedia.play.extensions.isAnyHidden
import com.tokopedia.play.extensions.isAnyShown
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
import com.tokopedia.play.util.CoroutineDispatcherProvider
import com.tokopedia.play.util.PlayFullScreenHelper
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.layout.PlayInteractionLayoutManager
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
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayInteractionFragment : BaseDaggerFragment(), CoroutineScope, PlayMoreActionBottomSheet.Listener {

    companion object {
        private const val REQUEST_CODE_LOGIN = 192

        private const val PERCENT_PRODUCT_SHEET_HEIGHT = 0.6

        private const val INVISIBLE_ALPHA = 0f
        private const val VISIBLE_ALPHA = 1f
        private const val VISIBILITY_ANIMATION_DURATION = 200L

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

    private val offset24 by lazy { resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl5) }
    private val offset16 by lazy { resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl4) }
    private val offset12 by lazy { resources.getDimensionPixelOffset(com.tokopedia.play.R.dimen.play_offset_12) }
    private val offset8 by lazy { resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl3) }
    private val offset4 by lazy { resources.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2) }

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayInteractionViewModel

    private lateinit var sendChatComponent: UIComponent<*>
    private lateinit var likeComponent: UIComponent<*>
    private lateinit var pinnedComponent: UIComponent<*>
    private lateinit var chatListComponent: UIComponent<*>
    private lateinit var immersiveBoxComponent: UIComponent<*>
    private lateinit var videoControlComponent: UIComponent<*>
    private lateinit var gradientBackgroundComponent: UIComponent<*>
    private lateinit var sizeContainerComponent: UIComponent<*>
    private lateinit var toolbarComponent: UIComponent<*>
    private lateinit var statsInfoComponent: UIComponent<*>
    private lateinit var quickReplyComponent: UIComponent<*>
    private lateinit var playButtonComponent: UIComponent<*>
    private lateinit var endLiveInfoComponent: UIComponent<*>

    private lateinit var bottomSheet: PlayMoreActionBottomSheet
    private lateinit var clPlayInteraction: ConstraintLayout

    private val productSheetMaxHeight: Int
        get() = (requireView().height * PERCENT_PRODUCT_SHEET_HEIGHT).toInt()

    private val sendChatView: View
        get() = requireView().findViewById(sendChatComponent.getContainerId())
    private val quickReplyView: View
        get() = requireView().findViewById(quickReplyComponent.getContainerId())
    private val statsInfoView: View
        get() = requireView().findViewById(statsInfoComponent.getContainerId())
    private val chatListView: View
        get() = requireView().findViewById(chatListComponent.getContainerId())

    private var channelId: String = ""

    private val playFragment: PlayFragment
        get() = requireParentFragment() as PlayFragment

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
        initComponents(view.findViewById(R.id.cl_play_interaction) as ViewGroup)
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
        super.onDestroyView()
        job.cancel()
    }

    override fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet) {
        PlayAnalytics.clickWatchMode(channelId, playViewModel.channelType)
        triggerImmersive(clPlayInteraction, VISIBLE_ALPHA)
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
        ViewCompat.setOnApplyWindowInsetsListener(view) { v, insets ->

            val sizeContainerView = view.findViewById<View>(sizeContainerComponent.getContainerId())
            val sizeContainerMarginLp = sizeContainerView.layoutParams as ViewGroup.MarginLayoutParams
            sizeContainerMarginLp.bottomMargin = offset16 + insets.systemWindowInsetBottom
            sizeContainerMarginLp.topMargin = insets.systemWindowInsetTop
            sizeContainerView.layoutParams = sizeContainerMarginLp

            val endLiveInfoView = view.findViewById<View>(endLiveInfoComponent.getContainerId())
            endLiveInfoView.setPadding(endLiveInfoView.paddingLeft, endLiveInfoView.paddingTop, endLiveInfoView.paddingRight, offset24 + insets.systemWindowInsetBottom)

            insets
        }

        invalidateInsets(view)
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
        playViewModel.observableVideoStream.observe(viewLifecycleOwner, Observer(::setVideoStream))
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
            requireView().gone()

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

                if (keyboardState?.isHidden == true) delay(PlayFragment.ANIMATION_DURATION)
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.BottomInsetsChanged(it, it.isAnyShown, it.isAnyHidden, playViewModel.stateHelper)
                        )

            }.apply {
                invokeOnCompletion { requireView().visible() }
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
        clPlayInteraction = view.findViewById(R.id.cl_play_interaction)

        clPlayInteraction.setOnClickListener {
            triggerImmersive(
                    view = clPlayInteraction,
                    whenAlpha = INVISIBLE_ALPHA
            )
        }
    }

    private fun triggerImmersive(view: View, whenAlpha: Float) {
        view.animate().apply {
            val finalAlpha = if (whenAlpha == VISIBLE_ALPHA) INVISIBLE_ALPHA else VISIBLE_ALPHA

            if (view.alpha == whenAlpha) alpha(finalAlpha)
            duration = VISIBILITY_ANIMATION_DURATION
        }
                .withStartAction {
                    view.isClickable = false
                    if (whenAlpha == VISIBLE_ALPHA) hideSystemUI()
                    else showSystemUI()
                }
                .withEndAction {
                    view.isClickable = true
                }
    }

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        sizeContainerComponent = initSizeContainerComponent(container)
        gradientBackgroundComponent = initGradientBackgroundComponent(container)
        sendChatComponent = initSendChatComponent(container)
        likeComponent = initLikeComponent(container)
        pinnedComponent = initPinnedComponent(container)
        chatListComponent = initChatListComponent(container)
        immersiveBoxComponent = initImmersiveBoxComponent(container)
        videoControlComponent = initVideoControlComponent(container)
        endLiveInfoComponent = initEndLiveInfoComponent(container)
        toolbarComponent = initToolbarComponent(container)
        statsInfoComponent = initStatsInfoComponent(container)
        quickReplyComponent = initQuickReplyComponent(container)
        //play button should be on top of other component so it can be clicked
        playButtonComponent = initPlayButtonComponent(container)

        sendInitState()

        PlayInteractionLayoutManager(container).layoutView(
                sizeContainerComponentId = sizeContainerComponent.getContainerId(),
                sendChatComponentId = sendChatComponent.getContainerId(),
                likeComponentId = likeComponent.getContainerId(),
                pinnedComponentId = pinnedComponent.getContainerId(),
                chatListComponentId = chatListComponent.getContainerId(),
                videoControlComponentId = videoControlComponent.getContainerId(),
                gradientBackgroundComponentId = gradientBackgroundComponent.getContainerId(),
                toolbarComponentId = toolbarComponent.getContainerId(),
                statsInfoComponentId = statsInfoComponent.getContainerId(),
                playButtonComponentId = playButtonComponent.getContainerId(),
                immersiveBoxComponentId = immersiveBoxComponent.getContainerId(),
                quickReplyComponentId = quickReplyComponent.getContainerId(),
                endLiveInfoComponentId = endLiveInfoComponent.getContainerId()
        )
    }

    private fun initSendChatComponent(container: ViewGroup): UIComponent<SendChatInteractionEvent> {
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

        return sendChatComponent
    }

    private fun initLikeComponent(container: ViewGroup): UIComponent<LikeInteractionEvent> {
        val likeComponent = LikeComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            likeComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is LikeInteractionEvent.LikeClicked -> doClickLike(it.shouldLike)
                        }
                    }
        }

        return likeComponent
    }

    private fun initPinnedComponent(container: ViewGroup): UIComponent<PinnedInteractionEvent> {
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

        return pinnedComponent
    }

    private fun initChatListComponent(container: ViewGroup): UIComponent<Unit> {
        return ChatListComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)
    }

    private fun initVideoControlComponent(container: ViewGroup): UIComponent<Unit> {
        return VideoControlComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
                .also(viewLifecycleOwner.lifecycle::addObserver)
    }

    private fun initSizeContainerComponent(container: ViewGroup): UIComponent<Unit> {
        return SizeContainerComponent(container)
    }

    private fun initGradientBackgroundComponent(container: ViewGroup): UIComponent<Unit> {
        return GradientBackgroundComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
    }

    private fun initToolbarComponent(container: ViewGroup): UIComponent<PlayToolbarInteractionEvent> {
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

        return toolbarComponent
    }

    private fun initStatsInfoComponent(container: ViewGroup): UIComponent<Unit> {
        return StatsInfoComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)
    }

    private fun initPlayButtonComponent(container: ViewGroup): UIComponent<PlayButtonInteractionEvent> {
        val playButtonComponent = PlayButtonComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            playButtonComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            PlayButtonInteractionEvent.PlayClicked -> playViewModel.startCurrentVideo()
                        }
                    }
        }

        return playButtonComponent
    }

    private fun initImmersiveBoxComponent(container: ViewGroup): UIComponent<ImmersiveBoxInteractionEvent> {
        val immersiveBoxComponent = ImmersiveBoxComponent(container, EventBusFactory.get(viewLifecycleOwner), this, dispatchers)

        launch {
            immersiveBoxComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            ImmersiveBoxInteractionEvent.BoxClicked -> {
                                PlayAnalytics.clickWatchArea(channelId, playViewModel.channelType)
                                triggerImmersive(clPlayInteraction, VISIBLE_ALPHA)
                            }
                        }
                    }
        }

        return immersiveBoxComponent
    }

    private fun initQuickReplyComponent(container: ViewGroup): UIComponent<QuickReplyInteractionEvent> {
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

        return quickReplyComponent
    }

    private fun initEndLiveInfoComponent(container: ViewGroup): UIComponent<EndLiveInfoInteractionEvent> {
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

        return endLiveInfoComponent
    }
    //endregion

    private fun sendInitState() {
        launch(dispatchers.immediate) {
            EventBusFactory.get(viewLifecycleOwner).emit(
                    ScreenStateEvent::class.java,
                    ScreenStateEvent.Init
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

    private fun showMoreActionBottomSheet() {
        getBottomSheetInstance().show(childFragmentManager)
    }

    private fun openPartnerPage(partnerId: Long, partnerType: PartnerType) {
        if (partnerType == PartnerType.SHOP) openShopPage(partnerId)
        else if (partnerType == PartnerType.INFLUENCER) openProfilePage(partnerId)
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

    private fun hideSystemUI() {
        PlayFullScreenHelper.hideSystemUi(requireActivity())
    }

    private fun showSystemUI() {
        PlayFullScreenHelper.showSystemUi(requireActivity())
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
        val sendChatViewTotalHeight = run {
            val height = sendChatView.height
            val marginLp = sendChatView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val quickReplyViewTotalHeight = run {
            val height = if (!playViewModel.observableQuickReply.value?.quickReplyList.isNullOrEmpty()) {
                if (quickReplyView.height <= 0) {
                    quickReplyView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
                    quickReplyView.measuredHeight
                } else quickReplyView.height
            } else 0
            val marginLp = quickReplyView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val chatListViewTotalHeight = run {
            val height = resources.getDimensionPixelSize(R.dimen.play_chat_max_height)
            val marginLp = chatListView.layoutParams as ViewGroup.MarginLayoutParams
            height + marginLp.bottomMargin + marginLp.topMargin
        }

        val statusBarHeight = view?.let { getStatusBarHeight(it.context) }.orZero()
        val requiredMargin = offset16

        val interactionTopmostY = getScreenHeight() - (estimatedKeyboardHeight + sendChatViewTotalHeight + chatListViewTotalHeight + quickReplyViewTotalHeight + statusBarHeight + requiredMargin)

        playFragment.onBottomInsetsViewShown(interactionTopmostY)
    }
}