package com.tokopedia.seller.menu.common.domain.entity

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserShopInfoResponse(
        @Expose
        @SerializedName("shopInfoByID")
        val shopInfoByID: ShopInfoByID = ShopInfoByID(),
        @Expose
        @SerializedName("userShopInfo")
        val userShopInfo: UserShopInfo = UserShopInfo()
) {
    data class UserShopInfo(
            @Expose
            @SerializedName("info")
            val info: Info = Info(),
            @Expose
            @SerializedName("owner")
            val owner: Owner = Owner(),
            @Expose
            @SerializedName("stats")
            val stats: Stats = Stats()
    ) {
        data class Info(
                @Expose
                @SerializedName("date_shop_created")
                val dateShopCreated: String = ""
        )

        data class Stats(
                @Expose
                @SerializedName("shop_total_transaction")
                val shopTotalTransaction: String = ""
        )

        data class Owner(
                @Expose
                @SerializedName("pm_status")
                val pmStatus: String = ""
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
                val goldOS: GoldOS = GoldOS()
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
        }
    }

}