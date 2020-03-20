package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.presentation.adapter.InvoiceTypeFactory

data class InvoiceSummery(
        val totalItemCount: String,
        val totalPriceStr: String,
        val totalItemDiscountStr: String,
        val totalProductProtectionStr: String,
        val totalShippingChargeStr: String,
        val totalShippingDiscountStr: String,
        val totalShippingInsuranceStr: String,
        val donationAmountStr: String,
        val eGoldPriceStr: String
) : Visitable<InvoiceTypeFactory> {
    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }
}


data class BillDetail(val totalBillAmountStr: String,
                      val tokoPointDeduction: String?,
                      val serviceFee: String
) : Visitable<InvoiceTypeFactory> {
    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class ObtainedAfterTransaction(
        val benefitName: String,
        val benefitAmount: String
) : Visitable<InvoiceTypeFactory> {
    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }

}

data class PaymentInfo(
        val totalAmountPaidStr: String,
        val paymentModeList: ArrayList<PaymentModeMap>?
) : Visitable<InvoiceTypeFactory> {

    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }

}

data class PaymentModeMap(
        val paymentModeStr: String,
        val paidAmountStr: String
)

data class ShopInvoice(
        val shopName: String,
        val orderedItem: List<OrderedItem>,
        val totalPriceStr: String,
        val itemDiscountStr: String,
        val productProtectionStr: String,
        val shippingPriceStr: String,
        val shippingTypeStr: String,
        val discountOnShippingStr: String,
        val shippingInsurancePriceStr: String,
        val shippingAddress: String
) : Visitable<InvoiceTypeFactory> {
    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class OrderedItem(
        val itemName: String,
        val itemCountAndPriceStr: String,
        val itemPriceStr: String
)