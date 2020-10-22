package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 17/03/19.
 */
@Parcelize
data class ClashingInfoDetailUiModel(
        var clashMessage: String = "",
        var clashReason: String = "",
        var isClashedPromos: Boolean = false,
        var options: ArrayList<ClashingVoucherOptionUiModel> = ArrayList()
) : Parcelable