package com.tokopedia.mvc.domain.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SelectedProduct(val parentProductId: Long, val variantProductIds: List<Long>) : Parcelable
