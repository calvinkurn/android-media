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

        var totalPrice = 0F
        var totalItemCount = 0

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

        thanksPageData.shopOrder.forEach { shopOrder ->
            shopOrder.purchaseItemList.forEach {
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
                PaymentDeductionKey.TOTAL_SHIPPING_DISCOUNT -> totalShippingDiscountStr = it.amountStr
                PaymentDeductionKey.TOTAL_DISCOUNT -> totalDiscountStr = it.amountStr
                PaymentDeductionKey.REWARDS_POINT -> paymentModeMapList.add(PaymentModeMap(it.itemDesc, it.amountStr))
                PaymentDeductionKey.CASH_BACK_OVO_POINT -> benefitMapList.add(BenefitMap(it.itemDesc, it.amountStr))
                PaymentDeductionKey.POTENTIAL_CASH_BACK -> benefitMapList.add(BenefitMap(it.itemDesc, it.amountStr))
            }
        }
        visitableList.add(PaymentMethodModel(thanksPageData.gatewayName))
        val invoiceSummery = InvoiceSummery(
                totalItemCount = totalItemCount.toString(),
                totalPriceStr = CurrencyFormatUtil.convertPriceValue(totalPrice.toDouble(), false),
                totalItemDiscountStr = totalDiscountStr,
                totalProductProtectionStr = totalProductProtectionStr,
                totalShippingChargeStr = totalShippingStr,
                totalShippingDiscountStr = totalShippingDiscountStr,
                totalShippingInsuranceStr = totalShippingInsurance,
                donationAmountStr = donationStr,
                eGoldPriceStr = eGold)

        visitableList.add(invoiceSummery)

        totalServiceFeeStr?.let {
            val billDetail = BillDetail(thanksPageData.orderAmountStr, null, totalServiceFeeStr)
            visitableList.add(billDetail)
        }

        thanksPageData.paymentDetails?.forEach { paymentDetail ->
            paymentModeMapList.add(PaymentModeMap(paymentDetail.gatewayName, paymentDetail.amountStr))
        }

        val paymentInfo = PaymentInfo(thanksPageData.amountStr, paymentModeList = paymentModeMapList)
        visitableList.add(paymentInfo)

        if (benefitMapList.isNotEmpty()) {
            val obtainedAfterTransaction = ObtainedAfterTransaction(benefitMapList)
            visitableList.add(obtainedAfterTransaction)
        }

    }

    private fun createShopsSummery(thanksPageData: ThanksPageData) {
        if (thanksPageData.shopOrder.isNotEmpty())
            visitableList.add(PurchasedProductTag())
        var currentIndex = 0
        thanksPageData.shopOrder.forEach { shopOrder ->
            if (currentIndex > 0)
                visitableList.add(ShopDivider())
            val orderedItemList = arrayListOf<OrderedItem>()
            var totalProductProtectionForShop = 0.0
            shopOrder.purchaseItemList.forEach { purchasedItem ->
                orderedItemList.add(OrderedItem(purchasedItem.productName, purchasedItem.quantity,
                        purchasedItem.priceStr, purchasedItem.totalPriceStr))
                totalProductProtectionForShop += purchasedItem.productPlanProtection
            }

            var logisticDiscountStr: String? = null
            var discountFromMerchant: String? = null

            shopOrder.promoData?.forEach {
                when (it.promoCode) {
                    PromoDataKey.LOGISTIC -> logisticDiscountStr = it.totalDiscountStr
                    PromoDataKey.MERCHANT -> discountFromMerchant = it.totalDiscountStr
                }
            }

            val shopInvoice = ShopInvoice(
                    shopOrder.storeName,
                    orderedItemList,
                    discountFromMerchant,
                    if (totalProductProtectionForShop > 0.0)
                        CurrencyFormatUtil.convertPriceValue(totalProductProtectionForShop,
                                false)
                    else null,
                    if (shopOrder.shippingAmount > 0F) shopOrder.shippingAmountStr else null,
                    shopOrder.logisticType,
                    logisticDiscountStr,
                    if (shopOrder.insuranceAmount > 0F) shopOrder.insuranceAmountStr else null,
                    shopOrder.address)

            visitableList.add(shopInvoice)
            currentIndex++
        }
    }

}

object PromoDataKey {
    const val LOGISTIC = "logistic"
    const val MERCHANT = "merchant"
}

object PaymentItemKey {
    const val PROTECTION_PLAN = "purchase_plan_protection"
    const val E_GOLD = "egold"
    const val DONATION = "donation"
    const val SERVICE_FEE = "total_fee"
    const val TOTAL_SHIPPING_INSURANCE = "total_insurance_fee"
    const val TOTAL_SHIPPING = "total_shipping"
}

object PaymentDeductionKey {
    const val TOTAL_SHIPPING_DISCOUNT = "total_logistic_discount"
    const val TOTAL_DISCOUNT = "total_discount"
    const val REWARDS_POINT = "rewards_point"
    const val CASH_BACK_OVO_POINT = "cashback"
    const val POTENTIAL_CASH_BACK = "potential_cashback"
}
