package com.tokopedia.product.addedit.draft.presentation.viewholder

import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.databinding.ItemProductDraftListBinding
import com.tokopedia.product.addedit.draft.presentation.listener.ProductDraftListListener
import com.tokopedia.product.addedit.draft.presentation.model.ProductDraftUiModel

class ProductDraftListViewHolder(
    private val binding: ItemProductDraftListBinding,
    private val listener: ProductDraftListListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bindData(draft: ProductDraftUiModel) {
        itemView.run {
            binding.ivProduct.setImageUrl(draft.imageUrl)

            if (draft.productName.isBlank()) {
                binding.tvProductName.text = itemView.context.getString(R.string.label_draft_has_not_have_product_name_yet)
                binding.tvProductName.setTypeface(binding.tvProductName.typeface, Typeface.ITALIC)
            } else {
                binding.tvProductName.text = draft.productName
                binding.tvProductName.setTypeface(binding.tvProductName.typeface, Typeface.BOLD)
            }

            binding.tvCompletionPercentage.text = itemView.context.getString(R.string.label_draft_item_percent_complete, draft.completionPercent)
            binding.pbCompletion.progress = draft.completionPercent.toFloat()

            if (draft.isCorrupt) {
                binding.tvCompletionPercentage.gone()
                binding.pbCompletion.gone()
                binding.tvProductName.text = itemView.context.getString(R.string.label_draft_product_corrupt)
            } else {
                setOnClickListener {
                    listener.onDraftClickListener(draft.draftId)
                }
            }

            binding.ivTrashCan.setOnClickListener {
                listener.onDraftDeleteListener(draft.draftId, adapterPosition, draft.productName)
            }
        }
    }
}
