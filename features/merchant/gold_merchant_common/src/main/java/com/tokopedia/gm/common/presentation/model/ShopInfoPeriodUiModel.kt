package com.tokopedia.gm.common.presentation.model


data class ShopInfoPeriodUiModel(var isNewSeller: Boolean = false,
                                 var isOfficialStore: Boolean = false,
                                 var isEndTenureNewSeller: Boolean = false,
                                 var periodType: String = "",
                                 var shopAge: Int = 0,
                                 var periodStartDate: String = "",
                                 var periodEndDate: String = ""
)