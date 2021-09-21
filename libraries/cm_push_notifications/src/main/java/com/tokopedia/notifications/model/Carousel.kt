package com.tokopedia.notifications.model

import androidx.room.ColumnInfo
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import com.tokopedia.notifications.common.CMConstant

/**
 * @author lalit.singh
 */
data class Carousel(

        @SerializedName(CMConstant.PayloadKeys.APP_LINK)
        @ColumnInfo(name = CMConstant.PayloadKeys.APP_LINK)
        @Expose
        var appLink: String? = null,

        @SerializedName(CMConstant.PayloadKeys.TEXT)
        @ColumnInfo(name = CMConstant.PayloadKeys.TEXT)
        @Expose
        var text: String? = null,

        @SerializedName(CMConstant.PayloadKeys.IMG)
        @ColumnInfo(name = CMConstant.PayloadKeys.IMG)
        @Expose
        var icon: String? = null,

        @SerializedName("filePath")
        @ColumnInfo(name = "filePath")
        @Expose
        var filePath: String? = null,

        @SerializedName("index")
        @ColumnInfo(name = "index")
        @Expose
        var index: Int? = 0,

        @SerializedName(CMConstant.PayloadKeys.ELEMENT_ID)
        @ColumnInfo(name = CMConstant.PayloadKeys.ELEMENT_ID)
        @Expose
        var element_id: String? = "") : Parcelable {

    constructor(source: Parcel) :
            this(
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readString(),
                    source.readInt(),
                    source.readString()
            )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(this.appLink)
        dest?.writeString(this.text)
        dest?.writeString(this.icon)
        dest?.writeString(this.filePath)
        dest?.writeInt(this.index ?: 0)
        dest?.writeString(this.element_id)
    }


    companion object CREATOR : Parcelable.Creator<Carousel> {
        override fun createFromParcel(parcel: Parcel): Carousel {
            return Carousel(parcel)
        }

        override fun newArray(size: Int): Array<Carousel?> {
            return arrayOfNulls(size)
        }
    }
}