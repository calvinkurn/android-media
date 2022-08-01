package com.tokopedia.tokomember_seller_dashboard.model

import android.os.Parcelable
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TmCouponListItemPreview
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TmCouponPreviewData(

    var voucherList: ArrayList<TmCouponListItemPreview> = arrayListOf(),
    var startDate:String = "",
    var endDate:String =  "",
    var startTime: String = "",
    var endTime: String = "",
    var maxValue: String = ""
) : Parcelable