package com.tokopedia.thankyou_native.presentation.adapter.viewholder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.OrderedItem
import com.tokopedia.thankyou_native.presentation.adapter.model.ShopInvoice

class ShopInvoiceViewHolder(val view: View) : AbstractViewHolder<ShopInvoice>(view) {

    private lateinit var inflater: LayoutInflater

    override fun bind(element: ShopInvoice?) {
        element?.let {
            view.findViewById<TextView>(R.id.tvInvoiceShopName).text = element.shopName
            addShopItems(view.findViewById(R.id.llShopItemContainer), shopInvoice = element)
            view.findViewById<TextView>(R.id.tvInvoiceShopDiscountValue).text = getString(R.string.thankyou_discounted_rp, element.itemDiscountStr)
            view.findViewById<TextView>(R.id.tvInvoiceShopItemProtection).text = getString(R.string.thankyou_rp, element.productProtectionStr)
            view.findViewById<TextView>(R.id.tvInvoiceShopItemShippingValue).text = getString(R.string.thankyou_rp, element.shippingPriceStr)
            view.findViewById<TextView>(R.id.tvInvoiceShopItemCourier).text = element.shippingTypeStr
            view.findViewById<TextView>(R.id.tvInvoiceShopItemShippingDiscountValue).text = getString(R.string.thankyou_discounted_rp, element.discountOnShippingStr)
            view.findViewById<TextView>(R.id.tvInvoiceShopItemShippingInsuranceValue).text = getString(R.string.thankyou_rp, element.shippingInsurancePriceStr)
            view.findViewById<TextView>(R.id.tvInvoiceShopShippingAddressValue).text = element.shippingAddress
        }
    }

    private fun addShopItems(container: LinearLayout, shopInvoice: ShopInvoice) {
        container.removeAllViews()
        shopInvoice.orderedItem.forEach { orderItem ->
            val view = createShopItemView(context = view.context, orderedItem = orderItem)
            container.addView(view)
        }
    }

    private fun createShopItemView(context: Context, orderedItem: OrderedItem): View {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(context)
        val shopItemView = inflater.inflate(R.layout.thank_widget_shop_item, null, false)
        shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemName).text = orderedItem.itemName
        shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemNameTotalPrice).text = getString(R.string.thankyou_rp, orderedItem.itemPriceStr)
        shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemNameCountPrice).text = orderedItem.itemCountAndPriceStr
        return shopItemView
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_shop_invoice
    }
}