package com.tokopedia.thankyou_native.presentation.adapter.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.domain.model.AddOnItem
import com.tokopedia.thankyou_native.presentation.adapter.factory.InvoiceTypeFactory

data class InvoiceSummery(
    val totalPriceStr: String,
    val totalCount: Int,
    val invoiceSummaryMapList: ArrayList<InvoiceSummaryMap>
) : Visitable<InvoiceTypeFactory> {
    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class InvoiceSummaryMap(
    val title: String,
    val value: String,
    var isDiscounted: Boolean = false
)

data class TotalFee(
    val totalBillAmountStr: String,
    val feeDetailList: ArrayList<FeeDetail>
) : Visitable<InvoiceTypeFactory> {
    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class FeeDetail(
    val feeTitle: String,
    val feeAmountStr: String,
    val showToolTip: String? = null,
    val tooltipTitle: String? = null,
    val tooltipDesc: String? = null
)

data class CashBackEarned(
    val benefitMapList: ArrayList<CashBackMap>,
    val cashBackOVOPoint: Boolean
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

data class CashBackMap(
    val benefitName: String,
    val benefitAmount: String,
    val cashBackDescription: String?,
    var isBBICashBack: Boolean = false,
    var isStackedCashBack: Boolean = false
)

data class PaymentModeMap(
    val paymentModeStr: String,
    val paidAmountStr: String,
    val gatewayCode: String?
)


class PurchasedProductTag : Visitable<InvoiceTypeFactory> {
    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }
}

class ShopDivider : Visitable<InvoiceTypeFactory> {
    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }

}

data class ShopInvoice(
    val shopName: String?,
    val orderedItem: List<OrderedItem>,
    val itemDiscountStr: String?,
    val productProtectionStr: String?,
    val shippingPriceStr: String?,
    val shippingInfo: String?,
    val discountOnShippingStr: String?,
    val shippingInsurancePriceStr: String?,
    val shippingAddress: String?,
    val orderLevelAddOn: OrderLevelAddOn,
    val shouldHideShopInvoice: Boolean,
    val shouldHideDivider: Boolean,
) : Visitable<InvoiceTypeFactory> {
    override fun type(typeFactory: InvoiceTypeFactory): Int {
        return typeFactory.type(this)
    }
}

data class OrderLevelAddOn(
    val addOnSectionDescription: String?,
    val addOnList: ArrayList<AddOnItem>
)

data class OrderedItem(
    val itemName: String,
    val itemVariant: String?,
    val itemCount: Int?,
    val itemPrice: String,
    val itemTotalPriceStr: String,
    val isBBIProduct: Boolean,
    val orderItemType: OrderItemType,
    val productLevelAddOn: ArrayList<AddOnItem>?
)

enum class OrderItemType {
    BUNDLE,
    SINGLE_PRODUCT,
    BUNDLE_PRODUCT
}
