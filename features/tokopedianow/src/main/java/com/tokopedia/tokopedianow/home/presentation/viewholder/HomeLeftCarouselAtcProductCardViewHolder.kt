package com.tokopedia.tokopedianow.home.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.common.model.LabelGroup
import com.tokopedia.tokopedianow.common.model.TokoNowProductCardViewUiModel
import com.tokopedia.tokopedianow.databinding.ItemTokopedianowHomeLeftCarouselAtcProductCardBinding
import com.tokopedia.tokopedianow.home.presentation.uimodel.HomeLeftCarouselAtcProductCardUiModel
import com.tokopedia.utils.view.binding.viewBinding

class HomeLeftCarouselAtcProductCardViewHolder(
    itemView: View,
    private var listener: HomeLeftCarouselAtcProductCardListener? = null
): AbstractViewHolder<HomeLeftCarouselAtcProductCardUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_home_left_carousel_atc_product_card
    }

    private var binding: ItemTokopedianowHomeLeftCarouselAtcProductCardBinding? by viewBinding()

    init {
        setIsRecyclable(false)
    }

    override fun bind(element: HomeLeftCarouselAtcProductCardUiModel) {
        binding?.productCard?.setData(
            TokoNowProductCardViewUiModel(
                imageUrl = element.productCardModel.productImageUrl,
                minOrder = element.productCardModel.nonVariant?.minQuantityFinal.orZero(),
                maxOrder = element.productCardModel.nonVariant?.maxQuantityFinal.orZero(),
                availableStock = element.productCardModel.nonVariant?.quantity.orZero(),
                orderQuantity = 0,
                price = element.productCardModel.formattedPrice,
                discount = element.productCardModel.discountPercentage,
                slashPrice = element.productCardModel.slashedPrice,
                name = element.productCardModel.productName,
                rating = element.productCardModel.countSoldRating,
                hasBeenWishlist = false,
                progressBarLabel = element.productCardModel.stockBarLabel,
                progressBarLabelColor = element.productCardModel.stockBarLabelColor,
                progressBarPercentage = element.productCardModel.stockBarPercentage,
                labelGroupList = element.productCardModel.labelGroupList.map {
                    LabelGroup(
                        position = it.position,
                        type = it.type,
                        title = it.title,
                        imageUrl = it.imageUrl
                    )
                }
            )
        )

//        binding?.productCardGridView?.apply {
//            setOnClickListener {
//                listener?.onProductCardClicked(
//                    position = element.position,
//                    product = element
//                )
//            }
//            setAddVariantClickListener {
//                listener?.onProductCardAddVariantClicked(element)
//            }
//            setAddToCartNonVariantClickListener(object: ATCNonVariantListener {
//                override fun onQuantityChanged(quantity: Int) {
//                    listener?.onProductCardQuantityChanged(
//                        product = element,
//                        quantity = quantity
//                    )
//                }
//            })
//            setImageProductViewHintListener(element, object : ViewHintListener{
//                override fun onViewHint() {
//                    listener?.onProductCardImpressed(
//                        position = element.position,
//                        product = element
//                    )
//                }
//            })
//        }
    }

    interface HomeLeftCarouselAtcProductCardListener {
        fun onProductCardAddVariantClicked(product: HomeLeftCarouselAtcProductCardUiModel)
        fun onProductCardQuantityChanged(product: HomeLeftCarouselAtcProductCardUiModel, quantity: Int)
        fun onProductCardClicked(position: Int, product: HomeLeftCarouselAtcProductCardUiModel)
        fun onProductCardImpressed(position: Int, product: HomeLeftCarouselAtcProductCardUiModel)
    }
}
