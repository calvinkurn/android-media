package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LastApplyVoucherOrdersItemUiModel(
    var code: String = "",
    var uniqueId: String = "",
    var message: LastApplyMessageUiModel = LastApplyMessageUiModel(),
    var shippingId: Int = 0,
    var spId: Int = 0,
    var type: String = "",
    var cartStringGroup: String = "",
    var success: Boolean = false
) : Parcelable {

    companion object {
        private const val TYPE_LOGISTIC = "logistic"
    }

    fun isTypeLogistic(): Boolean {
        return type == TYPE_LOGISTIC
    }
}
