package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Banner(
        val cta: Cta?,
        val id: Long,
        val title: String,
        val description: String,
        val url: String,
        val applink: String,
        @SerializedName("text_color") val textColor: String,
        @SerializedName("image_url") val imageUrl: String,
        val attribution: String
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            cta = parcel.readParcelable(Cta::class.java.classLoader),
            id = parcel.readLong(),
            title = parcel.readString() ?: "",
            description = parcel.readString() ?: "",
            url = parcel.readString() ?: "",
            applink = parcel.readString() ?: "",
            textColor = parcel.readString() ?: "",
            imageUrl = parcel.readString() ?: "",
            attribution = parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeParcelable(cta, flags)
            writeLong(id)
            writeString(title)
            writeString(description)
            writeString(url)
            writeString(applink)
            writeString(textColor)
            writeString(imageUrl)
            writeString(attribution)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { Banner(it) }
    }
}


