package com.tokopedia.product.manage.common.feature.draft.data.model.detail

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SpecificationInputModel (
        var id: String = "",
        var data: String = ""
): Parcelable