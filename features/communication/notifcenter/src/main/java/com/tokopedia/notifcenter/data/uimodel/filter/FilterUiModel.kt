package com.tokopedia.notifcenter.data.uimodel.filter

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.notifcenter.view.adapter.typefactory.NotificationFilterTypeFactory

data class FilterUiModel(
    // need to suppress as data type is expected (Long)
    @SuppressLint("Invalid Data Type")
    @SerializedName("tag_id")
    val tagId: Long = 0,

    @SerializedName("tag_key")
    val tagKey: String = "",

    @SerializedName("tag_name")
    val tagName: String = ""
) : Visitable<NotificationFilterTypeFactory> {
    override fun type(typeFactory: NotificationFilterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
