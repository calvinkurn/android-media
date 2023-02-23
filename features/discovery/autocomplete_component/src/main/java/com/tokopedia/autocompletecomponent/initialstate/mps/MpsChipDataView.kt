package com.tokopedia.autocompletecomponent.initialstate.mps

data class MpsChipDataView(
        val template: String = "",
        val type: String = "",
        val applink: String = "",
        val url: String = "",
        val title: String = "",
        val searchTerm: String = "",
        val dimension90: String = "",
        val position: Int = 0,
        val featureId: String = "",
        val disableAddButton: Boolean = false,
)
