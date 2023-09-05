package com.tokopedia.promocheckout.common.view.uimodel

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
    var cashbackAmount: Long = 0L,
    var discountAmount: Long = 0L,
    var message: MessageUiModel = MessageUiModel(),
    var couponAmountRaw: Long = 0,
    var uniqueId: String = ""
) : Parcelable
