package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.TobacoErrorData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.ProductDetailErrorHelper
import kotlinx.android.synthetic.main.item_dynamic_global_error.view.*

class PageErrorViewHolder(val view: View,
                          val listener: DynamicProductDetailListener) : AbstractViewHolder<PageErrorDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_global_error
        private const val IMG_URL_ERROR_TOBACCO = "https://ecs7.tokopedia.net/android/tobacco/banned_product_slice.png"
        private const val TNC_BANNED_PRODUCT = "https://m.tokopedia.com/terms"
    }

    override fun bind(element: PageErrorDataModel) {
        if (element.shouldShowTobacoError) {
            renderTobacoError(element.tobacoErrorData ?: TobacoErrorData())
        } else {
            if (element.errorCode == ProductDetailErrorHelper.CODE_PRODUCT_ERR_BANNED) {
                renderBannedProductError()
            } else {
                view.global_error_pdp.setType(element.errorCode.toInt())
            }
        }

        /*
         * If error code is product not found, button would be "Go To Homepage"
         */
        when {
            element.errorCode == GlobalError.PAGE_NOT_FOUND.toString() -> view.global_error_pdp.setActionClickListener {
                listener.goToHomePageClicked()
            }
            element.errorCode == ProductDetailErrorHelper.CODE_PRODUCT_ERR_BANNED -> view.global_error_pdp.setActionClickListener {
                listener.goToWebView(TNC_BANNED_PRODUCT)
            }
            element.shouldShowTobacoError -> view.global_error_pdp.setActionClickListener {
                listener.goToWebView(element.tobacoErrorData?.url ?: "")
            }
            else -> view.global_error_pdp.setActionClickListener {
                listener.onRetryClicked(true)
            }
        }
    }

    private fun renderBannedProductError() {
        view.global_error_pdp.run {
            clearView()
            errorTitle.show()
            errorTitle.text = getString(R.string.banned_product_title)

            errorDescription.text = getString(R.string.banned_product_description)
            errorDescription.show()

            errorIllustration.loadImage(IMG_URL_ERROR_TOBACCO)
            errorIllustration.adjustViewBounds = true
            errorIllustration.show()

            errorAction.text = getString(R.string.label_read_detail)
            errorAction.show()
        }
    }

    private fun renderTobacoError(tobacoErrorData: TobacoErrorData) {
        view.global_error_pdp.run {
            clearView()
            errorTitle.show()
            errorTitle.text = tobacoErrorData.title

            errorDescription.text = tobacoErrorData.messages
            errorDescription.show()

            errorIllustration.loadImage(IMG_URL_ERROR_TOBACCO)
            errorIllustration.adjustViewBounds = true
            errorIllustration.show()

            errorAction.text = tobacoErrorData.button
            if (tobacoErrorData.url.isEmpty()) {
                errorAction.hide()
            } else {
                errorAction.show()
            }
        }
    }
}