package com.tokopedia.product.addedit.detail.presentation.model

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
        val urlOriginal: String,
        val urlThumbnail: String,
        val url300: String,
        val status: String
) : Parcelable