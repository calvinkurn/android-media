package com.tokopedia.tokopedianow.seeallcategory.persentation.uimodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.model.ImpressHolder
import com.tokopedia.tokopedianow.seeallcategory.persentation.adapter.SeeAllCategoryTypeFactory

data class SeeAllCategoryItemUiModel(
    val id: String,
    val name: String,
    val imageUrl: String? = null,
    val appLink: String? = null,
    val color: String
): Visitable<SeeAllCategoryTypeFactory>, ImpressHolder() {
    override fun type(typeFactory: SeeAllCategoryTypeFactory): Int = typeFactory.type(this)
}
