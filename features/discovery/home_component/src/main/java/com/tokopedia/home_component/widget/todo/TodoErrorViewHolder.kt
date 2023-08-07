package com.tokopedia.home_component.widget.todo

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentTodoWidgetErrorBinding
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class TodoErrorViewHolder(
    view: View
) : AbstractViewHolder<TodoErrorDataModel>(view) {

    private var binding: HomeComponentTodoWidgetErrorBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.home_component_todo_widget_error
    }

    override fun bind(element: TodoErrorDataModel) {
        binding?.refreshTodoWidget?.progressState = false
        binding?.refreshTodoWidget?.refreshBtn?.setOnClickListener {
            binding?.refreshTodoWidget?.progressState = true
            element.todoWidgetComponentListener.refreshTodowidget()
        }
    }
}
