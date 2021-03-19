package com.tokopedia.shop.score.performance.domain.model

data class ShopScoreWrapperResponse(var shopScoreLevelResponse: ShopScoreLevelResponse.ShopScoreLevel? = null,
                                    var shopScoreTooltipResponse: ShopLevelTooltipResponse.ShopLevel? = null,
                                    var goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo? = null,
                                    var goldGetPMStatusResponse: GoldGetPMStatusResponse.GoldGetPMOSStatus? = null,
                                    var goldPMGradeBenefitInfoResponse: GoldPMGradeBenefitInfoResponse.GoldGetPMGradeBenefitInfo? = null
)