package com.tokopedia.product.addedit.description.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Created by faisalramd on 2020-03-22.
 */

@Parcelize
data class DescriptionInputModel (
        var productDescription: String = "",
        var videoLinkList: List<VideoLinkModel> = emptyList()
) : Parcelable