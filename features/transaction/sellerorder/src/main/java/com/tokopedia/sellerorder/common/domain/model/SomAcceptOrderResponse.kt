package com.tokopedia.sellerorder.common.domain.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by fwidjaja on 2019-10-09.
 */
data class SomAcceptOrderResponse (
        @SerializedName("data")
        @Expose
        val data: Data = Data()) {
            data class Data (
                    @SerializedName("accept_order")
                    @Expose
                    val acceptOrder: AcceptOrder = AcceptOrder()) {
                        data class AcceptOrder (
                                @SerializedName("success")
                                @Expose
                                val success: Int = 0,

                                @SerializedName("message")
                                @Expose
                                val listMessage: List<String> = listOf()
                        ) : Parcelable {
                            constructor(parcel: Parcel) : this(
                                    parcel.readInt(),
                                    parcel.createStringArrayList() ?: listOf())

                            override fun writeToParcel(parcel: Parcel, flags: Int) {
                                parcel.writeInt(success)
                                parcel.writeStringList(listMessage)
                            }

                            override fun describeContents(): Int {
                                return 0
                            }

                            companion object CREATOR : Parcelable.Creator<AcceptOrder> {
                                override fun createFromParcel(parcel: Parcel): AcceptOrder {
                                    return AcceptOrder(parcel)
                                }

                                override fun newArray(size: Int): Array<AcceptOrder?> {
                                    return arrayOfNulls(size)
                                }
                            }
                        }
            }
    }