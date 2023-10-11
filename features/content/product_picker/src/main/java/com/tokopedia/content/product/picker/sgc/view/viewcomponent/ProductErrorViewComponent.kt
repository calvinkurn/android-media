package com.tokopedia.content.product.picker.sgc.view.viewcomponent

import com.tokopedia.content.common.util.eventbus.EventBus
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.content.product.picker.R
import com.tokopedia.play_common.view.loadImage
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

    fun setServerError() {
        view.setType(GlobalError.SERVER_ERROR)
        view.setActionClickListener {
            eventBus.emit(Event.RetryClicked)
        }
    }

    fun setConnectionError() {
        view.setType(GlobalError.NO_CONNECTION)
        view.setActionClickListener {
            eventBus.emit(Event.RetryClicked)
        }
    }

    private fun GlobalError.productNotFoundState() {
        errorIllustration.loadImage(getString(R.string.img_search_product_empty))
        errorTitle.text = context.getString(R.string.product_not_found_title)
        errorDescription.text = context.getString(R.string.product_not_found_desc)
        errorAction.gone()
        errorSecondaryAction.gone()
    }

    private fun GlobalError.productEtalaseEmpty() {
        errorIllustration.setImageResource(R.drawable.ic_empty_product_etalase)
        errorTitle.text = context.getString(R.string.product_etalase_empty_title)
        errorDescription.text = context.getString(R.string.product_etalase_empty_desc)
        errorAction.text = context.getString(R.string.add_product)
        errorSecondaryAction.gone()
    }

    sealed class Event {
        object AddProductClicked : Event()
        object RetryClicked : Event()
    }
}
