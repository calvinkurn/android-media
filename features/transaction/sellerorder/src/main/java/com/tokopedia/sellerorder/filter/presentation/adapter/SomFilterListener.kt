package com.tokopedia.sellerorder.filter.presentation.adapter

interface SomFilterListener {
    fun onChipsSortClicked(id: Int, position: Int, chipType: String)
    fun onChipsStatusClicked(ids: List<Int>, position: Int, chipType: String)
    fun onChipsTypeOrderClicked(id: Int, position: Int, chipType: String)
    fun onChipsCourierClicked(id: Int, position: Int, chipType: String)
    fun onChipsLabelClicked(id: Int, position: Int, chipType: String)
    fun onChipsDeadlineClicked(id: Int, position: Int, chipType: String)
    fun onDateClicked(position: Int)
    fun onSeeAllFilter(nameFilter: String, position: Int)
}