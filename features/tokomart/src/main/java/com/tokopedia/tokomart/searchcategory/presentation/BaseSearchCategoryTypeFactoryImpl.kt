package com.tokopedia.tokomart.searchcategory.presentation

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.BannerViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ChooseAddressViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ProductCountViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.QuickFilterViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.TitleViewHolder

abstract class BaseSearchCategoryTypeFactoryImpl(

): BaseAdapterTypeFactory(), BaseSearchCategoryTypeFactory {

    override fun type(chooseAddressDataView: ChooseAddressDataView) = ChooseAddressViewHolder.LAYOUT

    override fun type(bannerDataView: BannerDataView) = BannerViewHolder.LAYOUT

    override fun type(titleDataView: TitleDataView) = TitleViewHolder.LAYOUT

    override fun type(quickFilterDataView: QuickFilterDataView) = QuickFilterViewHolder.LAYOUT

    override fun type(productCountDataView: ProductCountDataView) = ProductCountViewHolder.LAYOUT

    override fun type(productItemDataView: ProductItemDataView) = ProductItemViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(view)
            ChooseAddressViewHolder.LAYOUT -> ChooseAddressViewHolder(view)
            BannerViewHolder.LAYOUT -> BannerViewHolder(view)
            TitleViewHolder.LAYOUT -> TitleViewHolder(view)
            QuickFilterViewHolder.LAYOUT -> QuickFilterViewHolder(view)
            ProductCountViewHolder.LAYOUT -> ProductCountViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}