package com.tokopedia.play.channel.ui.component

import com.tokopedia.play.databinding.ViewVodCommentBinding
import com.tokopedia.play.ui.component.UiComponent
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.ui.view.comment.CommentIconUiView
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isAnyChanged
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play_common.eventbus.EventBus

/**
 * @author by astidhiyaa on 07/02/23
 */
class CommentIconUiComponent(
    bus: EventBus<in Event>,
    binding: ViewVodCommentBinding
) : UiComponent<PlayViewerNewUiState> {

    private val view = CommentIconUiView(
        binding,
        object : CommentIconUiView.Listener {
            override fun onCommentClicked(view: CommentIconUiView) {
                bus.emit(Event.OnCommentClicked)
            }
        }
    )

    override fun render(state: CachedState<PlayViewerNewUiState>) {
        // ToDo: add comment counter value
        if (state.isAnyChanged({ it.channel }, { it.partner })) {
            view.show(state.value.channel.channelInfo.channelType.isVod && state.value.partner.type != PartnerType.Tokopedia)
            // view.setCounter("")
        }
    }

    sealed interface Event {
        object OnCommentClicked : Event
    }
}
