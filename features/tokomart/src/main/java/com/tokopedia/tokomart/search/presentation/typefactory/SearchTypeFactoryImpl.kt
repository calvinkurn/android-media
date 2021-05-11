package com.tokopedia.tokomart.search.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.listener.BannerComponentListener
import com.tokopedia.tokomart.common.base.listener.BannerListener
import com.tokopedia.tokomart.search.presentation.typefactory.SearchTypeFactory
import com.tokopedia.tokomart.searchcategory.presentation.BaseSearchCategoryTypeFactoryImpl

class SearchTypeFactoryImpl(
    bannerListener: BannerListener
): BaseSearchCategoryTypeFactoryImpl(bannerListener), SearchTypeFactory {

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            else -> super.createViewHolder(view, type)
        }
    }
}
