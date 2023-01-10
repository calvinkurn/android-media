package com.tokopedia.tokopedianow.common.model.categorymenu

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowCategoryMenuItemTypeFactory

class TokoNowCategoryMenuItemSeeAllUiModel(
    val appLink: String
): Visitable<TokoNowCategoryMenuItemTypeFactory> {
    override fun type(typeFactoryCategory: TokoNowCategoryMenuItemTypeFactory): Int {
        return typeFactoryCategory.type(this)
    }
}
