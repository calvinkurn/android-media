package com.tokopedia.promocheckout.common.view.uimodel

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageUiModel(
	var color: String = "",
	var state: String = "",
	var text: String = ""
) : Parcelable
