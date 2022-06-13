package com.tokopedia.gm.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created By @ilhamsuaib on 26/02/21
 */

data class GMShopInfoResponse(
    @Expose
    @SerializedName("goldGetPMShopInfo")
    val goldGetPMShopInfo: GoldGetPMShopInfoDataModel? = GoldGetPMShopInfoDataModel(),
    @Expose
    @SerializedName("shopInfoByID")
    val shopInfoByID: ShopInfoByIDResponse.ShopInfoByID = ShopInfoByIDResponse.ShopInfoByID(),
    @Expose
    @SerializedName("goldGetPMGradeBenefitInfo")
    val nextUpdateInfo: NextUpdateInfoModel = NextUpdateInfoModel()
)

data class GoldGetPMShopInfoDataModel(
    @Expose
    @SerializedName("is_new_seller")
    val isNewSeller: Boolean? = true,
    @Expose
    @SerializedName("is_30_days_first_monday")
    val is30DaysFirstMonday: Boolean? = false,
    @Expose
    @SerializedName("shop_age")
    val shopAge: Long? = 0L,
    @Expose
    @SerializedName("is_kyc")
    val isKyc: Boolean? = false,
    @Expose
    @SerializedName("kyc_status_id")
    val kycStatusId: String? = "0",
    @Expose
    @SerializedName("shop_score_threshold")
    val shopScoreThreshold: Int? = 0,
    @Expose
    @SerializedName("shop_score_pm_pro_threshold")
    val shopScorePmProThreshold: Int? = 0,
    @Expose
    @SerializedName("is_has_active_product")
    val hasActiveProduct: Boolean? = false,
    @Expose
    @SerializedName("is_eligible_pm")
    val isEligiblePm: Boolean? = false,
    @Expose
    @SerializedName("is_eligible_pm_pro")
    val isEligiblePmPro: Boolean? = false,
    @Expose
    @SerializedName("shop_level")
    val shopLevel: Int? = 0,
    @Expose
    @SerializedName("item_sold_one_month")
    val itemSoldOneMonth: Long? = 0,
    @Expose
    @SerializedName("item_sold_pm_pro_threshold")
    val itemSoldPmProThreshold: Long? = 0,
    @Expose
    @SerializedName("niv_one_month")
    val nivOneMonth: Long? = 0,
    @Expose
    @SerializedName("niv_pm_pro_threshold")
    val nivPmProThreshold: Long? = 0
)

data class NextUpdateInfoModel(
    @Expose
    @SerializedName("next_monthly_refresh_date")
    val nextMonthlyRefreshDate: String? = ""
)