package com.tokopedia.play.channel.ui.component

import com.tokopedia.content.common.databinding.ViewTaggedProductLabelBinding
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.play.extensions.isAnyShown
import com.tokopedia.play.ui.component.UiComponent
import com.tokopedia.play.ui.view.taggedproductlabel.TaggedProductLabelUiView
import com.tokopedia.play.util.CachedState
import com.tokopedia.play.util.isNotChanged
import com.tokopedia.play.view.uimodel.recom.tagitem.ProductSectionUiModel
import com.tokopedia.play.view.uimodel.state.PlayViewerNewUiState
import com.tokopedia.play_common.eventbus.EventBus

/**
 * @author by astidhiyaa on 27/02/23
 */
class TaggedProductLabelUiComponent(
    bus: EventBus<in Event>, binding: ViewTaggedProductLabelBinding
) : UiComponent<PlayViewerNewUiState> {

    private val view =
        TaggedProductLabelUiView(binding, object : TaggedProductLabelUiView.Listener {
            override fun onTaggedProductLabelClicked(view: TaggedProductLabelUiView) {
                bus.emit(Event.OnClicked)
            }
        })

    override fun render(state: CachedState<PlayViewerNewUiState>) {
        if (state.isNotChanged({ it.tagItems },
                { it.status.channelStatus.statusType },
                { it.address },
                { it.bottomInsets }
            )
        ) return
        val productListSize =
            state.value.tagItems.product.productSectionList.filterIsInstance<ProductSectionUiModel.Section>()
                .sumOf { it.productList.size }

        view.show(
            !state.value.bottomInsets.isAnyShown &&
                !state.value.address.shouldShow && productListSize.isMoreThanZero() && state.value.channel.channelInfo.channelType.isLive
        )
        view.setProductSize(productListSize)
    }

    sealed interface Event {
        object OnClicked : Event
    }
}

