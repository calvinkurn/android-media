package com.tokopedia.purchase_platform.common.feature.promo.view.model.lastapply

import android.os.Parcelable
import com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse.BenefitSummaryInfoUiModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LastApplyUiModel(
		var codes: List<String> = emptyList(),
		var voucherOrders: List<LastApplyVoucherOrdersItemUiModel> = emptyList(),
		var additionalInfo: LastApplyAdditionalInfoUiModel = LastApplyAdditionalInfoUiModel(),
		var message: LastApplyMessageUiModel = LastApplyMessageUiModel(),
		var listRedPromos: List<String> = emptyList(),
        var listAllPromoCodes: List<String> = emptyList(),
		var defaultEmptyPromoMessage: String = "",
		var benefitSummaryInfo: BenefitSummaryInfoUiModel = BenefitSummaryInfoUiModel()
) : Parcelable