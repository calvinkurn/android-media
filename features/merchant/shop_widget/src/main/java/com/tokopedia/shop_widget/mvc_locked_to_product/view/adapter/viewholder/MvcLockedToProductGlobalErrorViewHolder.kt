package com.tokopedia.shop_widget.mvc_locked_to_product.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.shop_widget.R
import com.tokopedia.shop_widget.databinding.ItemMvcLockedToProductGlobalErrorLayoutBinding
import com.tokopedia.shop_widget.mvc_locked_to_product.util.MvcLockedToProductConstant.MVC_ERROR_IMAGE_URL
import com.tokopedia.shop_widget.mvc_locked_to_product.view.uimodel.MvcLockedToProductGlobalErrorUiModel
import com.tokopedia.utils.view.binding.viewBinding

open class MvcLockedToProductGlobalErrorViewHolder(
    itemView: View,
    private val listener: Listener
) : AbstractViewHolder<MvcLockedToProductGlobalErrorUiModel>(itemView) {
    private val viewBinding: ItemMvcLockedToProductGlobalErrorLayoutBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_mvc_locked_to_product_global_error_layout
    }

    interface Listener {
        fun onGlobalErrorActionRefreshPage()
        fun onGlobalErrorActionRedirectAppLink(appLink: String)
    }

    override fun bind(uiModel: MvcLockedToProductGlobalErrorUiModel) {
        setGlobalError(uiModel)
    }

    private fun setGlobalError(uiModel: MvcLockedToProductGlobalErrorUiModel) {
        viewBinding?.layoutGlobalError?.apply {
            setType(uiModel.globalErrorType)
            errorSecondaryAction.hide()
            errorDescription.text = MethodChecker.fromHtml(uiModel.errorDescription)
            if (uiModel.ctaText.isNotEmpty()) {
                errorTitle.text = uiModel.errorTitle
                errorAction.text = uiModel.ctaText
                errorIllustration.loadImage(MVC_ERROR_IMAGE_URL)
                errorIllustration.adjustViewBounds =true
                setActionClickListener {
                    listener.onGlobalErrorActionRedirectAppLink(uiModel.ctaLink)
                }
            } else {
                setActionClickListener {
                    listener.onGlobalErrorActionRefreshPage()
                }
            }
        }
    }

}