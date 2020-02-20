package com.tokopedia.flight.filter.presentation
/**
 * @author by jessica on 2020-02-20
 */

interface FlightFilterSortListener {
    fun onSortChanged(selectedSortOption: Int)
    fun onClickSeeAllSort()
}