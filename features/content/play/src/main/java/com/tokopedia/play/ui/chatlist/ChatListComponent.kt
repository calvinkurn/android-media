package com.tokopedia.play.ui.chatlist

import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
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
        bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<Unit>, CoroutineScope by coroutineScope {

    private val uiView = initView(container)

    init {
        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.IncomingChat -> uiView.showChat(it.chat)
                            is ScreenStateEvent.VideoPropertyChanged -> if (it.videoProp.type.isLive) uiView.show() else uiView.hide()
                            is ScreenStateEvent.VideoStreamChanged -> if (it.videoStream.videoType.isLive) uiView.show() else uiView.hide()
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<Unit> {
        return emptyFlow()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        uiView.onDestroy()
    }

    private fun initView(container: ViewGroup): ChatListView =
            ChatListView(container)
}