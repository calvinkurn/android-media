package com.tokopedia.tokofood.feature.merchant.presentation.model

import android.os.Parcelable
import com.tokopedia.tokofood.feature.merchant.presentation.enums.CustomListItemType
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomListItem(
        val listItemType: CustomListItemType,
        val addOnUiModel: AddOnUiModel?,
        var orderNote: String = "",
) : Parcelable
