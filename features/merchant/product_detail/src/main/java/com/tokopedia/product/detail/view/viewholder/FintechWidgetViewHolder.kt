package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup.LayoutParams;
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
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



    override fun bind(element: FintechWidgetDataModel?) {
        val fintechWidget = view.findViewById<PdpFintechWidget>(R.id.pdpBasicFintechWidget)
        fintechWidget.updateBaseFragmentContext(listener.getParentViewModelStoreOwner(),
            listener.getParentLifeCyclerOwner())
        listener.getProductId()?.let { productId ->
            fintechWidget.updateProductId(productId,this)
        }
    }

    override fun removeWidget() {
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, 0)
        view.layoutParams = lp

    }

    override fun showWidget() {
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        view.layoutParams = lp
    }

    override fun showWebview() {
        listener.showWebView()
    }

    override fun showBottomSheet(ctaType: Int) {
        listener.showBottomsheet(ctaType)
    }


}