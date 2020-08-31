package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ResponseGetPromoStackUiModel(
		var status: String = "",
		var message: List<String> = ArrayList(),
		var data: DataUiModel = DataUiModel()
) : Parcelable
