package com.tokopedia.topads.data

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.model.StepperModel

/**
 * Author errysuprayogi on 01,November,2019
 */
open class CreateManualAdsStepperModel() : StepperModel {

    constructor(source: Parcel) : this(
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {

    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<CreateManualAdsStepperModel> = object : Parcelable.Creator<CreateManualAdsStepperModel> {
            override fun createFromParcel(source: Parcel): CreateManualAdsStepperModel = CreateManualAdsStepperModel(source)
            override fun newArray(size: Int): Array<CreateManualAdsStepperModel?> = arrayOfNulls(size)
        }
    }
}