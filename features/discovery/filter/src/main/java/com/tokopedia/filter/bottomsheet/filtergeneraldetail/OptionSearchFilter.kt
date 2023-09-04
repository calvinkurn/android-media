package com.tokopedia.filter.bottomsheet.filtergeneraldetail

import android.widget.Filter
import com.tokopedia.filter.common.data.Option
import java.util.*

internal class OptionSearchFilter(
        private val onReceiveFilterResult: (List<GeneralFilterSortOptions>) -> Unit
): Filter() {

    private val sourceData = mutableListOf<GeneralFilterSortOptions>()

    fun setOptionList(optionList: List<GeneralFilterSortOptions>) {
        sourceData.clear()
        sourceData.addAll(optionList)
    }

    override fun performFiltering(constraint: CharSequence): FilterResults {
        val filterText = constraint.toString().toLowerCase(Locale.getDefault())
        val result = FilterResults()
        val filteredOption = sourceData.filter {
            it.name.toLowerCase(Locale.getDefault()).contains(filterText)
        }
        result.values = filteredOption
        result.count = filteredOption.size
        return result
    }

    override fun publishResults(constraint: CharSequence, results: FilterResults) {
        val resultList = results.values as List<Option>? ?: return

        onReceiveFilterResult(resultList)
    }
}
