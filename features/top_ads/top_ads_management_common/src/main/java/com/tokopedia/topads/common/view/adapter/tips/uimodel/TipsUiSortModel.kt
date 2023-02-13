package com.tokopedia.topads.common.view.adapter.tips.viewmodel

import androidx.annotation.StringRes
import kotlinx.android.parcel.Parcelize

@Parcelize
class TipsUiSortModel(@StringRes val headerText: Int = 0,
                      @StringRes val subHeaderText: Int = 0,
                      var isChecked: Boolean = false) : TipsUiModel