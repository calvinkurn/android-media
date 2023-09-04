package com.tokopedia.filter.common.data


interface OptionHolder {
    val options: List<IOption>
    val title: String
    val isLocationFilter: Boolean
    val isColorFilter: Boolean
    val search: Search

    fun copy(): OptionHolder?
}
