package com.tokopedia.filter.newdynamicfilter.analytics

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class FilterTrackingData(var event: String = "",
                         var filterCategory: String = "",
                         var categoryId: String = "",
                         var prefix: String = "") : Parcelable