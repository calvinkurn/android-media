package com.tokopedia.seller.menu.common.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.gm.common.constant.KYCStatusId
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.shop.common.graphql.data.shopinfo.ShopCore

data class UserShopInfoResponse(
        @Expose
        @SerializedName("shopInfoByID")
        val shopInfoByID: ShopInfoByID = ShopInfoByID(),
        @Expose
        @SerializedName("userShopInfo")
        val userShopInfo: UserShopInfo = UserShopInfo(),
        @Expose
        @SerializedName("goldGetPMSettingInfo")
        val goldGetPMSettingInfo: GoldGetPMSettingInfo = GoldGetPMSettingInfo(),
        @Expose
        @SerializedName("goldGetPMShopInfo")
        val goldGetPMShopInfo: GoldGetPMShopInfo = GoldGetPMShopInfo(),
        @Expose
        @SerializedName("goldGetPMOSStatus")
        val goldGetPMOSStatus: GoldGetPMOSStatus = GoldGetPMOSStatus()
) {
    data class UserShopInfo(
            @Expose
            @SerializedName("info")
            val info: Info = Info(),
    ) {
        data class Info(
                @Expose
                @SerializedName("date_shop_created")
                val dateShopCreated: String = ""
        )
    }

    data class ShopInfoByID(
            @Expose
            @SerializedName("result")
            val result: List<Result> = listOf()
    ) {
        data class Result(
            @Expose
            @SerializedName("goldOS")
            val goldOS: GoldOS = GoldOS(),
            @Expose
            @SerializedName("shopSnippetURL")
            val shopSnippetUrl: String = "",
            @Expose
            @SerializedName("location")
            val location: String = "",
            @Expose
            @SerializedName("branchLinkDomain")
            val branchLinkDomain: String = "",
            @Expose
            @SerializedName("shopCore")
            val shopCore: ShopCore = ShopCore(),
            @Expose
            @SerializedName("statsByDate")
            val statsByDate: List<StatsByDate> = listOf(),
            @Expose
            @SerializedName("statusInfo")
            val statusInfo: StatusInfo = StatusInfo(),
            @SerializedName("isKyc")
            val kycStatus: Int = 1
        ) {
            data class GoldOS(
                    @Expose
                    @SerializedName("badge")
                    val badge: String = "",
                    @Expose
                    @SerializedName("shopGrade")
                    val shopGrade: Int = 0,
                    @Expose
                    @SerializedName("shopGradeWording")
                    val shopGradeWording: String = "",
                    @Expose
                    @SerializedName("shopTier")
                    val shopTier: Int = 0,
                    @Expose
                    @SerializedName("shopTierWording")
                    val shopTierWording: String = "",
                    @Expose
                    @SerializedName("title")
                    val title: String = ""
            )
            data class StatsByDate(
                    @Expose
                    @SerializedName("identifier")
                    val identifier: String = "",
                    @Expose
                    @SerializedName("startTime")
                    val startTime: String = "",
                    @Expose
                    @SerializedName("value")
                    val value: Long = 0
            )

            data class StatusInfo(
                @Expose
                @SerializedName("statusTitle")
                val statusTitle: String = "",
                @Expose
                @SerializedName("statusMessage")
                val statusMessage: String = "",
                @Expose
                @SerializedName("tickerType")
                val tickerType: String = "",
                @Expose
                @SerializedName("shopStatus")
                val shopStatus: Int = 0
            )
        }
    }

    data class GoldGetPMSettingInfo(
            @Expose
            @SerializedName("period_type_pm_pro")
            val periodTypePmPro: String = ""
    )

    data class GoldGetPMShopInfo(
        @Expose
        @SerializedName("kyc_status_id")
        val kycStatusId: String? = "",
        @Expose
        @SerializedName("is_eligible_pm")
        val isEligiblePm: Boolean? = false,
        @Expose
        @SerializedName("is_eligible_pm_pro")
        val isEligiblePmPro: Boolean? = false,
    ) {
        fun isPendingKyc(): Boolean {
            return kycStatusId.toIntSafely() == KYCStatusId.PENDING
        }

        fun isVerifiedKyc(): Boolean {
            return kycStatusId.toIntSafely() == KYCStatusId.VERIFIED
        }
    }

    data class GoldGetPMOSStatus(
            @Expose
            @SerializedName("data")
            val `data`: Data = Data()
    ) {
        data class Data(
                @Expose
                @SerializedName("official_store")
                val officialStore: OfficialStore = OfficialStore(),
                @Expose
                @SerializedName("power_merchant")
                val powerMerchant: PowerMerchant = PowerMerchant()
        ) {
            data class PowerMerchant(
                    @Expose
                    @SerializedName("pm_tier")
                    val pmTier: Int = 0,
                    @Expose
                    @SerializedName("status")
                    val status: String = ""
            )

            data class OfficialStore(
                    @Expose
                    @SerializedName("error")
                    val error: String = "",
                    @Expose
                    @SerializedName("status")
                    val status: String = ""
            )
        }
    }
}
