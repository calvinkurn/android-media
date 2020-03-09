package com.tokopedia.onboarding.view.adapter

import com.tokopedia.adapterdelegate.BaseAdapter
import com.tokopedia.onboarding.domain.model.PageDataModel
import com.tokopedia.onboarding.view.component.delegate.PageAdapterDelegate

class PageAdapter: BaseAdapter<PageDataModel>() {

    init {
        delegatesManager.addDelegate(PageAdapterDelegate())
    }

    fun addPages(pages: MutableList<PageDataModel>) {
        addItems(pages)
        notifyDataSetChanged()
    }
}