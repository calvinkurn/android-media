package com.tokopedia.product.detail.view.viewholder.show_review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductShopReviewUiModel
import com.tokopedia.product.detail.databinding.ShopReviewListItemBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener

/**
 * Created by yovi.putra on 15/02/23"
 * Project name: android-tokopedia-core
 **/

class ShopReviewListItemViewHolder(
    private val binding: ShopReviewListItemBinding,
    private val listener: DynamicProductDetailListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiModel: ProductShopReviewUiModel.Review, trackData: ComponentTrackDataModel?) = with(binding) {
        renderUI(uiModel = uiModel)
        eventClick(uiModel = uiModel, trackData = trackData)
    }

    private fun ShopReviewListItemBinding.renderUI(uiModel: ProductShopReviewUiModel.Review) {
        shopReviewListImage.loadImage(uiModel.userImage)
        shopReviewListName.text = uiModel.userName
        shopReviewListUserTitle.text = uiModel.userTitle
        shopReviewListUserSubTitle.text = uiModel.userSubtitle
        shopReviewListReviewText.text = uiModel.reviewText
    }

    private fun ShopReviewListItemBinding.eventClick(
        uiModel: ProductShopReviewUiModel.Review,
        trackData: ComponentTrackDataModel?
    ) {
        val appLink = uiModel.appLink

        if (appLink.isNotBlank()) {
            root.setOnClickListener {
                listener.onShopReviewSeeMore(
                    appLink = appLink,
                    eventLabel = uiModel.talkId,
                    trackData = trackData
                )
            }
        }
    }

    companion object {
        fun create(
            parent: ViewGroup,
            listener: DynamicProductDetailListener
        ): ShopReviewListItemViewHolder {
            val inflate = LayoutInflater.from(parent.context)
            val binding = ShopReviewListItemBinding.inflate(inflate, parent, false)
            return ShopReviewListItemViewHolder(binding = binding, listener = listener)
        }
    }
}
