package com.tokopedia.product.manage.common.feature.draft.data.model.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WholeSaleInputModel(
        var price: String = "",
        var quantity: String = ""
) : Parcelable