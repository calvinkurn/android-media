package com.tokopedia.product.detail.view.viewholder.review_list.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductReviewListUiModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ReviewListItemAdapter(
    private val listener: DynamicProductDetailListener
) : ListAdapter<ProductReviewListUiModel.Review, ReviewListItemViewHolder>(DIFF_ITEM) {

    companion object {
        private val DIFF_ITEM = object : DiffUtil.ItemCallback<ProductReviewListUiModel.Review>() {
            override fun areItemsTheSame(
                oldItem: ProductReviewListUiModel.Review,
                newItem: ProductReviewListUiModel.Review
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ProductReviewListUiModel.Review,
                newItem: ProductReviewListUiModel.Review
            ): Boolean = oldItem.userImage == newItem.userImage &&
                oldItem.userName == newItem.userName &&
                oldItem.userTitle == newItem.userTitle &&
                oldItem.userSubtitle == newItem.userSubtitle &&
                oldItem.appLink == newItem.appLink &&
                oldItem.reviewText == newItem.reviewText
        }
    }

    private var trackData: ComponentTrackDataModel? = null

    fun submitList(items: List<ProductReviewListUiModel.Review>, trackDataModel: ComponentTrackDataModel) {
        trackData = trackDataModel
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewListItemViewHolder {
        return ReviewListItemViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: ReviewListItemViewHolder, position: Int) {
        val data = currentList.getOrNull(position) ?: return
        holder.bind(uiModel = data, trackData = trackData)
    }
}
