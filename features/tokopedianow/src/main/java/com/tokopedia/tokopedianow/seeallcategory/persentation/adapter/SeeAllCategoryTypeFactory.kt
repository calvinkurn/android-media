package com.tokopedia.tokopedianow.seeallcategory.persentation.adapter

import com.tokopedia.tokopedianow.seeallcategory.persentation.uimodel.SeeAllCategoryItemUiModel

interface SeeAllCategoryTypeFactory {
    fun type(uiModel: SeeAllCategoryItemUiModel): Int
}
