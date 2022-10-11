package com.tokopedia.sellerorder.list.domain.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.sellerorder.common.util.SomConsts
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SomListGetOrderListParam(
        @SerializedName("search")
        @Expose
        var search: String = "",
        @SerializedName("start_date")
        @Expose
        var startDate: String = SomFilterUtil.getDefaultDateFilter().first,
        @SerializedName("end_date")
        @Expose
        var endDate: String = SomFilterUtil.getDefaultDateFilter().second,
        @SerializedName("filter_status")
        @Expose
        var filterStatus: Int = 999,
        @SerializedName("status_list")
        @Expose
        var statusList: List<Int> = listOf(),
        @SerializedName("shipping_list")
        @Expose
        var shippingList: MutableSet<Long> = mutableSetOf(),
        @SerializedName("order_type_list")
        @Expose
        var orderTypeList: MutableSet<Long> = mutableSetOf(),
        @SerializedName("sort_by")
        @Expose
        var sortBy: Long = SomFilterUtil.getDefaultSortBy(SomConsts.STATUS_ALL_ORDER),
        @SerializedName("is_mobile")
        @Expose
        var isMobile: Boolean = true,
        @SerializedName("next_order_id")
        @Expose
        var nextOrderId: Long = 0,
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
        var isShippingPrinted: Long = 0,
        @SerializedName("deadline")
        @Expose
        var deadline: Int = 0,
        @SerializedName("source")
        @Expose
        var source: String = "som-list"
) : Parcelable