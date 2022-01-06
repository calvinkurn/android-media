package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup.LayoutParams;
import android.view.View.GONE
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.view.PdpFintechWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class FintechWidgetViewHolder(val view: View,val  listener: DynamicProductDetailListener):
    AbstractViewHolder<FintechWidgetDataModel>(view), ProductUpdateListner {

    companion object {
        val LAYOUT = R.layout.fintech_widget_layout
    }




    override fun bind(element: FintechWidgetDataModel?) {
        val fintechWidget = view.findViewById<PdpFintechWidget>(R.id.pdpBasicFintechWidget)
        fintechWidget.sendLifeCycle(listener.getLifecycleFragment())
        listener.getProductId()?.let { productId ->
            fintechWidget.updateProductId(productId,this)
        }
    }

    override fun removeWidget() {
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, 0)
        view.setLayoutParams(lp)

    }

    override fun showWidget() {
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        view.setLayoutParams(lp)
    }

}