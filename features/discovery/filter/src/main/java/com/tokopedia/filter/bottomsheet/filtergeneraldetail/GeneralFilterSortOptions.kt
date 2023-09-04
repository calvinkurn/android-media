package com.tokopedia.filter.bottomsheet.filtergeneraldetail

import com.tokopedia.filter.common.data.Search

interface GeneralFilterSortOptions {

    val uniqueId: String
    val name: String
    var inputState: String

    fun isTypeOptionRadio(): Boolean {
        return true
    }

    fun getHexColorOption(): String {
        return ""
    }

    fun getOptionsIconUrl(): String {
        return ""
    }

    fun isNewOption(): Boolean {
        return false
    }

    fun getDescriptionOption(): String {
        return ""
    }
}

interface GeneralFilterSort {
    fun getItemOptions(): List<GeneralFilterSortOptions>

    fun getTitleOptions(): String

    fun isFilterForLocation(): Boolean {
        return false
    }

    fun isFilterForColor(): Boolean {
        return false
    }

    fun getSearchable(): Search {
        return Search()
    }
}
