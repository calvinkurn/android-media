package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.view.PdpFintechWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener


class FintechWidgetViewHolder(val view: View,val  listener: DynamicProductDetailListener):
    AbstractViewHolder<FintechWidgetDataModel>(view), ProductUpdateListner{

    companion object {
        val LAYOUT = R.layout.fintech_widget_layout
    }
    private val fintechWidget:PdpFintechWidget = view.findViewById(R.id.pdpBasicFintechWidget)
    override fun bind(element: FintechWidgetDataModel?) {

        fintechWidget.updateBaseFragmentContext(
            listener.getParentViewModelStoreOwner(),
            listener.getParentLifeCyclerOwner()
        )
        element?.idToPriceUrlMap?.let { fintechWidget.updateidToPriceMap(it  ,
            element.categoryId
        ) }

        element?.productId?.let { fintechWidget.updateProductId(it, this) }
    }

    override fun removeWidget() {
        val params = itemView.layoutParams
        params.height = 0
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        itemView.layoutParams = params

    }

    override fun showWidget() {
        val params = itemView.layoutParams
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        itemView.layoutParams = params
    }


    override fun fintechRedirection(fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass) {
        listener.fintechRedirection(fintechRedirectionWidgetDataClass)
    }


}