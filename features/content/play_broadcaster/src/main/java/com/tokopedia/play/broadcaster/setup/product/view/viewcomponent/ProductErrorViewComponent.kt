package com.tokopedia.play.broadcaster.setup.product.view.viewcomponent

import com.tokopedia.globalerror.GlobalError
import com.tokopedia.play.broadcaster.util.eventbus.EventBus
import com.tokopedia.play.broadcaster.util.extension.productEtalaseEmpty
import com.tokopedia.play.broadcaster.util.extension.productNotFoundState
import com.tokopedia.play_common.viewcomponent.ViewComponent

/**
 * Created by kenny.hadisaputra on 10/02/22
 */
class ProductErrorViewComponent(
    private val view: GlobalError,
    private val eventBus: EventBus<in Event>,
) : ViewComponent(view) {

    fun setProductNotFound() {
        view.productNotFoundState()
        view.setActionClickListener {  }
    }

    fun setHasNoProduct() {
        view.productEtalaseEmpty()
        view.setActionClickListener {
            eventBus.emit(Event.AddProductClicked)
        }
    }

    sealed class Event {
        object AddProductClicked : Event()
    }
}
