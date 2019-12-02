package com.tokopedia.play.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DimenRes
import androidx.annotation.IdRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.play.R
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.like.LikeComponent
import com.tokopedia.play.ui.like.interaction.LikeInteractionEvent
import com.tokopedia.play.ui.sendchat.SendChatComponent
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
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

    override fun getScreenName(): String = "Play Interaction"

    override fun initInjector() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_play_interaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initComponents(view as ViewGroup)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initComponents(container: ViewGroup) {
        val sendChatComponent: UIComponent<*> = initSendChatComponent(container)
        val likeComponent: UIComponent<*> = initLikeComponent(container)

        layoutView(
                container = container,
                sendChatComponentId = sendChatComponent.getContainerId(),
                likeComponentId = likeComponent.getContainerId()
        )
    }

    private fun initSendChatComponent(container: ViewGroup): UIComponent<SendChatInteractionEvent> {
        val sendChatComponent = SendChatComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

        launch {
            sendChatComponent.getUserInteractionEvents()
                    .consumeEach {
                        when (it) {
                            SendChatInteractionEvent.FormClicked -> {}
                            SendChatInteractionEvent.SendClicked -> {}
                        }
                    }
        }

        return sendChatComponent
    }

    private fun initLikeComponent(container: ViewGroup): UIComponent<LikeInteractionEvent> {
        val likeComponent = LikeComponent(container, EventBusFactory.get(viewLifecycleOwner), this)

        launch {
            likeComponent.getUserInteractionEvents()
                    .consumeEach {
                        when (it) {
                            LikeInteractionEvent.LikeClicked -> {}
                        }
                    }
        }

        return likeComponent
    }

    private fun layoutView(
            container: ViewGroup,
            @IdRes sendChatComponentId: Int,
            @IdRes likeComponentId: Int) {

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
                connect(id, ConstraintSet.START, sendChatComponentId, ConstraintSet.END, resources.getDimensionPixelSize(R.dimen.dp_8))
                connect(id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                connect(id, ConstraintSet.BOTTOM, sendChatComponentId, ConstraintSet.BOTTOM)
                connect(id, ConstraintSet.TOP, sendChatComponentId, ConstraintSet.TOP)
            }

            constraintSet.applyTo(container)
        }

        layoutChat(container, sendChatComponentId, likeComponentId)
        layoutLike(container, likeComponentId, sendChatComponentId)
    }
}