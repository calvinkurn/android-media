package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetV2DataModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.pdp.fintech.view.PdpFintechWidgetV2

class FintechWidgetV2ViewHolder(val view: View, val  listener: DynamicProductDetailListener)
    : AbstractViewHolder<FintechWidgetV2DataModel>(view), ProductUpdateListner {

    companion object {
        val LAYOUT = R.layout.fintech_widget_v2_layout
    }

    private val fintechWidget: PdpFintechWidgetV2 = view.findViewById(R.id.pdpBasicFintechWidgetV2)

    override fun bind(element: FintechWidgetV2DataModel?) {
        fintechWidget.updateBaseFragmentContext(
            listener.getParentViewModelStoreOwner(),
            listener.getParentLifeCyclerOwner()
        )
        element?.idToPriceUrlMap?.let {
            fintechWidget.updateIdToPriceMap(it, element.categoryId)
        }

        element?.productId?.let {
            fintechWidget.updateProductId(
                it,
                this,
                element.isLoggedIn,
                element.shopId,
                element.parentId
            )
        }
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

    override fun fintechChipClicked(
        fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass,
        redirectionUrl: String
    ) {
        listener.fintechRedirection(fintechRedirectionWidgetDataClass, redirectionUrl)
    }
}
