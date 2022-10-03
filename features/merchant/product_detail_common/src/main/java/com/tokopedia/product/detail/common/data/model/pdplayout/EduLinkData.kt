package com.tokopedia.product.detail.common.data.model.pdplayout

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.EMPTY

/**
 * Created by yovi.putra on 03/10/22"
 * Project name: android-tokopedia-core
 **/

data class EduLinkData(
    @SerializedName("applink")
    @Expose
    val appLink: String = String.EMPTY,
    @SerializedName("webLink")
    @Expose
    val webLink: WebLinkData = WebLinkData()
) {

    fun isEmpty() = appLink.isEmpty() && webLink.isEmpty()
}

data class WebLinkData(
    @SerializedName("action")
    @Expose
    val action: String = String.EMPTY,
    @SerializedName("query")
    @Expose
    val query: String = String.EMPTY
) {

    fun isEmpty() = action.isEmpty() && query.isEmpty()
}
