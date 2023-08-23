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
                it.tgpTextWidgetHighlight.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
                it.tgpTextWidgetTitle.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                it.tgpTextWidgetDescription.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN600))
            }
        }
    }

}
