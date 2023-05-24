package com.tokopedia.play.channel.ui.component

import androidx.constraintlayout.widget.Group
import com.tokopedia.play.ui.component.UiComponent
import com.tokopedia.play.ui.view.comment.CommentIconUiView
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play.widget.ui.model.PartnerType
import com.tokopedia.play_common.eventbus.EventBus

/**
 * @author by astidhiyaa on 07/02/23
 */
class CommentIconUiComponent(
    bus: EventBus<in Event>,
    group: Group
) : UiComponent<PlayViewerNewUiState> {

    private val view = CommentIconUiView(
        group,
        object : CommentIconUiView.Listener {
            override fun onCommentClicked(view: CommentIconUiView) {
                bus.emit(Event.OnCommentClicked)
            }
        }
    )

    override fun render(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged { it.channel }) return
        view.show(state.value.channel.channelInfo.channelType.isVod && state.value.partner.type != PartnerType.Tokopedia && state.value.channel.commentConfig.shouldShow)
        view.setCounter(state.value.channel.commentConfig.total)
    }

    sealed interface Event {
        object OnCommentClicked : Event
    }
}
