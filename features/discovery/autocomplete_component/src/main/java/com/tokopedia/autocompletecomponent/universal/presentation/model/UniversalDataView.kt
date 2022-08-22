package com.tokopedia.autocompletecomponent.universal.presentation.model

import com.tokopedia.autocompletecomponent.universal.presentation.widget.carousel.CarouselDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.doubleline.DoubleLineDataView
import com.tokopedia.autocompletecomponent.universal.presentation.widget.listgrid.ListGridDataView

class UniversalDataView(
    val carouselDataView: List<CarouselDataView> = listOf(),
    val listDataView: List<ListGridDataView> = listOf(),
    val doubleLineDataView: List<DoubleLineDataView> = listOf(),
)