package com.tokopedia.tokomart.searchcategory.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryTypeFactory

class QuickFilterDataView: Visitable<BaseSearchCategoryTypeFactory> {

    override fun type(typeFactory: BaseSearchCategoryTypeFactory?) =
            typeFactory?.type(this) ?: 0
}