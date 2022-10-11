package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.unifyprinciples.Typography
import kotlinx.parcelize.Parcelize

/**
 * Created by ekades on 26/10/22.
 */

@Parcelize
class ShippingScheduleWidgetModel(
    val isEnable: Boolean,
    val title: CharSequence? = null,
    val titleWeightType: Int = Typography.BOLD,
    val description: String? = null,
    val label: CharSequence? = null,
    val isNowTwoHours: Boolean,
    var isSelected: Boolean,
    var isShowCoachMark: Boolean = false,
    var onSelectedWidgetListener: (() -> Unit)? = null,
    var onClickIconListener: (() -> Unit)? = null
) : Parcelable
