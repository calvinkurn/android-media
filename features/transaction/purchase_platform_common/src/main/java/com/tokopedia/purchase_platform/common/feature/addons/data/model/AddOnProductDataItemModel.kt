package com.tokopedia.purchase_platform.common.feature.addons.data.model

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.constant.AddOnConstant
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOnProductDataItemModel(
    var id: Long = 0L,
    var price: Double = 0.0,
    var uniqueId: String = "",
    var infoLink: String = "",
    var name: String = "",
    var status: Int = -1,
    var type: Int = -1,
    var qty: Int = -1,
    var iconUrl: String = "",
    var fixedQty: Boolean = false
) : Parcelable {

    val isChecked: Boolean
        get() {
            return status == AddOnConstant.ADD_ON_PRODUCT_STATUS_MANDATORY || status == AddOnConstant.ADD_ON_PRODUCT_STATUS_CHECK
        }
}
