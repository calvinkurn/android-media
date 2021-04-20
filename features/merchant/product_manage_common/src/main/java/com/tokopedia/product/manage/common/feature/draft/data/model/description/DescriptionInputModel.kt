package com.tokopedia.product.manage.common.feature.draft.data.model.description

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by faisalramd on 2020-03-22.
 */

@Parcelize
data class DescriptionInputModel (
        var productDescription: String = "",
        var videoLinkList: List<VideoLinkListModel> = emptyList()
) : Parcelable