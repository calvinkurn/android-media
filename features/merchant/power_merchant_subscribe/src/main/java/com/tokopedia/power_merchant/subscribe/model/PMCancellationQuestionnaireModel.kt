package com.tokopedia.power_merchant.subscribe.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

abstract class PMCancellationQuestionnaireModel(
        var priority: Int,
        var type : String,
        var question: String
): Parcelable{

    companion object{
        const val TYPE_RATE = "rate"
        const val TYPE_MULTIPLE_OPTION = "multi_option"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(priority)
        parcel.writeString(type)
        parcel.writeString(question)
    }

    override fun describeContents(): Int {
        return 0
    }

}