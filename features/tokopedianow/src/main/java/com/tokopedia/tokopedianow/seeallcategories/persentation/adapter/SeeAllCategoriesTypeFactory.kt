package com.tokopedia.tokopedianow.seeallcategories.persentation.adapter

import com.tokopedia.tokopedianow.seeallcategories.persentation.uimodel.SeeAllCategoriesItemUiModel

interface SeeAllCategoriesTypeFactory {
    fun type(uiModel: SeeAllCategoriesItemUiModel): Int
}
