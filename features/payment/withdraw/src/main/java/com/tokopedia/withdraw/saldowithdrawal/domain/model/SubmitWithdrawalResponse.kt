package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitWithdrawalResponse(
        @SerializedName("process_time")
        val processTime: Float?,
        @SerializedName("message")
        val messageArrayList: ArrayList<String>?,
        @SerializedName("status")
        val status: String?,
        @SerializedName("message_error")
        val messageErrorStr: String?,
        @SerializedName("withdrawalNote")
        val withdrawalNote: String?,
        @SerializedName("errorCode")
        val errorCode: String?,
        @SerializedName("joinPromptMessageResponse")
        val joinPromptMessageResponse: JoinPromptMessageResponse) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readParcelable(JoinPromptMessageResponse::class.java.classLoader))

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(processTime)
        parcel.writeString(status)
        parcel.writeString(messageErrorStr)
        parcel.writeString(withdrawalNote)
        parcel.writeString(errorCode)
        parcel.writeParcelable(joinPromptMessageResponse, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubmitWithdrawalResponse> {
        override fun createFromParcel(parcel: Parcel): SubmitWithdrawalResponse {
            return SubmitWithdrawalResponse(parcel)
        }

        override fun newArray(size: Int): Array<SubmitWithdrawalResponse?> {
            return arrayOfNulls(size)
        }
    }
}

data class JoinPromptMessageResponse(
        @SerializedName("title")
        val title: String,
        @SerializedName("desc")
        val description: String,
        @SerializedName("actionLink")
        val actionLink: String,
        @SerializedName("actionText")
        val actionText: String,
        @SerializedName("isSuccess")
        val isSuccess: Boolean,
        @SerializedName("statusCode")
        val statusCode: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readByte() != 0.toByte(),
            parcel.readInt()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(actionLink)
        parcel.writeString(actionText)
        parcel.writeByte(if (isSuccess) 1 else 0)
        parcel.writeInt(statusCode)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JoinPromptMessageResponse> {
        override fun createFromParcel(parcel: Parcel): JoinPromptMessageResponse {
            return JoinPromptMessageResponse(parcel)
        }

        override fun newArray(size: Int): Array<JoinPromptMessageResponse?> {
            return arrayOfNulls(size)
        }
    }
}

data class GQLSubmitWithdrawalResponse(
        @SerializedName("richieSubmitWithdrawal")
        @Expose
        val submitWithdrawalResponse: SubmitWithdrawalResponse
)