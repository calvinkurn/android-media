package com.tokopedia.common_digital.atc

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * created by @bayazidnasir on 3/8/2022
 */

@Parcelize
data class DigitalErrorAtcData(
    val redirectionLink:String = "",
    val errorTitle: String = "",
    val errorDescription: String = "",
    val buttonLabel: String = ""
): Parcelable
