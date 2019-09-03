package com.tokopedia.gm.common.data.source.cloud.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PMCancellationQuestionnaireAnswerModel(
        @SerializedName("question")
        @Expose
        var question: String = "",
        @SerializedName("answers")
        @Expose
        val answers: MutableList<String> = mutableListOf()
) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.createStringArrayList() ?: mutableListOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(question)
        parcel.writeStringList(answers)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PMCancellationQuestionnaireAnswerModel> {
        override fun createFromParcel(parcel: Parcel): PMCancellationQuestionnaireAnswerModel {
            return PMCancellationQuestionnaireAnswerModel(parcel)
        }

        override fun newArray(size: Int): Array<PMCancellationQuestionnaireAnswerModel?> {
            return arrayOfNulls(size)
        }
    }

}