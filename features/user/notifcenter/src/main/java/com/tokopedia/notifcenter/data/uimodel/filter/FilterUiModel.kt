package com.tokopedia.notifcenter.data.uimodel.filter

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.presentation.adapter.typefactory.notification.NotificationFilterTypeFactory

data class FilterUiModel(
        @SerializedName("status")
        val status: Int = 0,
        @SerializedName("tag_id")
        val tagId: Long = 0,
        @SerializedName("tag_key")
        val tagKey: String = "",
        @SerializedName("tag_name")
        val tagName: String = "",
        @SerializedName("update_by")
        val updateBy: Int = 0,
        @SerializedName("update_time_unix")
        val updateTimeUnix: Int = 0
) : Visitable<NotificationFilterTypeFactory> {
    override fun type(typeFactory: NotificationFilterTypeFactory): Int {
        return typeFactory.type(this)
    }
}