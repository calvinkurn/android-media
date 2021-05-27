package com.tokopedia.power_merchant.subscribe.view_old.model

import android.os.Parcel
import android.os.Parcelable

class PMCancellationQuestionnaireRateModel(
        type: String,
        question: String
) : PMCancellationQuestionnaireQuestionModel(type, question) {
    constructor(parcel: Parcel) : this(
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