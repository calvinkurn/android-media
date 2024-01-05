package com.tokopedia.thankyou_native.data.mapper

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.thankyou_native.data.mapper.PaymentDeductionKey.NEW_LINE
import com.tokopedia.thankyou_native.data.mapper.PaymentDeductionKey.PREV_ORDER_AMOUNT_VA
import com.tokopedia.thankyou_native.data.mapper.PaymentDeductionKey.THANK_STACKED_CASHBACK_TITLE
import com.tokopedia.thankyou_native.domain.model.AddOnItem
import com.tokopedia.thankyou_native.domain.model.BundleGroupItem
import com.tokopedia.thankyou_native.domain.model.OrderGroupList
import com.tokopedia.thankyou_native.domain.model.PromoData
import com.tokopedia.thankyou_native.domain.model.PurchaseItem
import com.tokopedia.thankyou_native.domain.model.ShopOrder
import com.tokopedia.thankyou_native.domain.model.ThanksPageData
import com.tokopedia.thankyou_native.presentation.adapter.model.CashBackEarned
import com.tokopedia.thankyou_native.presentation.adapter.model.CashBackMap
import com.tokopedia.thankyou_native.presentation.adapter.model.FeeDetail
import com.tokopedia.thankyou_native.presentation.adapter.model.InvoiceSummaryMap
import com.tokopedia.thankyou_native.presentation.adapter.model.InvoiceSummery
import com.tokopedia.thankyou_native.presentation.adapter.model.OrderItemType
import com.tokopedia.thankyou_native.presentation.adapter.model.OrderLevelAddOn
import com.tokopedia.thankyou_native.presentation.adapter.model.OrderedItem
import com.tokopedia.thankyou_native.presentation.adapter.model.PaymentInfo
import com.tokopedia.thankyou_native.presentation.adapter.model.PaymentModeMap
import com.tokopedia.thankyou_native.presentation.adapter.model.PurchasedProductTag
import com.tokopedia.thankyou_native.presentation.adapter.model.ShopInvoice
import com.tokopedia.thankyou_native.presentation.adapter.model.TotalFee
import com.tokopedia.utils.currency.CurrencyFormatUtil

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
        var totalPrice = 0.0
        var totalItemCount = Int.ZERO
        val invoiceSummaryMapList = arrayListOf<InvoiceSummaryMap>()

        thanksPageData.shopOrder.forEach { shopOrder ->
            shopOrder.purchaseItemList.forEach {
                totalPrice += it.totalPrice
                totalItemCount += it.quantity
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
        val totalPriceStr = CurrencyFormatUtil.convertPriceValueToIdrFormat(totalPrice, false)
        visitableList.add(InvoiceSummery(totalPriceStr, totalItemCount, invoiceSummaryMapList))
    }

    private fun addTotalFee() {
        val totalFee = TotalFee(thanksPageData.orderAmountStr, arrayListOf())
        var fee = 0L
        thanksPageData.feeDetailList?.forEach {
            fee += it.amount
            val formattedAmountStr = CurrencyFormatUtil.convertPriceValueToIdrFormat(it.amount, false)
            totalFee.feeDetailList.add(FeeDetail(it.name, formattedAmountStr, it.showTooltip, it.tooltipTitle, it.tooltipDesc))
        }
        if (thanksPageData.combinedAmount > Int.ZERO) {
            getPreviousVAOrderAmount(fee)?.let {
                totalFee.feeDetailList.add(it)
            }
        }
        if (totalFee.feeDetailList.isNotEmpty()) {
            visitableList.add(totalFee)
        }
    }

    private fun getPreviousVAOrderAmount(totalFee: Long): FeeDetail? {
        if (thanksPageData.combinedAmount > Int.ZERO) {
            val previousAmount = thanksPageData.combinedAmount - thanksPageData.amount
            if (previousAmount > Int.ZERO) {
                val formattedAmountStr = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                    previousAmount,
                    false
                )
                return FeeDetail(PREV_ORDER_AMOUNT_VA, formattedAmountStr)
            }
        }
        return null
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
            val amountStr = if (paymentDetail.amountCombine > Int.ZERO) {
                paymentDetail.amountCombineStr
            } else {
                paymentDetail.amountStr
            }
            paymentModeMapList.add(
                PaymentModeMap(
                    paymentDetail.gatewayName,
                    amountStr,
                    paymentDetail.gatewayCode
                )
            )
        }

        val totalPayment: String = if (thanksPageData.combinedAmount > Int.ZERO) {
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                thanksPageData.combinedAmount,
                false
            )
        } else {
            CurrencyFormatUtil.convertPriceValueToIdrFormat(
                thanksPageData.amount,
                false
            )
        }

        val paymentInfo = PaymentInfo(totalPayment, paymentModeList = paymentModeMapList)
        visitableList.add(paymentInfo)
    }

    private fun addCashBackEarned() {
        var cashBackMapList = arrayListOf<CashBackMap>()
        var isCashBackOVOPoint = false
        thanksPageData.paymentDeductions?.forEach {
            when (it.itemName) {
                PaymentDeductionKey.CASH_BACK_OVO_POINT -> {
                    isCashBackOVOPoint = true
                    cashBackMapList.add(
                        CashBackMap(
                            it.itemDesc,
                            it.amountStr,
                            null
                        )
                    )
                }
                PaymentDeductionKey.POTENTIAL_CASH_BACK -> cashBackMapList.add(
                    CashBackMap(
                        it.itemDesc,
                        it.amountStr,
                        null,
                        isBBICashBack = true
                    )
                )
                PaymentDeductionKey.CASHBACK_STACKED -> {
                    val cashBackMap = CashBackMap(
                        THANK_STACKED_CASHBACK_TITLE,
                        it.amountStr,
                        it.itemDesc,
                        isBBICashBack = false,
                        isStackedCashBack = true
                    )
                    if (cashBackMapList.size > Int.ZERO) {
                        val tempCashBackList = arrayListOf<CashBackMap>()
                        tempCashBackList.add(cashBackMap)
                        tempCashBackList.addAll(cashBackMapList)
                        cashBackMapList = tempCashBackList
                    } else {
                        cashBackMapList.add(cashBackMap)
                    }
                }
            }
        }
        if (cashBackMapList.isNotEmpty()) {
            val cashBackEarned = CashBackEarned(cashBackMapList, isCashBackOVOPoint)
            visitableList.add(cashBackEarned)
        }
    }

    /*
    * @param: bundleToProductMap: stores map of bundleId to OrderedItems with same bundle Id
    * to be rendered in view
    * @param: bundleMap: to enable O(1) access of bundleData while looping item_list from bundle_group_id
    * */
    private fun createShopsSummery(thanksPageData: ThanksPageData) {
        if (thanksPageData.shopOrder.isNotEmpty()) {
            visitableList.add(PurchasedProductTag())
        }
        var currentIndex = Int.ZERO
        val bundleToProductMap = mutableMapOf<String, ArrayList<OrderedItem>>()
        var bundleMap: MutableMap<String, BundleGroupItem>

        // Check if order group list size is 2 or more then we need to order shop order by order
        // group id, so it will be grouped when it rendered
        if (thanksPageData.orderGroupList.size > ONE) thanksPageData.shopOrder.sortBy { it.orderGroupId }

        thanksPageData.shopOrder.forEachIndexed { index, shopOrder ->
            val orderedItemList = arrayListOf<OrderedItem>()
            var totalProductProtectionForShop = 0.0

            // Map population
            bundleMap = shopOrder.bundleGroupList.associateBy({ it.groupId }, { it }).toMutableMap()

            // Map population
            shopOrder.purchaseItemList.forEach { purchasedItem ->
                val bundleGroupId = purchasedItem.bundleGroupId
                if (bundleGroupId.isNotEmpty()) {
                    if (bundleToProductMap.containsKey(bundleGroupId)) {
                        bundleToProductMap[bundleGroupId]?.add(createOrderItemFromPurchase(purchasedItem, purchasedItem.addOnList))
                    } else {
                        bundleToProductMap[bundleGroupId] = arrayListOf(createOrderItemFromPurchase(purchasedItem, purchasedItem.addOnList))
                    }
                }
            }

            shopOrder.purchaseItemList.forEach { purchasedItem ->
                val bundleGroupId = purchasedItem.bundleGroupId
                // Normal Product
                if (bundleGroupId.isEmpty()) {
                    orderedItemList.add(createOrderItemFromPurchase(purchasedItem, purchasedItem.addOnList, OrderItemType.SINGLE_PRODUCT))
                } else {
                    if (bundleToProductMap.containsKey(bundleGroupId)) {
                        // add bundle data name
                        // add product data having same bundle Id
                        // prevent same products from re-calculation in the current loop
                        orderedItemList.add(createOrderItemFromBundle(bundleMap[bundleGroupId]))
                        orderedItemList.addAll(bundleToProductMap[bundleGroupId] ?: arrayListOf())
                        bundleToProductMap.remove(bundleGroupId)
                    }
                }

                totalProductProtectionForShop += purchasedItem.productPlanProtection
            }

            val shippingDetail = thanksPageData.orderGroupList.find { shopOrder.orderGroupId == it.id }

            var logisticDiscountStr: String? = null
            var discountFromMerchant: String? = null

            shopOrder.promoData?.forEach {
                when (it.promoCode) {
                    PromoDataKey.LOGISTIC -> logisticDiscountStr = getPromoDiscountStr(it, shippingDetail)
                    PromoDataKey.MERCHANT -> discountFromMerchant = getPromoDiscountStr(it, shippingDetail)
                }
            }

            val shippingDurationOrETA = if (getShippingEta(shopOrder, shippingDetail).isNullOrBlank()) {
                if (shopOrder.logisticDuration.isNullOrBlank()) "" else shopOrder.logisticDuration
            } else {
                getShippingEta(shopOrder, shippingDetail)
            }

            val shippingInfo = if (shippingDurationOrETA?.isBlank() == true) {
                getLogisticType(shopOrder, shippingDetail)
            } else {
                getLogisticType(shopOrder, shippingDetail) + NEW_LINE + shippingDurationOrETA
            }

            val shouldHideShopInvoice = if (index == thanksPageData.shopOrder.size - 1) false else shopOrder.orderGroupId == thanksPageData.shopOrder[index + 1].orderGroupId

            val shopInvoice = ShopInvoice(
                shopName = shopOrder.storeName,
                orderedItem = orderedItemList,
                itemDiscountStr = discountFromMerchant,
                productProtectionStr = if (totalProductProtectionForShop > 0.0) {
                    CurrencyFormatUtil.convertPriceValueToIdrFormat(
                        totalProductProtectionForShop,
                        false
                    )
                } else {
                    null
                },
                shippingPriceStr = getShippingPriceStr(shopOrder, shippingDetail),
                shippingInfo = shippingInfo,
                discountOnShippingStr = logisticDiscountStr,
                shippingInsurancePriceStr = getInsurancePriceStr(shopOrder, shippingDetail),
                shippingAddress = shopOrder.address,
                // add order level add-ons here
                orderLevelAddOn = OrderLevelAddOn(shopOrder.addOnSectionDescription, shopOrder.addOnItemList),
                shouldHideShopInvoice = shouldHideShopInvoice,
                shouldHideDivider = shouldHideShopInvoice || currentIndex == thanksPageData.shopOrder.size - 1,
            )
            visitableList.add(shopInvoice)
            currentIndex++

            bundleToProductMap.clear()
            bundleMap.clear()
        }
    }

    private fun getShippingPriceStr(shopOrder: ShopOrder, orderGroupList: OrderGroupList?): String? {
        return if (orderGroupList != null) {
            if (orderGroupList.totalShippingFee > 0F) CurrencyFormatUtil.convertPriceValueToIdrFormat(orderGroupList.totalShippingFee.toLong(), false)
            else null
        } else {
            if (shopOrder.shippingAmount > 0F) CurrencyFormatUtil.convertPriceValueToIdrFormat(shopOrder.shippingAmount.toLong(), false) else null
        }
    }

    private fun getInsurancePriceStr(shopOrder: ShopOrder, orderGroupList: OrderGroupList?): String? {
        return if (orderGroupList != null) {
            if (orderGroupList.totalInsurancePrice > 0F) CurrencyFormatUtil.convertPriceValueToIdrFormat(orderGroupList.totalInsurancePrice.toLong(), false)
            else null
        } else {
            if (shopOrder.insuranceAmount > 0F) CurrencyFormatUtil.convertPriceValueToIdrFormat(shopOrder.insuranceAmount.toLong(), false) else null
        }
    }

    private fun getShippingEta(shopOrder: ShopOrder, orderGroupList: OrderGroupList?): String? {
        return if (orderGroupList != null) return orderGroupList.shipperEta
        else shopOrder.logisticETA
    }

    private fun getLogisticType(shopOrder: ShopOrder, orderGroupList: OrderGroupList?): String {
        return if (orderGroupList != null) return orderGroupList.shipperName
        else shopOrder.logisticType
    }

    private fun getPromoDiscountStr(promoData: PromoData, orderGroupList: OrderGroupList?): String? {
        return if (orderGroupList != null) {
            if (orderGroupList.totalBebasongkirPrice > 0F) CurrencyFormatUtil.convertPriceValueToIdrFormat(orderGroupList.totalBebasongkirPrice.toLong(), false)
            else null
        } else {
            if (promoData.totalDiscount > 0F) promoData.totalDiscountStr else null
        }
    }

    private fun createOrderItemFromPurchase(purchasedItem: PurchaseItem, addOnList: ArrayList<AddOnItem>, orderItemType: OrderItemType = OrderItemType.BUNDLE_PRODUCT) = OrderedItem(
        purchasedItem.productName,
        purchasedItem.variant,
        purchasedItem.quantity,
        purchasedItem.priceStr,
        purchasedItem.totalPriceStr,
        purchasedItem.isBBIProduct,
        orderItemType,
        addOnList
    )

    private fun createOrderItemFromBundle(bundleGroupItem: BundleGroupItem?) = OrderedItem(
        bundleGroupItem?.bundleTitle ?: "",
        null,
        null,
        bundleGroupItem?.totalPrice.toString(),
        bundleGroupItem?.totalPriceStr ?: "",
        false,
        OrderItemType.BUNDLE,
        null
    )

    companion object {
        private const val ONE = 1
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

object StoreItemKey {
    const val MARKETPLACE = "marketplace"
    const val MARKETPLACE_ALTERNATE = "Marketplace"
    const val GOLD_MERCHANT = "gold_merchant"
    const val GOLD_MERCHANT_ALTERNATE = "Gold Merchant"
    const val OFFICIAL_STORE = "official_store"
    const val OFFICIAL_STORE_ALTERNATE = "Official Store"
}

object PaymentDeductionKey {
    const val TOTAL_SHIPPING_DISCOUNT = "total_logistic_discount"
    const val TOTAL_DISCOUNT = "total_discount"
    const val REWARDS_POINT = "rewards_point"
    const val CASH_BACK_OVO_POINT = "cashback"
    const val POTENTIAL_CASH_BACK = "potential_cashback"
    const val CASHBACK_STACKED = "cashback_stacked"
    const val NEW_LINE = "\n"

    const val THANK_STACKED_CASHBACK_TITLE = "Dapat cashback senilai"
    const val PREV_ORDER_AMOUNT_VA = "Total Transaksi Sebelumnya"
}
