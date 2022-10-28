package com.tokopedia.shop.flashsale.presentation.creation.manage.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WarehouseUiModel (
    val id: String = "",
    val name: String = "",
    val stock: Long = 0,
    var isSelected: Boolean = false
): Parcelable