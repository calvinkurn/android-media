package com.tokopedia.filter.bottomsheet.pricefilter

internal interface PriceFilterViewListener {

    fun onMinPriceEditedFromTextInput(priceFilterViewModel: PriceFilterViewModel, minValue: Int)

    fun onMaxPriceEditedFromTextInput(priceFilterViewModel: PriceFilterViewModel, maxValue: Int)

    fun onPriceRangeClicked(priceFilterViewModel: PriceFilterViewModel, priceRangeOption: PriceOptionViewModel)
}