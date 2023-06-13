package com.tokopedia.product.detail.view.viewholder.show_review.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductShopReviewUiModel
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

class ShopReviewListItemAdapter(
    private val listener: DynamicProductDetailListener
) : ListAdapter<ProductShopReviewUiModel.Review, ShopReviewListItemViewHolder>(DIFF_ITEM) {

    companion object {
        private val DIFF_ITEM = object : DiffUtil.ItemCallback<ProductShopReviewUiModel.Review>() {
            override fun areItemsTheSame(
                oldItem: ProductShopReviewUiModel.Review,
                newItem: ProductShopReviewUiModel.Review
            ): Boolean = oldItem == newItem

            override fun areContentsTheSame(
                oldItem: ProductShopReviewUiModel.Review,
                newItem: ProductShopReviewUiModel.Review
            ): Boolean = oldItem.userImage == newItem.userImage &&
                oldItem.userName == newItem.userName &&
                oldItem.userTitle == newItem.userTitle &&
                oldItem.userSubtitle == newItem.userSubtitle &&
                oldItem.appLink == newItem.appLink &&
                oldItem.reviewText == newItem.reviewText
        }
    }

    private var trackData: ComponentTrackDataModel? = null

    fun submitList(items: List<ProductShopReviewUiModel.Review>, trackDataModel: ComponentTrackDataModel) {
        trackData = trackDataModel
        submitList(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopReviewListItemViewHolder {
        return ShopReviewListItemViewHolder.create(parent, listener)
    }

    override fun onBindViewHolder(holder: ShopReviewListItemViewHolder, position: Int) {
        val data = currentList.getOrNull(position) ?: return
        holder.bind(uiModel = data, trackData = trackData)
    }
}
