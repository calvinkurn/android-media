package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.product.addedit.databinding.ItemVariantTypeSuggestionBinding
import com.tokopedia.utils.view.binding.viewBinding

class VariantTypeSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var binding by viewBinding<ItemVariantTypeSuggestionBinding>()

    fun bindData(text: String, highlightCharLength: Int, enabled: Boolean) {
        binding?.variantTypeName?.isEnabled = enabled
        binding?.iconSuggestion?.isVisible = enabled
        if (enabled && highlightCharLength <= text.length) {
            val normalText = text.take(highlightCharLength)
            val boldText = text.takeLast(text.length - highlightCharLength)
            binding?.variantTypeName?.text = MethodChecker.fromHtml("$normalText<b>$boldText</b>")
        } else {
            binding?.variantTypeName?.text = text
        }
    }
}