package com.tokopedia.common.topupbills.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Data wrapper class used by seamless & perso page
 * */
@Parcelize
data class TopupBillsSavedNumber (
    val categoryId: String = "",
    val clientNumber: String = "",
    var clientName: String = "",
    val productId: String = "",
    var inputNumberActionTypeIndex: Int
) : Parcelable