package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.model.*
import com.tokopedia.utils.text.currency.CurrencyFormatHelper

class DetailInvoiceMapper(val thanksPageData: ThanksPageData) {

    private val visitableList = arrayListOf<Visitable<*>>()

    fun getDetailedInvoice(): ArrayList<Visitable<*>> {
        addInvoiceSummary()
        addTotalFee()
        addPaymentInfo()
        addCashBackEarned()
        createShopsSummery(thanksPageData)
        return visitableList
    }

    private fun addInvoiceSummary() {
        var totalPrice = 0F
        var totalItemCount = 0
        val invoiceSummaryMapList = arrayListOf<InvoiceSummaryMap>()

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
            invoiceSummaryMapList.add(InvoiceSummaryMap(it.itemDesc, it.amountStr, true))
        }
        val totalPriceStr = CurrencyFormatHelper.convertToRupiah(totalPrice.toString())
        //CurrencyFormatUtil.convertPriceValue(totalPrice.toDouble(), false)
        visitableList.add(InvoiceSummery(totalPriceStr, totalItemCount, invoiceSummaryMapList))
    }

    private fun addTotalFee() {
        var totalFee: String? = null
        thanksPageData.paymentItems?.filter {
            it.itemName == PaymentItemKey.SERVICE_FEE
        }?.forEach {
            totalFee = it.amountStr
        }
        totalFee?.let {
            visitableList.add(TotalFee(thanksPageData.orderAmountStr, totalFee.toString()))
        }
    }

    private fun addPaymentInfo() {
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
        visitableList.add(PaymentInfo(thanksPageData.amountStr, paymentModeList = paymentModeMapList))
    }

    private fun addCashBackEarned() {
        val cashBackMapList = arrayListOf<CashBackMap>()
        thanksPageData.paymentDeductions?.forEach {
            when (it.itemName) {
                PaymentDeductionKey.CASH_BACK_OVO_POINT -> cashBackMapList.add(CashBackMap(it.itemDesc, it.amountStr))
                PaymentDeductionKey.POTENTIAL_CASH_BACK -> cashBackMapList.add(CashBackMap(it.itemDesc, it.amountStr, true))
            }
        }
        if (cashBackMapList.isNotEmpty()) {
            val cashBackEarned = CashBackEarned(cashBackMapList)
            visitableList.add(cashBackEarned)
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

            val shippingDurationOrETA = if (shopOrder.logisticETA.isNullOrBlank()) {
                if (shopOrder.logisticDuration.isNullOrBlank()) "" else shopOrder.logisticDuration
            } else shopOrder.logisticETA

            val shippingInfo = if (shippingDurationOrETA.isBlank())
                shopOrder.logisticType
            else shopOrder.logisticType + "\n" + shippingDurationOrETA

            val shopInvoice = ShopInvoice(
                    shopOrder.storeName,
                    orderedItemList,
                    discountFromMerchant,
                    if (totalProductProtectionForShop > 0.0)
                        CurrencyFormatHelper.convertToRupiah(totalProductProtectionForShop.toString())
                        ///CurrencyFormatUtil.convertPriceValue(totalProductProtectionForShop,false)
                    else null,
                    if (shopOrder.shippingAmount > 0F) shopOrder.shippingAmountStr else null,
                    shippingInfo,
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
    const val E_GOLD = "egold"
    const val DONATION = "donation"
}

object PaymentDeductionKey {
    const val TOTAL_SHIPPING_DISCOUNT = "total_logistic_discount"
    const val TOTAL_DISCOUNT = "total_discount"
    const val REWARDS_POINT = "rewards_point"
    const val CASH_BACK_OVO_POINT = "cashback"
    const val POTENTIAL_CASH_BACK = "potential_cashback"
}

