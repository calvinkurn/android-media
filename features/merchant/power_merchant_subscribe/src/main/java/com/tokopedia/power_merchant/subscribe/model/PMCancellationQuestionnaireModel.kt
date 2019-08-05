package com.tokopedia.power_merchant.subscribe.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable

open class PMCancellationQuestionnaireModel() : Parcelable {
    var type = -1

    constructor(parcel: Parcel) : this() {
        type = parcel.readInt()
    }

    data class LinearScaleQuestionnaire(
            val questionString: String = ""
    ) : PMCancellationQuestionnaireModel() {
        constructor(parcel: Parcel) : this(parcel.readString()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeString(questionString)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<LinearScaleQuestionnaire> {
            override fun createFromParcel(parcel: Parcel): LinearScaleQuestionnaire {
                return LinearScaleQuestionnaire(parcel)
            }

            override fun newArray(size: Int): Array<LinearScaleQuestionnaire?> {
                return arrayOfNulls(size)
            }
        }
    }

    data class MultipleChecklistQuestionnaire(
            val questionString: String? = "",
            val listChecklistOption: ArrayList<String>? = ArrayList()
    ) : PMCancellationQuestionnaireModel() {
        constructor(parcel: Parcel) : this(
                parcel.readString(),
                parcel.createStringArrayList()) {
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeString(questionString)
            parcel.writeStringList(listChecklistOption)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<MultipleChecklistQuestionnaire> {
            override fun createFromParcel(parcel: Parcel): MultipleChecklistQuestionnaire {
                return MultipleChecklistQuestionnaire(parcel)
            }

            override fun newArray(size: Int): Array<MultipleChecklistQuestionnaire?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(type)
    }

    override fun describeContents(): Int {
        return 0
    }

    @SuppressLint("ParcelCreator")
    companion object CREATOR : Parcelable.Creator<PMCancellationQuestionnaireModel> {
        override fun createFromParcel(parcel: Parcel): PMCancellationQuestionnaireModel {
            return PMCancellationQuestionnaireModel(parcel)
        }

        override fun newArray(size: Int): Array<PMCancellationQuestionnaireModel?> {
            return arrayOfNulls(size)
        }
    }

}