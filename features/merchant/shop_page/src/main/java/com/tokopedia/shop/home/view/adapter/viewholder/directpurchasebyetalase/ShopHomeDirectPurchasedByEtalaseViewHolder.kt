package com.tokopedia.shop.home.view.adapter.viewholder.directpurchasebyetalase

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.LayoutShopHomeDirectPurchaseByEtalaseBinding
import com.tokopedia.shop.home.view.customview.directpurchase.DirectPurchaseWidgetView
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.viewholder.ShopDirectPurchaseByEtalaseUiModel
import com.tokopedia.utils.view.binding.viewBinding

class ShopHomeDirectPurchasedByEtalaseViewHolder(
    itemView: View,
    private val shopHomeListener: ShopHomeListener,
) : AbstractViewHolder<ShopDirectPurchaseByEtalaseUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_shop_home_direct_purchase_by_etalase

    }

    private val viewBinding: LayoutShopHomeDirectPurchaseByEtalaseBinding? by viewBinding()
    private val containerPlaceholder: View? =
        viewBinding?.layoutPlaceholder?.containerPlaceholder
    private val directPurchaseWidget: DirectPurchaseWidgetView? =
        viewBinding?.shopDirectPurchaseWidget

    override fun bind(element: ShopDirectPurchaseByEtalaseUiModel) {
        if(element.isWidgetShowPlaceholder()){
            showPlaceholderView()
        } else {
            setDirectPurchaseWidgetData(element)
        }

    }

    private fun showPlaceholderView() {
        containerPlaceholder?.show()
        directPurchaseWidget?.hide()
    }

    private fun setDirectPurchaseWidgetData(element: ShopDirectPurchaseByEtalaseUiModel) {
        containerPlaceholder?.hide()
        directPurchaseWidget?.apply {
            setListener(shopHomeListener.getShopPageHomeFragment())
            setData(element.widgetData)
        }
    }

}
