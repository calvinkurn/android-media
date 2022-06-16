package com.tokopedia.tokomember_seller_dashboard.domain.requestparam

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TmMerchantCouponUnifyRequest(
    var status: String = "",
    var token: String = "",
    var source: String = "",
    var voucher: ArrayList<TmCouponCreateRequest> = arrayListOf()
) : Parcelable