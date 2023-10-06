package com.tokopedia.logisticseller.ui.confirmshipping.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-23.
 */
data class SomChangeCourier(
    @SerializedName("data")
    val data: Data = Data(),

    @SerializedName("errors")
    val errorList: List<Error> = listOf()
) {
    data class Data(
        @SerializedName("mpLogisticChangeCourier")
        val mpLogisticChangeCourier: MpLogisticChangeCourier = MpLogisticChangeCourier()
    ) {

        data class MpLogisticChangeCourier(
            @SerializedName("message")
            val listMessage: List<String> = listOf()
        ) : Parcelable {
            constructor(parcel: Parcel) : this(parcel.createStringArrayList() ?: arrayListOf())

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeStringList(listMessage)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<MpLogisticChangeCourier> {
                override fun createFromParcel(parcel: Parcel): MpLogisticChangeCourier {
                    return MpLogisticChangeCourier(parcel)
                }

                override fun newArray(size: Int): Array<MpLogisticChangeCourier?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    data class Error(
        @SerializedName("message")
        val errorMessage: String = ""
    )
}
