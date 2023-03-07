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
    var type: String = ""
) : Parcelable
