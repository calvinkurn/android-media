package com.tokopedia.home_component.widget.todo

import android.os.Bundle
import com.tokopedia.abstraction.base.view.adapter.Visitable

/**
 * Created by frenzel
 */
interface TodoWidgetVisitable: Visitable<TodoWidgetTypeFactory> {
    fun getId(): String
    fun equalsWith(visitable: TodoWidgetVisitable): Boolean
    fun getChangePayloadFrom(visitable: TodoWidgetVisitable): Bundle = Bundle()
}
