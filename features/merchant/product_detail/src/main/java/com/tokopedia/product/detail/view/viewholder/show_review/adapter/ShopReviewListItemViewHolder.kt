package com.tokopedia.product.detail.view.viewholder.show_review.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.setOnClickDebounceListener
import com.tokopedia.kotlin.extensions.view.showIfWithBlock
import com.tokopedia.media.loader.loadImage
import com.tokopedia.product.detail.common.utils.extensions.updateLayoutParams
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.review_list.ProductShopReviewUiModel
import com.tokopedia.product.detail.databinding.ShopReviewListItemBinding
import com.tokopedia.product.detail.view.listener.DynamicProductDetailListener
import com.tokopedia.unifycomponents.toPx

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
        shopReviewListUserSubTitle.text = uiModel.userSubtitle
        shopReviewListReviewText.text = uiModel.reviewText
        setUserName(uiModel = uiModel)
        setUserTitle(uiModel = uiModel)
    }

    private fun ShopReviewListItemBinding.setUserName(uiModel: ProductShopReviewUiModel.Review) {
        shopReviewListName.text = uiModel.userName

        // create pretty UI when user title not appear
        val isUserTitleVisible = uiModel.userTitle.isNotBlank()
        if (isUserTitleVisible) {
            setUserNameMaxWidth()
        } else {
            setUserNameWidth()
        }
    }

    private fun ShopReviewListItemBinding.setUserNameMaxWidth() {
        val maxWidthPx = USERNAME_MAX_WIDTH.toPx()

        if (shopReviewListName.maxWidth != maxWidthPx) {
            shopReviewListName.maxWidth = USERNAME_MAX_WIDTH.toPx()
        }
    }

    private fun ShopReviewListItemBinding.setUserNameWidth() {
        shopReviewListName.updateLayoutParams<ViewGroup.LayoutParams> {
            this?.width = Int.ZERO
        }
    }

    private fun ShopReviewListItemBinding.setUserTitle(uiModel: ProductShopReviewUiModel.Review) {
        val isUserTitleVisible = uiModel.userTitle.isNotBlank()

        shopReviewListTitleDot.isVisible = isUserTitleVisible
        shopReviewListUserTitle.showIfWithBlock(isUserTitleVisible) {
            text = uiModel.userTitle
        }
    }

    private fun ShopReviewListItemBinding.eventClick(
        uiModel: ProductShopReviewUiModel.Review,
        trackData: ComponentTrackDataModel?
    ) {
        val appLink = uiModel.appLink

        if (appLink.isNotBlank()) {
            root.setOnClickDebounceListener {
                listener.onShopReviewSeeMore(
                    appLink = appLink,
                    eventLabel = uiModel.id,
                    trackData = trackData
                )
            }
        }
    }

    companion object {
        private const val USERNAME_MAX_WIDTH = 92

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
