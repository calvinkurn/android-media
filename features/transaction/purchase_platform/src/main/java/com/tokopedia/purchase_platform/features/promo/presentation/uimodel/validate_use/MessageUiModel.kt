package com.tokopedia.purchase_platform.features.promo.presentation.uimodel.validate_use

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageUiModel(
	var color: String = "",
	var state: String = "",
	var text: String = ""
): Parcelable
