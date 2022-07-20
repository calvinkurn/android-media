package com.tokopedia.digital.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.old.model.DigitalHomePageSearchCategoryModel
import com.tokopedia.digital.home.presentation.adapter.viewholder.*
import com.tokopedia.digital.home.presentation.listener.SearchAutoCompleteListener

class DigitalHomePageSearchTypeFactory(
        val onSearchCategoryClickListener: DigitalHomePageSearchViewHolder.OnSearchCategoryClickListener,
        val onSearchDoubleLineClickListener: DigitalHomePageSearchDoubleLineViewHolder.OnSearchDoubleLineClickListener,
        val onSearchAutoCompleteListener: SearchAutoCompleteListener,
        val onEmptySearchListener: DigitalHomePageSearchEmptyStateViewHolder.DigitalHomepageSearchEmptyListener
)
    : BaseAdapterTypeFactory() {

    fun type(digitalHomePageSearchCategoryModel: DigitalHomePageSearchCategoryModel): Int {
        return digitalHomePageSearchCategoryModel.typeLayout
    }

    override fun type(emptyModel: EmptyModel): Int {
        return DigitalHomePageSearchEmptyStateViewHolder.LAYOUT
    }

    override fun createViewHolder(parent: View?, type: Int): AbstractViewHolder<out Visitable<*>> {
        when (type) {
            DigitalHomePageSearchViewHolder.LAYOUT -> return DigitalHomePageSearchViewHolder(parent, onSearchCategoryClickListener, onSearchAutoCompleteListener)
            DigitalHomePageSearchHeaderViewHolder.LAYOUT -> return DigitalHomePageSearchHeaderViewHolder(parent)
            DigitalHomePageSearchDoubleLineViewHolder.LAYOUT -> return DigitalHomePageSearchDoubleLineViewHolder(parent, onSearchDoubleLineClickListener, onSearchAutoCompleteListener)
            DigitalHomePageSearchEmptyStateViewHolder.LAYOUT -> return DigitalHomePageSearchEmptyStateViewHolder(parent, onEmptySearchListener)
        }
        return super.createViewHolder(parent, type)
    }

}
