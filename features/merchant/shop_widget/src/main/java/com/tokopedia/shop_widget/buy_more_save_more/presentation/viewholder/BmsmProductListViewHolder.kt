package com.tokopedia.shop_widget.buy_more_save_more.presentation.viewholder

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup.LayoutParams
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
    private val parentWidth: Int,
    private val listener: BmsmWidgetItemEventListener
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val LABEL_GROUP_PRICE = "price"
        private const val TWO_ITEMS_PRODUCT_CARD_WIDTH = 176
    }

    private val binding: ItemBmsmWidgetProductListBinding? by viewBinding()

    fun bind(element: Product, isTwoProductItem: Boolean = false) {
        binding?.apply {
            productCard.apply {
                if (isTwoProductItem) setItemWidthForTwoProductItem()
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
            slashedPrice = campaign.originalPrice.ifEmpty { price },
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

    private fun Label.setBenefitLabel(labelGroup: List<Product.LabelGroup>) {
        val label = labelGroup.find { it.position == LABEL_GROUP_PRICE }
        this.apply {
            visibleWithCondition(label?.title?.isNotEmpty() == true)
            label?.title?.let { setLabel(it) }
        }
    }

    private fun ProductCardGridView.setItemWidthForTwoProductItem() {
        val params = this.layoutParams
        params.width = LayoutParams.MATCH_PARENT
        this.layoutParams = LayoutParams(params)
    }

    private fun toDp(value: Int): Int {
        return (value * Resources.getSystem().displayMetrics.density).toInt()
    }
}
