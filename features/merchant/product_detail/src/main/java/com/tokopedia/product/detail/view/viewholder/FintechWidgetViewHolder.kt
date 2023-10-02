package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.listner.ProductUpdateListner
import com.tokopedia.pdp.fintech.view.PdpFintechWidget
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.FintechWidgetDataModel
import com.tokopedia.product.detail.data.util.ProductDetailConstant
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class FintechWidgetViewHolder(val view: View, val listener: DynamicProductDetailListener) :
    AbstractViewHolder<FintechWidgetDataModel>(view), ProductUpdateListner {

    companion object {
        val LAYOUT = R.layout.fintech_widget_layout
    }

    private val fintechWidget: PdpFintechWidget = view.findViewById(R.id.pdpBasicFintechWidget)

    private var previousProductId: String = ""
    private var previousLoginStatus: Boolean = false

    override fun bind(element: FintechWidgetDataModel) {
        if (isSameSession(element = element)) return

        fintechWidget.updateBaseFragmentContext(
            parentViewModelStore = listener.getParentViewModelStoreOwner(),
            parentLifeCycleOwner = listener.getParentLifeCyclerOwner()
        )

        fintechWidget.updateIdToPriceMap(
            productIdToPrice = element.idToPriceUrlMap,
            productCategoryId = element.categoryId
        )

        fintechWidget.updateProductId(
            productID = element.productId,
            fintechWidgetViewHolder = this,
            loggedIn = element.isLoggedIn,
            shopID = element.shopId,
            parentId = element.parentId
        )

        updateSession(element = element)
    }

    private fun updateSession(element: FintechWidgetDataModel?) {
        previousProductId = element?.productId.orEmpty()
        previousLoginStatus = element?.isLoggedIn.orFalse()
    }

    private fun isSameSession(element: FintechWidgetDataModel?) =
        element?.productId == previousProductId && element.isLoggedIn == previousLoginStatus

    override fun removeWidget() {
        // remove this widget if product non-variant
        // when product is variant, possibility to visible when variant changed
        if (listener.getProductInfo()?.isProductVariant() == true) {
            val params = itemView.layoutParams
            params.height = 0
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            itemView.layoutParams = params
        } else {
            listener.removeComponent(ProductDetailConstant.FINTECH_WIDGET_NAME)
        }
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
