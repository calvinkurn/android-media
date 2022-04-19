package com.tokopedia.shop.score.performance.presentation.model

import com.tokopedia.shop.score.performance.domain.model.GoldGetPMOStatusResponse

data class PopupEndTenureUiModel(
    val powerMerchantData: GoldGetPMOStatusResponse.GoldGetPMOSStatus.Data? = null,
    val shopScore: String = "",
    val shopLevel: String = ""
)