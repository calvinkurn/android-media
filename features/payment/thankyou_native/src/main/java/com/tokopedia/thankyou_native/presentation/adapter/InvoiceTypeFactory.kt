package com.tokopedia.thankyou_native.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.presentation.adapter.model.*
import com.tokopedia.thankyou_native.presentation.adapter.viewholder.*

class InvoiceTypeFactory : BaseAdapterTypeFactory() {

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            InvoiceSummaryViewHolder.LAYOUT_ID -> return InvoiceSummaryViewHolder(parent!!)
            ShopInvoiceViewHolder.LAYOUT_ID -> return ShopInvoiceViewHolder(parent!!)
        }
        return super.createViewHolder(parent, type)
    }

    fun type(invoiceSummery: InvoiceSummery): Int {
        return InvoiceSummaryViewHolder.LAYOUT_ID
    }

    fun type(orderDataByShop: ShopInvoice): Int {
        return ShopInvoiceViewHolder.LAYOUT_ID
    }

    fun type(billDetail: BillDetail): Int {
        return BillDetailViewHolder.LAYOUT_ID

    }

    fun type(paymentInfo: PaymentInfo): Int {
        return PaymentInfoViewHolder.LAYOUT_ID
    }

    fun type(obtainedAfterTransaction: ObtainedAfterTransaction): Int {
        return ObtainedBenefitViewHolder.LAYOUT_ID
    }

}