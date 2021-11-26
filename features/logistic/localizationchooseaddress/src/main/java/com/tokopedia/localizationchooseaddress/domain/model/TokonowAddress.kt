package com.tokopedia.localizationchooseaddress.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TokonowAddress(
    var warehouseId: Long = 0,
    var coverageLabel: String = ""
): Parcelable