package com.tokopedia.onboarding.view.component.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.onboarding.R
import com.tokopedia.onboarding.domain.model.PageDataModel
import com.tokopedia.onboarding.view.component.viewholder.PageViewHolder

class PageAdapterDelegate : TypedAdapterDelegate<PageDataModel, PageDataModel, PageViewHolder>(R.layout.layout_dynamic_onboarding_page) {

    override fun onBindViewHolder(item: PageDataModel, holder: PageViewHolder) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): PageViewHolder {
        return PageViewHolder(basicView)
    }
}