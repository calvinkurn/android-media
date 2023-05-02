package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by ekades on 26/10/22.
 */

@Parcelize
class ShippingScheduleWidgetModel(
    val isEnable: Boolean,
    val title: CharSequence? = null,
    val description: String? = null,
    val label: CharSequence? = null,
    var isSelected: Boolean,
    var isShowCoachMark: Boolean = false,
    var onSelectedWidgetListener: (() -> Unit)? = null,
    var onClickIconListener: (() -> Unit)? = null
) : Parcelable
