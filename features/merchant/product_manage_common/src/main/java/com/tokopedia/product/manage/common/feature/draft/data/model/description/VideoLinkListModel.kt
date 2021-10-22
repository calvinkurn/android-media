package com.tokopedia.product.manage.common.feature.draft.data.model.description

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class VideoLinkListModel(
        var inputUrl : String = "",
        var inputTitle : String = "",
        var inputDescription : String = "",
        var inputImage : String = ""
): Parcelable