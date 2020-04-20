package com.tokopedia.product.manage.common.draft.data.model.description

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class VideoLinkListModel(
        var inputUrl : String = "",
        var inputTitle : String = "",
        var inputDescription : String = "",
        var inputImage : String = ""
): Parcelable