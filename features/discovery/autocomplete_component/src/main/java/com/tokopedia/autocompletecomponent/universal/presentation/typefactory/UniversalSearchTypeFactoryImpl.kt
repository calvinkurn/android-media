package com.tokopedia.autocompletecomponent.universal.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.factory.BaseAdapterTypeFactory
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselListener
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselViewHolder
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineViewHolder
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.ListGridDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.ListGridViewHolder

class UniversalSearchTypeFactoryImpl(
    private val carouselListener: CarouselListener,
): UniversalSearchTypeFactory, BaseAdapterTypeFactory() {
    override fun type(carouselDataView: CarouselDataView): Int {
        return CarouselViewHolder.LAYOUT
    }

    override fun type(doubleLineDataView: DoubleLineDataView): Int {
        return DoubleLineViewHolder.LAYOUT
    }

    override fun type(listGridDataView: ListGridDataView): Int {
        return ListGridViewHolder.LAYOUT
    }

    override fun createViewHolder(view: View, type: Int): AbstractViewHolder<*> {
        return when (type) {
            CarouselViewHolder.LAYOUT -> CarouselViewHolder(view, carouselListener)
            DoubleLineViewHolder.LAYOUT -> DoubleLineViewHolder(view)
            ListGridViewHolder.LAYOUT -> ListGridViewHolder(view)
            else -> super.createViewHolder(view, type)
        }
    }
}