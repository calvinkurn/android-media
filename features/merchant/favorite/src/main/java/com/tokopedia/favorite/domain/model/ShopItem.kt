package com.tokopedia.favorite.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShopItem(
        @SerializedName("shop_name") var name: String? = null,
        @SerializedName("shop_image") var iconUri: String? = null,
        var coverUri: String? = null,
        @SerializedName("shop_location") var location: String? = null,
        var isFav: String? = null,
        @SerializedName("shop_id") var id: String? = null,
        @SerializedName("ad_key") var adKey : String? = null,
        @SerializedName("ad_r") var adR : String? = null,
        @SerializedName("shop_click_url") var shopClickUrl: String? = null,
        @SerializedName("shop_badge") var shopBadge: List<Badge>? = null
) : RecyclerViewItem(), Parcelable {

    override fun describeContents(): Int {
        return 0
    }

}
