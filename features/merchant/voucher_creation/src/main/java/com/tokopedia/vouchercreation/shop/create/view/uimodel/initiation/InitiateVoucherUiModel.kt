package com.tokopedia.vouchercreation.shop.create.view.uimodel.initiation

data class InitiateVoucherUiModel(
        val token: String = "",
        val accessToken: String = "",
        val uploadAppUrl: String = "",
        val bannerBaseUrl: String = "",
        val bannerIgPostUrl: String = "",
        val bannerIgStoryUrl: String = "",
        val bannerFreeShippingLabelUrl: String = "",
        val bannerCashbackLabelUrl: String = "",
        val bannerCashbackUntilLabelUrl: String = "",
        val voucherCodePrefix: String = "",
        val isCreateVoucherEligible: Boolean = false,
        val maxProducts : Int = 0
)