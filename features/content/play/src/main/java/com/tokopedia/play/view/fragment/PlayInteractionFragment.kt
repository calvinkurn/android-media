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
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.di.DaggerPlayComponent
import com.tokopedia.play.ui.chatlist.ChatListComponent
import com.tokopedia.play.ui.like.LikeComponent
import com.tokopedia.play.ui.like.interaction.LikeInteractionEvent
import com.tokopedia.play.ui.pinned.PinnedComponent
import com.tokopedia.play.ui.pinned.interaction.PinnedInteractionEvent
import com.tokopedia.play.ui.sendchat.SendChatComponent
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent
import com.tokopedia.play.ui.stats.StatsComponent
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.viewmodel.PlayInteractionViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayInteractionFragment : BaseDaggerFragment(), CoroutineScope {

    companion object {

        fun newInstance(): PlayInteractionFragment {
            return PlayInteractionFragment()
        }
    }

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var playInteractionViewModel: PlayInteractionViewModel

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
        playInteractionViewModel = ViewModelProvider(this, viewModelFactory).get(PlayInteractionViewModel::class.java)
        return inflater.inflate(R.layout.fragment_play_interaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents(view as ViewGroup)
        setPinnedMessage("Yoenik Apparel", "Visit my collections here!")
        playInteractionViewModel.startObservingChatList()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        playInteractionViewModel.observableChatList.observe(viewLifecycleOwner, Observer {
            launch {
                EventBusFactory.get(viewLifecycleOwner)
                        .emit(
                                ScreenStateEvent::class.java,
                                ScreenStateEvent.Chat(it)
                        )
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    //region Component Initialization
    private fun initComponents(container: ViewGroup) {
        val sendChatComponent: UIComponent<*> = initSendChatComponent(container)
        val likeComponent: UIComponent<*> = initLikeComponent(container)
        val statsComponent: UIComponent<*> = initStatsComponent(container)
        val pinnedComponent: UIComponent<*> = initPinnedComponent(container)
        val chatListComponent: UIComponent<*> = initChatListComponent(container)

        layoutView(
                container = container,
                sendChatComponentId = sendChatComponent.getContainerId(),
                likeComponentId = likeComponent.getContainerId(),
                statsComponentId = statsComponent.getContainerId(),
                pinnedComponentId = pinnedComponent.getContainerId(),
                chatListComponentId = chatListComponent.getContainerId()
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
    }

    private fun layoutView(
            container: ViewGroup,
            @IdRes sendChatComponentId: Int,
            @IdRes likeComponentId: Int,
            @IdRes statsComponentId: Int,
            @IdRes pinnedComponentId: Int,
            @IdRes chatListComponentId: Int
    ) {

        fun layoutChat(container: ViewGroup, @IdRes id: Int, @IdRes likeComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                connect(id, ConstraintSet.END, likeComponentId, ConstraintSet.START)
                connect(id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
            }

            constraintSet.applyTo(container)
        }

        fun layoutLike(container: ViewGroup, @IdRes id: Int, @IdRes sendChatComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, sendChatComponentId, ConstraintSet.END, resources.getDimensionPixelOffset(R.dimen.dp_8))
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, sendChatComponentId, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.TOP, sendChatComponentId, ConstraintSet.TOP)
            }

            constraintSet.applyTo(container)
        }

        fun layoutChatList(container: ViewGroup, @IdRes id: Int, @IdRes sendChatComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, sendChatComponentId, ConstraintSet.START)
                connect(id, ConstraintSet.END, sendChatComponentId, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, sendChatComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_8))
            }

            constraintSet.applyTo(container)
        }

        fun layoutPinned(container: ViewGroup, @IdRes id: Int, @IdRes chatListComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, chatListComponentId, ConstraintSet.START)
                connect(id, ConstraintSet.END, chatListComponentId, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, chatListComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_8))
            }

            constraintSet.applyTo(container)
        }

        fun layoutStats(container: ViewGroup, @IdRes id: Int, @IdRes pinnedComponentId: Int) {
            val constraintSet = ConstraintSet()

            constraintSet.clone(container as ConstraintLayout)

            constraintSet.apply {
                connect(id, ConstraintSet.START, pinnedComponentId, ConstraintSet.START)
                connect(id, ConstraintSet.BOTTOM, pinnedComponentId, ConstraintSet.TOP, resources.getDimensionPixelOffset(R.dimen.dp_8))
            }

            constraintSet.applyTo(container)
        }

        layoutChat(container, sendChatComponentId, likeComponentId)
        layoutLike(container, likeComponentId, sendChatComponentId)
        layoutChatList(container, chatListComponentId, sendChatComponentId)
        layoutPinned(container, pinnedComponentId, chatListComponentId)
        layoutStats(container, statsComponentId, pinnedComponentId)
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
}