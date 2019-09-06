package com.tokopedia.power_merchant.subscribe.view.model

import android.os.Parcel
import android.os.Parcelable

class PMCancellationQuestionnaireMultipleOptionModel(
        type: String,
        questionString: String,
        var listOptionModel: List<OptionModel>
) : PMCancellationQuestionnaireQuestionModel(type, questionString) {
    constructor(parcel: Parcel) : this(
            parcel.readString() ?: "",
            parcel.readString() ?: "" ,
            parcel.createTypedArrayList(OptionModel) ?: listOf()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeTypedList(listOptionModel)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PMCancellationQuestionnaireMultipleOptionModel> {
        override fun createFromParcel(parcel: Parcel): PMCancellationQuestionnaireMultipleOptionModel {
            return PMCancellationQuestionnaireMultipleOptionModel(parcel)
        }

        override fun newArray(size: Int): Array<PMCancellationQuestionnaireMultipleOptionModel?> {
            return arrayOfNulls(size)
        }
    }

    data class OptionModel(
            var value: String
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeString(value)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<OptionModel> {
            override fun createFromParcel(parcel: Parcel): OptionModel {
                return OptionModel(parcel)
            }

            override fun newArray(size: Int): Array<OptionModel?> {
                return arrayOfNulls(size)
            }
        }
    }
}

