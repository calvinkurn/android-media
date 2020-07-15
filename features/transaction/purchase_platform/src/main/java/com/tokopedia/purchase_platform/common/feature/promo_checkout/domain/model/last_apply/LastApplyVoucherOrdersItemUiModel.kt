package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LastApplyVoucherOrdersItemUiModel(
	var code: String = "",
	var uniqueId: String = "",
	var message: LastApplyMessageUiModel = LastApplyMessageUiModel()
) : Parcelable
