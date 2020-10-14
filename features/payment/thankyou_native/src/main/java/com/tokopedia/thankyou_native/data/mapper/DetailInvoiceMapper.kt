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
       // var totalServiceFeeStr: String? = null
        val benefitMapList = arrayListOf<BenefitMap>()

        thanksPageData.paymentItems?.forEach {
            when (it.itemName) {
                PaymentItemKey.SERVICE_FEE -> totalServiceFeeStr = it.amountStr
            }
        }

        thanksPageData.paymentDeductions?.forEach {
            when (it.itemName) {
                PaymentDeductionKey.CASH_BACK_OVO_POINT -> benefitMapList.add(BenefitMap(it.itemDesc, it.amountStr))
                PaymentDeductionKey.POTENTIAL_CASH_BACK -> benefitMapList.add(BenefitMap(it.itemDesc, it.amountStr, true))
            }
        }

        visitableList.add(getInvoiceSummary())

        totalServiceFeeStr?.let {
            val billDetail = TotalFee(thanksPageData.orderAmountStr, null, totalServiceFeeStr)
            visitableList.add(billDetail)
        }

        visitableList.add(getPaymentInfo())

        if (benefitMapList.isNotEmpty()) {
            val obtainedAfterTransaction = ObtainedAfterTransaction(benefitMapList)
            visitableList.add(obtainedAfterTransaction)
        }

    }

    private fun getTotalFee(){
        thanksPageData.paymentItems?.filter {
            it.itemName == PaymentItemKey.SERVICE_FEE
        }?.forEach {
            invoiceSummaryMapList.add(InvoiceSummaryMap(it.itemDesc, it.amountStr))
        }


    }

    private fun getPaymentInfo(): PaymentInfo {
        val paymentModeMapList = arrayListOf<PaymentModeMap>()
        thanksPageData.paymentDeductions?.filter {
            it.itemName == PaymentDeductionKey.REWARDS_POINT
        }?.forEach {
            when (it.itemName) {
                PaymentDeductionKey.REWARDS_POINT -> paymentModeMapList.add(PaymentModeMap(it.itemDesc, it.amountStr, null))
            }
        }
        thanksPageData.paymentDetails?.forEach { paymentDetail ->
            paymentModeMapList.add(PaymentModeMap(paymentDetail.gatewayName,
                    paymentDetail.amountStr, paymentDetail.gatewayCode))
        }

        return PaymentInfo(thanksPageData.amountStr, paymentModeList = paymentModeMapList)
    }

    private fun getInvoiceSummary(): InvoiceSummery {
        var totalPrice = 0F
        var totalItemCount = 0
        var invoiceSummaryMapList = arrayListOf<InvoiceSummaryMap>()

        thanksPageData.shopOrder.forEach { shopOrder ->
            shopOrder.purchaseItemList.forEach {
                totalPrice += it.totalPrice
                totalItemCount += 1
            }
        }

        thanksPageData.paymentItems?.filter {
            it.itemName != PaymentItemKey.SERVICE_FEE
        }?.forEach {
            invoiceSummaryMapList.add(InvoiceSummaryMap(it.itemDesc, it.amountStr))
        }

        thanksPageData.paymentDeductions?.filter {
            (it.itemName == PaymentDeductionKey.TOTAL_SHIPPING_DISCOUNT) ||
                    (it.itemName == PaymentDeductionKey.TOTAL_DISCOUNT)

        }?.forEach {
            invoiceSummaryMapList.add(InvoiceSummaryMap(it.itemDesc, it.amountStr))
        }

        return InvoiceSummery(totalPrice.toString(), totalItemCount, invoiceSummaryMapList)

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
                        purchasedItem.priceStr, purchasedItem.totalPriceStr, purchasedItem.isBBIProduct))
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
    const val SERVICE_FEE = "total_fee"
}

object PaymentDeductionKey {
    const val TOTAL_SHIPPING_DISCOUNT = "total_logistic_discount"
    const val TOTAL_DISCOUNT = "total_discount"
    const val REWARDS_POINT = "rewards_point"
    const val CASH_BACK_OVO_POINT = "cashback"
    const val POTENTIAL_CASH_BACK = "potential_cashback"
}
/*

--- Total Harga --- PREV

---- Item Paymetn iTem - except Total Fee

---- Payment Deduction ---
PaymentDeductionKey.TOTAL_SHIPPING_DISCOUNT -> totalShippingDiscountStr = it.amountStr
PaymentDeductionKey.TOTAL_DISCOUNT -> totalDiscountStr = it.amountStr
*/


/*
* Total Tagihan
*
* -- Payment ITEM ---- Total Fee
*
*
* */

/*
* Total Bayar
*
*
                PaymentDeductionKey.REWARDS_POINT -> paymentModeMapList.add(PaymentModeMap(it.itemDesc, it.amountStr, null))
                *
                * thanksPageData.paymentDetails?.forEach { paymentDetail ->
            paymentModeMapList.add(PaymentModeMap(paymentDetail.gatewayName, paymentDetail.amountStr, paymentDetail.gatewayCode))
        }

*
* */


//total_fee
