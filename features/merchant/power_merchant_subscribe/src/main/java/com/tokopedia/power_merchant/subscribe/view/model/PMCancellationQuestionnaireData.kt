package com.tokopedia.power_merchant.subscribe.view.model

import android.os.Parcel
import android.os.Parcelable

class PMCancellationQuestionnaireData(
        val expiredDate: String = "",
        val listQuestion: MutableList<PMCancellationQuestionnaireQuestionModel> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            mutableListOf<PMCancellationQuestionnaireQuestionModel>().apply {
                parcel.readList(this, PMCancellationQuestionnaireQuestionModel::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(expiredDate)
        parcel.writeList(listQuestion)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PMCancellationQuestionnaireData> {
        override fun createFromParcel(parcel: Parcel): PMCancellationQuestionnaireData {
            return PMCancellationQuestionnaireData(parcel)
        }

        override fun newArray(size: Int): Array<PMCancellationQuestionnaireData?> {
            return arrayOfNulls(size)
        }
    }
}