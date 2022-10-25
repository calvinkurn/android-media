package com.tokopedia.tokopedianow.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardSeeMoreViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardViewHolder

class HomeLeftCarouselAtcProductCardTypeFactoryImpl(
    private val productCardListener: HomeLeftCarouselAtcProductCardViewHolder.HomeLeftCarouselAtcProductCardListener? = null,
    private val productCardSeeMoreListener: HomeLeftCarouselAtcProductCardSeeMoreViewHolder.HomeLeftCarouselAtcProductCardSeeMoreListener? = null
):  BaseAdapterTypeFactory(), HomeLeftCarouselAtcProductCardTypeFactory {
    override fun type(uiModel: HomeLeftCarouselAtcProductCardUiModel): Int = HomeLeftCarouselAtcProductCardViewHolder.LAYOUT

    override fun type(uiModel: HomeLeftCarouselAtcProductCardSeeMoreUiModel): Int = HomeLeftCarouselAtcProductCardSeeMoreViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HomeLeftCarouselAtcProductCardViewHolder.LAYOUT -> HomeLeftCarouselAtcProductCardViewHolder(view, productCardListener)
            HomeLeftCarouselAtcProductCardSeeMoreViewHolder.LAYOUT -> HomeLeftCarouselAtcProductCardSeeMoreViewHolder(view, productCardSeeMoreListener)
            else -> super.createViewHolder(view, type)
        }
    }
}
