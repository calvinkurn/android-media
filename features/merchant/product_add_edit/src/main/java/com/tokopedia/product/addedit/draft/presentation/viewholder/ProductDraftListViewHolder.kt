package com.tokopedia.product.addedit.draft.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.draft.presentation.listener.ProductDraftListListener
import com.tokopedia.product.addedit.draft.presentation.model.ProductDraftUiModel
import kotlinx.android.synthetic.main.item_product_draft_list.view.*

class ProductDraftListViewHolder(itemView: View, private val listener: ProductDraftListListener)
    :  RecyclerView.ViewHolder(itemView) {

    fun bindData(draft: ProductDraftUiModel)  {
        itemView.run {
            ivProduct.loadImage(draft.imageUrl)
            tvProductName.text = draft.productName
            tvCompletionPercentage.text = itemView.context.getString(R.string.label_draft_item_percent_complete, draft.completionPercent)
            pbCompletion.progress = draft.completionPercent.toFloat()
            setOnClickListener {
                listener.onDraftClickListener(draft.draftId)
            }
            ivTrashCan.setOnClickListener {
                listener.onDraftDeleteListener(draft.draftId, adapterPosition)
            }
        }
    }

}