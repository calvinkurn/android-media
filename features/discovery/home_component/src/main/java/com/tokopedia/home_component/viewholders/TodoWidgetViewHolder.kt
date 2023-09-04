package com.tokopedia.home_component.viewholders

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentTodoWidgetBinding
import com.tokopedia.home_component.decoration.TodoWidgetItemDecoration
import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactory
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.home_component.widget.common.carousel.CarouselListAdapter
import com.tokopedia.home_component.widget.common.carousel.CommonCarouselDiffUtilCallback
import com.tokopedia.home_component.widget.todo.TodoErrorDataModel
import com.tokopedia.home_component.widget.todo.TodoShimmerDataModel
import com.tokopedia.home_component.widget.todo.TodoWidgetTypeFactoryImpl
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class TodoWidgetViewHolder(
    itemView: View,
    private val todoWidgetComponentListener: TodoWidgetComponentListener
) : AbstractViewHolder<TodoWidgetListDataModel>(itemView), TodoWidgetDismissListener {

    companion object {
        val LAYOUT = R.layout.home_component_todo_widget
    }

    private var binding: HomeComponentTodoWidgetBinding? by viewBinding()
    private val todoAdapter by lazy {
        CarouselListAdapter(
            TodoWidgetTypeFactoryImpl(todoWidgetComponentListener, this),
            CommonCarouselDiffUtilCallback()
        )
    }

    init {
        initAdapter()
    }

    private fun setHeaderComponent(element: TodoWidgetListDataModel) {
        binding?.homeComponentHeaderView?.bind(element.header)
    }

    private fun setChannelDivider(element: TodoWidgetListDataModel) {
        binding?.homeComponentDividerHeader?.gone()
        binding?.homeComponentDividerFooter?.gone()
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelConfig = element.config,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
    }

    private fun initAdapter() {
        binding?.homeComponentTodoWidgetRv?.apply {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    TodoWidgetItemDecoration()
                )
            }
            layoutManager = LinearLayoutManager(
                itemView.context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
            (itemAnimator as? SimpleItemAnimator)?.supportsChangeAnimations = true
            adapter = todoAdapter
        }
    }

    private fun renderError() {
        todoAdapter.submitList(listOf(TodoErrorDataModel()) as? List<Visitable<CommonCarouselProductCardTypeFactory>>)
    }

    private fun renderShimmering() {
        todoAdapter.submitList(listOf(TodoShimmerDataModel()) as? List<Visitable<CommonCarouselProductCardTypeFactory>>)
    }

    private fun renderTodoWidget(element: TodoWidgetListDataModel) {
        val items = element.todoWidgetList.mapIndexed { index, item ->
            CarouselTodoWidgetDataModel(
                data = item,
                channelId = element.id,
                headerName = element.header.name,
                verticalPosition = element.verticalPosition,
                cardPosition = index,
                isCarousel = element.todoWidgetList.size > 1,
                cardInteraction = true
            )
        }
        todoAdapter.submitList(items as? List<Visitable<CommonCarouselProductCardTypeFactory>>)
        binding?.homeComponentTodoWidgetRv?.scrollToPosition(0)
    }

    override fun bind(element: TodoWidgetListDataModel) {
        if (element.isShowTodoWidget()) {
            binding?.homeComponentTodoWidgetRv?.show()
            setHeaderComponent(element)
            setChannelDivider(element)
            when (element.status) {
                TodoWidgetListDataModel.STATUS_ERROR -> {
                    renderError()
                }
                TodoWidgetListDataModel.STATUS_SUCCESS -> {
                    renderTodoWidget(element)
                }
                TodoWidgetListDataModel.STATUS_LOADING -> {
                    renderShimmering()
                }
            }
        } else {
            binding?.homeComponentTodoWidgetRv?.gone()
            binding?.homeComponentHeaderView?.gone()
            binding?.homeComponentDividerHeader?.gone()
            binding?.homeComponentDividerFooter?.gone()
        }
    }

    override fun bind(element: TodoWidgetListDataModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && (payloads[0] as? Bundle)?.getBoolean(TodoWidgetListDataModel.PAYLOAD_IS_REFRESH, false) == true) return
        bind(element)
    }

    override fun dismiss(element: CarouselTodoWidgetDataModel, position: Int) {
        todoWidgetComponentListener.onTodoCloseClicked(element)
    }
}

interface TodoWidgetDismissListener {
    fun dismiss(element: CarouselTodoWidgetDataModel, position: Int)
}
