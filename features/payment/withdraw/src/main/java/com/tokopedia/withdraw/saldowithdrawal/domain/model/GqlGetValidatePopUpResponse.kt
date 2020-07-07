package com.tokopedia.withdraw.saldowithdrawal.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class GqlGetValidatePopUpResponse(
        @SerializedName("ValidatePopUpWithdrawal")
        var validatePopUpWithdrawal: ValidatePopUpWithdrawal
)


data class ValidatePopUpWithdrawal(
        @SerializedName("data")
        var data: ValidatePopUpData,
        @SerializedName("joinData")
        var joinData: JoinRekeningPremium,
        @SerializedName("message")
        val message: String? = null,
        @SerializedName("status")
        val status: Int = 0
)

data class ValidatePopUpData(

        @SerializedName("note")
        var note: String,

        @SerializedName("needShow")
        val needShow: Boolean,

        @SerializedName("title")
        val title: String
)

data class JoinRekeningPremium(
        @SerializedName("title")
        var title: String,

        @SerializedName("desc")
        val descriptionStringArray: ArrayList<String>,

        @SerializedName("isJoinPrompt")
        var isJoinPrompt: Boolean = false,

        @SerializedName("programID")
        val programID: Int,

        @SerializedName("programName")
        val programName: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.createStringArrayList() ?: arrayListOf(),
            parcel.readValue(Boolean::class.java.classLoader) as Boolean,
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeStringList(descriptionStringArray)
        parcel.writeValue(isJoinPrompt)
        parcel.writeInt(programID)
        parcel.writeString(programName)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<JoinRekeningPremium> {
        override fun createFromParcel(parcel: Parcel): JoinRekeningPremium {
            return JoinRekeningPremium(parcel)
        }

        override fun newArray(size: Int): Array<JoinRekeningPremium?> {
            return arrayOfNulls(size)
        }
    }
}