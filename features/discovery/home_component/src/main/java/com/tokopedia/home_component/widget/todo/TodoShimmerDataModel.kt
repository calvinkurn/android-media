package com.tokopedia.home_component.widget.todo

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.home_component.widget.common.carousel.HomeComponentCarouselDiffUtil

/**
 * Created by frenzel
 */
class TodoShimmerDataModel: Visitable<TodoWidgetTypeFactory>, HomeComponentCarouselDiffUtil {
    companion object {
        const val ID = "TODO_SHIMMER"
    }

    override fun getId(): String = ID

    override fun equalsWith(visitable: Any?): Boolean {
        return visitable is TodoShimmerDataModel
    }

    override fun type(typeFactory: TodoWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
