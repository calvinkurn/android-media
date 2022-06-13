package com.tokopedia.tokopoints.view.model.section

import com.google.gson.annotations.SerializedName
import com.tokopedia.mvcwidget.multishopmvc.data.CatalogMVCWithProductsListItem

data class LayoutQuestWidgetAttr(
        @SerializedName("jsonQuestWidgetDisplayParam")
        val jsonQuestWidgetDisplayParam: String? = null,
)