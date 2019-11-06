package com.tokopedia.atc_common.domain.model.response

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Irfan Khoirul on 2019-07-10.
 */

data class AddToCartDataModel(
    var errorMessage: ArrayList<String> = arrayListOf(),
    var status: String = "",
    var data: DataModel = DataModel(),
    var errorReporter: ErrorReporterModel = ErrorReporterModel(),
    var responseJson: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
            parcel.createStringArrayList(),
            parcel.readString(),
            parcel.readParcelable(DataModel::class.java.classLoader),
            parcel.readParcelable(ErrorReporterModel::class.java.classLoader),
            parcel.readString())

    companion object {
        const val STATUS_OK = "OK"
        const val STATUS_ERROR = "ERROR"

        @JvmField
        val CREATOR = object : Parcelable.Creator<AddToCartDataModel> {
            override fun createFromParcel(parcel: Parcel): AddToCartDataModel {
                return AddToCartDataModel(parcel)
            }

            override fun newArray(size: Int): Array<AddToCartDataModel?> {
                return arrayOfNulls(size)
            }
        }
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeStringList(errorMessage)
        parcel.writeString(status)
        parcel.writeParcelable(data, flags)
        parcel.writeParcelable(errorReporter, flags)
        parcel.writeString(responseJson)
    }

    override fun describeContents(): Int {
        return 0
    }
}