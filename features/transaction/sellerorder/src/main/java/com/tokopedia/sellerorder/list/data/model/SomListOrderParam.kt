package com.tokopedia.sellerorder.list.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.createIntList
import com.tokopedia.kotlin.extensions.view.writeIntList

/**
 * Created by fwidjaja on 2019-08-28.
 */

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
data class SomListOrderParam(
        @SerializedName("search")
        @Expose
        var search: String = "",

        @SerializedName("start_date")
        @Expose
        var startDate: String = "01/01/2010",

        @SerializedName("end_date")
        @Expose
        var endDate: String = "",

        @SerializedName("filter_status")
        @Expose
        var filterStatus: Int = 999,

        @SerializedName("status_list")
        @Expose
        var statusList: List<Int> = listOf(),

        @SerializedName("shipping_list")
        @Expose
        var shippingList: List<Int> = arrayListOf(),

        @SerializedName("order_type_list")
        @Expose
        var orderTypeList: List<Int> = arrayListOf(),

        @SerializedName("sort_by")
        @Expose
        var sortBy: Int = 0,

        @SerializedName("is_mobile")
        @Expose
        var isMobile: Boolean = true,

        @SerializedName("next_order_id")
        @Expose
        var nextOrderId: Int = 0

) : Parcelable {
        constructor(parcel: Parcel) : this(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readInt(),
                parcel.createIntList(),
                parcel.createIntList(),
                parcel.createIntList(),
                parcel.readInt(),
                parcel.readByte() != 0.toByte(),
                parcel.readInt())

        override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeString(search)
                parcel.writeString(startDate)
                parcel.writeString(endDate)
                parcel.writeInt(filterStatus)
                parcel.writeIntList(statusList)
                parcel.writeIntList(shippingList)
                parcel.writeIntList(orderTypeList)
                parcel.writeInt(sortBy)
                parcel.writeByte(if (isMobile) 1 else 0)
                parcel.writeInt(nextOrderId)
        }

        override fun describeContents(): Int {
                return 0
        }

        companion object CREATOR : Parcelable.Creator<SomListOrderParam> {
                override fun createFromParcel(parcel: Parcel): SomListOrderParam {
                        return SomListOrderParam(parcel)
                }

                override fun newArray(size: Int): Array<SomListOrderParam?> {
                        return arrayOfNulls(size)
                }
        }

}