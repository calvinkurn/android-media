package com.tokopedia.tokopedianow.search.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.search.presentation.listener.SuggestionListener
import com.tokopedia.tokopedianow.search.presentation.model.SuggestionDataView
import com.tokopedia.tokopedianow.search.presentation.viewholder.SearchChooseAddressViewHolder
import com.tokopedia.tokopedianow.search.presentation.viewholder.SuggestionViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.EmptyProductListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokopedianow.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactoryImpl
import com.tokopedia.tokopedianow.searchcategory.presentation.viewholder.BaseChooseAddressViewHolder


class SearchTypeFactoryImpl(
        chooseAddressListener: ChooseAddressListener,
        titleListener: TitleListener,
        bannerListener: BannerComponentListener,
        quickFilterListener: QuickFilterListener,
        categoryFilterListener: CategoryFilterListener,
        productItemListener: ProductItemListener,
        emptyProductListener: EmptyProductListener,
        private val suggestionListener: SuggestionListener,
): BaseSearchCategoryTypeFactoryImpl(
        chooseAddressListener,
        titleListener,
        bannerListener,
        quickFilterListener,
        categoryFilterListener,
        productItemListener,
        emptyProductListener,
), SearchTypeFactory {

    override fun type(suggestionDataView: SuggestionDataView): Int {
        return SuggestionViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            BaseChooseAddressViewHolder.LAYOUT -> SearchChooseAddressViewHolder(view, chooseAddressListener)
            SuggestionViewHolder.LAYOUT -> SuggestionViewHolder(view, suggestionListener)
            else -> super.createViewHolder(view, type)
        }
    }
}
