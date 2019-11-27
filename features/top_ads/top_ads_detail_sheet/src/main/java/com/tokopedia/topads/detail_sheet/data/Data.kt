package com.tokopedia.topads.detail_sheet.data


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("ad_commission_fmt")
    val adCommissionFmt: String = "",
    @SerializedName("ad_commission_nominal_fmt")
    val adCommissionNominalFmt: String = "",
    @SerializedName("ad_editable")
    val adEditable: Int = 0,
    @SerializedName("ad_end_date")
    val adEndDate: String = "",
    @SerializedName("ad_end_time")
    val adEndTime: String = "",
    @SerializedName("ad_id")
    val adId: Int = 0,
    @SerializedName("ad_moderated")
    val adModerated: Int = 0,
    @SerializedName("ad_moderated_reason")
    val adModeratedReason: String = "",
    @SerializedName("ad_platform")
    val adPlatform: String = "",
    @SerializedName("ad_price_bid_fmt")
    val adPriceBidFmt: String = "",
    @SerializedName("ad_price_daily_bar")
    val adPriceDailyBar: String = "",
    @SerializedName("ad_price_daily_fmt")
    val adPriceDailyFmt: String = "",
    @SerializedName("ad_price_daily_spent_fmt")
    val adPriceDailySpentFmt: String = "",
    @SerializedName("ad_promo_code")
    val adPromoCode: String = "",
    @SerializedName("ad_start_date")
    val adStartDate: String = "",
    @SerializedName("ad_start_time")
    val adStartTime: String = "",
    @SerializedName("ad_status")
    val adStatus: Int = 0,
    @SerializedName("ad_status_desc")
    val adStatusDesc: String = "",
    @SerializedName("ad_status_toogle")
    val adStatusToogle: Int = 0,
    @SerializedName("ad_type")
    val adType: Int = 0,
    @SerializedName("group_id")
    val groupId: Int = 0,
    @SerializedName("group_name")
    val groupName: String = "",
    @SerializedName("item_id")
    val itemId: Int = 0,
    @SerializedName("label_edit")
    val labelEdit: String = "",
    @SerializedName("label_of")
    val labelOf: String = "",
    @SerializedName("label_per_click")
    val labelPerClick: String = "",
    @SerializedName("label_per_impression")
    val labelPerImpression: String = "",
    @SerializedName("product_active")
    val productActive: Int = 0,
    @SerializedName("product_image_uri")
    val productImageUri: String = "",
    @SerializedName("product_name")
    val productName: String = "",
    @SerializedName("product_uri")
    val productUri: String = "",
    @SerializedName("RawStatTtlCTR")
    val rawStatTtlCTR: Int = 0,
    @SerializedName("RawStatTtlClick")
    val rawStatTtlClick: Int = 0,
    @SerializedName("RawStatTtlCommission")
    val rawStatTtlCommission: Int = 0,
    @SerializedName("RawStatTtlCommissionNominal")
    val rawStatTtlCommissionNominal: Int = 0,
    @SerializedName("RawStatTtlGrossProfit")
    val rawStatTtlGrossProfit: Int = 0,
    @SerializedName("RawStatTtlImpression")
    val rawStatTtlImpression: Int = 0,
    @SerializedName("RawStatTtlSpent")
    val rawStatTtlSpent: Int = 0,
    @SerializedName("shop_id")
    val shopId: Int = 0,
    @SerializedName("stat_avg_click")
    val statAvgClick: String = "",
    @SerializedName("stat_avg_position")
    val statAvgPosition: Int = 0,
    @SerializedName("stat_avg_position_fmt")
    val statAvgPositionFmt: String = "",
    @SerializedName("stat_total_click")
    val statTotalClick: String = "",
    @SerializedName("stat_total_conversion")
    val statTotalConversion: String = "",
    @SerializedName("stat_total_ctr")
    val statTotalCtr: String = "",
    @SerializedName("stat_total_follow")
    val statTotalFollow: String = "",
    @SerializedName("stat_total_gross_profit")
    val statTotalGrossProfit: String = "",
    @SerializedName("stat_total_impression")
    val statTotalImpression: String = "",
    @SerializedName("stat_total_sold")
    val statTotalSold: String = "",
    @SerializedName("stat_total_spent")
    val statTotalSpent: String = "",
    @SerializedName("user_target")
    val userTarget: String = ""
)