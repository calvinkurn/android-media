package com.tokopedia.filter.bottomsheet.filtergeneraldetail

import android.widget.Filter
import com.tokopedia.filter.common.data.IOption
import com.tokopedia.filter.common.data.Option
import java.util.*

internal class OptionSearchFilter(
    private val onReceiveFilterResult: (List<IOption>) -> Unit
): Filter() {

    private val sourceData = mutableListOf<IOption>()

    fun setOptionList(optionList: List<IOption>) {
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
        val resultList = results.values as List<IOption>? ?: return

        onReceiveFilterResult(resultList)
    }
}
