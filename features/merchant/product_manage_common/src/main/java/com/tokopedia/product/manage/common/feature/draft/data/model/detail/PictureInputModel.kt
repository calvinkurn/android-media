package com.tokopedia.product.manage.common.feature.draft.data.model.detail

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Created by faisalramd on 2020-04-01.
 */

@Parcelize
data class PictureInputModel(
        var picID: String = "",
        var description: String = "",
        var filePath: String = "",
        var fileName: String = "",
        var width: Int = 0,
        var height: Int = 0,
        var isFromIG: String = "",
        var urlOriginal: String = "",
        var urlThumbnail: String = "",
        var url300: String = "",
        var status: String = ""
) : Parcelable