package com.tokopedia.play.ui.quickreply

import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.ui.quickreply.interaction.QuickReplyInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
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
        coroutineScope: CoroutineScope
) : UIComponent<QuickReplyInteractionEvent>, CoroutineScope by coroutineScope, QuickReplyView.Listener {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val uiView = initView(container)

    init {
        uiView.hide()

        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.SetQuickReply -> uiView.setQuickReply(it.quickReply)
                            is ScreenStateEvent.KeyboardStateChanged -> if (it.isShown) uiView.show() else uiView.hide()
                            is ScreenStateEvent.OnNewPlayRoomEvent -> if(it.event.isFreeze) uiView.hide()
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
        launch {
            bus.emit(QuickReplyInteractionEvent::class.java, QuickReplyInteractionEvent.ReplyClicked(replyString))
        }
    }

    protected open fun initView(container: ViewGroup) =
            QuickReplyView(container, this)
}