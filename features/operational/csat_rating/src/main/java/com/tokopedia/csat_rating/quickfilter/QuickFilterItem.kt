package com.tokopedia.csat_rating.quickfilter


class QuickFilterItem {
    var name: String? = null
    var type: String? = null
    var isSelected = false
    var colorFilter = 0
        private set
    var id = 0
    fun setColorBorder(colorFilter: Int) {
        this.colorFilter = colorFilter
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }
}