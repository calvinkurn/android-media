package com.tokopedia.tokopedianow.common.adapter.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.exception.TypeNotSupportedException
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardCarouselItemUiModel
import com.tokopedia.tokopedianow.common.model.TokoNowSeeMoreCardCarouselUiModel
import com.tokopedia.tokopedianow.common.viewholder.TokoNowProductCardCarouselItemViewHolder
import com.tokopedia.tokopedianow.common.viewholder.TokoNowSeeMoreCardCarouselViewHolder

class TokoNowProductCardCarouselTypeFactoryImpl(
    private val productCardCarouselItemListener: TokoNowProductCardCarouselItemViewHolder.TokoNowCarouselProductCardItemListener? = null,
    private val productCardCarouselSeeMoreListener: TokoNowSeeMoreCardCarouselViewHolder.TokoNowCarouselProductCardSeeMoreListener? = null
): TokoNowProductCardCarouselTypeFactory {

    override fun type(
        visitable: Visitable<*>
    ): Int {
        return when (visitable) {
            is TokoNowProductCardCarouselItemUiModel -> TokoNowProductCardCarouselItemViewHolder.LAYOUT
            is TokoNowSeeMoreCardCarouselUiModel -> TokoNowSeeMoreCardCarouselViewHolder.LAYOUT
            else -> {
                throw TypeNotSupportedException.create("Type not supported")
            }
        }
    }

    fun createViewHolder(
        view: View,
        type: Int
    ): AbstractViewHolder<out Visitable<*>> {
        return when (type) {
            TokoNowProductCardCarouselItemViewHolder.LAYOUT -> TokoNowProductCardCarouselItemViewHolder(
                view = view,
                listener = productCardCarouselItemListener
            )
            TokoNowSeeMoreCardCarouselViewHolder.LAYOUT -> TokoNowSeeMoreCardCarouselViewHolder(
                view = view,
                listener = productCardCarouselSeeMoreListener
            )
            else -> {
                throw TypeNotSupportedException.create("Layout not supported")
            }
        }
    }
}
