package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.product.addedit.databinding.ItemVariantTypeSuggestionBinding
import com.tokopedia.utils.view.binding.viewBinding

class VariantTypeSuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var binding by viewBinding<ItemVariantTypeSuggestionBinding>()

    fun bindData(text: String, highlightCharLength: Int) {
        val boldText = text.take(highlightCharLength)
        val normalText = text.takeLast(highlightCharLength)
        binding?.variantTypeName?.text = MethodChecker.fromHtml("<b>$boldText</b>$normalText")
    }
}