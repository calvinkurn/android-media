package com.tokopedia.product.addedit.specification.presentation.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SpecificationInputModel (
        var id: String = "",
        var data: String = ""
): Parcelable