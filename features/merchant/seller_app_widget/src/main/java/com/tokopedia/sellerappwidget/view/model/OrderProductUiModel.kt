package com.tokopedia.sellerappwidget.view.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created By @ilhamsuaib on 16/11/20
 */

@Parcelize
data class OrderProductUiModel(
        val productId: String? = "",
        val productName: String? = "",
        val picture: String? = ""
): Parcelable