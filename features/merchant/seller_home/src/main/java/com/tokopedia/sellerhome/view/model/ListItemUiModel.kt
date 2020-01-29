package com.tokopedia.sellerhome.view.model

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.sellerhome.view.adapter.ListAdapterTypeFactory

class ListItemUiModel(
        @SerializedName("title")
        val title: String,
        @SerializedName("appLink")
        val appLink: String,
        @SerializedName("url")
        val url: String,
        @SerializedName("featuredMediaURL")
        val featuredMediaURL: String,
        @SerializedName("subtitle")
        val subtitle: String
) : Visitable<ListAdapterTypeFactory> {
    override fun type(typeFactory: ListAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}