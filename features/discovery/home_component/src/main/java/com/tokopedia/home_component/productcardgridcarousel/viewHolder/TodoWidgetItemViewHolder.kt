package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentTodoWidgetItemBinding
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class TodoWidgetItemViewHolder(
    view: View
) : AbstractViewHolder<CarouselTodoWidgetDataModel>(view) {

    private var binding: HomeComponentTodoWidgetItemBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.home_banner_item_mission_widget
    }

    override fun bind(element: CarouselTodoWidgetDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: CarouselTodoWidgetDataModel) {
        binding?.run {
            cardContainerMissionWidget.animateOnPress = if (element.cardInteraction) {
                CardUnify2.ANIMATE_OVERLAY_BOUNCE
            } else {
                CardUnify2.ANIMATE_OVERLAY
            }
            titleTodoWidget.text = element.title

            if(element.dueDate.isNotEmpty()) {
                dueDateTodoWidget.visible()
                dueDateTodoWidget.text = element.dueDate
            } else {
                dueDateTodoWidget.gone()
            }

            if(element.description.isNotEmpty()) {
                descTodoWidget.visible()
                descTodoWidget.text = element.description
            } else {
                descTodoWidget.gone()
            }

            if(element.price.isNotEmpty()) {
                priceTodoWidget.visible()
                priceTodoWidget.text = element.price
            } else {
                priceTodoWidget.gone()
            }
        }
    }
}
