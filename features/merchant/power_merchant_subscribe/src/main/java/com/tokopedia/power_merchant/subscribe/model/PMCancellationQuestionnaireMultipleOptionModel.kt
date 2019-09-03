package com.tokopedia.power_merchant.subscribe.model

import android.os.Parcel
import android.os.Parcelable

class PMCancellationQuestionnaireMultipleOptionModel(
        priority: Int,
        type: String,
        questionString: String,
        var listOptionModel: List<OptionModel>
) : PMCancellationQuestionnaireModel(priority,type, questionString) {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
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
            var priority: Int,
            var value: String
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readInt(),
                parcel.readString() ?: ""
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeInt(priority)
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

