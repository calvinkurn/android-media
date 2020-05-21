package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitWithdrawalResponse(
        @SerializedName("process_time")
        @Expose
        val processTime: Float?,
        @SerializedName("message")
        @Expose
        val messageArrayList: ArrayList<String>?,
        @SerializedName("status")
        @Expose
        val status: String?,
        @SerializedName("message_error")
        @Expose
        val messageErrorStr: String?,
        @SerializedName("withdrawalNote")
        @Expose
        val withdrawalNote: String?,
        @SerializedName("errorCode")
        @Expose
        val errorCode: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readValue(Float::class.java.classLoader) as? Float,
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(processTime)
        parcel.writeString(status)
        parcel.writeString(messageErrorStr)
        parcel.writeString(withdrawalNote)
        parcel.writeString(errorCode)
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

data class GQLSubmitWithdrawalResponse(
        @SerializedName("richieSubmitWithdrawal")
        @Expose
        val submitWithdrawalResponse: SubmitWithdrawalResponse
)