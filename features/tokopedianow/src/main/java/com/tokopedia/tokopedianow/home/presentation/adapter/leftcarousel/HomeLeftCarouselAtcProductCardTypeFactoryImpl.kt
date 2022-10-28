package com.tokopedia.tokopedianow.home.presentation.adapter.leftcarousel

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardSeeMoreUiModel
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardSeeMoreViewHolder
import com.tokopedia.tokopedianow.home.presentation.viewholder.HomeLeftCarouselAtcProductCardViewHolder

class HomeLeftCarouselAtcProductCardTypeFactoryImpl(
    private val productCardListener: HomeLeftCarouselAtcProductCardViewHolder.HomeLeftCarouselAtcProductCardListener? = null,
    private val productCardSeeMoreListener: HomeLeftCarouselAtcProductCardSeeMoreViewHolder.HomeLeftCarouselAtcProductCardSeeMoreListener? = null
): HomeLeftCarouselAtcProductCardTypeFactory {

    override fun type(visitable: Visitable<*>): Int {
        return when (visitable) {
            is HomeLeftCarouselAtcProductCardUiModel -> HomeLeftCarouselAtcProductCardViewHolder.LAYOUT
            is HomeLeftCarouselAtcProductCardSeeMoreUiModel -> HomeLeftCarouselAtcProductCardSeeMoreViewHolder.LAYOUT
            else -> {
                throw TypeNotSupportedException.create("Type not supported")
            }
        }
    }

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            HomeLeftCarouselAtcProductCardViewHolder.LAYOUT -> HomeLeftCarouselAtcProductCardViewHolder(view, productCardListener)
            HomeLeftCarouselAtcProductCardSeeMoreViewHolder.LAYOUT -> HomeLeftCarouselAtcProductCardSeeMoreViewHolder(view, productCardSeeMoreListener)
            else -> {
                throw TypeNotSupportedException.create("Layout not supported")
            }
        }
    }
}
