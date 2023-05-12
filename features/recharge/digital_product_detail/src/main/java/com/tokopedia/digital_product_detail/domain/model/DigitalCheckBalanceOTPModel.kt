package com.tokopedia.digital_product_detail.domain.model

data class DigitalCheckBalanceOTPModel(
    val subtitle: String = "",
    val label: String = "",
    val bottomSheetModel: DigitalCheckBalanceOTPBottomSheetModel = DigitalCheckBalanceOTPBottomSheetModel()
)

data class DigitalCheckBalanceOTPBottomSheetModel(
    val title: String = "",
    val mediaUrl: String = "",
    val description: String = "",
    val buttonText: String = "",
    val buttonAppLink: String = ""
)
