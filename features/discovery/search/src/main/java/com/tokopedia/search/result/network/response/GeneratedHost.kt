package com.tokopedia.search.result.network.response

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeneratedHost(
        @SerializedName("server_id")
        @Expose
        private val serverId: Int = 0,

        @SerializedName("upload_host")
        @Expose
        private val uploadHost: String? = null,

        @SerializedName("user_id")
        @Expose
        private val userId: Int = 0,

        @SerializedName("message")
        @Expose
        private val message: String? = null,

        @SerializedName("status")
        @Expose
        private val status: String? = null,

        @SerializedName("message_error")
        @Expose
        private val messageError: List<String>? = null,
) : Parcelable {

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

    protected constructor(parcel: Parcel) : this(
            serverId = parcel.readInt(),
            uploadHost = parcel.readString(),
            userId = parcel.readInt(),
            message = parcel.readString(),
            status = parcel.readString(),
            messageError = parcel.createStringArrayList()
    )

    companion object CREATOR : Parcelable.Creator<GeneratedHost> {
        override fun createFromParcel(source: Parcel): GeneratedHost? {
            return GeneratedHost(source)
        }

        override fun newArray(size: Int): Array<GeneratedHost?> {
            return arrayOfNulls(size)
        }
    }
}
