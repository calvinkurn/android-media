package com.tokopedia.product.detail.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.product.detail.view.viewholder.ActionUiModel

/**
 * Created by yovi.putra on 27/11/23"
 * Project name: android-tokopedia-core
 **/

data class ActionData(
    @SerializedName("type")
    @Expose
    val type: String = "",
    @SerializedName("link")
    @Expose
    val link: String = ""
)

fun ActionData.asUiModel() = ActionUiModel(
    type = type,
    link = link
)
