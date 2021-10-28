package com.tokopedia.product.addedit.variant.presentation.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.addedit.databinding.ItemVariantTypeSelectedBinding
import com.tokopedia.utils.view.binding.viewBinding

class VariantTypeSelectedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var binding by viewBinding<ItemVariantTypeSelectedBinding>()

    fun bindData(text: String) {
        binding?.variantTypeName?.text = text
    }
}