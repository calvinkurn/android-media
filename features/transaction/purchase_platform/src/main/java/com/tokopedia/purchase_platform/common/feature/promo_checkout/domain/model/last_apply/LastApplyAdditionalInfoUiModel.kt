package com.tokopedia.purchase_platform.common.feature.promo_checkout.domain.model.last_apply

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LastApplyAdditionalInfoUiModel(
		var messageInfo: LastApplyMessageInfoUiModel = LastApplyMessageInfoUiModel(),
		var errorDetail: LastApplyErrorDetailUiModel = LastApplyErrorDetailUiModel(),
		var emptyCartInfo: LastApplyEmptyCartInfoUiModel = LastApplyEmptyCartInfoUiModel(),
		var usageSummaries: List<LastApplyUsageSummariesUiModel> = emptyList()
) : Parcelable
