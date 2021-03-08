package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Banner(
        @Expose @SerializedName("cta") val cta: Cta?,
        @Expose @SerializedName("id") val id: Long,
        @Expose @SerializedName("title") val title: String,
        @Expose @SerializedName("description") val description: String,
        @Expose @SerializedName("url") val url: String,
        @Expose @SerializedName("applink") val applink: String,
        @Expose @SerializedName("text_color") val textColor: String,
        @Expose @SerializedName("image_url") val imageUrl: String,
        @Expose @SerializedName("back_color") val backColor: String,
        @Expose @SerializedName("gradient_color") val gradientColor: ArrayList<String> = arrayListOf(),
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
            backColor = parcel.readString() ?: "",
            gradientColor = parcel.createStringArrayList() ?: arrayListOf(),
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
            writeString(backColor)
            writeStringList(gradientColor)
            writeString(attribution)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { Banner(it) }
    }
}


