package com.tokopedia.content.product.picker.seller.view.viewcomponent

import com.tokopedia.content.common.util.eventbus.EventBus
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by kenny.hadisaputra on 08/02/22
 */
class SaveButtonViewComponent(
    private val view: UnifyButton,
    eventBus: EventBus<in Event>,
) : ViewComponent(view) {

    init {
        view.setOnClickListener {
            eventBus.emit(Event.OnClicked)
        }
    }

    fun setState(
        isLoading: Boolean,
        isEnabled: Boolean
    ) {
        view.isLoading = isLoading
        view.isEnabled = isEnabled
    }

    fun isEnabled() = view.isEnabled

    sealed class Event {
        object OnClicked : Event()
    }
}
