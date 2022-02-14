package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AddOns(
        var addOnButton: AddOnButton = AddOnButton(),
        var addOnBottomsheet: AddOnBottomsheet = AddOnBottomsheet(),
        var addOnData: List<AddOnDataItem> = emptyList(),
        var status: Int = 0
): Parcelable
