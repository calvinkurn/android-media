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
import com.tokopedia.play.PLAY_KEY_CHANNEL_ID
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.ui.chatlist.ChatListComponent
import com.tokopedia.play.ui.like.LikeComponent
import com.tokopedia.play.ui.like.interaction.LikeInteractionEvent
import com.tokopedia.play.ui.pinned.PinnedComponent
import com.tokopedia.play.ui.pinned.model.PinnedMessage
import com.tokopedia.play.ui.playbutton.PlayButtonComponent
import com.tokopedia.play.ui.playbutton.interaction.PlayButtonInteractionEvent
import com.tokopedia.play.ui.sendchat.SendChatComponent
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent
import com.tokopedia.play.ui.stats.StatsComponent
import com.tokopedia.play.ui.toolbar.ToolbarComponent
import com.tokopedia.play.ui.toolbar.interaction.PlayToolbarInteractionEvent
import com.tokopedia.play.ui.toolbar.model.TitleToolbar
import com.tokopedia.play.ui.videocontrol.VideoControlComponent
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayInteractionFragment : BaseDaggerFragment(), CoroutineScope, PlayMoreActionBottomSheet.Listener {

    companion object {

        private const val INVISIBLE_ALPHA = 0f
        private const val VISIBLE_ALPHA = 1f
        private const val VISIBILITY_ANIMATION_DURATION = 200L

        fun newInstance(channelId: String): PlayInteractionFragment {
            return PlayInteractionFragment().apply {
                arguments?.putString(PLAY_KEY_CHANNEL_ID, channelId)
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

    private lateinit var bottomSheet: PlayMoreActionBottomSheet

    private val channelId: String = arguments?.getString(PLAY_KEY_CHANNEL_ID).orEmpty()

    override fun getScreenName(): String = "Play Interaction"

    override fun initInjector() {
        DaggerPlayComponent.builder()
                .baseAppComponent(
                        (requireContext().applicationContext as BaseMainApplication).baseAppComponent
                )
                .build()
                .inject(this)
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
        playViewModel.observableVideoState.observe(this, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.VideoStateChanged(it)
                        )
            }
        })

        playViewModel.observeGetChannelInfo.observe(viewLifecycleOwner, Observer {
            when(it) {
                 is Success -> {
                     launch {
                         viewModel.getToolbarInfo(it.data.partnerType, it.data.partnerId)
                         setTitle(it.data.title)
                         setTotalView(it.data.totalViews)
                         setPinnedMessage(PinnedMessage(
                                 it.data.pinnedMessage.title,
                                 it.data.pinnedMessage.message,
                                 it.data.pinnedMessage.redirectUrl,
                                 it.data.pinnedMessage.imageUrl))
                         setQuickReply(it.data.quickReply)
                     }
                 }
                is Fail -> {
                    showToast("don't forget to handle when get channel info return error ")
                }
            }
        })

        viewModel.observableToolbarInfo.observe(viewLifecycleOwner, Observer {
            when(it) {
                is Success -> {
                    launch {
                        setToolbarTitle(it.data)
                    }
                }
                is Fail -> {
                    showToast("don't forget to handle when get total likes return error ")
                }
            }
        })

        viewModel.observableTotalLikes.observe(viewLifecycleOwner, Observer {
            when (it) {
                is  Success -> {
                    launch {
                        setTotalLikes(it.data.totalClick)
                    }
                }
                is Fail -> {
                    showToast("don't forget to handle when get total likes return error ")
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet) {
        view?.run(::hideInteraction)
        bottomSheet.dismiss()
    }

    private fun setupView(view: View) {
        view.setOnClickListener { hideInteraction(view) }
    }

    private fun hideInteraction(view: View) {
        view.animate().apply {

            if (view.alpha == INVISIBLE_ALPHA) alpha(VISIBLE_ALPHA)
            else alpha(INVISIBLE_ALPHA)
            duration = VISIBILITY_ANIMATION_DURATION
        }
                .withStartAction { view.isClickable = false }
                .withEndAction { view.isClickable = true }
    }

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        val sendChatComponent: UIComponent<*> = initSendChatComponent(container)
        val likeComponent: UIComponent<*> = initLikeComponent(container)
        val statsComponent: UIComponent<*> = initStatsComponent(container)
        val pinnedComponent: UIComponent<*> = initPinnedComponent(container)
        val chatListComponent: UIComponent<*> = initChatListComponent(container)
        val videoControlComponent: UIComponent<*> = initVideoControlComponent(container)
        val toolbarComponent: UIComponent<*> = initToolbarComponent(container)
        val playButtonComponent: UIComponent<*> = initPlayButtonComponent(container)

        layoutView(
                container = container,
                sendChatComponentId = sendChatComponent.getContainerId(),
                likeComponentId = likeComponent.getContainerId(),
                statsComponentId = statsComponent.getContainerId(),
                pinnedComponentId = pinnedComponent.getContainerId(),
                chatListComponentId = chatListComponent.getContainerId(),
                videoControlComponentId = videoControlComponent.getContainerId(),
                toolbarComponentId = toolbarComponent.getContainerId(),
                playButtonComponentId = playButtonComponent.getContainerId()
        )
    }

    private fun initSendChatComponent(container: ViewGroup): UIComponent<SendChatInteractionEvent> {
        val sendChatComponent = SendChatComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

        launch {
            sendChatComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            SendChatInteractionEvent.FormClicked -> showToast("Chat Form Clicked")
                            SendChatInteractionEvent.SendClicked -> showToast("Chat Send Clicked")
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
                            LikeInteractionEvent.LikeClicked -> showToast("Like Clicked")
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

    private fun initChatListComponent(container: ViewGroup): UIComponent<Unit> {
        return ChatListComponent(container, EventBusFactory.get(viewLifecycleOwner), this)
                .also(viewLifecycleOwner.lifecycle::addObserver)
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
                            PlayToolbarInteractionEvent.FollowButtonClicked -> showToast("Follow Button Clicked")
                            PlayToolbarInteractionEvent.MoreButtonClicked -> showMoreActionBottomSheet()
                            PlayToolbarInteractionEvent.UnFollowButtonClicked -> showToast("UnFollow Button Clicked")
                            PlayToolbarInteractionEvent.MoreButtonClicked -> showToast("More Button Clicked")
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

    private fun layoutView(
            container: ViewGroup,
            @IdRes sendChatComponentId: Int,
            @IdRes likeComponentId: Int,
            @IdRes statsComponentId: Int,
            @IdRes pinnedComponentId: Int,
            @IdRes chatListComponentId: Int,
            @IdRes videoControlComponentId: Int,
            @IdRes toolbarComponentId: Int,
            @IdRes playButtonComponentId: Int
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

        fun layoutChatList(container: ViewGroup, @IdRes id: Int, @IdRes sendChatComponentId: Int, @IdRes likeComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START, resources.getDimensionPixelOffset(R.dimen.dp_8))
                connect(id, ConstraintSet.BOTTOM, sendChatComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_8))
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

        layoutToolbar(container, toolbarComponentId)
        layoutVideoControl(container, videoControlComponentId)
        layoutLike(container, likeComponentId, videoControlComponentId)
        layoutChat(container, sendChatComponentId, likeComponentId)
        layoutChatList(container, chatListComponentId, sendChatComponentId, likeComponentId)
        layoutPinned(container, pinnedComponentId, chatListComponentId, likeComponentId)
        layoutStats(container, statsComponentId, pinnedComponentId)
        layoutPlayButton(container, playButtonComponentId)
    }
    //endregion

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }


    /**
     * Emit data to ui component
     */
    private suspend fun setTitle(title: String) {
        EventBusFactory.get(viewLifecycleOwner)
                .emit(
                        ScreenStateEvent::class.java,
                        ScreenStateEvent.SetTitle(title)
                )
    }

    private suspend fun setToolbarTitle(titleToolbar: TitleToolbar) {
        EventBusFactory.get(viewLifecycleOwner)
                .emit(
                        ScreenStateEvent::class.java,
                        ScreenStateEvent.SetTitleToolbar(titleToolbar)
                )
    }

    private suspend fun setTotalView(totalView: String) {
        EventBusFactory.get(viewLifecycleOwner)
                .emit(
                        ScreenStateEvent::class.java,
                        ScreenStateEvent.SetTotalViews(totalView)
                )
    }


    private suspend fun setTotalLikes(totalLikes: String) {
        EventBusFactory.get(viewLifecycleOwner)
                .emit(
                        ScreenStateEvent::class.java,
                        ScreenStateEvent.SetTotalLikes(totalLikes)
                )
    }

    private suspend fun setQuickReply(quickReply: List<String>) {
        EventBusFactory.get(viewLifecycleOwner)
                .emit(
                        ScreenStateEvent::class.java,
                        ScreenStateEvent.SetQuickReply(quickReply)
                )
    }

    private suspend fun setPinnedMessage(pinnedMessage: PinnedMessage) {
        EventBusFactory.get(viewLifecycleOwner)
                .emit(
                        ScreenStateEvent::class.java,
                        ScreenStateEvent.SetPinned(pinnedMessage)
                )
    }

    private fun showMoreActionBottomSheet() {
        if (!::bottomSheet.isInitialized) {
            bottomSheet = PlayMoreActionBottomSheet.newInstance(requireContext(), this)
        }
        bottomSheet.show(childFragmentManager)
    }
}