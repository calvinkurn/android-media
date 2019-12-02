package com.tokopedia.play.ui.sendchat

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * Created by jegul on 02/12/19
 */
class SendChatComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val coroutineScope: CoroutineScope
) : UIComponent<SendChatInteractionEvent>, ChatFormView.Listener, CoroutineScope by coroutineScope {

    private val uiView = initChatFormView(container, bus)

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): ReceiveChannel<SendChatInteractionEvent> {
        return bus.getSafeManagedReceiveChannel(SendChatInteractionEvent::class.java)
    }

    override fun onChatFormClicked(view: ChatFormView) {
        launch {
            bus.emit(SendChatInteractionEvent::class.java, SendChatInteractionEvent.FormClicked)
        }
    }

    override fun onSendChatClicked(view: ChatFormView) {
        launch {
            bus.emit(SendChatInteractionEvent::class.java, SendChatInteractionEvent.SendClicked)
        }
    }

    private fun initChatFormView(container: ViewGroup, bus: EventBusFactory): ChatFormView =
            ChatFormView(container, this)
}