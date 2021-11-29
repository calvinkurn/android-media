package com.tokopedia.tokopedianow.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactory

object ProgressBarDataView : Visitable<BaseSearchCategoryTypeFactory> {
    override fun type(typeFactory: BaseSearchCategoryTypeFactory): Int {
        return typeFactory.type(this)
    }
}