package com.tokopedia.shop_widget.buy_more_save_more.presentation.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetItemEventListener
import com.tokopedia.shop_widget.databinding.ItemBmsmWidgetTwoProductListItemBinding
import com.tokopedia.utils.view.binding.viewBinding

class BmsmTwoProductListViewHolder(
    itemView: View,
    private val listener: BmsmWidgetItemEventListener
) : RecyclerView.ViewHolder(itemView) {


    private val binding: ItemBmsmWidgetTwoProductListItemBinding? by viewBinding()

    fun bind(element: OfferingProductListUiModel.Product) {
        binding?.apply {
            productCard.apply {
                setProductModel(element.mapToProductCardModel())
                setAddToCartOnClickListener { listener.onAtcClicked(element) }
                setOnClickListener { listener.onProductCardClicked(element) }
                applyCarousel()
            }
        }
    }

    private fun OfferingProductListUiModel.Product.mapToProductCardModel(): ProductCardModel {
        return ProductCardModel(
            productImageUrl = imageUrl,
            productName = name,
            discountPercentage = if (campaign.discountedPercentage != Int.ZERO) "${campaign.discountedPercentage}%" else "",
            slashedPrice = campaign.originalPrice,
            formattedPrice = campaign.discountedPrice.ifEmpty { price },
            countSoldRating = rating,
            hasAddToCartButton = true,
            labelGroupList = labelGroup.toLabelGroup()
        )
    }

    private fun List<OfferingProductListUiModel.Product.LabelGroup>.toLabelGroup(): List<ProductCardModel.LabelGroup> {
        return map {
            ProductCardModel.LabelGroup(
                position = it.position,
                title = it.title,
                type = it.type,
                imageUrl = it.url
            )
        }
    }
}
