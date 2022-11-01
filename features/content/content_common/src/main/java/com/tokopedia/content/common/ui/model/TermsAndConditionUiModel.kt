package com.tokopedia.content.common.ui.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TermsAndConditionUiModel(
        val desc: String,
) : Parcelable
