package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokopointsDetailUiModel(
	var conversionRateUiModel: ConversionRateUiModel? = ConversionRateUiModel()
): Parcelable
