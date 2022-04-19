package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromoTypeUiModel(
	var isExclusiveShipping: Boolean = false,
	var isBebasOngkir: Boolean = false
) : Parcelable