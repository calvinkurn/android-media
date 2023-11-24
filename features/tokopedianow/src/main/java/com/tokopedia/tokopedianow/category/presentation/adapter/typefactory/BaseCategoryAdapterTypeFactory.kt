package com.tokopedia.tokopedianow.category.presentation.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.category.presentation.adapter.typefactory.listener.CategoryHeaderSpaceTypeFactory
import com.tokopedia.tokopedianow.category.presentation.uimodel.CategoryHeaderSpaceUiModel
import com.tokopedia.tokopedianow.category.presentation.viewholder.CategoryHeaderSpaceViewHolder
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowAdsCarouselTypeFactory
import com.tokopedia.tokopedianow.common.adapter.typefactory.TokoNowChooseAddressWidgetTypeFactory
import com.tokopedia.tokopedianow.common.listener.ProductAdsCarouselListener
import com.tokopedia.tokopedianow.common.model.TokoNowAdsCarouselUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowChooseAddressWidgetUiModel
import com.tokopedia.tokopedianow.common.view.TokoNowView
import com.tokopedia.tokopedianow.common.viewholder.TokoNowAdsCarouselViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowChooseAddressWidgetViewHolder.TokoNowChooseAddressWidgetListener

open class BaseCategoryAdapterTypeFactory(
    private var chooseAddressListener: TokoNowChooseAddressWidgetListener? = null,
    private var productAdsCarouselListener: ProductAdsCarouselListener? = null,
    private var tokoNowView: TokoNowView? = null,
) : BaseAdapterTypeFactory(),
    CategoryHeaderSpaceTypeFactory,
    TokoNowChooseAddressWidgetTypeFactory,
    TokoNowAdsCarouselTypeFactory {

    override fun type(uiModel: CategoryHeaderSpaceUiModel): Int = CategoryHeaderSpaceViewHolder.LAYOUT

    override fun type(uiModel: TokoNowChooseAddressWidgetUiModel): Int = TokoNowChooseAddressWidgetViewHolder.LAYOUT

    override fun type(uiModel: TokoNowAdsCarouselUiModel): Int = TokoNowAdsCarouselViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when(type) {
            CategoryHeaderSpaceViewHolder.LAYOUT -> CategoryHeaderSpaceViewHolder(view)
            TokoNowChooseAddressWidgetViewHolder.LAYOUT -> TokoNowChooseAddressWidgetViewHolder(
                itemView = view,
                tokoNowView = tokoNowView,
                tokoNowChooseAddressWidgetListener = chooseAddressListener
            )
            TokoNowAdsCarouselViewHolder.LAYOUT -> TokoNowAdsCarouselViewHolder(
                itemView = view,
                listener  = productAdsCarouselListener
            )
            else -> super.createViewHolder(view, type)
        }
    }

    open fun onDestroy() {
        chooseAddressListener = null
        productAdsCarouselListener = null
        tokoNowView = null
    }
}
