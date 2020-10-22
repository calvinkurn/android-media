package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fajarnuha on 02/04/19.
 */
@Parcelize
data class VoucherLogisticItemUiModel(
        var code: String = "",
        var couponDesc: String = "",
        var couponAmount: String = "",
        var cashbackAmount: Int = 0,
        var discountAmount: Int = 0,
        var message: MessageUiModel = MessageUiModel(),
        var couponAmountRaw: Int = 0) : Parcelable