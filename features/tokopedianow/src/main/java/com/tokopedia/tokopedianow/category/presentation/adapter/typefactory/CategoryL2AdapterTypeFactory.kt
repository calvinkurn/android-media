package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryL2TypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2HeaderUiModel
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryL2TabUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2HeaderViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2TabViewHolder
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryL2TabViewHolder.CategoryL2TabListener
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener

class CategoryL2AdapterTypeFactory(
    private val tokoNowView: TokoNowView,
    private val tabListener: CategoryL2TabListener,
    chooseAddressListener: TokoNowChooseAddressWidgetListener,
    productAdsCarouselListener: ProductAdsCarouselListener,
) : BaseCategoryAdapterTypeFactory(
    chooseAddressListener,
    productAdsCarouselListener,
    tokoNowView
), CategoryL2TypeFactory {

    override fun type(uiModel: CategoryL2HeaderUiModel): Int = CategoryL2HeaderViewHolder.LAYOUT

    override fun type(uiModel: CategoryL2TabUiModel): Int = CategoryL2TabViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            CategoryL2HeaderViewHolder.LAYOUT -> {
                CategoryL2HeaderViewHolder(view)
            }
            CategoryL2TabViewHolder.LAYOUT -> {
                CategoryL2TabViewHolder(view, tabListener, tokoNowView)
            }
            else -> super.createViewHolder(view, type)
        }
    }
}
