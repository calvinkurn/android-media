package com.tokopedia.tokopedianow.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSpaceUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardSeeMoreViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardSpaceViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardViewHolder

class HomeLeftCarouselProductCardTypeFactoryImpl(
    private val productCardListener: HomeLeftCarouselAtcProductCardViewHolder.HomeLeftCarouselProductCardListener? = null,
    private val productCardSeeMoreListener: HomeLeftCarouselAtcProductCardSeeMoreViewHolder.HomeLeftCarouselProductCardSeeMoreListener? = null,
    private val productCardSpaceListener: HomeLeftCarouselAtcProductCardSpaceViewHolder.HomeLeftCarouselProductCardSpaceListener? = null
):  BaseAdapterTypeFactory(), HomeLeftCarouselProductCardTypeFactory {
    override fun type(uiModel: HomeLeftCarouselProductCardUiModel): Int = HomeLeftCarouselAtcProductCardViewHolder.LAYOUT

    override fun type(uiModel: HomeLeftCarouselProductCardSpaceUiModel): Int = HomeLeftCarouselAtcProductCardSpaceViewHolder.LAYOUT

    override fun type(uiModel: HomeLeftCarouselProductCardSeeMoreUiModel): Int = HomeLeftCarouselAtcProductCardSeeMoreViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HomeLeftCarouselAtcProductCardViewHolder.LAYOUT -> HomeLeftCarouselAtcProductCardViewHolder(view, productCardListener)
            HomeLeftCarouselAtcProductCardSpaceViewHolder.LAYOUT -> HomeLeftCarouselAtcProductCardSpaceViewHolder(view, productCardSpaceListener)
            HomeLeftCarouselAtcProductCardSeeMoreViewHolder.LAYOUT -> HomeLeftCarouselAtcProductCardSeeMoreViewHolder(view, productCardSeeMoreListener)
            else -> super.createViewHolder(view, type)
        }
    }
}