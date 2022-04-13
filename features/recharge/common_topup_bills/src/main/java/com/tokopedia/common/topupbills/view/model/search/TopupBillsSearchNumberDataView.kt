package com.tokopedia.common.topupbills.view.model.search

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TopupBillsSearchNumberDataView(
    val clientName: String = "",
    val clientNumber: String = "",
    val categoryId: String = "",
    val operatorId: String = "",
    val productId: String = "",
    var isFavorite: Boolean = true
): Parcelable