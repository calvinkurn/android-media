package com.tokopedia.tokomart.search.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.common.base.listener.BannerComponentListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactoryImpl


class SearchTypeFactoryImpl(
        chooseAddressListener: ChooseAddressListener,
        titleListener: TitleListener,
        bannerListener: BannerComponentListener
): BaseSearchCategoryTypeFactoryImpl(
        chooseAddressListener,
        titleListener,
        bannerListener
), SearchTypeFactory {

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            else -> super.createViewHolder(view, type)
        }
    }
}
