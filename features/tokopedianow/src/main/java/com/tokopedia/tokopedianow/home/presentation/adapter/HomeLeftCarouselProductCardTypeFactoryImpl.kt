package com.tokopedia.tokopedianow.home.presentation.adapter

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselProductCardSpaceUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselProductCardSeeMoreViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselProductCardSpaceViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselProductCardViewHolder

class HomeLeftCarouselProductCardTypeFactoryImpl(
    private val productCardListener: HomeLeftCarouselProductCardViewHolder.HomeLeftCarouselProductCardListener? = null,
    private val productCardSeeMoreListener: HomeLeftCarouselProductCardSeeMoreViewHolder.HomeLeftCarouselProductCardSeeMoreListener? = null,
    private val productCardSpaceListener: HomeLeftCarouselProductCardSpaceViewHolder.HomeLeftCarouselProductCardSpaceListener? = null
):  BaseAdapterTypeFactory(), HomeLeftCarouselProductCardTypeFactory {
    override fun type(uiModel: HomeLeftCarouselProductCardUiModel): Int = HomeLeftCarouselProductCardViewHolder.LAYOUT

    override fun type(uiModel: HomeLeftCarouselProductCardSpaceUiModel): Int = HomeLeftCarouselProductCardSpaceViewHolder.LAYOUT

    override fun type(uiModel: HomeLeftCarouselProductCardSeeMoreUiModel): Int = HomeLeftCarouselProductCardSeeMoreViewHolder.LAYOUT

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HomeLeftCarouselProductCardViewHolder.LAYOUT -> HomeLeftCarouselProductCardViewHolder(view, productCardListener)
            HomeLeftCarouselProductCardSpaceViewHolder.LAYOUT -> HomeLeftCarouselProductCardSpaceViewHolder(view, productCardSpaceListener)
            HomeLeftCarouselProductCardSeeMoreViewHolder.LAYOUT -> HomeLeftCarouselProductCardSeeMoreViewHolder(view, productCardSeeMoreListener)
            else -> super.createViewHolder(view, type)
        }
    }
}