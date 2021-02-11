package com.tokopedia.sellerorder.filter.presentation.adapter

import com.tokopedia.sellerorder.filter.presentation.model.SomFilterChipsUiModel
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel

interface SomFilterListener {
    fun onDateClicked(position: Int)
    fun onFilterChipsClicked(somFilterData: SomFilterChipsUiModel, idFilter: String, position: Int, chipType: String, orderStatus: String)
    fun onSeeAllFilter(somFilterData: SomFilterUiModel, position: Int, idFilter: String)
}