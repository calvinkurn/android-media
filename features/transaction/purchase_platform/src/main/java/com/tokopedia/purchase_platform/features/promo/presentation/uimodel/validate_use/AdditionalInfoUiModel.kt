package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use
import android.os.Parcelable
import android.os.Parcel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AdditionalInfoUiModel(
		var messageInfoUiModel: MessageInfoUiModel = MessageInfoUiModel(),
		var errorDetailUiModel: ErrorDetailUiModel = ErrorDetailUiModel(),
		var emptyCartInfoUiModel: EmptyCartInfoUiModel = EmptyCartInfoUiModel(),
		var usageSummariesUiModel: List<UsageSummariesUiModel> = emptyList()
) : Parcelable
