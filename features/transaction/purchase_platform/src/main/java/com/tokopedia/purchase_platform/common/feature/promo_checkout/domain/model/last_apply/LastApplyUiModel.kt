package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LastApplyUiModel(
		var codes: List<String> = emptyList(),
		var voucherOrders: List<LastApplyVoucherOrdersItemUiModel> = emptyList(),
		var additionalInfo: LastApplyAdditionalInfoUiModel = LastApplyAdditionalInfoUiModel(),
		var message: LastApplyMessageUiModel = LastApplyMessageUiModel(),
		var listRedPromos: List<String> = emptyList(),
        var listAllPromoCodes: List<String> = emptyList(),
		var defaultEmptyPromoMessage: String = ""
) : Parcelable