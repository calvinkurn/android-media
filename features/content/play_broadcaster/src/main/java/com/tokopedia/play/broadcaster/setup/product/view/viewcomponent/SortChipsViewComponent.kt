package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.ChipsUnify

/**
 * Created by kenny.hadisaputra on 28/01/22
 */
class SortChipsViewComponent(
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

    fun setText(text: String) {
        if (text.isBlank()) {
            view.chipText = getString(R.string.play_bro_etalase_sort)
            view.chipType = ChipsUnify.TYPE_NORMAL
        } else {
            view.chipText = text
            view.chipType = ChipsUnify.TYPE_SELECTED
        }
    }

    sealed class Event {
        object OnClicked : Event()
    }
}