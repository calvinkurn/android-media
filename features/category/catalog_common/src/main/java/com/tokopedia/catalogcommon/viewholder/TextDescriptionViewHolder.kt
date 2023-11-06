package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
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
        private val TEXT_LOW_EMPHASIS = R.color.dms_static_Unify_NN600_light
        private const val layout_4th_version = 4
    }

    private val binding by viewBinding<ItemTextDescriptionBinding>()

    override fun bind(element: TextDescriptionUiModel) {
        binding?.let {
            it.tgpTextWidgetHighlight.showWithCondition(element.item.highlight.isNotEmpty())
            if (element.layoutVersion == layout_4th_version){
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
        listener?.onTextDescriptionImpression()
    }

    private fun overrideWidgetTheme(element: TextDescriptionUiModel) {
        binding?.let {
            if (element.isDarkMode) {
                it.tgpTextWidgetHighlight.setTextColor(ContextCompat.getColor(itemView.context, TEXT_LOW_EMPHASIS))
                it.tgpTextWidgetDescription.setTextColor(ContextCompat.getColor(itemView.context, TEXT_LOW_EMPHASIS))
            }
            it.tgpTextWidgetTitle.setTextColor(element.widgetTextColor ?: return)
        }
    }

}
