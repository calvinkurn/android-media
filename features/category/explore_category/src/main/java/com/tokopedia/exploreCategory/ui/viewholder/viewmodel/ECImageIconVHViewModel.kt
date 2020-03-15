package com.tokopedia.exploreCategory.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.exploreCategory.adapter.ECServiceTypeFactory
import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData.DynamicHomeIcon.CategoryGroup.CategoryRow

class ECImageIconVHViewModel(val categoryRow: CategoryRow?, val categoryTitle: String?, val categoryId: Int?) : Visitable<ECServiceTypeFactory> {

    override fun type(typeFactory: ECServiceTypeFactory): Int {
        return typeFactory.type(this)
    }

}