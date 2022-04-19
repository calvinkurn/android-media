package com.tokopedia.product.addedit.draft.presentation.viewholder

import android.graphics.Typeface
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.product.addedit.R
import com.tokopedia.product.addedit.draft.presentation.listener.ProductDraftListListener
import com.tokopedia.product.addedit.draft.presentation.model.ProductDraftUiModel
import com.tokopedia.unifyprinciples.Typography

class ProductDraftListViewHolder(itemView: View, private val listener: ProductDraftListListener)
    :  RecyclerView.ViewHolder(itemView) {

    fun bindData(draft: ProductDraftUiModel)  {
        itemView.run {
            val ivProduct: AppCompatImageView? = findViewById(R.id.ivProduct)
            val ivTrashCan: AppCompatImageView? = findViewById(R.id.ivTrashCan)
            val tvCompletionPercentage: Typography? = findViewById(R.id.tvCompletionPercentage)
            val tvProductName: Typography? = findViewById(R.id.tvProductName)
            val pbCompletion: RoundCornerProgressBar? = findViewById(R.id.pbCompletion)

            ivProduct?.loadImage(draft.imageUrl, R.drawable.ic_image_not_available)

            if (draft.productName.isBlank()) {
                tvProductName?.text = itemView.context.getString(R.string.label_draft_has_not_have_product_name_yet)
                tvProductName?.setTypeface(tvProductName.typeface, Typeface.ITALIC)
            } else {
                tvProductName?.text = draft.productName
                tvProductName?.setTypeface(tvProductName.typeface, Typeface.BOLD)
            }

            tvCompletionPercentage?.text = itemView.context.getString(R.string.label_draft_item_percent_complete, draft.completionPercent)
            pbCompletion?.progress = draft.completionPercent.toFloat()

            setOnClickListener {
                listener.onDraftClickListener(draft.draftId)
            }

            ivTrashCan?.setOnClickListener {
                listener.onDraftDeleteListener(draft.draftId, adapterPosition, draft.productName)
            }
        }
    }

}