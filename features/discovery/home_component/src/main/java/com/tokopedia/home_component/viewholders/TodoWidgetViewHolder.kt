package com.tokopedia.home_component.viewholders

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.customview.HeaderListener
import com.tokopedia.home_component.databinding.HomeComponentTodoWidgetBinding
import com.tokopedia.home_component.decoration.TodoWidgetItemDecoration
import com.tokopedia.home_component.listener.TodoWidgetComponentListener
import com.tokopedia.home_component.model.ChannelModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.typeFactory.CommonCarouselProductCardTypeFactoryImpl
import com.tokopedia.home_component.util.ChannelWidgetUtil
import com.tokopedia.home_component.viewholders.adapter.TodoWidgetAdapter
import com.tokopedia.home_component.visitable.TodoWidgetListDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class TodoWidgetViewHolder(
    itemView: View,
    private val todoWidgetComponentListener: TodoWidgetComponentListener
) : AbstractViewHolder<TodoWidgetListDataModel>(itemView) {

    companion object {
        val LAYOUT = R.layout.home_component_todo_widget
    }

    private var binding: HomeComponentTodoWidgetBinding? by viewBinding()
    private var adapter: TodoWidgetAdapter? = null

    private fun setHeaderComponent(element: TodoWidgetListDataModel) {
        binding?.homeComponentHeaderView?.setChannel(
            element.channelModel,
            object : HeaderListener {
                override fun onSeeAllClick(link: String) {
                    // no-op
                }

                override fun onChannelExpired(channelModel: ChannelModel) {
                    // no-op
                }
            }
        )
    }

    private fun setChannelDivider(element: TodoWidgetListDataModel) {
        ChannelWidgetUtil.validateHomeComponentDivider(
            channelModel = element.channelModel,
            dividerTop = binding?.homeComponentDividerHeader,
            dividerBottom = binding?.homeComponentDividerFooter
        )
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

    private fun mappingItem(channel: ChannelModel, visitables: MutableList<Visitable<*>>) {
        val typeFactoryImpl = CommonCarouselProductCardTypeFactoryImpl(channel)
        adapter = TodoWidgetAdapter(visitables, typeFactoryImpl)
        binding?.homeComponentTodoWidgetRv?.adapter = adapter
        binding?.homeComponentTodoWidgetRv?.scrollToPosition(0)
    }

    private fun convertDataToMissionWidgetData(element: TodoWidgetListDataModel): MutableList<Visitable<*>> {
        val list: MutableList<Visitable<*>> = mutableListOf()

        for (todoWidget in element.todoWidgetList) {
            list.add(
                CarouselTodoWidgetDataModel(
                    id = todoWidget.id,
                    title = todoWidget.title,
                    dueDate = todoWidget.dueDate,
                    description = todoWidget.description,
                    imageUrl = todoWidget.imageUrl,
                    price = todoWidget.price,
                    slashedPrice = todoWidget.slashedPrice,
                    recommendationType = todoWidget.recommendationType,
                    buType = todoWidget.buType,
                    todoWidgetComponentListener = todoWidgetComponentListener,
                    cardApplink = todoWidget.cardApplink,
                    ctaApplink = todoWidget.ctaApplink,
                    channel = element.channelModel,
                    verticalPosition = adapterPosition
                )
            )
        }
        return list
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
                    val visitables = convertDataToMissionWidgetData(element)
                    mappingItem(element.channelModel, visitables)
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
}
