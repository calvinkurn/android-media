package com.tokopedia.content.product.picker.sgc.view.viewcomponent

import com.tokopedia.content.common.util.eventbus.EventBus
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
class EtalaseChipsViewComponent(
    private val view: ChipsUnify,
    eventBus: EventBus<in Event>,
) : ViewComponent(view) {

    init {
        view.setChevronClickListener {
            eventBus.emit(Event.OnClicked)
        }

        rootView.setOnClickListener {
            eventBus.emit(Event.OnClicked)
        }
    }

    fun setState(text: String, isSelected: Boolean) {
        view.chipText = text
        view.chipType = if (isSelected) ChipsUnify.TYPE_SELECTED else ChipsUnify.TYPE_NORMAL
    }

    sealed class Event {
        object OnClicked : Event()
    }
}
