package com.tokopedia.play.ui.quickreply

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.quickreply.interaction.QuickReplyInteractionEvent
import com.tokopedia.play.util.coroutine.CoroutineDispatcherProvider
import com.tokopedia.play.view.event.ScreenStateEvent
import com.tokopedia.play.view.type.BottomInsetsType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 13/12/19
 */
open class QuickReplyComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        private val scope: CoroutineScope,
        dispatchers: CoroutineDispatcherProvider
) : UIComponent<QuickReplyInteractionEvent>, QuickReplyView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        scope.launch(dispatchers.immediate) {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.Init -> uiView.hide()
                            is ScreenStateEvent.SetQuickReply -> uiView.setQuickReply(it.quickReply)
                            is ScreenStateEvent.BottomInsetsChanged -> {
                                /**
                                 * If channel is Live && NO BottomSheet is shown && Keyboard is shown -> show()
                                 * else -> hide()
                                 */
                                if (it.stateHelper.channelType.isLive &&
                                        it.insetsViewMap[BottomInsetsType.ProductSheet]?.isShown == false &&
                                        it.insetsViewMap[BottomInsetsType.VariantSheet]?.isShown == false &&
                                        it.insetsViewMap[BottomInsetsType.Keyboard]?.isShown == true) {
                                    uiView.show()
                                } else uiView.hide()
                            }
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze || it.event.isBanned) uiView.hide()
                        }
                    }
        }
    }

    override fun getContainerId(): Int {
        return uiView.containerId
    }

    override fun getUserInteractionEvents(): Flow<QuickReplyInteractionEvent> {
        return bus.getSafeManagedFlow(QuickReplyInteractionEvent::class.java)
    }

    override fun onQuickReplyClicked(view: QuickReplyView, replyString: String) {
        scope.launch {
            bus.emit(QuickReplyInteractionEvent::class.java, QuickReplyInteractionEvent.ReplyClicked(replyString))
        }
    }

    protected open fun initView(container: ViewGroup) =
            QuickReplyView(container, this)
}