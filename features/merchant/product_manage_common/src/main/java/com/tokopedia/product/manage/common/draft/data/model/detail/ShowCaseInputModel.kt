package com.tokopedia.product.manage.common.draft.data.model.detail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ShowCaseInputModel(
        var showcaseId: String = "",
        var showcaseName: String = ""
) : Parcelable