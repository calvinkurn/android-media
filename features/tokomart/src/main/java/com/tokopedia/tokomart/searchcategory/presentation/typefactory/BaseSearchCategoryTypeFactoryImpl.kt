package com.tokopedia.tokomart.searchcategory.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.BannerViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ChooseAddressViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.LoadingMoreViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ProductCountViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.QuickFilterViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.TitleViewHolder

abstract class BaseSearchCategoryTypeFactoryImpl(
        protected val chooseAddressListener: ChooseAddressListener,
        protected val titleListener: TitleListener,
): BaseAdapterTypeFactory(), BaseSearchCategoryTypeFactory {

    override fun type(chooseAddressDataView: ChooseAddressDataView) = ChooseAddressViewHolder.LAYOUT

    override fun type(bannerDataView: BannerDataView) = BannerViewHolder.LAYOUT

    override fun type(titleDataView: TitleDataView) = TitleViewHolder.LAYOUT

    override fun type(quickFilterDataView: QuickFilterDataView) = QuickFilterViewHolder.LAYOUT

    override fun type(productCountDataView: ProductCountDataView) = ProductCountViewHolder.LAYOUT

    override fun type(productItemDataView: ProductItemDataView) = ProductItemViewHolder.LAYOUT

    override fun type(viewModel: LoadingMoreModel) = LoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(view)
            ChooseAddressViewHolder.LAYOUT -> ChooseAddressViewHolder(view, chooseAddressListener)
            BannerViewHolder.LAYOUT -> BannerViewHolder(view)
            TitleViewHolder.LAYOUT -> TitleViewHolder(view, titleListener)
            QuickFilterViewHolder.LAYOUT -> QuickFilterViewHolder(view)
            ProductCountViewHolder.LAYOUT -> ProductCountViewHolder(view)
            LoadingMoreViewHolder.LAYOUT -> LoadingMoreViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}