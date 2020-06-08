package com.tokopedia.play.ui.sendchat

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.sendchat.interaction.SendChatInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsState
import com.tokopedia.play.view.type.BottomInsetsType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 02/12/19
 */
open class SendChatComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<SendChatInteractionEvent>, SendChatView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.VideoStreamChanged -> if (it.videoStream.channelType.isLive) uiView.show() else uiView.hide()
                            is ScreenStateEvent.ComposeChat -> uiView.focusChatForm(shouldFocus = true, forceChangeKeyboardState = true)
                            is ScreenStateEvent.BottomInsetsChanged -> {
                                /**
                                 * If channel is Live && NO BottomSheet is shown -> show()
                                 * else -> hide()
                                 */
                                if (it.stateHelper.channelType.isLive &&
                                        it.insetsViewMap[BottomInsetsType.ProductSheet]?.isShown == false &&
                                        it.insetsViewMap[BottomInsetsType.VariantSheet]?.isShown == false) {
                                    uiView.show()
                                } else uiView.hide()

                                uiView.focusChatForm(it.stateHelper.channelType.isLive && it.insetsViewMap[BottomInsetsType.Keyboard] is BottomInsetsState.Shown)
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) {
                                uiView.focusChatForm(false)
                                uiView.hide()
                            }
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
        scope.launch {
            bus.emit(SendChatInteractionEvent::class.java, SendChatInteractionEvent.FormClicked)
        }
    }

    override fun onSendChatClicked(view: SendChatView, message: String) {
        scope.launch {
            bus.emit(SendChatInteractionEvent::class.java, SendChatInteractionEvent.SendClicked(message))
        }
    }

    protected open fun initView(container: ViewGroup) =
            SendChatView(container, this)
}