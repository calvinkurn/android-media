package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TrackingDetailsItemUiModel(
	var promoDetailsTracking: String? = null,
	var productId: Long? = null,
	var promoCodesTracking: String? = null
) : Parcelable