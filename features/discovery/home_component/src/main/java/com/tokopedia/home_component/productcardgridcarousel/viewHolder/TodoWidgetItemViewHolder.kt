package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentTodoWidgetItemBinding
import com.tokopedia.home_component.model.HomeComponentCta
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.util.TodoWidgetUtil
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel
 */
class TodoWidgetItemViewHolder(
    view: View
) : AbstractViewHolder<CarouselTodoWidgetDataModel>(view) {

    private var binding: HomeComponentTodoWidgetItemBinding? by viewBinding()

    companion object {
        val LAYOUT = R.layout.home_component_todo_widget_item
    }

    override fun bind(element: CarouselTodoWidgetDataModel) {
        setLayout(element)
    }

    private fun setLayout(element: CarouselTodoWidgetDataModel) {
        binding?.run {
            cardContainerTodoWidget.animateOnPress = if (element.cardInteraction) {
                CardUnify2.ANIMATE_OVERLAY_BOUNCE
            } else {
                CardUnify2.ANIMATE_OVERLAY
            }

            icCloseTodoWidget.setOnClickListener {
                element.todoWidgetDismissListener.dismiss(element, absoluteAdapterPosition)
            }

            cardContainerTodoWidget.addOnImpressionListener(element) {
                element.todoWidgetComponentListener.onTodoImpressed(element, absoluteAdapterPosition)
            }

            setLayoutWidth(element)
            setTextContent(element)
            mappingCtaButton(element)
        }
    }

    private fun setTextContent(element: CarouselTodoWidgetDataModel) {
        binding?.run {
            titleTodoWidget.text = element.title
            imageTodoWidget.setImageUrl(element.imageUrl)

            if (element.dueDate.isNotEmpty()) {
                dueDateTodoWidget.visible()
                dueDateTodoWidget.text = element.dueDate
            } else {
                dueDateTodoWidget.gone()
            }

            if (element.contextInfo.isNotEmpty()) {
                descTodoWidget.visible()
                descTodoWidget.text = element.contextInfo
            } else {
                descTodoWidget.gone()
            }

            if (element.price.isNotEmpty()) {
                priceTodoWidget.visible()
                priceTodoWidget.text = element.price
            } else {
                priceTodoWidget.gone()
            }
        }
    }

    private fun setLayoutWidth(element: CarouselTodoWidgetDataModel) {
        binding?.run {
            val layoutParams = cardContainerTodoWidget?.layoutParams
            if (element.isCarousel) {
                layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
            }
            cardContainerTodoWidget?.layoutParams = layoutParams

            cardLayout.maxWidth = TodoWidgetUtil.measureTodoWidgetCardMaxWidth(itemView.context)
            (descTodoWidget.layoutParams as? ConstraintLayout.LayoutParams)?.matchConstraintMaxWidth = TodoWidgetUtil.measureTodoWidgetContentMaxWidth(itemView.context)
        }
    }

    private fun mappingCtaButton(element: CarouselTodoWidgetDataModel) {
        binding?.ctaTodoWidget?.run {
            isInverse = false

            visibility = if (element.ctaText.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
            var mode = HomeComponentCta.CTA_MODE_MAIN
            var type = HomeComponentCta.CTA_TYPE_FILLED

            if (element.ctaMode.isNotEmpty()) mode = element.ctaMode

            if (element.ctaType.isNotEmpty()) type = element.ctaType

            when (type) {
                HomeComponentCta.CTA_TYPE_FILLED -> buttonVariant = UnifyButton.Variant.FILLED
                HomeComponentCta.CTA_TYPE_GHOST -> buttonVariant = UnifyButton.Variant.GHOST
                HomeComponentCta.CTA_TYPE_TEXT -> buttonVariant = UnifyButton.Variant.TEXT_ONLY
            }

            when (mode) {
                HomeComponentCta.CTA_MODE_MAIN -> buttonType = UnifyButton.Type.MAIN
                HomeComponentCta.CTA_MODE_TRANSACTION -> buttonType = UnifyButton.Type.TRANSACTION
                HomeComponentCta.CTA_MODE_ALTERNATE -> buttonType = UnifyButton.Type.ALTERNATE
                HomeComponentCta.CTA_MODE_DISABLED -> isEnabled = false
                HomeComponentCta.CTA_MODE_INVERTED -> isInverse = true
            }

            text = element.ctaText

            setOnClickListener {
                element.todoWidgetComponentListener.onTodoCTAClicked(element, absoluteAdapterPosition)
            }
        }
    }
}
