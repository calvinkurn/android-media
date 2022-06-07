package com.tokopedia.shop.flashsale.data.response


import com.google.gson.annotations.SerializedName

data class ShopInfoByIdResponse(
    @SerializedName("shopInfoByID")
    val shopInfoByID: ShopInfoByID = ShopInfoByID()
) {
    data class ShopInfoByID(
        @SerializedName("result")
        val result: List<Result> = listOf()
    ) {
        data class Result(
            @SerializedName("closedInfo")
            val closedInfo: ClosedInfo = ClosedInfo(),
            @SerializedName("goldOS")
            val goldOS: GoldOS = GoldOS(),
            @SerializedName("isOwner")
            val isOwner: Int = 0,
            @SerializedName("os")
            val os: Os = Os(),
            @SerializedName("shopCore")
            val shopCore: ShopCore = ShopCore(),
            @SerializedName("shopHomeType")
            val shopHomeType: String = "",
            @SerializedName("statusInfo")
            val statusInfo: StatusInfo = StatusInfo()
        ) {
            data class ClosedInfo(
                @SerializedName("closedNote")
                val closedNote: String = "",
                @SerializedName("detail")
                val detail: Detail = Detail(),
                @SerializedName("until")
                val until: String = ""
            ) {
                data class Detail(
                    @SerializedName("endDate")
                    val endDate: String = "",
                    @SerializedName("openDate")
                    val openDate: String = "",
                    @SerializedName("startDate")
                    val startDate: String = "",
                    @SerializedName("status")
                    val status: Int = 0
                )
            }

            data class GoldOS(
                @SerializedName("badge")
                val badge: String = "",
                @SerializedName("isGold")
                val isGold: Int = 0,
                @SerializedName("isOfficial")
                val isOfficial: Int = 0,
                @SerializedName("shopGrade")
                val shopGrade: Int = 0,
                @SerializedName("shopGradeWording")
                val shopGradeWording: String = "",
                @SerializedName("shopTier")
                val shopTier: Int = 0,
                @SerializedName("shopTierWording")
                val shopTierWording: String = ""
            )

            data class Os(
                @SerializedName("expired")
                val expired: String = "",
                @SerializedName("isOfficial")
                val isOfficial: Int = 0
            )

            data class ShopCore(
                @SerializedName("description")
                val description: String = "",
                @SerializedName("domain")
                val domain: String = "",
                @SerializedName("name")
                val name: String = "",
                @SerializedName("shopID")
                val shopID: String = "",
                @SerializedName("tagLine")
                val tagLine: String = ""
            )

            data class StatusInfo(
                @SerializedName("shopStatus")
                val shopStatus: Int = 0,
                @SerializedName("statusName")
                val statusName: String = ""
            )
        }
    }
}