package com.tokopedia.home_component.productcardgridcarousel.viewHolder

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home_component.R
import com.tokopedia.home_component.databinding.HomeComponentTodoWidgetItemBinding
import com.tokopedia.home_component.model.HomeComponentCta
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel
import com.tokopedia.home_component.productcardgridcarousel.dataModel.CarouselTodoWidgetDataModel.Companion.PAYLOAD_ITEM_POSITION
import com.tokopedia.home_component.util.TodoWidgetUtil
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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

    override fun bind(element: CarouselTodoWidgetDataModel, payloads: MutableList<Any>) {
        if(payloads.isNotEmpty() && (payloads[0] as? Bundle)?.getBoolean(PAYLOAD_ITEM_POSITION).orFalse()) {
            setListener(element)
        }
    }

    private fun setLayout(element: CarouselTodoWidgetDataModel) {
        binding?.run {
            cardContainerTodoWidget.setInteraction(element)
            setListener(element)
            setLayoutWidth(element)
            setTextContent(element)
            mappingCtaButton(element)
        }
    }

    private fun setListener(element: CarouselTodoWidgetDataModel) {
        binding?.run {
            cardContainerTodoWidget.addOnImpressionListener(element) {
                element.todoWidgetComponentListener.onTodoImpressed(element)
            }

            cardContainerTodoWidget.setOnClickListener {
                element.todoWidgetComponentListener.onTodoCardClicked(element)
            }

            icCloseTodoWidget.setOnClickListener {
                element.todoWidgetDismissListener.dismiss(element, element.cardPosition)
            }
        }
    }

    private fun setTextContent(element: CarouselTodoWidgetDataModel) {
        binding?.run {
            titleTodoWidget.text = element.data.title
            imageTodoWidget.setImageUrl(element.data.imageUrl)
            dueDateTodoWidget.renderData(element.data.dueDate)
            descTodoWidget.renderData(element.data.contextInfo)
            priceTodoWidget.renderData(element.data.price)
            slashedPriceTodoWidget.apply {
                renderData(element.data.slashedPrice)
                paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            labelDiscountTodoWidget.renderData(element.data.discountPercentage)
        }
    }

    private fun setLayoutWidth(element: CarouselTodoWidgetDataModel) {
        binding?.run {
            val cardWidth = if (element.isCarousel) {
                ViewGroup.LayoutParams.WRAP_CONTENT
            } else {
                ViewGroup.LayoutParams.MATCH_PARENT
            }
            cardLayout.layoutParams.width = cardWidth
            cardContainerTodoWidget.layoutParams.width = cardWidth

            cardLayout.maxWidth = TodoWidgetUtil.measureTodoWidgetCardMaxWidth(itemView.context)
            (descTodoWidget.layoutParams as? ConstraintLayout.LayoutParams)?.matchConstraintMaxWidth = TodoWidgetUtil.measureTodoWidgetContentMaxWidth(itemView.context)
        }
    }

    private fun mappingCtaButton(element: CarouselTodoWidgetDataModel) {
        binding?.ctaTodoWidget?.run {
            isInverse = false

            visibility = if (element.data.ctaText.isEmpty()) {
                View.GONE
                return
            } else {
                View.VISIBLE
            }
            val mode = element.data.ctaMode.ifEmpty { HomeComponentCta.CTA_MODE_MAIN }
            val type = element.data.ctaType.ifEmpty { HomeComponentCta.CTA_TYPE_FILLED }

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

            text = element.data.ctaText

            setOnClickListener {
                element.todoWidgetComponentListener.onTodoCTAClicked(element)
            }
        }
    }

    private fun CardUnify2.setInteraction(element: CarouselTodoWidgetDataModel) {
        animateOnPress = if (element.cardInteraction) {
            CardUnify2.ANIMATE_OVERLAY_BOUNCE
        } else {
            CardUnify2.ANIMATE_OVERLAY
        }
    }

    private fun TextView.renderData(data: String) {
        if (data.isNotEmpty()) {
            text = data
            show()
        } else {
            hide()
        }
    }
}
