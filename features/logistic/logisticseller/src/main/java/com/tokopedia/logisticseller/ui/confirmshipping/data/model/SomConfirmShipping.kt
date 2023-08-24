package com.tokopedia.logisticseller.ui.confirmshipping.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-12.
 */
data class SomConfirmShipping (
    @SerializedName("data")
    @Expose
    val data: Data = Data()
) {
        data class Data(
            @SerializedName("mpLogisticConfirmShipping")
            @Expose
            val mpLogisticConfirmShipping: MpLogisticConfirmShipping = MpLogisticConfirmShipping()
        ) {

        data class MpLogisticConfirmShipping(
                @SerializedName("message")
                @Expose
                val listMessage: List<String> = listOf()) : Parcelable {
            constructor(parcel: Parcel) : this(parcel.createStringArrayList() ?: arrayListOf())

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeStringList(listMessage)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<MpLogisticConfirmShipping> {
                override fun createFromParcel(parcel: Parcel): MpLogisticConfirmShipping {
                    return MpLogisticConfirmShipping(parcel)
                }

                override fun newArray(size: Int): Array<MpLogisticConfirmShipping?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}
