package com.tokopedia.logisticcart.shipping.model

import android.os.Parcelable
import com.tokopedia.coachmark.CoachMark2
import kotlinx.parcelize.Parcelize

/**
 * Created by ekades on 26/10/22.
 */

@Parcelize
class ShippingNowTimeOptionModel(
    val isEnable: Boolean,
    val title: String,
    val description: String? = null,
    val warning: String? = null,
    val isError: Boolean = false,
    val isDefaultNowShipment: Boolean,
    var isSelected: Boolean,
    var isShowCoachMark: Boolean = false
) : Parcelable
