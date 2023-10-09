package com.tokopedia.review.feature.inbox.pending.presentation.adapter.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup.MarginLayoutParams
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.reputation.common.view.AnimatedRatingPickerReviewPendingView
import com.tokopedia.review.feature.inbox.pending.presentation.adapter.uimodel.BulkReviewUiModel
import com.tokopedia.review.feature.inbox.pending.presentation.util.ReviewPendingItemListener
import com.tokopedia.review.inbox.R
import com.tokopedia.review.inbox.databinding.ItemBulkReviewBinding
import com.tokopedia.review.inbox.databinding.ItemReviewPendingBulkCardProductBinding
import com.tokopedia.unifycomponents.toPx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BulkReviewViewHolder(
    view: View,
    private val listener: ReviewPendingItemListener
) : AbstractViewHolder<BulkReviewUiModel>(view) {
    companion object {
        val LAYOUT = R.layout.item_bulk_review

        private const val PRODUCT_CARD_SIZE = 52
        private const val PRODUCT_CARD_MARGIN = 8
        private const val STAR_ANIMATION_DELAY = 100L
        private const val STAR_FINISHED_DELAY = 200L
    }

    private val binding = ItemBulkReviewBinding.bind(view).apply {
        bulkReviewImageBackground.setImageUrl(
            getString(R.string.bulk_review_card_background)
        )
    }

    override fun bind(element: BulkReviewUiModel) = with(binding) {
        val data = element.data
        val title = data.title
        bulkReviewTitle.text = title

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
            ).apply {
                setOnClickListener { listener.onClickBulkReview(title, data.appLink) }
            }
            thumbnailContainer.addView(imageUnify)
        }

        bulkReviewStars.setListener(object :
                AnimatedRatingPickerReviewPendingView.AnimatedReputationListener {
                private var disableClick = false
                override fun onClick(position: Int) {
                    if (disableClick) return
                    disableClick = true
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(STAR_ANIMATION_DELAY * position)
                        listener.onClickStarBulkReview(title, data.appLink, position)
                        delay(STAR_FINISHED_DELAY)
                        bulkReviewStars.resetStars()
                        disableClick = false
                    }
                }
            })
        root.setOnClickListener { listener.onClickBulkReview(title, data.appLink) }
        root.addOnImpressionListener(element.impressHolder) {
            listener.onImpressBulkReviewCard(title)
        }
    }

    private fun BulkReviewUiModel.Product.toProductCard(
        addMargin: Boolean,
        moreInfo: String
    ): View {
        val context = binding.root.context
        val layoutInflater = LayoutInflater.from(context)
        return ItemReviewPendingBulkCardProductBinding.inflate(layoutInflater).apply {
            val layoutParams = MarginLayoutParams(PRODUCT_CARD_SIZE.toPx(), PRODUCT_CARD_SIZE.toPx())
            if (addMargin) layoutParams.marginStart = PRODUCT_CARD_MARGIN.toPx()
            root.layoutParams = layoutParams

            reviewPendingBulkCardProductImage.setImageUrl(imageUrl)
            reviewPendingBulkCardProductMore.showIfWithBlock(moreInfo.isNotEmpty()) {
                text = moreInfo
            }
        }.root
    }
}
