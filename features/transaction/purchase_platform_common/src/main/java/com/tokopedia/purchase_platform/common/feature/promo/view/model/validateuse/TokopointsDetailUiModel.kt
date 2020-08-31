package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokopointsDetailUiModel(
	var conversionRateUiModel: ConversionRateUiModel? = ConversionRateUiModel()
): Parcelable
