package com.tokopedia.shop.common.data.source.cloud.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShopInfoStats {
    @SerializedName("favorite_count")
    @Expose
    var favoriteCount: Long = 0

    @SerializedName("hide_rate")
    @Expose
    var hideRate: Long = 0

    @SerializedName("rate_failure")
    @Expose
    var rateFailure = 0.0

    @SerializedName("rate_success")
    @Expose
    var rateSuccess: Long = 0

    @SerializedName("shop_accuracy_description")
    @Expose
    var shopAccuracyDescription: Long = 0

    @SerializedName("shop_accuracy_rate")
    @Expose
    var shopAccuracyRate: Long = 0

    @SerializedName("shop_badge_level")
    @Expose
    var shopBadgeLevel: ShopInfoBadgeLevel? = null

    @SerializedName("shop_item_sold")
    @Expose
    var shopItemSold: String? = null

    @SerializedName("shop_last_one_month")
    @Expose
    var shopLastOneMonth: ShopInfoLastScore? = null

    @SerializedName("shop_last_six_months")
    @Expose
    var shopLastSixMonths: ShopInfoLastScore? = null

    @SerializedName("shop_last_twelve_months")
    @Expose
    var shopLastTwelveMonths: ShopInfoLastScore? = null

    @SerializedName("shop_reputation_score")
    @Expose
    var shopReputationScore: String? = null

    @SerializedName("shop_service_description")
    @Expose
    var shopServiceDescription: Long = 0

    @SerializedName("shop_service_rate")
    @Expose
    var shopServiceRate: Long = 0

    @SerializedName("shop_speed_description")
    @Expose
    var shopSpeedDescription: Long = 0

    @SerializedName("shop_speed_rate")
    @Expose
    var shopSpeedRate: Long = 0

    @SerializedName("shop_total_etalase")
    @Expose
    var shopTotalEtalase: String? = null

    @SerializedName("shop_total_product")
    @Expose
    var shopTotalProduct: String? = null

    @SerializedName("shop_total_transaction")
    @Expose
    var shopTotalTransaction: String? = null

    @SerializedName("shop_total_transaction_canceled")
    @Expose
    var shopTotalTransactionCanceled: String? = null

    @SerializedName("tx_count")
    @Expose
    var txCount: String? = null

    @SerializedName("tx_count_success")
    @Expose
    var txCountSuccess: String? = null
}