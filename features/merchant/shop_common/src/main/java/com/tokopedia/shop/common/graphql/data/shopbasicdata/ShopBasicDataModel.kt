package com.tokopedia.shop.common.graphql.data.shopbasicdata

import android.os.Parcel
import android.os.Parcelable

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.shop.common.constant.ShopStatusDef
import com.tokopedia.shop.common.constant.ShopStatusLevelDef

/**
 * Created by hendry on 08/08/18.
 */

data class ShopBasicDataModel(
        @SerializedName("domain")
        @Expose
        var domain: String? = "",
        @SerializedName("name")
        @Expose
        var name: String? = "",
        @SerializedName("status")
        @Expose
        var status: Int = ShopStatusDef.OPEN,
        @SerializedName("closeSchedule")
        @Expose
        var closeSchedule: String? = "",
        @SerializedName("closeNote")
        @Expose
        var closeNote: String? = "",
        @SerializedName("closeUntil")
        @Expose
        var closeUntil: String? = "",
        @SerializedName("openSchedule")
        @Expose
        var openSchedule: String? = "",
        @SerializedName("tagline")
        @Expose
        var tagline: String? = "",
        @SerializedName("description")
        @Expose
        var description: String? = "",
        @SerializedName("logo")
        @Expose
        var logo: String? = "",
        @SerializedName("level")
        @Expose
        var level: Int = ShopStatusLevelDef.LEVEL_REGULAR,
        @SerializedName("expired")
        @Expose
        var expired: String? = "") : Parcelable {

    val isOpen: Boolean
        get() = status == ShopStatusDef.OPEN

    val isClosed: Boolean
        get() = status == ShopStatusDef.CLOSED

    val isRegular: Boolean
        get() = level == ShopStatusLevelDef.LEVEL_REGULAR

    val isGold: Boolean
        get() = level == ShopStatusLevelDef.LEVEL_GOLD

    val isOfficialStore: Boolean
        get() = level == ShopStatusLevelDef.LEVEL_OFFICIAL_STORE

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(this.domain)
        dest.writeString(this.name)
        dest.writeInt(this.status)
        dest.writeString(this.closeSchedule)
        dest.writeString(this.closeNote)
        dest.writeString(this.closeUntil)
        dest.writeString(this.openSchedule)
        dest.writeString(this.tagline)
        dest.writeString(this.description)
        dest.writeString(this.logo)
        dest.writeInt(this.level)
        dest.writeString(this.expired)
    }

    protected constructor(`in`: Parcel) : this() {
        this.domain = `in`.readString()
        this.name = `in`.readString()
        this.status = `in`.readInt()
        this.closeSchedule = `in`.readString()
        this.closeNote = `in`.readString()
        this.closeUntil = `in`.readString()
        this.openSchedule = `in`.readString()
        this.tagline = `in`.readString()
        this.description = `in`.readString()
        this.logo = `in`.readString()
        this.level = `in`.readInt()
        this.expired = `in`.readString()
    }

    companion object CREATOR : Parcelable.Creator<ShopBasicDataModel> {
        override fun createFromParcel(source: Parcel): ShopBasicDataModel {
            return ShopBasicDataModel(source)
        }

        override fun newArray(size: Int): Array<ShopBasicDataModel?> {
            return arrayOfNulls(size)
        }
    }
}

