package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.model.*

class DetailInvoiceMapper(val thanksPageData: ThanksPageData) {


    private val visitableList = arrayListOf<Visitable<*>>()

    fun getDetailedInvoice(): ArrayList<Visitable<*>> {
        createInvoiceSummary(thanksPageData)
        createShopsSummery(thanksPageData)
        return visitableList
    }

    private fun createInvoiceSummary(thanksPageData: ThanksPageData) {


        var totalPrice: Float = 0F
        var totalItemCount: Int = 0

        var totalProductProtectionStr: String? = null
        var totalDiscountStr: String? = null
        var totalShippingStr: String? = null
        var totalShippingDiscountStr: String? = null
        var totalShippingInsurance: String? = null
        var donationStr: String? = null
        var eGold: String? = null

        var totalServiceFeeStr: String? = null


        val paymentModeMapList = arrayListOf<PaymentModeMap>()
        val benefitMapList = arrayListOf<BenefitMap>()

        thanksPageData.orderList.forEach { orderList ->
            orderList.purchaseItemList.forEach {
                totalPrice += it.totalPrice
                totalItemCount += 1
            }
        }

        thanksPageData.paymentItems?.forEach {
            when (it.itemName) {
                PaymentItemKey.PROTECTION_PLAN -> totalProductProtectionStr = it.amountStr
                PaymentItemKey.E_GOLD -> eGold = it.amountStr
                PaymentItemKey.DONATION -> donationStr = it.amountStr
                PaymentItemKey.SERVICE_FEE -> totalServiceFeeStr = it.amountStr
                PaymentItemKey.TOTAL_SHIPPING -> totalShippingStr = it.amountStr
                PaymentItemKey.TOTAL_SHIPPING_INSURANCE -> totalShippingInsurance = it.amountStr
            }
        }

        thanksPageData.paymentDeductions?.forEach {
            when (it.itemName) {
                PaymentDeductionKey.PAID_BY_SALDO -> paymentModeMapList.add(PaymentModeMap(it.itemDesc, it.amountStr))
                PaymentDeductionKey.PAID_BY_OVO_CASH -> paymentModeMapList.add(PaymentModeMap(it.itemDesc, it.amountStr))
                PaymentDeductionKey.PAID_BY_OVO_POINT -> paymentModeMapList.add(PaymentModeMap(it.itemDesc, it.amountStr))
                PaymentDeductionKey.TOTAL_SHIPPING_DISCOUNT -> totalShippingDiscountStr = it.amountStr
                PaymentDeductionKey.TOTAL_DISCOUNT -> totalDiscountStr = it.amountStr
                //PaymentDeductionKey.REWARDS_POINT -> obtainedTokoPointStr = it.amountStr
                PaymentDeductionKey.CASH_BACK_OVO_POINT -> benefitMapList.add(BenefitMap(it.itemDesc, it.amountStr))
            }
        }
        visitableList.add(PaymentMethodModel(thanksPageData.gatewayName))
        val invoiceSummery = InvoiceSummery(
                totalItemCount = totalItemCount.toString(),
                totalPriceStr = CurrencyFormatUtil.convertPriceValue(totalPrice.toDouble(), true),
                totalItemDiscountStr = totalDiscountStr,
                totalProductProtectionStr = totalProductProtectionStr,
                totalShippingChargeStr = totalShippingStr,
                totalShippingDiscountStr = totalShippingDiscountStr,
                totalShippingInsuranceStr = totalShippingInsurance,
                donationAmountStr = donationStr,
                eGoldPriceStr = eGold)

        visitableList.add(invoiceSummery)

        val billDetail = BillDetail(thanksPageData.amountStr, null, totalServiceFeeStr)
        visitableList.add(billDetail)

        val paymentInfo = PaymentInfo(thanksPageData.amountStr, paymentModeList = paymentModeMapList)
        visitableList.add(paymentInfo)

        if (benefitMapList.isNotEmpty()) {
            val obtainedAfterTransaction = ObtainedAfterTransaction(benefitMapList)
            visitableList.add(obtainedAfterTransaction)
        }

    }

    private fun createShopsSummery(thanksPageData: ThanksPageData) {
        thanksPageData.orderList.forEach { orderList ->
            val orderedItemList = arrayListOf<OrderedItem>()
            orderList.purchaseItemList.forEach { purchasedItem ->
                orderedItemList.add(OrderedItem(purchasedItem.productName, purchasedItem.quantity,
                        purchasedItem.priceStr, purchasedItem.totalPriceStr))
            }

            var logisticDiscountStr: String? = null

            orderList.promoData?.forEach {
                when (it.promoCode) {
                    PromoDataKey.LOGISTIC -> logisticDiscountStr = it.totalDiscountStr
                }
            }
            visitableList.add(PurchasedProductTag())
            val shopInvoice = ShopInvoice(
                    orderList.storeName,
                    orderedItemList,
                    null,//todo not available
                    if (orderList.insuranceAmount > 0F) orderList.insuranceAmountStr else null,
                    if (orderList.shippingAmount > 0F) orderList.shippingAmountStr else null,
                    orderList.logisticType,
                    logisticDiscountStr,
                    null,//todo no available
                    orderList.address)

            visitableList.add(shopInvoice)
        }
    }

}

object PromoDataKey {
    const val LOGISTIC = "logistic"
}

object PaymentItemKey {
    const val MACRO_INSURANCE = "macroinsurance"
    const val PROTECTION_PLAN = "protection_plan"
    const val E_GOLD = "egold"
    const val DONATION = "donation"
    const val SERVICE_FEE = "total_fee"
    const val TOTAL_SHIPPING_INSURANCE = "total_insurance_fee"
    const val TOTAL_SHIPPING = "total_shipping"
}

object PaymentDeductionKey {
    const val PAID_BY_SALDO = "saldo"
    const val PAID_BY_OVO_CASH = "ovocash"
    const val PAID_BY_OVO_POINT = "ovopoint"
    const val TOTAL_SHIPPING_DISCOUNT = "total_logistic_discount"
    const val TOTAL_DISCOUNT = "total_discount"
    const val REWARDS_POINT = "rewards_point"
    const val CASH_BACK_OVO_POINT = "cashback"
}
