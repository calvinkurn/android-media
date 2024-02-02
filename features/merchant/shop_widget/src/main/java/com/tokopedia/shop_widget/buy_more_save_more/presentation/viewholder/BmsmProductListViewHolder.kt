package com.tokopedia.shop_widget.buy_more_save_more.presentation.viewholder

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.ViewGroup.MarginLayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.visibleWithCondition
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop_widget.buy_more_save_more.entity.OfferingProductListUiModel.Product
import com.tokopedia.shop_widget.buy_more_save_more.presentation.listener.BmsmWidgetItemEventListener
import com.tokopedia.shop_widget.databinding.ItemBmsmWidgetProductListBinding
import com.tokopedia.unifycomponents.Label
import com.tokopedia.utils.view.binding.viewBinding

class BmsmProductListViewHolder(
    itemView: View,
    private val listener: BmsmWidgetItemEventListener
) : RecyclerView.ViewHolder(itemView) {


    private val binding: ItemBmsmWidgetProductListBinding? by viewBinding()

    fun bind(element: Product) {
        binding?.apply {
            productCard.apply {
                setProductModel(element.mapToProductCardModel())
                setAddToCartOnClickListener { listener.onAtcClicked(element) }
                setOnClickListener { listener.onProductCardClicked(element) }
                applyCarousel()
            }
        }
    }

    private fun Product.mapToProductCardModel(): ProductCardModel {
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

    private fun List<Product.LabelGroup>.toLabelGroup(): List<ProductCardModel.LabelGroup> {
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
