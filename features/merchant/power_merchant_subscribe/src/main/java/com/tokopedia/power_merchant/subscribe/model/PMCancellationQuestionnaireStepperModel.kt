package com.tokopedia.power_merchant.subscribe.model

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.model.StepperModel

class PMCancellationQuestionnaireStepperModel() : StepperModel {

    var star: Int = 0

    var listChoice = ArrayList<ArrayList<Int>>()

    constructor(parcel: Parcel) : this() {
        star = parcel.readInt()
        listChoice = parcel.readArrayList(ArrayList::class.java.classLoader) as ArrayList<ArrayList<Int>>
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(star)
        parcel.writeList(listChoice)
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
}