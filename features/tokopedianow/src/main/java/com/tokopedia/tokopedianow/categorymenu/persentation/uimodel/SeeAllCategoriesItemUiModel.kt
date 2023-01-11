package com.tokopedia.tokopedianow.categorymenu.persentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.categorymenu.persentation.adapter.SeeAllCategoriesTypeFactory

data class SeeAllCategoriesItemUiModel(
    val id: String = "",
    val title: String = "",
    val imageUrl: String? = null,
    val appLink: String = "",
    val warehouseId: String = "",
    val headerName: String = ""
): Visitable<SeeAllCategoriesTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: SeeAllCategoriesTypeFactory): Int = typeFactory.type(this)
}
