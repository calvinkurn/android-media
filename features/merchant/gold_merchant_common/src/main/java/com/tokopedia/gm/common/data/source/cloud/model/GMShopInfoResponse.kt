package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 26/02/21
 */

data class GMShopInfoResponse(
        @SerializedName("goldGetPMShopInfo")
        val goldGetPMShopInfo: GoldGetPMShopInfoDataModel? = GoldGetPMShopInfoDataModel()
)

data class GoldGetPMShopInfoDataModel(
        @SerializedName("shop_id")
        val shopId: Long? = 0,
        @SerializedName("is_new_seller")
        val isNewSeller: Boolean? = true,
        @SerializedName("shop_age")
        val shopAge: Int? = 0,
        @SerializedName("is_kyc")
        val isKyc: Boolean? = false,
        @SerializedName("kyc_status_id")
        val kycStatusId: Int? = 0,
        @SerializedName("shop_score_sum")
        val shopScore: Int? = 0,
        @SerializedName("shop_score_threshold")
        val shopScoreThreshold: Int? = 0,
        @SerializedName("is_eligible_shop_score")
        val isEligibleShopScore: Boolean? = false,
        @SerializedName("is_has_active_product")
        val hasActiveProduct: Boolean? = false,
        @SerializedName("is_eligible_pm")
        val isEligiblePm: Boolean? = false,
        @SerializedName("shop_level")
        val shopLevel: Int? = 0
)