package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConversionRateUiModel(
        var pointsCoefficient: Int = -1,
        var rate: Int = -1,
        var externalCurrencyCoefficient: Int = -1
) : Parcelable
