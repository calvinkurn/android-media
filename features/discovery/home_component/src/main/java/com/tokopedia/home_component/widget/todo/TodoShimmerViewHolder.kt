package com.tokopedia.home_component.widget.todo

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R

/**
 * Created by frenzel
 */
class TodoShimmerViewHolder(view: View) : AbstractViewHolder<Visitable<TodoWidgetTypeFactory>>(view) {

    companion object {
        val LAYOUT = R.layout.home_component_todo_widget_shimmering
    }

    override fun bind(element: Visitable<TodoWidgetTypeFactory>) { }
}
