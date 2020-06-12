package com.tokopedia.play.ui.chatlist

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.extensions.isAnyBottomSheetsShown
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.launch

/**
 * Created by jegul on 03/12/19
 */
open class ChatListComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<Unit> {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.IncomingChat -> uiView.showNewChat(it.chat)
                            is ScreenStateEvent.SetChatList -> uiView.setChatList(it.chatList)
                            is ScreenStateEvent.VideoStreamChanged -> if (it.videoStream.channelType.isLive && !it.stateHelper.bottomInsets.isAnyBottomSheetsShown) uiView.show() else uiView.hide()
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) uiView.hide()
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
                            }
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

    protected open fun initView(container: ViewGroup): ChatListView =
            ChatListView(container)
}