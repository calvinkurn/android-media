package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.ui.chatlist.ChatListComponent
import com.tokopedia.play.ui.chatlist.interaction.ChatListInteractionEvent
import com.tokopedia.play.ui.immersivebox.ImmersiveBoxComponent
import com.tokopedia.play.ui.immersivebox.interaction.ImmersiveBoxInteractionEvent
import com.tokopedia.play.ui.like.LikeComponent
import com.tokopedia.play.ui.like.interaction.LikeInteractionEvent
import com.tokopedia.play.ui.pinned.PinnedComponent
import com.tokopedia.play.ui.playbutton.PlayButtonComponent
import com.tokopedia.play.ui.playbutton.interaction.PlayButtonInteractionEvent
import com.tokopedia.play.ui.quickreply.QuickReplyComponent
import com.tokopedia.play.ui.quickreply.interaction.QuickReplyInteractionEvent
import com.tokopedia.play.ui.sendchat.SendChatComponent
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent
import com.tokopedia.play.ui.stats.StatsComponent
import com.tokopedia.play.ui.toolbar.ToolbarComponent
import com.tokopedia.play.ui.toolbar.interaction.PlayToolbarInteractionEvent
import com.tokopedia.play.ui.toolbar.model.PartnerFollowAction
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.ui.videocontrol.VideoControlComponent
import com.tokopedia.play.util.event.EventObserver
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.uimodel.*
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.play.view.wrapper.InteractionEvent
import com.tokopedia.play.view.wrapper.LoginStateEvent
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

        private const val REQUEST_CODE_LOGIN = 55

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
        get() = job + Dispatchers.Main

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var playViewModel: PlayViewModel
    private lateinit var viewModel: PlayInteractionViewModel
    
    private lateinit var sendChatComponent: UIComponent<*>
    private lateinit var likeComponent: UIComponent<*>
    private lateinit var statsComponent: UIComponent<*>
    private lateinit var pinnedComponent: UIComponent<*>
    private lateinit var chatListComponent: UIComponent<*>
    private lateinit var immersiveBoxComponent: UIComponent<*>
    private lateinit var videoControlComponent: UIComponent<*>
    private lateinit var toolbarComponent: UIComponent<*>
    private lateinit var quickReplyComponent: UIComponent<*>
    private lateinit var playButtonComponent: UIComponent<*>

    private lateinit var bottomSheet: PlayMoreActionBottomSheet

    private var interactionHeightOnKeyboardShown = -1

    private var channelId: String = ""

    override fun getScreenName(): String = "Play Interaction"

    override fun initInjector() {
        DaggerPlayComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        channelId  = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        playViewModel = ViewModelProvider(parentFragment!!, viewModelFactory).get(PlayViewModel::class.java)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PlayInteractionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_play_interaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents(view as ViewGroup)
        setupView(view)

        viewModel.getTotalLikes(channelId)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        playViewModel.observableVOD.observe(this, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.SetVideo(it)
                        )
            }
        })
        playViewModel.observableVideoProperty.observe(this, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.VideoPropertyChanged(it)
                        )
            }
        })

        //TODO("propagate this to each of the observable")
        playViewModel.observableGetChannelInfo.observe(viewLifecycleOwner, Observer {
            when(it) {
                 is Success -> {
                     setChannelTitle(it.data.title)
                 }
                is Fail -> {
                    showToast("don't forget to handle when get channel info return error ")
                }
            }
        })

        observeQuickReply()
        observeVideoStream()
        observeToolbarInfo()
        observeTotalLikes()
        observeTotalViews()
        observeChatList()
        observePinnedMessage()
        observeFollowShop()
        observeKeyboardState()

        observeLoggedInInteractionEvent()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet) {
        view?.let { triggerImmersive(it, VISIBLE_ALPHA) }
        bottomSheet.dismiss()
    }

    //region observe
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
        viewModel.observableTotalLikes.observe(viewLifecycleOwner, Observer {
            when (it) {
                is  Success -> {
                    setTotalLikes(it.data.totalClick)
                }
                is Fail -> {
                    showToast("don't forget to handle when get total likes return error ")
                }
            }
        })
    }

    private fun observeTotalViews() {
        playViewModel.observableTotalViews.observe(viewLifecycleOwner, Observer(::setTotalView))
    }

    private fun observeChatList() {
        playViewModel.observableChatList.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.IncomingChat(it)
                        )
            }
        })
    }

    private fun observePinnedMessage() {
        playViewModel.observablePinnedMessage.observe(this, Observer(::setPinnedMessage))
    }

    private fun observeLoggedInInteractionEvent() {
        viewModel.observableLoggedInInteractionEvent.observe(this, EventObserver(::handleLoginInteractionEvent))
    }

    private fun observeFollowShop() {
        viewModel.observableFollowShop.observe(this, Observer {
            if (it is Fail) {
                showToast(it.throwable.message.orEmpty())
            }
        })
    }

    private fun observeKeyboardState() {
        playViewModel.observableKeyboardState.observe(this, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(ScreenStateEvent::class.java, ScreenStateEvent.KeyboardStateChanged(it.isShown))

                if (it.isShown) calculateInteractionHeightOnKeyboardShown()
            }
        })
    }
    //endregion

    private fun setupView(view: View) {
        view.setOnClickListener {
            triggerImmersive(
                    view = view,
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
                .withStartAction { view.isClickable = false }
                .withEndAction { view.isClickable = true }
    }

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        sendChatComponent = initSendChatComponent(container)
        likeComponent = initLikeComponent(container)
        statsComponent = initStatsComponent(container)
        pinnedComponent = initPinnedComponent(container)
        chatListComponent = initChatListComponent(container)
        immersiveBoxComponent = initImmersiveBoxComponent(container)
        videoControlComponent = initVideoControlComponent(container)
        toolbarComponent = initToolbarComponent(container)
        quickReplyComponent = initQuickReplyComponent(container)
        //play button should be on top of other component so it can be clicked
        playButtonComponent = initPlayButtonComponent(container)

        layoutView(
                container = container,
                sendChatComponentId = sendChatComponent.getContainerId(),
                likeComponentId = likeComponent.getContainerId(),
                statsComponentId = statsComponent.getContainerId(),
                pinnedComponentId = pinnedComponent.getContainerId(),
                chatListComponentId = chatListComponent.getContainerId(),
                videoControlComponentId = videoControlComponent.getContainerId(),
                toolbarComponentId = toolbarComponent.getContainerId(),
                playButtonComponentId = playButtonComponent.getContainerId(),
                immersiveBoxComponentId = immersiveBoxComponent.getContainerId(),
                quickReplyComponentId = quickReplyComponent.getContainerId()
        )
    }

    private fun initSendChatComponent(container: ViewGroup): UIComponent<SendChatInteractionEvent> {
        val sendChatComponent = SendChatComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

        launch {
            sendChatComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            SendChatInteractionEvent.FormClicked -> doClickChatBox()
                            is SendChatInteractionEvent.SendClicked -> doSendChat("${it.message} from Pixel 2 API 29")
                        }
                    }
        }

        return sendChatComponent
    }

    private fun initLikeComponent(container: ViewGroup): UIComponent<LikeInteractionEvent> {
        val likeComponent = LikeComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

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

    private fun initStatsComponent(container: ViewGroup): UIComponent<Unit> {
        return StatsComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
    }

    private fun initPinnedComponent(container: ViewGroup): UIComponent<Unit> {
        return PinnedComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
    }

    private fun initChatListComponent(container: ViewGroup): UIComponent<ChatListInteractionEvent> {
        val chatListComponent = ChatListComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
                .also(viewLifecycleOwner.lifecycle::addObserver)

        launch {
            chatListComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is ChatListInteractionEvent.PositionYCalculated -> (requireParentFragment() as PlayFragment).onKeyboardShown(it.yPos)
                        }
                    }
        }

        return chatListComponent
    }

    private fun initVideoControlComponent(container: ViewGroup): UIComponent<Unit> {
        return VideoControlComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
    }

    private fun initToolbarComponent(container: ViewGroup): UIComponent<PlayToolbarInteractionEvent> {
        val toolbarComponent = ToolbarComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

        launch {
            toolbarComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            PlayToolbarInteractionEvent.BackButtonClicked -> activity?.onBackPressed()
                            is PlayToolbarInteractionEvent.FollowButtonClicked -> doActionFollowShop(it.partnerId, it.action)
                            PlayToolbarInteractionEvent.MoreButtonClicked -> showMoreActionBottomSheet()
                            is PlayToolbarInteractionEvent.PartnerNameClicked -> openPartnerPage(it.partnerId, it.type)
                        }
                    }
        }

        return toolbarComponent
    }

    private fun initPlayButtonComponent(container: ViewGroup): UIComponent<PlayButtonInteractionEvent> {
        val playButtonComponent = PlayButtonComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

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
        val immersiveBoxComponent = ImmersiveBoxComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

        launch {
            immersiveBoxComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            ImmersiveBoxInteractionEvent.BoxClicked -> view?.let { fragmentView -> triggerImmersive(fragmentView, VISIBLE_ALPHA) }
                        }
                    }
        }

        return immersiveBoxComponent
    }

    private fun initQuickReplyComponent(container: ViewGroup): UIComponent<QuickReplyInteractionEvent> {
        val quickReplyComponent = QuickReplyComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

        launch {
            quickReplyComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            is QuickReplyInteractionEvent.ReplyClicked -> doSendChat(it.replyString)
                        }
                    }
        }

        return quickReplyComponent
    }
    //endregion

    //region layouting
    private fun layoutView(
            container: ViewGroup,
            @IdRes sendChatComponentId: Int,
            @IdRes likeComponentId: Int,
            @IdRes statsComponentId: Int,
            @IdRes pinnedComponentId: Int,
            @IdRes chatListComponentId: Int,
            @IdRes videoControlComponentId: Int,
            @IdRes toolbarComponentId: Int,
            @IdRes playButtonComponentId: Int,
            @IdRes immersiveBoxComponentId: Int,
            @IdRes quickReplyComponentId: Int
    ) {

        fun layoutChat(container: ViewGroup, @IdRes id: Int, @IdRes likeComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, resources.getDimensionPixelOffset(R.dimen.dp_8))
                connect(id, ConstraintSet.BOTTOM, likeComponentId, ConstraintSet.BOTTOM)
            }

            constraintSet.applyTo(container)
        }

        fun layoutLike(container: ViewGroup, @IdRes id: Int, @IdRes videoControlComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, videoControlComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_8))
            }

            constraintSet.applyTo(container)
        }

        fun layoutChatList(container: ViewGroup, @IdRes id: Int, @IdRes quickReplyComponentId: Int, @IdRes likeComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, resources.getDimensionPixelOffset(R.dimen.dp_8))
                connect(id, ConstraintSet.BOTTOM, quickReplyComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_8))
            }

            constraintSet.applyTo(container)
        }

        fun layoutPinned(container: ViewGroup, @IdRes id: Int, @IdRes chatListComponentId: Int, @IdRes likeComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, resources.getDimensionPixelOffset(R.dimen.dp_8))
                connect(id, ConstraintSet.BOTTOM, chatListComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_8))
            }

            constraintSet.applyTo(container)
        }

        fun layoutStats(container: ViewGroup, @IdRes id: Int, @IdRes pinnedComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.BOTTOM, pinnedComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_8))
            }

            constraintSet.applyTo(container)
        }

        fun layoutVideoControl(container: ViewGroup, @IdRes id: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }

            constraintSet.applyTo(container)
        }

        fun layoutToolbar(container: ViewGroup, @IdRes id: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
            }

            constraintSet.applyTo(container)
        }

        fun layoutPlayButton(container: ViewGroup, @IdRes id: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }

            constraintSet.applyTo(container)
        }

        fun layoutImmersiveBox(container: ViewGroup, @IdRes id: Int, toolbarComponentId: Int, statsComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.TOP, toolbarComponentId, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.BOTTOM, statsComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_16))
            }

            constraintSet.applyTo(container)
        }

        fun layoutQuickReply(container: ViewGroup, @IdRes id: Int, sendChatComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, sendChatComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_8))
            }

            constraintSet.applyTo(container)
        }

        layoutToolbar(container, toolbarComponentId)
        layoutVideoControl(container, videoControlComponentId)
        layoutLike(container, likeComponentId, videoControlComponentId)
        layoutChat(container, sendChatComponentId, likeComponentId)
        layoutChatList(container, chatListComponentId, quickReplyComponentId, likeComponentId)
        layoutPinned(container, pinnedComponentId, chatListComponentId, likeComponentId)
        layoutStats(container, statsComponentId, pinnedComponentId)
        layoutPlayButton(container, playButtonComponentId)
        layoutImmersiveBox(container, immersiveBoxComponentId, toolbarComponentId, statsComponentId)
        layoutQuickReply(container, quickReplyComponentId, sendChatComponentId)
    }
    //endregion

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun doActionFollowShop(partnerId: Long, action: PartnerFollowAction) {
        viewModel.doFollow(partnerId, action)
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

    private fun setTotalView(totalView: TotalViewUiModel) {
       launch {
           EventBusFactory.get(viewLifecycleOwner)
                   .emit(
                           ScreenStateEvent::class.java,
                           ScreenStateEvent.SetTotalViews(totalView)
                   )
       }
    }


    private fun setTotalLikes(totalLikes: String) {
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

    private fun setPinnedMessage(pinnedMessage: PinnedMessageUiModel) {
        launch {
          EventBusFactory.get(viewLifecycleOwner)
                  .emit(
                          ScreenStateEvent::class.java,
                          ScreenStateEvent.SetPinned(pinnedMessage)
                  )
        }
    }

    private fun setVideoStream(videoStream: VideoStreamUiModel) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.VideoStreamChanged(videoStream)
                    )
        }
    }
    //endregion

    private fun showMoreActionBottomSheet() {
        if (!::bottomSheet.isInitialized) {
            bottomSheet = PlayMoreActionBottomSheet.newInstance(requireContext(), this)
        }
        bottomSheet.show(childFragmentManager)
    }

    private fun openPartnerPage(partnerId: Long, partnerType: PartnerType) {
        if (partnerType == PartnerType.SHOP) openShopPage(partnerId)
        else if (partnerType == PartnerType.INFLUENCER) openProfilePage(partnerId)
    }

    private fun openShopPage(partnerId: Long) {
        RouteManager.route(
                requireContext(),
                ApplinkConst.SHOP,
                partnerId.toString()
        )
    }

    private fun openProfilePage(partnerId: Long) {
        RouteManager.route(
                requireContext(),
                ApplinkConst.PROFILE,
                partnerId.toString()
        )
    }

    private fun doClickChatBox() {
        viewModel.doInteractionEvent(InteractionEvent.SendChat)
    }

    private fun doClickLike(shouldLike: Boolean) {
        viewModel.doInteractionEvent(InteractionEvent.Like(shouldLike))
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
            InteractionEvent.SendChat -> sendEventComposeChat()
            is InteractionEvent.Like -> doLikeUnlike(event.shouldLike)
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
        viewModel.doLikeUnlike(shouldLike)
    }

    private fun openLoginPage() {
//        val loginIntent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
//        startActivityForResult(loginIntent, REQUEST_CODE_LOGIN)
        RouteManager.route(context, ApplinkConst.LOGIN)
    }

    private fun calculateInteractionHeightOnKeyboardShown() {
        if (interactionHeightOnKeyboardShown == -1) {
            interactionHeightOnKeyboardShown = requireView().findViewById<View>(statsComponent.getContainerId()).y.toInt()
        }
        (requireParentFragment() as PlayFragment).onKeyboardShown(interactionHeightOnKeyboardShown)
    }
}