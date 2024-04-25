package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PaidSectionInfoUiModel(
    val showCollapsableIcon: Boolean = false,
    var isCollapsed: Boolean = true,
    val title: String = ""
) : Parcelable, RatesViewModelType
