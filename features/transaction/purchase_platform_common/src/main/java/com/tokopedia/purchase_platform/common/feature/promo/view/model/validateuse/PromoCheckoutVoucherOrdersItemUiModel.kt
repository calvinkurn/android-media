package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromoCheckoutVoucherOrdersItemUiModel(
        var code: String = "",
        var uniqueId: String = "",
        var discountAmount: Int = -1,
        var titleDescription: String = "",
        var type: String = "",
        var messageUiModel: MessageUiModel = MessageUiModel(),
        var success: Boolean = false
): Parcelable
