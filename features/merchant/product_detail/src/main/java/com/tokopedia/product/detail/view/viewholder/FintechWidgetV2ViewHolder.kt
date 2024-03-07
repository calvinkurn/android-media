package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.view.PdpFintechWidgetV2
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetV2DataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.ProductDetailListener

class FintechWidgetV2ViewHolder(val view: View, val listener: ProductDetailListener) :
    AbstractViewHolder<FintechWidgetV2DataModel>(view), ProductUpdateListner {

    companion object {
        val LAYOUT = R.layout.fintech_widget_v2_layout
    }

    private val fintechWidget: PdpFintechWidgetV2 = view.findViewById(R.id.pdpBasicFintechWidgetV2)

    private var previousData: FintechWidgetV2DataModel? = null

    override fun bind(element: FintechWidgetV2DataModel) {
        if (isSameSession(element = element)) return

        fintechWidget.updateBaseFragmentContext(
            listener.getParentViewModelStoreOwner(),
            listener.getParentLifeCyclerOwner()
        )

        fintechWidget.updateIdToPriceMap(element.idToPriceUrlMap, element.categoryId)

        fintechWidget.updateProductId(
            element.productId,
            this,
            element.isLoggedIn,
            element.shopId,
            element.parentId
        )

        updateSession(element = element)
    }

    private fun updateSession(element: FintechWidgetV2DataModel?) {
        previousData = element
    }

    private fun isSameSession(element: FintechWidgetV2DataModel?) = previousData == element

    override fun removeWidget() {
        if (listener.getProductInfo()?.isProductVariant() == true) {
            itemView.setLayoutHeight(0)
            itemView.requestLayout()
        } else {
            listener.removeComponent(ProductDetailConstant.FINTECH_WIDGET_V2_NAME)
        }
    }

    override fun showWidget() {
        itemView.setLayoutHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
        itemView.requestLayout()
    }

    override fun fintechChipClicked(
        fintechRedirectionWidgetDataClass: FintechRedirectionWidgetDataClass,
        redirectionUrl: String
    ) {
        listener.fintechRedirection(fintechRedirectionWidgetDataClass, redirectionUrl)
    }
}
