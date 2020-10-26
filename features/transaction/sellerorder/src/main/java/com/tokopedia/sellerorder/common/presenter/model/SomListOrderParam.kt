package com.tokopedia.sellerorder.common.presenter.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 2019-08-28.
 */

@Parcelize
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
        var nextOrderId: Int = 0,

        @SerializedName("lang")
        @Expose
        var lang: String = "id",

        @SerializedName("page")
        @Expose
        var page: Int = 1,

        @SerializedName("batch_page")
        @Expose
        var batchPage: Int = 0,

        @SerializedName("is_shipping_printed")
        @Expose
        var isShippingPrinted: Int = 0,

        @SerializedName("deadline")
        @Expose
        var deadline: Int = 0

) : Parcelable