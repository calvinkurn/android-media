package com.tokopedia.buy_more_get_more.olp.data.response

import com.google.gson.annotations.SerializedName

data class OfferInfoForBuyerResponse(
    @SerializedName("GetOfferingInfoForBuyer")
    val offeringInforBuyer: OfferInfoForBuyer = OfferInfoForBuyer()
) {

    data class OfferInfoForBuyer(
        @SerializedName("response_header")
        val responseHeader: ResponseHeader = ResponseHeader(),
        @SerializedName("offering_json_data")
        val offeringJsonData: String = "",
        @SerializedName("nearest_warehouse_id")
        val nearestWarehouseIds: List<Long> = emptyList(),
        @SerializedName("offering")
        val offerings: List<Offering> = emptyList()
    )

    data class ResponseHeader(
        @SerializedName("success")
        val success: Boolean = true,
        @SerializedName("errorCode")
        val errorCode: Long = 0,
        @SerializedName("processTime")
        val processTime: String = ""
    )

    data class Offering(
        @SerializedName("offer_id")
        val id: Long = 0,
        @SerializedName("offer_name")
        val offerName: String = "",
        @SerializedName("offer_type_id")
        val offerTypeId: Long = 0,
        @SerializedName("start_date")
        val startDate: String = "",
        @SerializedName("end_date")
        val endDate: String = "",
        @SerializedName("max_applied_tier")
        val maxAppliedTier: Int = 0,
        @SerializedName("tier_list")
        val tierList: List<Tier> = emptyList(),
        @SerializedName("shop_data")
        val shopData: ShopData = ShopData(),
        @SerializedName("term_and_condition")
    val tnc: List<String> = emptyList(),
    ) {
        data class ShopData(
            @SerializedName("shop_id")
            val shopId: Long = 0,
            @SerializedName("shop_name")
            val shopName: String = "",
            @SerializedName("badge")
            val badge: String = ""
        )

        data class Tier(
            @SerializedName("tier_id")
            val tierId: Long = 0,
            @SerializedName("level")
            val level: Int = 0,
            @SerializedName("rule")
            val rules: List<Rule> = emptyList(),
            @SerializedName("benefit")
            val benefits: List<Benefit> = emptyList(),
            @SerializedName("attributes")
            val attributes: String = ""
        ) {
            data class Rule(
                @SerializedName("type_id")
                val typeId: Long = 0,
                @SerializedName("operation")
                val operation: String = "",
                @SerializedName("value")
                val value: Int = 0
            )

            data class Benefit(
                @SerializedName("type_id")
                val typeId: Long = 0,
                @SerializedName("value")
                val value: Int = 0
            )
        }
    }
}
