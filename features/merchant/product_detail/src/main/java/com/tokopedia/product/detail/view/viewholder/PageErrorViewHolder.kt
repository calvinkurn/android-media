package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.TobacoErrorData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_global_error.view.*

class PageErrorViewHolder(val view: View,
                          val listener: DynamicProductDetailListener) : AbstractViewHolder<PageErrorDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_global_error
        const val IMG_URL_ERROR_TOBACCO = "https://ecs7.tokopedia.net/android/tobacco/banned_product_slice.png"
    }

    override fun bind(element: PageErrorDataModel) {
        if (element.shouldShowTobacoError) {
            renderTobacoError(element.tobacoErrorData ?: TobacoErrorData())
        } else {
            view.global_error_pdp.setType(element.errorCode.toInt())
        }

        /*
         * If error code is product not found, button would be "Go To Homepage"
         */
        if (element.errorCode == GlobalError.PAGE_NOT_FOUND.toString()) {
            view.global_error_pdp.setActionClickListener {
                listener.goToHomePageClicked()
            }
        } else {
            view.global_error_pdp.setActionClickListener {
                listener.onRetryClicked(true)
            }
        }
    }

    private fun renderTobacoError(tobacoErrorData: TobacoErrorData) {
        view.global_error_pdp.run {
            clearView()
            errorTitle.show()
            errorTitle.text = tobacoErrorData.title

            errorDescription.text = tobacoErrorData.messages
            errorDescription.show()

            ImageHandler.LoadImage(errorIllustration, IMG_URL_ERROR_TOBACCO)
            errorIllustration.adjustViewBounds = true
            errorIllustration.show()

            errorAction.text = tobacoErrorData.button
            errorAction.show()
        }
    }
}