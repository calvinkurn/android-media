package com.tokopedia.search.result.product.separator

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.model.BroadMatchDataView
import com.tokopedia.search.result.presentation.model.SuggestionDataView

object VerticalSeparatorMapper {

    fun addVerticalSeparator(
        visitableList: List<Visitable<*>>,
        addTopSeparator: Boolean = true,
        addBottomSeparator: Boolean = true
    ): List<Visitable<*>> {
        val size = visitableList.size
        return visitableList.mapIndexed { index, visitable ->
            if (index == 0 && addTopSeparator) {
                when (visitable) {
                    is SuggestionDataView -> {
                        visitable.copy(verticalSeparator = VerticalSeparator.Top)
                    }
                    is BroadMatchDataView -> {
                        visitable.copy(verticalSeparator = VerticalSeparator.Top)
                    }
                    else -> visitable
                }
            } else if (addBottomSeparator && index == size - 1 && visitable is BroadMatchDataView) {
                visitable.copy(verticalSeparator = VerticalSeparator.Bottom)
            } else visitable
        }
    }
}