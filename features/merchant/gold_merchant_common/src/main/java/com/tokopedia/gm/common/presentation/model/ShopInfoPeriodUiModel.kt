package com.tokopedia.gm.common.presentation.model


data class ShopInfoPeriodUiModel(var isNewSeller: Boolean = false,
                                 var isEndTenureNewSeller: Boolean = false,
                                 var periodType: String = "",
                                 var shopAge: Long = 0,
                                 var periodStartDate: String = "",
                                 var periodEndDate: String = ""
)