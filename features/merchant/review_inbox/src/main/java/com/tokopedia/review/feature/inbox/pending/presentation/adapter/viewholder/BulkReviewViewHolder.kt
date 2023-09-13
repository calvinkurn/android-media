package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.BulkReviewUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemBulkReviewBinding
import com.tokopedia.review.inbox.databinding.ItemReviewPendingBulkCardProductBinding
import com.tokopedia.unifycomponents.toPx

class BulkReviewViewHolder(
    view: View,
    private val listener: ReviewPendingItemListener
) : AbstractViewHolder<BulkReviewUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_bulk_review
    }

    private val binding = ItemBulkReviewBinding.bind(view).apply {
        bulkReviewImageBackground.setImageUrl(
            getString(R.string.bulk_review_card_background)
        )
    }

    override fun bind(element: BulkReviewUiModel) = with(binding) {
        val data = element.data
        bulkReviewTitle.text = data.title

        val thumbnailContainer = bulkReviewThumbnailContainer
        thumbnailContainer.removeAllViews()
        val products = data.products
        products.forEachIndexed { index, product ->
            val moreInfo = data.productCountFmt.takeIf {
                it.isNotEmpty() && index == products.size - 1
            } ?: ""

            val imageUnify = product.toProductCard(
                addMargin = index != 0,
                moreInfo = moreInfo
            )
            thumbnailContainer.addView(imageUnify)
        }

        root.setOnClickListener {
            listener.onClickBulkReview(data.appLink)
        }
    }

    private fun BulkReviewUiModel.Product.toProductCard(
        addMargin: Boolean,
        moreInfo: String
    ): View {
        val context = binding.root.context
        val layoutInflater = LayoutInflater.from(context)
        return ItemReviewPendingBulkCardProductBinding.inflate(layoutInflater).apply {
            val layoutParams = MarginLayoutParams(52.toPx(), 52.toPx())
            if (addMargin) layoutParams.marginStart = 8.toPx()
            root.layoutParams = layoutParams

            reviewPendingBulkCardProductImage.setImageUrl(imageUrl)
            reviewPendingBulkCardProductMore.showIfWithBlock(moreInfo.isNotEmpty()) {
                text = moreInfo
            }
        }.root
    }
}
