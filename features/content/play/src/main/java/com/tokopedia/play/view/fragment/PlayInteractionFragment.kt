package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.ui.chatlist.ChatListComponent
import com.tokopedia.play.ui.immersivebox.ImmersiveBoxComponent
import com.tokopedia.play.ui.immersivebox.interaction.ImmersiveBoxInteractionEvent
import com.tokopedia.play.ui.like.LikeComponent
import com.tokopedia.play.ui.like.interaction.LikeInteractionEvent
import com.tokopedia.play.ui.pinned.PinnedComponent
import com.tokopedia.play.ui.pinned.interaction.PinnedInteractionEvent
import com.tokopedia.play.ui.playbutton.PlayButtonComponent
import com.tokopedia.play.ui.playbutton.interaction.PlayButtonInteractionEvent
import com.tokopedia.play.ui.sendchat.SendChatComponent
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent
import com.tokopedia.play.ui.stats.StatsComponent
import com.tokopedia.play.ui.toolbar.ToolbarComponent
import com.tokopedia.play.ui.toolbar.interaction.PlayToolbarInteractionEvent
import com.tokopedia.play.ui.videocontrol.VideoControlComponent
import com.tokopedia.play.view.bottomsheet.PlayMoreActionBottomSheet
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import com.tokopedia.play.view.viewmodel.PlayViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
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

        fun newInstance(): PlayInteractionFragment {
            return PlayInteractionFragment()
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
        setPinnedMessage("Yoenik Apparel", "Visit my collections here!")
        viewModel.startObservingChatList()
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
        viewModel.observableChatList.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.IncomingChat(it)
                        )
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    override fun onWatchModeClicked(bottomSheet: PlayMoreActionBottomSheet) {
        view?.let { triggerImmersive(it, VISIBLE_ALPHA) }
        bottomSheet.dismiss()
    }

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
        val sendChatComponent: UIComponent<*> = initSendChatComponent(container)
        val likeComponent: UIComponent<*> = initLikeComponent(container)
        val statsComponent: UIComponent<*> = initStatsComponent(container)
        val pinnedComponent: UIComponent<*> = initPinnedComponent(container)
        val chatListComponent: UIComponent<*> = initChatListComponent(container)
        val immersiveBoxComponent: UIComponent<*> = initImmersiveBoxComponent(container)
        val videoControlComponent: UIComponent<*> = initVideoControlComponent(container)
        val toolbarComponent: UIComponent<*> = initToolbarComponent(container)
        //play button should be on top of other component so it can be clicked
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
                playButtonComponentId = playButtonComponent.getContainerId(),
                immersiveBoxComponentId = immersiveBoxComponent.getContainerId()
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
        return StatsComponent(container)
    }

    private fun initPinnedComponent(container: ViewGroup): UIComponent<PinnedInteractionEvent> {
        val pinnedComponent = PinnedComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

        launch {
            pinnedComponent.getUserInteractionEvents()
                    .collect {
                        when (it) {
                            PinnedInteractionEvent.ActionClicked -> showToast("Action Pinned Clicked")
                        }
                    }
        }

        return pinnedComponent
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
            @IdRes immersiveBoxComponentId: Int
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

        fun layoutImmersiveBox(container: ViewGroup, @IdRes id: Int, toolbarComponentId: Int, statsComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.TOP, toolbarComponentId, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.BOTTOM, statsComponentId, ConstraintSet.TOP)
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
        layoutImmersiveBox(container, immersiveBoxComponentId, toolbarComponentId, statsComponentId)
    }
    //endregion

    private fun showToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    private fun setPinnedMessage(author: String, message: String) {
        launch {
            EventBusFactory.get(viewLifecycleOwner)
                    .emit(
                            ScreenStateEvent::class.java,
                            ScreenStateEvent.SetPinned(author, message)
                    )
        }
    }

    private fun showMoreActionBottomSheet() {
        if (!::bottomSheet.isInitialized) {
            bottomSheet = PlayMoreActionBottomSheet.newInstance(requireContext(), this)
        }
        bottomSheet.show(childFragmentManager)
    }
}