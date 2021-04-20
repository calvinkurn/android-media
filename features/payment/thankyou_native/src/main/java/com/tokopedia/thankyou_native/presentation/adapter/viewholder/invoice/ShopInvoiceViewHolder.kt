package com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.OrderedItem
import com.tokopedia.thankyou_native.presentation.adapter.model.ShopInvoice
import kotlinx.android.synthetic.main.thank_widget_shop_invoice.view.*

class ShopInvoiceViewHolder(val view: View) : AbstractViewHolder<ShopInvoice>(view) {

    private lateinit var inflater: LayoutInflater

    private val tvShopName: TextView = view.tvInvoiceShopName
    private val llItemContainer = view.llShopItemContainer

    private val tvInvoiceShopDiscountValue = view.tvInvoiceShopDiscountValue
    private val tvInvoiceShopDiscount = view.tvInvoiceShopDiscount

    private val tvInvoiceShopItemProtectionValue = view.tvInvoiceShopItemProtectionValue
    private val tvInvoiceShopItemProtection = view.tvInvoiceShopItemProtection

    private val tvInvoiceShopItemShippingValue = view.tvInvoiceShopItemShippingValue
    private val tvInvoiceShopItemShipping = view.tvInvoiceShopItemShipping

    private val tvInvoiceShopItemCourier = view.tvInvoiceShopItemCourier

    private val tvInvoiceShopItemShippingDiscountValue = view.tvInvoiceShopItemShippingDiscountValue
    private val tvInvoiceShopItemShippingDiscount = view.tvInvoiceShopItemShippingDiscount


    private val tvInvoiceShopItemShippingInsuranceValue = view.tvInvoiceShopItemShippingInsuranceValue
    private val tvInvoiceShopItemShippingInsurance = view.tvInvoiceShopItemShippingInsurance

    private val tvInvoiceShopShippingAddressValue = view.tvInvoiceShopShippingAddressValue
    private val tvInvoiceShopShippingAddress = view.tvInvoiceShopShippingAddress



    override fun bind(element: ShopInvoice?) {
        element?.let {
            if(element.shopName.isNullOrBlank()){
                tvShopName.gone()

            }else{
                tvShopName.visible()
                tvShopName.text = element.shopName

            }

            addShopItems(llItemContainer, shopInvoice = element)

            element.itemDiscountStr?.let {
                tvInvoiceShopDiscountValue.text = getString(R.string.thankyou_discounted_rp, element.itemDiscountStr)
                tvInvoiceShopDiscountValue.visible()
                tvInvoiceShopDiscount.visible()

            } ?: run {
                tvInvoiceShopDiscountValue.gone()
                tvInvoiceShopDiscount.gone()
            }

            element.productProtectionStr?.let {
                tvInvoiceShopItemProtectionValue.text = element.productProtectionStr
                tvInvoiceShopItemProtectionValue.visible()
                tvInvoiceShopItemProtection.visible()
            } ?: run {
                tvInvoiceShopItemProtection.gone()
                tvInvoiceShopItemProtectionValue.gone()
            }

            element.shippingPriceStr?.let {
                tvInvoiceShopItemShippingValue.text = getString(R.string.thankyou_rp_without_space, element.shippingPriceStr)
                tvInvoiceShopItemShippingValue.visible()
                tvInvoiceShopItemShipping.visible()
            } ?: run {
                tvInvoiceShopItemShippingValue.gone()
                tvInvoiceShopItemShipping.gone()
            }


            element.shippingInfo?.let {
                if(it.isNotEmpty()) {
                    tvInvoiceShopItemCourier.text = element.shippingInfo
                    tvInvoiceShopItemCourier.visible()
                }else{
                    tvInvoiceShopItemCourier.gone()
                }
            } ?: run {
                tvInvoiceShopItemCourier.gone()
            }

            element.discountOnShippingStr?.let {
                tvInvoiceShopItemShippingDiscountValue.text = getString(R.string.thankyou_discounted_rp, element.discountOnShippingStr)
                tvInvoiceShopItemShippingDiscountValue.visible()
                tvInvoiceShopItemShippingDiscount.visible()
            } ?: run {
                tvInvoiceShopItemShippingDiscountValue.gone()
                tvInvoiceShopItemShippingDiscount.gone()
            }


            element.shippingInsurancePriceStr?.let {
                tvInvoiceShopItemShippingInsuranceValue.text = getString(R.string.thankyou_rp_without_space, element.shippingInsurancePriceStr)
                tvInvoiceShopItemShippingInsuranceValue.visible()
                tvInvoiceShopItemShippingInsurance.visible()
            } ?: run {
                tvInvoiceShopItemShippingInsuranceValue.gone()
                tvInvoiceShopItemShippingInsurance.gone()
            }
            if(element.shippingAddress.isNullOrBlank()){
                tvInvoiceShopShippingAddressValue.gone()
                tvInvoiceShopShippingAddress.gone()
            }else{
                tvInvoiceShopShippingAddressValue.text = element.shippingAddress
                tvInvoiceShopShippingAddressValue.visible()
                tvInvoiceShopShippingAddress.visible()
            }

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
        shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemNameTotalPrice).text = getString(R.string.thankyou_rp_without_space, orderedItem.itemTotalPriceStr)

        shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemNameCountPrice)
                .text = itemView.context.getString(R.string.thank_invoice_item_count_price, orderedItem.itemCount, orderedItem.itemPrice)
        if (orderedItem.isBBIProduct)
            shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemNameCountPrice)
                    .append("\n${getString(R.string.thank_bbi_cash_back)}")
        return shopItemView
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_shop_invoice
    }
}