package com.tokopedia.purchase_platform.common.feature.promo.view.model.validateuse

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class BenefitProductDetailsItemUiModel(
	var cashbackAmountIdr: Int? = -1,
	var cashbackAmount: Int? = -1,
	var discountAmount: Int? = -1,
	var productId: Long? = -1,
	var isBebasOngkir: Boolean? = false
) : Parcelable