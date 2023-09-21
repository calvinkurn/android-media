package com.tokopedia.oneclickcheckout.order.view.model

import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.AddOnsProductDataModel
import com.tokopedia.purchase_platform.common.feature.addonsproduct.data.model.SummaryAddOnProductDataModel

data class OrderCost(
    val totalPrice: Double = 0.0,
    val totalItemPrice: Double = 0.0,
    val shippingFee: Double = 0.0,
    val insuranceFee: Double = 0.0,
    val isUseInsurance: Boolean = false,
    val paymentFee: Double = 0.0,
    val shippingDiscountAmount: Int = 0,
    val productDiscountAmount: Int = 0,
    val purchaseProtectionPrice: Int = 0,
    val addOnPrice: Double = 0.0,
    val hasAddOn: Boolean = false,
    val cashbacks: List<OrderCostCashbackData> = emptyList(),
    val installmentData: OrderCostInstallmentData? = null,
    val summaryAddOnsProduct: List<SummaryAddOnProductDataModel> = emptyList(),
    val addOnsProductSelectedList: List<AddOnsProductDataModel.Data> = emptyList(),

    val orderPaymentFees: List<OrderPaymentFee> = emptyList(),
    val isInstallment: Boolean = false,

    // Easy access for calculation
    val totalPriceWithoutPaymentFees: Double = 0.0,
    val totalPriceWithoutDiscountsAndPaymentFees: Double = 0.0,
    val totalItemPriceAndShippingFee: Double = 0.0,
    val totalAdditionalFee: Double = 0.0,
    val totalDiscounts: Int = 0
)

data class OrderCostCashbackData(
    val description: String = "",
    val amountStr: String = "",
    val currencyDetailStr: String = ""
)

data class OrderCostInstallmentData(
    val installmentFee: Double = 0.0,
    val installmentTerm: Int = 0,
    val installmentAmountPerPeriod: Double = 0.0,
    val installmentFirstDate: String = "",
    val installmentLastDate: String = ""
)
