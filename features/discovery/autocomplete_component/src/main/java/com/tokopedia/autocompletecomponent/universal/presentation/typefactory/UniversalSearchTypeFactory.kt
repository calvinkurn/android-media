package com.tokopedia.autocompletecomponent.universal.presentation.typefactory

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.errorstate.ErrorStateDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.ListGridDataView

interface UniversalSearchTypeFactory {
    fun type(carouselDataView: CarouselDataView): Int

    fun type(doubleLineDataView: DoubleLineDataView): Int

    fun type(listGridDataView: ListGridDataView): Int

    fun type(errorStateDataView: ErrorStateDataView): Int

    fun createViewHolder(view: View, type: Int): AbstractViewHolder<*>
}