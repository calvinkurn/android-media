package com.tokopedia.topads.common.view.adapter.tips.viewmodel

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TipsUiHeaderModel(@StringRes val headerText: Int = 0,
                             @DrawableRes val headerRes: Int = 0) : TipsUiModel