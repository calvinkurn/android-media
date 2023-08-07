package com.tokopedia.home_component.viewholders

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
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.home_component.widget.common.CarouselListAdapter
import com.tokopedia.home_component.widget.todo.TodoWidgetDiffUtil
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
    private val adapter: CarouselListAdapter<CarouselTodoWidgetDataModel, CommonCarouselProductCardTypeFactory> by lazy {
        CarouselListAdapter(CommonCarouselProductCardTypeFactoryImpl(), TodoWidgetDiffUtil())
    }
    private var visitables = mutableListOf<CarouselTodoWidgetDataModel>()

    private fun setHeaderComponent(element: TodoWidgetListDataModel) {
        binding?.homeComponentHeaderView?.bind(element.header)
    }

    private fun setChannelDivider(element: TodoWidgetListDataModel) {
//        ChannelWidgetUtil.validateHomeComponentDivider(
//            channelModel = element.channelModel,
//            dividerTop = binding?.homeComponentDividerHeader,
//            dividerBottom = binding?.homeComponentDividerFooter
//        )
    }

    private fun valuateRecyclerViewDecoration() {
        if (binding?.homeComponentTodoWidgetRv?.itemDecorationCount == 0) {
            binding?.homeComponentTodoWidgetRv?.addItemDecoration(
                TodoWidgetItemDecoration()
            )
        }
        binding?.homeComponentTodoWidgetRv?.layoutManager = LinearLayoutManager(
            itemView.context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    private fun initAdapter() {
        binding?.homeComponentTodoWidgetRv?.adapter = adapter
        binding?.homeComponentTodoWidgetRv?.scrollToPosition(0)
        (binding?.homeComponentTodoWidgetRv?.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = true
    }

    private fun mappingItem(element: TodoWidgetListDataModel) {
        val items = element.todoWidgetList.mapIndexed { index, item ->
            CarouselTodoWidgetDataModel(
                data = item,
                channelId = element.id,
                headerName = element.header.name,
                todoWidgetComponentListener = todoWidgetComponentListener,
                verticalPosition = element.verticalPosition,
                cardPosition = index,
                isCarousel = element.todoWidgetList.size > 1,
                todoWidgetDismissListener = this,
                cardInteraction = true
            )
        }
        visitables.clear()
        visitables.addAll(items)
        adapter.submitList(visitables.toList())
    }

    private fun setLayoutByStatus(element: TodoWidgetListDataModel) {
        if (element.isShowTodoWidget()) {
            binding?.homeComponentHeaderView?.show()
            binding?.root?.show()
            itemView.show()
            binding?.homeComponentDividerHeader?.show()
            binding?.containerTodoWidgetItem?.show()
            binding?.refreshTodoWidget?.refreshBtn?.setOnClickListener {
                todoWidgetComponentListener.refreshTodowidget(element)
            }
            setHeaderComponent(element = element)
            setChannelDivider(element)
            when (element.status) {
                TodoWidgetListDataModel.STATUS_LOADING -> {
                    binding?.homeComponentHeaderView?.gone()
                    binding?.refreshTodoWidget?.gone()
                    binding?.homeComponentTodoWidgetRv?.gone()
                    binding?.shimmeringTodoWidget?.show()
                }
                TodoWidgetListDataModel.STATUS_ERROR -> {
                    binding?.refreshTodoWidget?.show()
                    binding?.homeComponentTodoWidgetRv?.gone()
                    binding?.shimmeringTodoWidget?.gone()
                    binding?.homeComponentHeaderView?.show()
                }
                else -> {
                    binding?.refreshTodoWidget?.gone()
                    binding?.homeComponentTodoWidgetRv?.show()
                    binding?.shimmeringTodoWidget?.gone()
                    binding?.homeComponentHeaderView?.show()
                    binding?.homeComponentTodoWidgetRv?.setHasFixedSize(true)
                    valuateRecyclerViewDecoration()
                    initAdapter()
                    mappingItem(element)
                }
            }
        } else {
            binding?.refreshTodoWidget?.gone()
            binding?.homeComponentTodoWidgetRv?.gone()
            binding?.shimmeringTodoWidget?.gone()
            binding?.homeComponentHeaderView?.gone()
            binding?.homeComponentHeaderView?.gone()
            binding?.homeComponentDividerHeader?.gone()
            binding?.containerTodoWidgetItem?.gone()
            binding?.root?.gone()
            itemView.gone()
        }
    }

    override fun bind(element: TodoWidgetListDataModel) {
        setLayoutByStatus(element)
    }

    override fun bind(element: TodoWidgetListDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun dismiss(element: CarouselTodoWidgetDataModel, position: Int) {
        visitables.removeAt(position)
        adapter.submitList(visitables.toList())
        todoWidgetComponentListener.onTodoCloseClicked(element)
    }
}

interface TodoWidgetDismissListener {
    fun dismiss(element: CarouselTodoWidgetDataModel, position: Int)
}
