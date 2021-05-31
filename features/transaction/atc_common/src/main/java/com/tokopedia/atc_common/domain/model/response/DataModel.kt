package com.tokopedia.atc_common.domain.model.response

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by Irfan Khoirul on 2019-07-15.
 */
@Parcelize
data class DataModel(
        var success: Int = 0,
        var cartId: String = "",
        var productId: Long = 0,
        var quantity: Int = 0,
        var notes: String = "",
        var shopId: Long = 0,
        var customerId: Long = 0,
        var warehouseId: Long = 0,
        var trackerAttribution: String = "",
        var trackerListName: String = "",
        var ucUtParam: String = "",
        var isTradeIn: Boolean = false,
        var message: ArrayList<String> = arrayListOf(),
        var ovoValidationDataModel: OvoValidationDataModel = OvoValidationDataModel(),
        var refreshPrerequisitePage: Boolean = false
) : Parcelable