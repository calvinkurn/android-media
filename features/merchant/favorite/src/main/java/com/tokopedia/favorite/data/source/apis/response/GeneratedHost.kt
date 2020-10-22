package com.tokopedia.favorite.data.source.apis.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeneratedHost() : Parcelable {

    companion object {
        private val TAG = GeneratedHost::class.java.simpleName

        val CREATOR: Parcelable.Creator<GeneratedHost> = object : Parcelable.Creator<GeneratedHost> {
            override fun createFromParcel(source: Parcel): GeneratedHost? {
                return GeneratedHost(source)
            }

            override fun newArray(size: Int): Array<GeneratedHost?> {
                return arrayOfNulls(size)
            }
        }
    }

    protected constructor(`in`: Parcel) : this() {
        serverId = `in`.readInt()
        uploadHost = `in`.readString()
        userId = `in`.readInt()
        message = `in`.readString()
        status = `in`.readString()
        messageError = `in`.createStringArrayList()
    }

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
