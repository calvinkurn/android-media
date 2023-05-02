package com.tokopedia.favorite.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * @author Kulomady on 12/2/16.
 */
@Parcelize
data class Badge(
        @SerializedName("title") @Expose var title: String? = null,
        @SerializedName("img_url") @Expose var imgUrl: String? = null,
        //sometimes it's different at ws (sometimes image_url and sometimes imgurl)
        @SerializedName("image_url") @Expose var imageUrl: String? = null
) : RecyclerViewItem(), Parcelable {

    override fun describeContents(): Int {
        return 0
    }

}
