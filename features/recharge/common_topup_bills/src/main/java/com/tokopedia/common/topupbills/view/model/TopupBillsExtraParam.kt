package com.tokopedia.common.topupbills.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class TopupBillsExtraParam(
    var categoryId: String = "",
    var productId: String = "",
    var clientNumber: String = "",
    var menuId: String = ""
) : Parcelable
