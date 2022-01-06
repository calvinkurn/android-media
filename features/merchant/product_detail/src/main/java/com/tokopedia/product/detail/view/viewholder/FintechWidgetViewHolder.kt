package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdp.fintech.view.PdpFintechWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class FintechWidgetViewHolder(val view: View,val  listener: DynamicProductDetailListener):
    AbstractViewHolder<FintechWidgetDataModel>(view) {

    companion object {
        val LAYOUT = R.layout.fintech_widget_layout
    }




    override fun bind(element: FintechWidgetDataModel?) {
        val fintechWidget = view.findViewById<PdpFintechWidget>(R.id.pdpBasicFintechWidget)
        listener.getProductId()?.let { fintechWidget.updateProductId(it) }
    }


}