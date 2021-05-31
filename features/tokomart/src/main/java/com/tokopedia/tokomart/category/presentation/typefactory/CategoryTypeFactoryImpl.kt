package com.tokopedia.tokomart.category.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.category.presentation.model.CategoryAisleDataView
import com.tokopedia.tokomart.category.presentation.viewholder.CategoryAisleViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.listener.BannerComponentListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.ProductItemListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokomart.searchcategory.presentation.typefactory.BaseSearchCategoryTypeFactoryImpl

class CategoryTypeFactoryImpl(
        chooseAddressListener: ChooseAddressListener,
        titleListener: TitleListener,
        bannerListener: BannerComponentListener,
        quickFilterListener: QuickFilterListener,
        categoryFilterListener: CategoryFilterListener,
        productItemListener: ProductItemListener,
): BaseSearchCategoryTypeFactoryImpl(
        chooseAddressListener,
        titleListener,
        bannerListener,
        quickFilterListener,
        categoryFilterListener,
        productItemListener,
), CategoryTypeFactory {

    override fun type(categoryAisleDataView: CategoryAisleDataView) = CategoryAisleViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            CategoryAisleViewHolder.LAYOUT -> CategoryAisleViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}