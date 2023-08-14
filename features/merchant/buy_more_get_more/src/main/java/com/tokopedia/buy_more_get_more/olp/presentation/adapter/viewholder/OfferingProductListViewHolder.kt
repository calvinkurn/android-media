package com.tokopedia.buy_more_get_more.olp.presentation.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.buy_more_get_more.R
import com.tokopedia.buy_more_get_more.databinding.ItemOlpProductListBinding
import com.tokopedia.buy_more_get_more.olp.domain.entity.OfferProductListUiModel
import com.tokopedia.buy_more_get_more.olp.presentation.listener.AtcProductListener
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.utils.view.binding.viewBinding

class OfferingProductListViewHolder(
    itemView: View,
    private val atcProductListener: AtcProductListener
) : AbstractViewHolder<OfferProductListUiModel.Product>(itemView) {

    private val binding: ItemOlpProductListBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_olp_product_list
    }

    override fun bind(element: OfferProductListUiModel.Product) {
        itemView.setBackgroundColor(
            MethodChecker.getColor(
                itemView.context,
                com.tokopedia.unifyprinciples.R.color.Unify_NN0
            )
        )
        binding?.run {
            productCard.apply {
                setProductModel(element.mapToProductCardModel())
                setAddToCartOnClickListener { atcProductListener.onProductAtcVariantClicked(element) }
            }
        }
    }

    private fun OfferProductListUiModel.Product.mapToProductCardModel(): ProductCardModel {
        return ProductCardModel(
            productImageUrl = imageUrl,
            productName = name,
            discountPercentage = campaign.discountedPercentage,
            slashedPrice = campaign.originalPrice,
            formattedPrice = campaign.discountedPrice.ifEmpty { price },
            countSoldRating = rating,
            hasAddToCartButton = true,
            labelGroupList = listOf(
                ProductCardModel.LabelGroup(
                    position = "integrity",
                    title = "terjual $soldCount",
                    type = "textDarkGrey"
                )
            )
        )
    }
}
