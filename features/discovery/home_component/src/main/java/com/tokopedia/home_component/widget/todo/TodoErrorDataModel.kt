package com.tokopedia.home_component.widget.todo

import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.kotlin.model.ImpressHolder

/**
 * Created by frenzel
 */
class TodoErrorDataModel(val todoWidgetComponentListener: TodoWidgetComponentListener): TodoWidgetVisitable, ImpressHolder() {
    companion object {
        const val ID = "TODO_ERROR"
    }

    override fun getId(): String = ID
    override fun equalsWith(visitable: TodoWidgetVisitable): Boolean {
        return visitable is TodoErrorDataModel
    }

    override fun type(typeFactory: TodoWidgetTypeFactory): Int {
        return typeFactory.type(this)
    }
}
