package com.tokopedia.favorite.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class FavShopsItem(
        @SerializedName("image") @Expose var image: String? = null,
        @SerializedName("badge") @Expose var badge: List<Badge>? = null,
        @SerializedName("name") @Expose var name: String?  = null,
        @SerializedName("reputation") @Expose var reputation: Reputation? = null,
        @SerializedName("location") @Expose var location: String? = null,
        @SerializedName("id") @Expose var id: String? = null
) : RecyclerViewItem(), Parcelable {

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return "FavShopsItem{" +
                "image = '" + image + '\'' +
                ",badge = '" + badge + '\'' +
                ",name = '" + name + '\'' +
                ",reputation = '" + reputation + '\'' +
                ",location = '" + location + '\'' +
                ",id = '" + id + '\'' +
                "}"
    }

}
