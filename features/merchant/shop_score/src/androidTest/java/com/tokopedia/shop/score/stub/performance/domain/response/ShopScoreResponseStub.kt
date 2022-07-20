package com.tokopedia.shop.score.stub.performance.domain.response

import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.score.performance.domain.model.GetRecommendationToolsResponse
import com.tokopedia.shop.score.performance.domain.model.GoldGetPMOStatusResponse
import com.tokopedia.shop.score.performance.domain.model.GoldGetPMShopInfoResponse
import com.tokopedia.shop.score.performance.domain.model.ShopLevelTooltipResponse
import com.tokopedia.shop.score.performance.domain.model.ShopScoreLevelResponse

data class ShopScoreResponseStub(
    @SerializedName("shopScoreLevel")
    val shopScoreLevel: ShopScoreLevelResponse.ShopScoreLevel =
        ShopScoreLevelResponse.ShopScoreLevel(),
    @SerializedName("shopLevel")
    val shopLevelTooltipResponse: ShopLevelTooltipResponse.ShopLevel =
        ShopLevelTooltipResponse.ShopLevel(),
    @SerializedName("goldGetPMShopInfo")
    val goldGetPMShopInfoResponse: GoldGetPMShopInfoResponse.GoldGetPMShopInfo =
        GoldGetPMShopInfoResponse.GoldGetPMShopInfo(),
    @SerializedName("valuePropositionGetRecommendationTools")
    val valuePropositionGetRecommendationTools: GetRecommendationToolsResponse.ValuePropositionGetRecommendationTools =
        GetRecommendationToolsResponse.ValuePropositionGetRecommendationTools(),
    @SerializedName("goldGetPMOSStatus")
    val goldGetPMOSStatus: GoldGetPMOStatusResponse.GoldGetPMOSStatus = GoldGetPMOStatusResponse.GoldGetPMOSStatus()
)