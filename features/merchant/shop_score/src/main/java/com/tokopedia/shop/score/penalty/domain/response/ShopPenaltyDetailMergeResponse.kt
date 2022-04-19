package com.tokopedia.shop.score.penalty.domain.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ShopPenaltyDetailMergeResponse(
    @Expose
    @SerializedName("shopScorePenaltySummary")
    val shopScorePenaltySummary: ShopScorePenaltySummary = ShopScorePenaltySummary(),
    @Expose
    @SerializedName("shopScorePenaltyTypes")
    val shopScorePenaltyTypes: ShopScorePenaltyTypes = ShopScorePenaltyTypes(),
    @Expose
    @SerializedName("shopScorePenaltyDetail")
    val shopScorePenaltyDetail: ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail =
        ShopScorePenaltyDetailResponse.ShopScorePenaltyDetail()
)