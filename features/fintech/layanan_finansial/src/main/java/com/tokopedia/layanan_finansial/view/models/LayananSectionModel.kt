package com.tokopedia.layanan_finansial.view.models

import com.google.gson.annotations.SerializedName
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.layanan_finansial.view.utils.ViewType

data class LayananSectionModel (
        @SerializedName("title")
        val title: String? = null,
        @SerializedName("subtitle")
        val subtitle: String? =null,
        @SerializedName("background")
        val backgroundColor: String? = null,
        @SerializedName("type")
        val type:String?= null,
        @SerializedName("widget_list")
        val list: List<LayananListItem>? = null

) : Visitable<ViewType> {
        override fun type(typeFactory: ViewType): Int {
                return typeFactory.type(this)
        }
}
