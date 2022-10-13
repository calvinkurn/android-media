package com.tokopedia.checkout.domain.model.cartshipmentform

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by victor on 13/10/22
 */

@Parcelize
data class ScheduleDeliveryData(
    val timeslotId: Long = 0L,
    val scheduleDate: String = "",
    val validationMetadata: String = "",
) : Parcelable
