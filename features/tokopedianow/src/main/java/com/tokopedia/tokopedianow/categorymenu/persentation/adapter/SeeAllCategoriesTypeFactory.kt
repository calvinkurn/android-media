package com.tokopedia.tokopedianow.categorymenu.persentation.adapter

import com.tokopedia.tokopedianow.categorymenu.persentation.uimodel.SeeAllCategoriesItemUiModel

interface SeeAllCategoriesTypeFactory {
    fun type(uiModel: SeeAllCategoriesItemUiModel): Int
}
