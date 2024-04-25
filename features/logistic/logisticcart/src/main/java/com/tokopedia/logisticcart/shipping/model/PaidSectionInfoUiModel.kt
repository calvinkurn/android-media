package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaidSectionInfoUiModel(
    val showCollapsableIcon: Boolean = false,
    val isCollapsed: Boolean = false,
    val title: String = ""
) : Parcelable, RatesViewModelType
