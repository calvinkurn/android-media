package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TabAdapterFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryQuickFilterUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryQuickFilterViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowAdsCarouselTypeFactory
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowAdsCarouselViewHolder
import com.tokopedia.tokopedianow.searchcategory.presentation.listener.QuickFilterListener

class CategoryL2TabAdapterTypeFactory(
    private var adsCarouselListener: ProductAdsCarouselListener?,
    private var quickFilterListener: QuickFilterListener?
): BaseAdapterTypeFactory(), TokoNowAdsCarouselTypeFactory, CategoryL2TabAdapterFactory {

    override fun type(uiModel: TokoNowAdsCarouselUiModel): Int = TokoNowAdsCarouselViewHolder.LAYOUT

    override fun type(uiModel: CategoryQuickFilterUiModel): Int =
        CategoryQuickFilterViewHolder.LAYOUT

    override fun createViewHolder(
        view: View,
        type: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            TokoNowAdsCarouselViewHolder.LAYOUT -> {
                TokoNowAdsCarouselViewHolder(
                    itemView = view,
                    listener = adsCarouselListener
                )
            }
            CategoryQuickFilterViewHolder.LAYOUT -> {
                CategoryQuickFilterViewHolder(view, quickFilterListener)
            }
            else -> super.createViewHolder(view, type)
        }
    }

    fun onDestroy() {
        adsCarouselListener = null
        quickFilterListener = null
    }
}
