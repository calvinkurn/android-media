package com.tokopedia.play.channel.ui.component

import com.tokopedia.play.databinding.ViewKebabIconBinding
import com.tokopedia.play.extensions.isKeyboardShown
import com.tokopedia.play.ui.component.UiComponent
import com.tokopedia.play.ui.view.kebab.KebabIconUiView
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play_common.eventbus.EventBus

/**
 * Created by kenny.hadisaputra on 19/07/22
 */
class KebabIconUiComponent(
    binding: ViewKebabIconBinding,
    bus: EventBus<in Event>,
) : UiComponent<PlayViewerNewUiState> {

    private val uiView = KebabIconUiView(
        binding,
        object : KebabIconUiView.Listener {
            override fun onClicked(view: KebabIconUiView) {
                bus.emit(Event.OnClicked)
            }

            override fun onImpressed(view: KebabIconUiView) {
                bus.emit(Event.OnImpressed)
            }
        }
    )

    override fun render(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged(
                { it.status },
                { it.bottomInsets })) return

        uiView.show(
            shouldShow = !(state.value.status.channelStatus.statusType.isFreeze ||
                    state.value.status.channelStatus.statusType.isBanned) &&
                    !state.value.bottomInsets.isKeyboardShown
        )
    }

    sealed interface Event {
        object OnClicked : Event
        object OnImpressed : Event
    }
}
