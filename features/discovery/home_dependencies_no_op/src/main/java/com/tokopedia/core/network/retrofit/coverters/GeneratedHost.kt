package com.tokopedia.core.network.retrofit.coverters

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeneratedHost() : Parcelable {
    private val TAG = GeneratedHost::class.java.simpleName

    @SerializedName("server_id")
    @Expose
    internal var serverId: Int = 0
    @SerializedName("upload_host")
    @Expose
    internal var uploadHost: String? = null
    @SerializedName("user_id")
    @Expose
    internal var userId: Int = 0

    @SerializedName("message")
    @Expose
    private var message: String? = null
    @SerializedName("status")
    @Expose
    private var status: String? = null
    @SerializedName("message_error")
    @Expose
    private var messageError: List<String>? = null

    constructor(parcel: Parcel) : this() {
        serverId = parcel.readInt()
        uploadHost = parcel.readString()
        userId = parcel.readInt()
        message = parcel.readString()
        status = parcel.readString()
        messageError = parcel.createStringArrayList()
    }

    fun getServerId(): Int? {
        return serverId
    }

    fun setServerId(serverId: Int?) {
        this.serverId = serverId!!
    }

    fun getUploadHost(): String? {
        return uploadHost
    }

    fun setUploadHost(uploadHost: String) {
        this.uploadHost = uploadHost
    }

    fun getUserId(): Int? {
        return userId
    }

    fun setUserId(userId: Int?) {
        this.userId = userId!!
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getStatus(): String? {
        return status
    }

    fun setStatus(status: String) {
        this.status = status
    }

    fun getMessageError(): List<String>? {
        return messageError
    }

    fun setMessageError(messageError: List<String>) {
        this.messageError = messageError
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(serverId)
        parcel.writeString(uploadHost)
        parcel.writeInt(userId)
        parcel.writeString(message)
        parcel.writeString(status)
        parcel.writeStringList(messageError)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GeneratedHost> {
        override fun createFromParcel(parcel: Parcel): GeneratedHost {
            return GeneratedHost(parcel)
        }

        override fun newArray(size: Int): Array<GeneratedHost?> {
            return arrayOfNulls(size)
        }
    }
}