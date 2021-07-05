package com.tokopedia.thankyou_native.presentation.adapter.factory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.model.*
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.invoice.*

class InvoiceTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            InvoiceSummaryViewHolder.LAYOUT_ID -> return InvoiceSummaryViewHolder(parent!!)
            ShopInvoiceViewHolder.LAYOUT_ID -> return ShopInvoiceViewHolder(parent!!)
            BillDetailViewHolder.LAYOUT_ID -> return BillDetailViewHolder(parent!!)
            PaymentInfoViewHolder.LAYOUT_ID -> return PaymentInfoViewHolder(parent!!)
            ObtainedBenefitViewHolder.LAYOUT_ID -> return ObtainedBenefitViewHolder(parent!!)
            PurchasedProductTagViewHolder.LAYOUT_ID -> return PurchasedProductTagViewHolder(parent!!)
            ShopDividerViewHolder.LAYOUT_ID -> return ShopDividerViewHolder(parent!!)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(invoiceSummery: InvoiceSummery): Int {
        return InvoiceSummaryViewHolder.LAYOUT_ID
    }

    fun type(shopInvoice: ShopInvoice): Int {
        return ShopInvoiceViewHolder.LAYOUT_ID
    }

    fun type(totalFee: TotalFee): Int {
        return BillDetailViewHolder.LAYOUT_ID
    }

    fun type(paymentInfo: PaymentInfo): Int {
        return PaymentInfoViewHolder.LAYOUT_ID
    }

    fun type(cashBackEarned: CashBackEarned): Int {
        return ObtainedBenefitViewHolder.LAYOUT_ID
    }

    fun type(purchasedProductTag: PurchasedProductTag): Int {
        return PurchasedProductTagViewHolder.LAYOUT_ID
    }

    fun type(shopDivider: ShopDivider) : Int{
        return ShopDividerViewHolder.LAYOUT_ID
    }

}