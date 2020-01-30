package com.tokopedia.play.ui.sendchat

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 02/12/19
 */
class SendChatComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val coroutineScope: CoroutineScope
) : UIComponent<SendChatInteractionEvent>, SendChatView.Listener, CoroutineScope by coroutineScope {

    private val uiView = initChatFormView(container)

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.VideoPropertyChanged -> if (it.videoProp.type.isLive) uiView.show() else uiView.hide()
                            is ScreenStateEvent.VideoStreamChanged -> if (it.videoStream.channelType.isLive) uiView.show() else uiView.hide()
                            is ScreenStateEvent.ComposeChat -> uiView.focusChatForm(shouldFocus = true, forceChangeKeyboardState = true)
                            is ScreenStateEvent.KeyboardStateChanged -> uiView.focusChatForm(it.isShown)
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze) uiView.hide()
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<SendChatInteractionEvent> {
        return bus.getSafeManagedFlow(SendChatInteractionEvent::class.java)
    }

    override fun onChatFormClicked(view: SendChatView) {
        launch {
            bus.emit(SendChatInteractionEvent::class.java, SendChatInteractionEvent.FormClicked)
        }
    }

    override fun onSendChatClicked(view: SendChatView, message: String) {
        launch {
            bus.emit(SendChatInteractionEvent::class.java, SendChatInteractionEvent.SendClicked(message))
        }
    }

    private fun initChatFormView(container: ViewGroup) =
            SendChatView(container, this)
}