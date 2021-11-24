package com.tokopedia.common.topupbills.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsSavedNumber (
    val categoryId: String = "",
    val clientNumber: String = "",
    var clientName: String = "",
    val productId: String = "",
    var inputNumberActionTypeIndex: Int
) : Parcelable