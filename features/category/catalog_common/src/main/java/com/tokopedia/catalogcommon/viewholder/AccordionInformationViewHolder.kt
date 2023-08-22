package com.tokopedia.catalogcommon.viewholder

import android.view.LayoutInflater
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accordion.AccordionDataUnify
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ViewAccordionExpandBinding
import com.tokopedia.catalogcommon.databinding.WidgetAccordionInformationBinding
import com.tokopedia.catalogcommon.uimodel.AccordionInformationUiModel
import com.tokopedia.catalogcommon.util.orDefaultColor
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.utils.view.binding.viewBinding

class AccordionInformationViewHolder(itemView: View) : AbstractViewHolder<AccordionInformationUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_accordion_information
    }

    private val binding by viewBinding<WidgetAccordionInformationBinding>()

    private var lastPositionExpand = -1

    override fun bind(element: AccordionInformationUiModel) {
        binding?.clRootAccordion?.setBackgroundColor(element.widgetBackgroundColor.orDefaultColor(itemView.context))
        binding?.tvTitle?.text = element.titleWidget
//        binding?.tvTitle?.setTextColor(fontColor)

        element.contents.forEach {
            val bindingViewExpandable = ViewAccordionExpandBinding.inflate(
                LayoutInflater.from(itemView.context),
                binding?.root,
                false
            )
            bindingViewExpandable.tvDescription.text = it.description
//            bindingViewExpandable?.tvDescription?.setTextColor(fontColor)

            val data = AccordionDataUnify(title = it.title, subtitle = String.EMPTY, isExpanded = false, expandableView = bindingViewExpandable.root)
            data.setContentPadding(0,0,0,0)
            binding?.accordion?.addGroup(data)
        }

        binding?.accordion?.onItemClick= { position,isExpand->
            if (lastPositionExpand != -1){
                binding?.accordion?.collapseGroup(lastPositionExpand)
            }
            lastPositionExpand = position
        }

    }
}
