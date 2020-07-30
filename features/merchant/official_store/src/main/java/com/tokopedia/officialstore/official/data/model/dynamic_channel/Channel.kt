package com.tokopedia.officialstore.official.data.model.dynamic_channel

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Channel(
        @Expose @SerializedName("header") val header: Header?,
        @Expose @SerializedName("hero") val hero: Hero?,
        @Expose @SerializedName("banner") val banner: Banner?,
        @Expose @SerializedName("id") val id: String,
        @Expose @SerializedName("name") val name: String,
        @Expose @SerializedName("layout") val layout: String,
        @Expose @SerializedName("campaignID") val campaignID: Int,
        @Expose @SerializedName("grids") val grids: MutableList<Grid?>?,
        @Expose @SerializedName("galaxy_attribution") val galaxyAttribution: String = "",
        @Expose @SerializedName("persona") val persona: String = "",
        @Expose @SerializedName("category_persona") val categoryPersona: String = "",
        @Expose @SerializedName("brand_id") val brandId: String = ""
) : Parcelable {

    private constructor(parcel: Parcel) : this(
            header = parcel.readParcelable(Header::class.java.classLoader),
            hero = parcel.readParcelable(Hero::class.java.classLoader),
            banner = parcel.readParcelable(Banner::class.java.classLoader),
            id = parcel.readString() ?: "",
            name = parcel.readString() ?: "",
            layout = parcel.readString() ?: "",
            campaignID = parcel.readInt() ?: 0,
            grids = parcel.createTypedArrayList(Grid.CREATOR),
            galaxyAttribution = parcel.readString() ?: "",
            persona = parcel.readString() ?: "",
            categoryPersona = parcel.readString() ?: "",
            brandId = parcel.readString() ?: ""
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.run {
            writeParcelable(header, flags)
            writeParcelable(hero, flags)
            writeParcelable(banner, flags)
            writeString(id)
            writeString(name)
            writeString(layout)
            writeInt(campaignID)
            writeTypedList(grids)
            writeString(galaxyAttribution)
            writeString(persona)
            writeString(categoryPersona)
            writeString(brandId)
        }
    }

    override fun describeContents() = 0

    companion object {
        @JvmField val CREATOR = createParcel { Channel(it) }
    }
}
