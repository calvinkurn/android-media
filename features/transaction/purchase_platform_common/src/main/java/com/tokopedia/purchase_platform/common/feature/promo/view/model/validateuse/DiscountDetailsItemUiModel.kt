package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DiscountDetailsItemUiModel(
	var amount: Int = -1,
	var dataType: String = ""
) : Parcelable