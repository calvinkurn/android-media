package com.tokopedia.product.detail.view.viewholder.review_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductReviewListUiModel
import com.tokopedia.product.detail.databinding.ReviewListItemBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by yovi.putra on 15/02/23"
 * Project name: android-tokopedia-core
 **/

class ReviewListItemViewHolder(
    private val binding: ReviewListItemBinding,
    private val listener: DynamicProductDetailListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiModel: ProductReviewListUiModel.Review, trackData: ComponentTrackDataModel?) = with(binding) {
        renderUI(uiModel = uiModel)
        eventClick(uiModel = uiModel, trackData = trackData)
    }

    private fun ReviewListItemBinding.renderUI(uiModel: ProductReviewListUiModel.Review) {
        reviewListImage.loadImage(uiModel.userImage)
        reviewListName.text = uiModel.userName
        reviewListUserTitle.text = uiModel.userTitle
        reviewListUserSubTitle.text = uiModel.userSubtitle
        reviewListReviewText.text = uiModel.reviewText
    }

    private fun ReviewListItemBinding.eventClick(
        uiModel: ProductReviewListUiModel.Review,
        trackData: ComponentTrackDataModel?
    ) {
        val appLink = uiModel.appLink

        if (appLink.isNotBlank()) {
            root.setOnClickListener {
                Toast.makeText(itemView.context, "under maintenance", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: DynamicProductDetailListener
        ): ReviewListItemViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val binding = ReviewListItemBinding.inflate(inflate, parent, false)
            return ReviewListItemViewHolder(binding = binding, listener = listener)
        }
    }
}
