package com.tokopedia.search.result.product.separator

import com.tokopedia.abstraction.base.view.adapter.Visitable

object VerticalSeparatorMapper {

    fun addVerticalSeparator(
        visitableList: List<Visitable<*>>,
        addTopSeparator: Boolean = true,
        addBottomSeparator: Boolean = true
    ): List<Visitable<*>> {
        return visitableList.mapIndexed { index, visitable ->
            val canAddTopSeparator = index == 0 && addTopSeparator
            val canAddBottomSeparator = index == visitableList.lastIndex && addBottomSeparator

            if (canAddTopSeparator && visitable is VerticalSeparable) {
                visitable.addTopSeparator() as Visitable<*>
            } else if (canAddBottomSeparator && visitable is VerticalSeparable) {
                visitable.addBottomSeparator() as Visitable<*>
            } else visitable
        }
    }
}