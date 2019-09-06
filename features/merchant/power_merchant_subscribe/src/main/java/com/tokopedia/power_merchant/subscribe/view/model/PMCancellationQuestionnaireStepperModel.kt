package com.tokopedia.power_merchant.subscribe.view.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.model.StepperModel
import com.tokopedia.gm.common.data.source.cloud.model.PMCancellationQuestionnaireAnswerModel

data class PMCancellationQuestionnaireStepperModel(
        var listQuestionnaireAnswer: MutableList<PMCancellationQuestionnaireAnswerModel> = mutableListOf()
) : StepperModel {
    constructor(parcel: Parcel) : this(
            mutableListOf<PMCancellationQuestionnaireAnswerModel>().apply {
                parcel.readList(this, PMCancellationQuestionnaireAnswerModel::class.java.classLoader)
            }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeList(listQuestionnaireAnswer)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PMCancellationQuestionnaireStepperModel> {
        override fun createFromParcel(parcel: Parcel): PMCancellationQuestionnaireStepperModel {
            return PMCancellationQuestionnaireStepperModel(parcel)
        }

        override fun newArray(size: Int): Array<PMCancellationQuestionnaireStepperModel?> {
            return arrayOfNulls(size)
        }
    }

    fun isCurrentQuestionAnswered(currentPosition: Int): Boolean {
        return listQuestionnaireAnswer[currentPosition - 1].answers.isNotEmpty()
    }

}