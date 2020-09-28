package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcel
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 21/03/19.
 */
@Parcelize
data class ClashingVoucherOptionUiModel(
        var voucherOrders: ArrayList<ClashingVoucherOrderUiModel> = ArrayList(),
        var isSelected: Boolean = false
) : Parcelable