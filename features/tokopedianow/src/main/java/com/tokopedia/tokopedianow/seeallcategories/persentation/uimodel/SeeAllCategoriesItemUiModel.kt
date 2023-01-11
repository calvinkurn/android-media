package com.tokopedia.tokopedianow.seeallcategories.persentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.seeallcategories.persentation.adapter.SeeAllCategoriesTypeFactory

data class SeeAllCategoriesItemUiModel(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val appLink: String? = null,
): Visitable<SeeAllCategoriesTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: SeeAllCategoriesTypeFactory): Int = typeFactory.type(this)
}
