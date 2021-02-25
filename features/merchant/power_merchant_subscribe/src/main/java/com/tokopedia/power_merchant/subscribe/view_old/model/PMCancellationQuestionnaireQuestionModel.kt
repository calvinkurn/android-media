package com.tokopedia.power_merchant.subscribe.view_old.model

import android.os.Parcel
import android.os.Parcelable

abstract class PMCancellationQuestionnaireQuestionModel(
        var type : String = "",
        var question: String = ""
): Parcelable{

    companion object{
        const val TYPE_RATE = "rate"
        const val TYPE_MULTIPLE_OPTION = "multi_answer_question"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(type)
        parcel.writeString(question)
    }

    override fun describeContents(): Int {
        return 0
    }

}