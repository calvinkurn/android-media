package com.tokopedia.favorite.data.source.apis.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeneratedHost() : Parcelable {

    @SerializedName("server_id")
    @Expose
    var serverId = 0

    @SerializedName("upload_host")
    @Expose
    var uploadHost: String? = null

    @SerializedName("user_id")
    @Expose
    var userId = 0

    @SerializedName("message")
    @Expose
    var message: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("message_error")
    @Expose
    var messageError: List<String>? = null

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(serverId)
        dest.writeString(uploadHost)
        dest.writeInt(userId)
        dest.writeString(message)
        dest.writeString(status)
        dest.writeStringList(messageError)
    }

}
