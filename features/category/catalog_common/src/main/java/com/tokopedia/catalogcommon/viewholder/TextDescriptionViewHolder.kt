package com.tokopedia.catalogcommon.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.catalogcommon.R
import com.tokopedia.catalogcommon.databinding.ItemTextDescriptionBinding
import com.tokopedia.catalogcommon.uimodel.TextDescriptionUiModel
import com.tokopedia.utils.view.binding.viewBinding

class TextDescriptionViewHolder(itemView: View):
    AbstractViewHolder<TextDescriptionUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_text_description

        private val TEXT_HIGH_EMPHASIS = R.color.dms_static_Unify_NN950_light
        private val TEXT_LOW_EMPHASIS = R.color.dms_static_Unify_NN600_light
    }

    private val binding by viewBinding<ItemTextDescriptionBinding>()

    override fun bind(element: TextDescriptionUiModel) {
        binding?.let {
            it.tgpTextWidgetHighlight.text = element.item.highlight
            it.tgpTextWidgetTitle.text = element.item.title
            it.tgpTextWidgetDescription.text = element.item.description
        }
        overrideWidgetTheme(element)
    }

    private fun overrideWidgetTheme(element: TextDescriptionUiModel) {
        binding?.let {
            if (element.isDarkMode) {
                it.tgpTextWidgetHighlight.setTextColor(ContextCompat.getColor(itemView.context, TEXT_LOW_EMPHASIS))
                it.tgpTextWidgetTitle.setTextColor(ContextCompat.getColor(itemView.context, TEXT_HIGH_EMPHASIS))
                it.tgpTextWidgetDescription.setTextColor(ContextCompat.getColor(itemView.context, TEXT_LOW_EMPHASIS))
            }
        }
    }

}
