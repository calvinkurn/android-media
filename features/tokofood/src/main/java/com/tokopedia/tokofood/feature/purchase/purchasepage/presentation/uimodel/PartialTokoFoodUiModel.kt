package com.tokopedia.tokofood.feature.purchase.purchasepage.presentation.uimodel

class PartialTokoFoodUiModel(
    val topTickerUiModel: TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel?,
    val shippingUiModel: TokoFoodPurchaseShippingTokoFoodPurchaseUiModel?,
    val promoUiModel: TokoFoodPurchasePromoTokoFoodPurchaseUiModel?,
    val summaryUiModel: TokoFoodPurchaseSummaryTransactionTokoFoodPurchaseUiModel?,
    val totalAmountUiModel: TokoFoodPurchaseTotalAmountTokoFoodPurchaseUiModel,
    val tickerErrorShopLevelUiModel: TokoFoodPurchaseTickerErrorShopLevelTokoFoodPurchaseUiModel?,
    val bottomTickerUiModel: TokoFoodPurchaseGeneralTickerTokoFoodPurchaseUiModel?
)