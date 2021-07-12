package com.tokopedia.localizationchooseaddress.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TokonowModel(
        var shopId: Long = 0,
        var warehouseId: Long = 0
): Parcelable