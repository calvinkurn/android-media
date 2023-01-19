package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.withdraw.saldowithdrawal.presentation.viewmodel.SubmitWithdrawalViewModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SubmitWithdrawalResponse(
        @SerializedName("process_time")
        val processTime: Float? = null,
        @SerializedName("message")
        val messageArrayList: ArrayList<String>? = null,
        @SerializedName("status")
        val status: String? = null,
        @SerializedName("message_error")
        val messageErrorStr: String? = null,
        @SerializedName("withdrawalNote")
        val withdrawalNote: String? = null,
        @SerializedName("errorCode")
        val errorCode: String? = null,
        @SerializedName("joinPromptMessageResponse")
        val joinPromptMessageResponse: JoinPromptMessageResponse? = null,
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("description")
        val description: String? = null,
) : Parcelable {
    fun isSuccess(): Boolean {
        return status == SubmitWithdrawalViewModel.WITHDRAWAL_SUCCESS
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
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "",
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
