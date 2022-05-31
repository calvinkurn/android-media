package com.tokopedia.tokomember_seller_dashboard.view.adapter.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TmCouponListItemPreview(
    var couponImage: String = "",
    var level: String = "",
    var quota: String = ""
) : Parcelable