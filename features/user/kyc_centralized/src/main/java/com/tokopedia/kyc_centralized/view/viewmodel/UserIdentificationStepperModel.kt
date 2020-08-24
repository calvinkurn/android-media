package com.tokopedia.kyc_centralized.view.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.abstraction.base.view.model.StepperModel
import java.util.*

/**
 * @author by alvinatin on 12/11/18.
 */
class UserIdentificationStepperModel : StepperModel {
    var ktpFile: String = ""
    var faceFile: String = ""
    var listRetake: ArrayList<Int> = arrayListOf()
    var listMessage: ArrayList<String> = arrayListOf()
    var titleText: String = ""
    var subtitleText: String = ""
    var buttonText: String = ""

    constructor()

    constructor(`in`: Parcel) {
        ktpFile = `in`.readSerializable() as String
        faceFile = `in`.readSerializable() as String
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeSerializable(ktpFile)
        dest.writeSerializable(faceFile)
    }

    companion object {
        val CREATOR: Parcelable.Creator<UserIdentificationStepperModel?> = object : Parcelable.Creator<UserIdentificationStepperModel?> {
            override fun createFromParcel(source: Parcel): UserIdentificationStepperModel? {
                return UserIdentificationStepperModel(source)
            }

            override fun newArray(size: Int): Array<UserIdentificationStepperModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}