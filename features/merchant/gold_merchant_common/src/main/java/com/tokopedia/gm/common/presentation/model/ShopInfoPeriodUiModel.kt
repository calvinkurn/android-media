package com.tokopedia.gm.common.presentation.model

data class ShopInfoPeriodUiModel(var isNewSeller: Boolean = false,
                                 var isEndTenureNewSeller: Boolean = false,
                                 var periodType: String = "",
                                 var joinDate: String = "",
)