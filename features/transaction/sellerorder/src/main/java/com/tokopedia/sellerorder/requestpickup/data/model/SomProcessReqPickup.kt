package com.tokopedia.sellerorder.requestpickup.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-14.
 */
data class SomProcessReqPickup (
        @SerializedName("data")
        @Expose
        val data: Data = Data(),

        @SerializedName("errors")
        @Expose
        val listError: List<Error> = listOf()) {

    data class Data(
            @SerializedName("mpLogisticRequestPickup")
            @Expose
            val mpLogisticRequestPickup: MpLogisticRequestPickup = MpLogisticRequestPickup()){

        data class MpLogisticRequestPickup(
                @SerializedName("message")
                @Expose
                val listMessage: List<String> = listOf()
        ) : Parcelable {
            constructor(parcel: Parcel) : this(parcel.createStringArrayList() ?: arrayListOf())

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeStringList(listMessage)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<MpLogisticRequestPickup> {
                override fun createFromParcel(parcel: Parcel): MpLogisticRequestPickup {
                    return MpLogisticRequestPickup(parcel)
                }

                override fun newArray(size: Int): Array<MpLogisticRequestPickup?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    data class Error(
            @SerializedName("message")
            @Expose
            val msgError: String = ""
    )
}