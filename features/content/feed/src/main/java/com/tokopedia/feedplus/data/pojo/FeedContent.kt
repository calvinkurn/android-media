package com.tokopedia.feedplus.data.pojo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * @author by astidhiyaa on 29/08/22
 */
data class FeedContent(
    @SerializedName("products")
    @Expose val products: List<ProductFeedType> = emptyList(),

    @SerializedName("type")
    @Expose val type: String = "",

    @SerializedName("total_product")
    @Expose val totalProduct: Int = 0,

    @SerializedName("display")
    @Expose val display: String = "",

    @SerializedName("status_activity")
    @Expose val statusActivity: String = "",

    @SerializedName("new_status_activity")
    @Expose val newStatusActivity: StatusActivity = StatusActivity(),

    @SerializedName("total_store")
    @Expose val totalStore: Int = 0,

    @SerializedName("redirect_url_app")
    @Expose val redirectUrlApp: String = "",

    @SerializedName("polling")
    @Expose val polling: FeedPolling = FeedPolling(),

    @SerializedName("banner")
    @Expose val banner: List<FeedBanner> = emptyList(),

    @SerializedName("kolpost")
    @Expose val kolpost: FeedKolType = FeedKolType(),

    @SerializedName("followedkolpost")
    @Expose val followedkolpost: FeedKolType = FeedKolType(),

    @SerializedName("kolrecommendation")
    @Expose val kolrecommendation: KolRecommendedDataType = KolRecommendedDataType(),

    @SerializedName("favorite_cta")
    @Expose val favoriteCta: FeedsFavoriteCta = FeedsFavoriteCta(),

    @SerializedName("kol_cta")
    @Expose val kolCta: FeedsKolCta = FeedsKolCta(),

    @SerializedName("topads")
    @Expose val topads: List<TopAd> = emptyList()
)
