package com.tokopedia.product.detail.view.viewholder

import com.tokopedia.imageassets.ImageUrl

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.TobacoErrorData
import com.tokopedia.product.detail.databinding.ItemDynamicGlobalErrorBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.ProductDetailErrorHelper

class PageErrorViewHolder(val view: View,
                          val listener: DynamicProductDetailListener) : AbstractViewHolder<PageErrorDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_global_error
        private const val IMG_URL_ERROR_TOBACCO = ImageUrl.IMG_URL_ERROR_TOBACCO
        private const val TNC_BANNED_PRODUCT = "https://m.tokopedia.com/terms"
    }

    private val binding = ItemDynamicGlobalErrorBinding.bind(view)

    override fun bind(element: PageErrorDataModel) {
        if (element.shouldShowTobacoError) {
            renderTobacoError(element.tobacoErrorData ?: TobacoErrorData())
        } else {
            if (element.errorCode == ProductDetailErrorHelper.CODE_PRODUCT_ERR_BANNED) {
                renderBannedProductError()
            } else {
                binding.globalErrorPdp.setType(element.errorCode.toInt())
            }
        }

        /*
         * If error code is product not found, button would be "Go To Homepage"
         */
        when {
            element.errorCode == GlobalError.PAGE_NOT_FOUND.toString() -> binding.globalErrorPdp.setActionClickListener {
                listener.goToHomePageClicked()
            }
            element.errorCode == ProductDetailErrorHelper.CODE_PRODUCT_ERR_BANNED -> binding.globalErrorPdp.setActionClickListener {
                listener.goToWebView(TNC_BANNED_PRODUCT)
            }
            element.shouldShowTobacoError -> binding.globalErrorPdp.setActionClickListener {
                listener.goToWebView(element.tobacoErrorData?.url ?: "")
            }
            else -> binding.globalErrorPdp.setActionClickListener {
                listener.onRetryClicked(true)
            }
        }

        itemView.addOnImpressionListener(element.impressHolder) {
            if (element.errorCode == GlobalError.PAGE_NOT_FOUND.toString()) {
                listener.onImpressPageNotFound()
            }
        }
    }

    private fun renderBannedProductError() {
        binding.globalErrorPdp.run {
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
        binding.globalErrorPdp.run {
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
