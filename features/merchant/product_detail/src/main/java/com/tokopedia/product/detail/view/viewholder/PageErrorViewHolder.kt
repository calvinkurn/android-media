package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.PageErrorDataModel
import com.tokopedia.product.detail.data.model.datamodel.TobacoErrorData
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import kotlinx.android.synthetic.main.item_dynamic_global_error.view.*

class PageErrorViewHolder(val view: View,
                          val listener: DynamicProductDetailListener) : AbstractViewHolder<PageErrorDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_dynamic_global_error
    }

    override fun bind(element: PageErrorDataModel) {
        if (element.shouldShowTobacoError) {
            renderTobacoError(element.tobacoErrorData)
        } else {
            view.global_error_pdp.setType(element.errorCode.toInt())
        }
        view.global_error_pdp.setActionClickListener {
            listener.onRetryClicked(true)
        }
    }

    private fun renderTobacoError(tobacoErrorData: TobacoErrorData?) {

    }
}