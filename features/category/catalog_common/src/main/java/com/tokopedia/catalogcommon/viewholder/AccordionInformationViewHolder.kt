package com.tokopedia.catalogcommon.viewholder

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.view.get
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ViewAccordionExpandBinding
import com.tokopedia.catalogcommon.databinding.WidgetAccordionInformationBinding
import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.util.orDefaultColor
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class AccordionInformationViewHolder(itemView: View) :
    AbstractViewHolder<AccordionInformationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_accordion_information
    }

    private val binding by viewBinding<WidgetAccordionInformationBinding>()

    private var lastPositionExpand = -1

    override fun bind(element: AccordionInformationUiModel) {
        binding?.accordion?.removeAllViews()
        binding?.tvTitle?.text = element.titleWidget
        binding?.tvTitle?.setTextColor(element?.widgetTextColor.orDefaultColor(itemView.context))
        element.contents.forEachIndexed { index, item ->
            val bindingViewExpandable = ViewAccordionExpandBinding.inflate(
                LayoutInflater.from(itemView.context),
                binding?.root,
                false
            )
            bindingViewExpandable.tvDescription.text = item.description
            bindingViewExpandable.tvDescription.setTextColor(
                item.textDescriptionColor.orDefaultColor(
                    itemView.context
                )
            )
            val data = AccordionDataUnify(
                title = item.title,
                subtitle = String.EMPTY,
                isExpanded = false,
                expandableView = bindingViewExpandable.root
            )
            data.setContentPadding(0, 0, 0, 0)
            binding?.accordion?.addGroup(data)

            data.arrowView?.setColorFilter(
                element.widgetTextColor.orDefaultColor(itemView.context),
                PorterDuff.Mode.SRC_IN
            )
            binding?.accordion?.get(index)?.findViewById<Typography>(com.tokopedia.accordion.R.id.accordion_title)
                ?.setTextColor(item.textTitleColor.orDefaultColor(itemView.context))
        }

        binding?.accordion?.onItemClick = { position, isExpand ->
            if (lastPositionExpand != -1 && position != lastPositionExpand) {
                binding?.accordion?.collapseGroup(lastPositionExpand)
            }
            lastPositionExpand = position
        }

    }

}
