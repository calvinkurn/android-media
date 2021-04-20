package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.*

class DigitalHomePageSearchTypeFactory(val onSearchCategoryClickListener: DigitalHomePageSearchViewHolder.OnSearchCategoryClickListener)
    : BaseAdapterTypeFactory() {

    fun type(digitalHomePageSearchCategoryModel: DigitalHomePageSearchCategoryModel): Int {
        return DigitalHomePageSearchViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            DigitalHomePageSearchViewHolder.LAYOUT -> return DigitalHomePageSearchViewHolder(parent, onSearchCategoryClickListener)
        }
        return super.createViewHolder(parent, type)
    }

}
