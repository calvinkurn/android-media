package com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.displayTextOrHide
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.model.OrderItemType
import com.tokopedia.thankyou_native.presentation.adapter.model.OrderedItem
import com.tokopedia.thankyou_native.presentation.adapter.model.ShopInvoice
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import kotlinx.android.synthetic.main.thank_widget_shop_invoice.view.*

class ShopInvoiceViewHolder(val view: View) : AbstractViewHolder<ShopInvoice>(view) {

    private lateinit var inflater: LayoutInflater

    private val tvShopName: TextView = view.tvInvoiceShopName
    private val llItemContainer = view.llShopItemContainer
    private val addOnItemContainer = view.llAddOnContainer

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

    private val divider = view.dividerShopShipping

    override fun bind(element: ShopInvoice?) {
        element?.let {
            if(element.shopName.isNullOrBlank()){
                tvShopName.gone()

            }else{
                tvShopName.visible()
                tvShopName.text = element.shopName

            }

            addShopItems(llItemContainer, shopInvoice = element)
            addOrderLevelAddOn(addOnItemContainer, element)

            element.itemDiscountStr?.let {
                tvInvoiceShopDiscountValue.text = getString(R.string.thankyou_discounted, element.itemDiscountStr)
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

            if (element.shippingPriceStr != null && !element.shouldHideShopInvoice) {
                tvInvoiceShopItemShippingValue.text = element.shippingPriceStr
                tvInvoiceShopItemShippingValue.visible()
                tvInvoiceShopItemShipping.visible()
            } else {
                tvInvoiceShopItemShippingValue.gone()
                tvInvoiceShopItemShipping.gone()
            }

            if (element.shippingInfo != null && !element.shouldHideShopInvoice) {
                if(element.shippingInfo.isNotEmpty()) {
                    tvInvoiceShopItemCourier.text = element.shippingInfo
                    tvInvoiceShopItemCourier.visible()
                }else{
                    tvInvoiceShopItemCourier.gone()
                }
            } else {
                tvInvoiceShopItemCourier.gone()
            }

            if (element.discountOnShippingStr != null && !element.shouldHideShopInvoice) {
                tvInvoiceShopItemShippingDiscountValue.text = getString(R.string.thankyou_discounted, element.discountOnShippingStr)
                tvInvoiceShopItemShippingDiscountValue.visible()
                tvInvoiceShopItemShippingDiscount.visible()
            } else {
                tvInvoiceShopItemShippingDiscountValue.gone()
                tvInvoiceShopItemShippingDiscount.gone()
            }

            if (element.shippingInsurancePriceStr != null && !element.shouldHideShopInvoice) {
                tvInvoiceShopItemShippingInsuranceValue.text = element.shippingInsurancePriceStr
                tvInvoiceShopItemShippingInsuranceValue.visible()
                tvInvoiceShopItemShippingInsurance.visible()
            } else {
                tvInvoiceShopItemShippingInsuranceValue.gone()
                tvInvoiceShopItemShippingInsurance.gone()
            }

            if(element.shippingAddress.isNullOrBlank() || element.shouldHideShopInvoice){
                tvInvoiceShopShippingAddressValue.gone()
                tvInvoiceShopShippingAddress.gone()
            }else{
                tvInvoiceShopShippingAddressValue.text = element.shippingAddress
                tvInvoiceShopShippingAddressValue.visible()
                tvInvoiceShopShippingAddress.visible()
            }

            if (it.shouldHideDivider) divider.gone() else divider.show()
        }
    }

    private fun addOrderLevelAddOn(container: LinearLayout, shopInvoice: ShopInvoice) {
        container.visible()
        if (!shopInvoice.orderLevelAddOn.addOnSectionDescription.isNullOrBlank()) {
            val descText = addOnDescriptionView(shopInvoice.orderLevelAddOn.addOnSectionDescription)
            val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            lp.topMargin = 12.toPx()
            container.addView(descText)
            descText.layoutParams = lp
            descText.requestLayout()
        }
        shopInvoice.orderLevelAddOn.addOnList.forEach { addOn ->
            val orderLevel= addOrderLevelGifting(container, addOn.name, addOn.addOnPrice)
            container.addView(orderLevel)
        }
    }

    private fun addShopItems(container: LinearLayout, shopInvoice: ShopInvoice) {
        container.removeAllViews()
        shopInvoice.orderedItem.forEach { orderItem ->
            val view = createShopItemView(context = view.context, orderedItem = orderItem)
            container.addView(view)
            orderItem.productLevelAddOn?.forEach { addOn ->
                val productLevel = addOrderLevelGifting(container, addOn.name, addOn.addOnPrice)
                container.addView(productLevel)
            }
        }

    }

    private fun addOnDescriptionView(addOnSectionDescription: String): View {
        val descText = Typography(itemView.context)
        descText.text = addOnSectionDescription
        descText.setType(Typography.DISPLAY_3)
        descText.setWeight(Typography.BOLD)
        descText.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96))
        return descText
    }

    private fun addOrderLevelGifting(parent: LinearLayout, titleStr: String, value: String): View {
        val rowView = inflater.inflate(R.layout.thank_payment_mode_item, parent, false)
        val tvTitle = rowView.findViewById<TextView>(R.id.tvInvoicePaymentModeName)
        val tvValue = rowView.findViewById<TextView>(R.id.tvInvoicePaidWithModeValue)
        tvTitle.setTextColor(ContextCompat.getColor(view.context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
        tvTitle.text = titleStr
        tvValue.text = getString(R.string.thankyou_rp_without_space, value)
        return rowView
    }

    private fun createShopItemView(context: Context, orderedItem: OrderedItem): View {
        if (!::inflater.isInitialized)
            inflater = LayoutInflater.from(context)

        val shopItemView = inflater.inflate(R.layout.thank_widget_shop_item, null, false)
        val titleView = shopItemView.findViewById<Typography>(R.id.tvInvoiceShopItemName)
        val foodDescriptionView = shopItemView.findViewById<Typography>(R.id.tvGoFooVariantDescription)
        val totalPriceView = shopItemView.findViewById<Typography>(R.id.tvInvoiceShopItemNameTotalPrice)
        titleView.text = orderedItem.itemName
        foodDescriptionView.displayTextOrHide(orderedItem.itemVariant.orEmpty())
        totalPriceView.text = getString(R.string.thankyou_rp_without_space, orderedItem.itemTotalPriceStr)

        when(orderedItem.orderItemType) {
            OrderItemType.BUNDLE_PRODUCT -> {
                titleView.setWeight(Typography.REGULAR)
                shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemNameCountPrice)
                    .text = itemView.context.getString(
                    R.string.thank_invoice_item_count_price,
                    orderedItem.itemCount,
                    orderedItem.itemPrice
                )
            }

            OrderItemType.BUNDLE -> {
                titleView.setTextColor(MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                totalPriceView.setWeight(Typography.BOLD)
                shopItemView.findViewById<ImageUnify>(R.id.ivProductBundle).visible()
                shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemNameCountPrice).gone()
            }
            else -> {
                shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemNameCountPrice)
                    .text = itemView.context.getString(
                    R.string.thank_invoice_item_count_price,
                    orderedItem.itemCount,
                    orderedItem.itemPrice
                )
            }
        }

        if (orderedItem.isBBIProduct)
            shopItemView.findViewById<TextView>(R.id.tvInvoiceShopItemNameCountPrice)
                    .append("\n${getString(R.string.thank_bbi_cash_back)}")
        return shopItemView
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_widget_shop_invoice
    }
}
