package com.tokopedia.logisticCommon.data.entity.address

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 2/26/21.
 */

@Parcelize
data class PageInfoDataModel (
        var ticker: String = "",
        var buttonLabel: String = ""
) : Parcelable