package com.tokopedia.discovery.categoryrevamp.data.bannedCategory

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.tokopedia.discovery.categoryrevamp.data.subCategoryModel.SubCategoryItem

class Data() : Parcelable {

    @SerializedName("id")
    var id: Int? = null

    @SerializedName("name")
    var name: String? = null

    @SerializedName("bannedMsg")
    var bannedMessage: String? = null


    @SerializedName("bannedMsgHeader")
    var bannedMsgHeader: String? = null

    @SerializedName("redirectionURL")
    var redirectionURL: String? = null

    @SerializedName("appRedirectionURL")
    var appRedirectionURL: String? = null

    @SerializedName("appRedirection")
    var appRedirection: String? = null

    @SerializedName("displayButton")
    var displayButton: Boolean = false

    @SerializedName("isBanned")
    var isBanned: Int = 0

    @SerializedName("isAdult")
    var isAdult: Int = 0

    @SerializedName("child")
    val child: List<SubCategoryItem?>? = null

    constructor(parcel: Parcel) : this() {
        id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
        bannedMessage = parcel.readString()
        bannedMsgHeader = parcel.readString()
        redirectionURL = parcel.readString()
        appRedirectionURL = parcel.readString()
        appRedirection = parcel.readString()
        displayButton = parcel.readByte() != 0.toByte()
        isBanned = parcel.readInt()
        isAdult = parcel.readInt()
    }

    override fun toString(): String {
        return "Data{" +
                "banned_message = '" + bannedMessage + '\''.toString() +
                ",app_redirection = '" + appRedirection + '\''.toString() +
                ",redirectionURL = '" + redirectionURL + '\''.toString() +
                ",appRedirectionURL = '" + appRedirectionURL + '\''.toString() +
                ",isBanned = '" + isBanned + '\''.toString() +
                ",isAdult = '" + isAdult + '\''.toString() +
                "}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(name)
        parcel.writeString(bannedMessage)
        parcel.writeString(bannedMsgHeader)
        parcel.writeString(redirectionURL)
        parcel.writeString(appRedirectionURL)
        parcel.writeString(appRedirection)
        parcel.writeByte(if (displayButton) 1 else 0)
        parcel.writeInt(isBanned)
        parcel.writeInt(isAdult)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Data> {
        override fun createFromParcel(parcel: Parcel): Data {
            return Data(parcel)
        }

        override fun newArray(size: Int): Array<Data?> {
            return arrayOfNulls(size)
        }
    }
}