package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.BulkReviewUiModel
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemBulkReviewBinding
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.toPx

class BulkReviewViewHolder(
    view: View
) : AbstractViewHolder<BulkReviewUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_bulk_review
    }

    private val binding = ItemBulkReviewBinding.bind(view)

    override fun bind(element: BulkReviewUiModel) {
        val data = element.data
        binding.bulkReviewTitle.text = data.title

        val thumbnailContainer = binding.bulkReviewThumbnailContainer
        thumbnailContainer.removeAllViews()
        data.products.forEachIndexed { index, product ->
            val imageUnify = product.toImageUnify(binding.root.context, index != 0)
            thumbnailContainer.addView(imageUnify)
        }
    }

    private fun BulkReviewUiModel.Data.Product.toImageUnify(
        context: Context, addMargin: Boolean
    ): ImageUnify {
        val layoutParams = MarginLayoutParams(52.toPx(), 52.toPx())
        if (addMargin) layoutParams.marginStart = 8.toPx()

        return ImageUnify(context).apply {
            setLayoutParams(layoutParams)
            setImageUrl(imageUrl)
        }
    }
}
