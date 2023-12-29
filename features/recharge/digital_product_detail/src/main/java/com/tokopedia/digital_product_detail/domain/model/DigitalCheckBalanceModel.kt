package com.tokopedia.digital_product_detail.domain.model

data class DigitalCheckBalanceModel(
    val title: String = "",
    val subtitle: String = "",
    val label: String = "",
    val bottomSheetModel: DigitalCheckBalanceOTPBottomSheetModel = DigitalCheckBalanceOTPBottomSheetModel(),
    val campaignLabelText: String = "",
    val campaignLabelTextColor: String = "",
    val iconUrl: String = "",
    val widgets: List<DigitalCheckBalanceWidgetModel> = emptyList(),
    val products: List<DigitalCheckBalanceProductModel> = emptyList(),
    val widgetType: String = ""
)

data class DigitalCheckBalanceOTPBottomSheetModel(
    val title: String = "",
    val mediaUrl: String = "",
    val description: String = "",
    val buttonText: String = "",
    val buttonAppLink: String = ""
)

data class DigitalCheckBalanceWidgetModel(
    val title: String = "",
    val subtitle: String = "",
    val iconUrl: String = ""
)

data class DigitalCheckBalanceProductModel(
    val title: String = "",
    val subtitle: String = "",
    val subtitleColor: String = "",
    val applink: String = "",
    val buttonText: String = "",
    val productId: String = "",
    val productPrice: Double = 0.0
)
