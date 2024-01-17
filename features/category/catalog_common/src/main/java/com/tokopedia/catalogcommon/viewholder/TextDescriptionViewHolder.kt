package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemTextDescriptionBinding
import com.tokopedia.catalogcommon.listener.TextDescriptionListener
import com.tokopedia.catalogcommon.uimodel.TextDescriptionUiModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.utils.view.binding.viewBinding

class TextDescriptionViewHolder(
    itemView: View,
    private val listener: TextDescriptionListener? = null
) :
    AbstractViewHolder<TextDescriptionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_text_description
    }

    private val binding by viewBinding<ItemTextDescriptionBinding>()

    override fun bind(element: TextDescriptionUiModel) {
        binding?.let {
            it.tgpTextWidgetHighlight.showWithCondition(element.item.highlight.isNotEmpty())
            if (element.item.highlight.isNotEmpty()){
                it.tgpTextWidgetHighlight.text = element.item.highlight
                it.tgpTextWidgetTitle.text = element.item.title
                it.tgpTextWidgetDescription.text = element.item.description
            }else{
                it.tgpTextWidgetHighlight.gone()
                it.tgpTextWidgetTitle.text = element.item.title
                it.tgpTextWidgetDescription.text = element.item.description
                it.tgpTextWidgetDescription.maxLines = Integer.MAX_VALUE

            }
        }
        overrideWidgetTheme(element)
        listener?.onTextDescriptionImpression(element.widgetName)
    }

    private fun overrideWidgetTheme(element: TextDescriptionUiModel) {
        binding?.let {
            it.tgpTextWidgetTitle.setTextColor(element.widgetTextColor ?: return)
            it.tgpTextWidgetHighlight.setTextColor(element.widgetTextColor ?: return)
            it.tgpTextWidgetDescription.setTextColor(element.widgetTextColor ?: return)
        }
    }

}
