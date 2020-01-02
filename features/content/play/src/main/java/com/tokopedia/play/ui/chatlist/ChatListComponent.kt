package com.tokopedia.play.ui.chatlist

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.chatlist.interaction.ChatListInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 03/12/19
 */
class ChatListComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<ChatListInteractionEvent>, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    private var estimatedYPos: Int = -1

    init {
        uiView.hide()

        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.IncomingChat -> uiView.showChat(it.chat)
                            is ScreenStateEvent.VideoPropertyChanged -> if (it.videoProp.type.isLive) uiView.show() else uiView.hide()
                            is ScreenStateEvent.VideoStreamChanged -> if (it.videoStream.videoType.isLive) uiView.show() else uiView.hide()
                            is ScreenStateEvent.KeyboardStateChanged -> if (it.isShown) sendPosYEvent()
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<ChatListInteractionEvent> {
        return bus.getSafeManagedFlow(ChatListInteractionEvent::class.java)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        uiView.onDestroy()
    }

    private fun initView(container: ViewGroup): ChatListView =
            ChatListView(container)

    private fun sendPosYEvent() {
        if (estimatedYPos == -1) {
            estimatedYPos = uiView.getEstimatedYPos()
        }
        launch {
            bus.emit(ChatListInteractionEvent::class.java, ChatListInteractionEvent.PositionYCalculated(estimatedYPos))
        }
    }
}