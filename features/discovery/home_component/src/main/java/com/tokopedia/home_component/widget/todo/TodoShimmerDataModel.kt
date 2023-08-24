package com.tokopedia.home_component.widget.todo

import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by frenzel
 */
class TodoShimmerDataModel: TodoWidgetVisitable, ImpressHolder() {
    companion object {
        const val ID = "TODO_SHIMMER"
    }

    override fun getId(): String = ID
    override fun equalsWith(visitable: TodoWidgetVisitable): Boolean {
        return visitable is TodoShimmerDataModel
    }

    override fun type(typeFactory: TodoWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
