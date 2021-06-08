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
        @SerializedName("is_new_seller")
        val isNewSeller: Boolean? = true,
        @SerializedName("shop_age")
        val shopAge: Int? = 0,
        @SerializedName("is_kyc")
        val isKyc: Boolean? = false,
        @SerializedName("kyc_status_id")
        val kycStatusId: String? = "0",
        @SerializedName("shop_score_threshold")
        val shopScoreThreshold: Int? = 0,
        @SerializedName("shop_score_pm_pro_threshold")
        val shopScorePmProThreshold: Int? = 0,
        @SerializedName("is_has_active_product")
        val hasActiveProduct: Boolean? = false,
        @SerializedName("is_eligible_pm")
        val isEligiblePm: Boolean? = false,
        @SerializedName("is_eligible_pm_pro")
        val isEligiblePmPro: Boolean? = false,
        @SerializedName("shop_level")
        val shopLevel: Int? = 0,
        @SerializedName("item_sold_one_month")
        val itemSoldOneMonth: Long? = 0,
        @SerializedName("item_sold_pm_pro_threshold")
        val itemSoldPmProThreshold: Long? = 0,
        @SerializedName("niv_one_month")
        val nivOneMonth: Long? = 0,
        @SerializedName("niv_pm_pro_threshold")
        val nivPmProThreshold: Long? = 0
)