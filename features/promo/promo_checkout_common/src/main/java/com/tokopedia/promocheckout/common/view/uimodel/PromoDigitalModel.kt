package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PromoDigitalModel(
	var categoryId: Int = 0,
	var categoryName: String = "",
	var operatorName: String = "",
	var productId: Int = 0,
	var clientNumber: String = "",
	var price: Long = 0
) : Parcelable
