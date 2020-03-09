package com.tokopedia.exploreCategory.ui.viewholder.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.exploreCategory.adapter.ECServiceTypeFactory
import com.tokopedia.exploreCategory.model.ECDynamicHomeIconData.DynamicHomeIcon.CategoryGroup
import java.util.ArrayList

class ECAccordionVHViewModel(val categoryGroup: CategoryGroup?) : Visitable<ECServiceTypeFactory> {

    override fun type(typeFactory: ECServiceTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getRows(): ArrayList<Visitable<*>> {
        val visitable = ArrayList<Visitable<*>>()
        if (categoryGroup?.categoryRows?.isNullOrEmpty() == false) {
            for (item in categoryGroup.categoryRows) {
                visitable.add(ECImageIconVHViewModel(item, categoryGroup.title, categoryGroup.id))
            }
        }
        return visitable
    }

}
