package com.tokopedia.tokomart.searchcategory.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokomart.common.base.listener.BannerComponentListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.CategoryFilterListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.ChooseAddressListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.QuickFilterListener
import com.tokopedia.tokomart.searchcategory.presentation.listener.TitleListener
import com.tokopedia.tokomart.searchcategory.presentation.model.BannerDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.CategoryFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.BannerViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.model.ChooseAddressDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductCountDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.ProductItemDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.QuickFilterDataView
import com.tokopedia.tokomart.searchcategory.presentation.model.TitleDataView
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.CategoryFilterViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ChooseAddressViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.LoadingMoreViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ProductCountViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.ProductItemViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.QuickFilterViewHolder
import com.tokopedia.tokomart.searchcategory.presentation.viewholder.TitleViewHolder

abstract class BaseSearchCategoryTypeFactoryImpl(
        protected val chooseAddressListener: ChooseAddressListener,
        protected val titleListener: TitleListener,
        protected val bannerListener: BannerComponentListener,
        protected val quickFilterListener: QuickFilterListener,
        protected val categoryFilterListener: CategoryFilterListener,
): BaseAdapterTypeFactory(), BaseSearchCategoryTypeFactory {

    override fun type(chooseAddressDataView: ChooseAddressDataView) = ChooseAddressViewHolder.LAYOUT

    override fun type(bannerDataView: BannerDataView) = BannerViewHolder.LAYOUT

    override fun type(titleDataView: TitleDataView) = TitleViewHolder.LAYOUT

    override fun type(categoryFilterDataView: CategoryFilterDataView) = CategoryFilterViewHolder.LAYOUT

    override fun type(quickFilterDataView: QuickFilterDataView) = QuickFilterViewHolder.LAYOUT

    override fun type(productCountDataView: ProductCountDataView) = ProductCountViewHolder.LAYOUT

    override fun type(productItemDataView: ProductItemDataView) = ProductItemViewHolder.LAYOUT

    override fun type(viewModel: LoadingMoreModel) = LoadingMoreViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            ProductItemViewHolder.LAYOUT -> ProductItemViewHolder(view)
            ChooseAddressViewHolder.LAYOUT -> ChooseAddressViewHolder(view, chooseAddressListener)
            BannerViewHolder.LAYOUT -> BannerViewHolder(view, bannerListener)
            TitleViewHolder.LAYOUT -> TitleViewHolder(view, titleListener)
            CategoryFilterViewHolder.LAYOUT -> CategoryFilterViewHolder(view, categoryFilterListener)
            QuickFilterViewHolder.LAYOUT -> QuickFilterViewHolder(view, quickFilterListener)
            ProductCountViewHolder.LAYOUT -> ProductCountViewHolder(view)
            LoadingMoreViewHolder.LAYOUT -> LoadingMoreViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}