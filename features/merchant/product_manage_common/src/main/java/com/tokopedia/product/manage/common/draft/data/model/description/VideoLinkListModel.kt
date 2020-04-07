package com.tokopedia.product.manage.common.draft.data.model.description

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoLinkListModel(
        var inputId : Int = 0,
        var inputUrl : String = "",
        var inputImage : String = ""
): Parcelable