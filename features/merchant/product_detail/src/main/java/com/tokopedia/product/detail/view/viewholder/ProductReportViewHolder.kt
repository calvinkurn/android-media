package com.tokopedia.product.detail.view.viewholder

import android.text.method.LinkMovementMethod
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.ProductReportDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.product.detail.view.util.boldOrLinkText
import kotlinx.android.synthetic.main.item_product_report_view_holder.view.*

/**
 * Created by Yehezkiel on 12/11/20
 */
class ProductReportViewHolder(val view: View, val listener: DynamicProductDetailListener) : AbstractViewHolder<ProductReportDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_product_report_view_holder
    }

    override fun bind(element: ProductReportDataModel) {
        view.product_report_txt.movementMethod = LinkMovementMethod.getInstance()
        view.product_report_txt.text = view.context.getString(R.string.merchant_product_detail_report_text)
                .boldOrLinkText(true, view.context, view.context.getString(R.string.merchant_product_detail_report_suffix) to {
                    listener.reportProductFromComponent(getComponentTrackData(element))
                })
    }

    private fun getComponentTrackData(element: ProductReportDataModel?) = ComponentTrackDataModel(element?.type
            ?: "", element?.name ?: "", adapterPosition + 1)

}