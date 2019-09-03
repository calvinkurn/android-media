package com.tokopedia.power_merchant.subscribe.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

class PMCancellationQuestionnaireRateModel(
        priority: Int,
        type: String,
        question: String
) : PMCancellationQuestionnaireModel(priority, type, question) {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString() ?: "",
            parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PMCancellationQuestionnaireRateModel> {
        override fun createFromParcel(parcel: Parcel): PMCancellationQuestionnaireRateModel {
            return PMCancellationQuestionnaireRateModel(parcel)
        }

        override fun newArray(size: Int): Array<PMCancellationQuestionnaireRateModel?> {
            return arrayOfNulls(size)
        }
    }

}