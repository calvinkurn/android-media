package com.tokopedia.play.ui.quickreply

import android.view.ViewGroup
import com.tokopedia.play.component.EventBusFactory
import com.tokopedia.play.component.UIComponent
import com.tokopedia.play.data.QuickReply
import com.tokopedia.play.ui.quickreply.interaction.QuickReplyInteractionEvent
import com.tokopedia.play.view.event.ScreenStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * Created by jegul on 13/12/19
 */
class QuickReplyComponent(
        container: ViewGroup,
        private val bus: EventBusFactory,
        coroutineScope: CoroutineScope
) : UIComponent<QuickReplyInteractionEvent>, CoroutineScope by coroutineScope, QuickReplyView.Listener {

    private val uiView = initView(container)

    init {
        uiView.hide()

        launch {
            bus.getSafeManagedFlow(ScreenStateEvent::class.java)
                    .collect {
                        when (it) {
                            is ScreenStateEvent.VideoPropertyChanged -> if (it.videoProp.type.isLive) uiView.show() else uiView.hide()
                            is ScreenStateEvent.VideoStreamChanged -> if (it.videoStream.videoType.isLive) uiView.show() else uiView.hide()
                            is ScreenStateEvent.SetQuickReply -> uiView.setQuickReply(it.quickReply)
                        }
                    }
        }

//        uiView.setQuickReply(
//                QuickReply(
//                        listOf(
//                                "Keren",
//                                "Mantap",
//                                "UUuuuuuuuuUUUUU",
//                                "YEYEYEY",
//                                "KEREN BANGET CUI",
//                                "WOOOOOOSSSHHH"
//                        )
//                )
//        )
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

    private fun initView(container: ViewGroup) =
            QuickReplyView(container, this)
}