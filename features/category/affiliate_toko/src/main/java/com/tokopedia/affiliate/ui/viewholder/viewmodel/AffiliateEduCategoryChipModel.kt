package com.tokopedia.affiliate.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.affiliate.adapter.AffiliateAdapterTypeFactory
import com.tokopedia.affiliate.model.response.AffiliateEducationCategoryResponse

class AffiliateEduCategoryChipModel(
    val chipType: AffiliateEducationCategoryResponse.CategoryTree.CategoryTreeData.CategoriesItem.ChildrenItem?
) : Visitable<AffiliateAdapterTypeFactory> {

    override fun type(typeFactory: AffiliateAdapterTypeFactory): Int {
        return typeFactory.type(this)
    }
}
