package com.tokopedia.product.detail.data.model.review

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by yovi.putra on 09/11/23"
 * Project name: android-tokopedia-core
 **/

data class ReviewRatingKeyword(
    @SerializedName("text")
    @Expose
    val text: String = String.EMPTY,
    @SerializedName("filter")
    @Expose
    val filter: String = String.EMPTY
)
