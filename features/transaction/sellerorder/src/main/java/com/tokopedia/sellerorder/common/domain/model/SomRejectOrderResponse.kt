package com.tokopedia.sellerorder.common.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-11-06.
 */
class SomRejectOrderResponse (
        @SerializedName("data")
        @Expose
        val data: Data = Data()
) {
    data class Data (
            @SerializedName("reject_order")
            @Expose
            val rejectOrder: RejectOrder = RejectOrder()
    ) {
        data class RejectOrder (
                @SerializedName("success")
                @Expose
                val success: Int = -1,

                @SerializedName("message")
                @Expose
                val message: List<String> = listOf()
        ) : Parcelable {
            constructor(parcel: Parcel) : this(
                    parcel.readInt(),
                    parcel.createStringArrayList() ?: listOf())

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeInt(success)
                parcel.writeStringList(message)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<RejectOrder> {
                override fun createFromParcel(parcel: Parcel): RejectOrder {
                    return RejectOrder(parcel)
                }

                override fun newArray(size: Int): Array<RejectOrder?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }
}