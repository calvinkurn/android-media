package com.tokopedia.notifcenter.data.entity.filter

import com.google.gson.annotations.SerializedName
import com.tokopedia.notifcenter.data.uimodel.filter.FilterUiModel

data class NotifcenterTagList(
        @SerializedName("list")
        val list: List<FilterUiModel> = listOf()
)