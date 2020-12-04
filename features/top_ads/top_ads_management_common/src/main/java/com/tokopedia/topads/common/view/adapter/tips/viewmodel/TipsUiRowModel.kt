package com.tokopedia.topads.common.view.adapter.tips.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TipsUiRowModel(@StringRes val rowText: Int = 0,
                          @DrawableRes val rowRes: Int = 0) : TipsUiModel