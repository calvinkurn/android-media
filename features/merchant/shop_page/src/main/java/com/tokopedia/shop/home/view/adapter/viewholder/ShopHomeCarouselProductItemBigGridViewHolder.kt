package com.tokopedia.shop.home.view.adapter.viewholder

import android.annotation.SuppressLint
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.databinding.ItemShopCarouselProductCardBigGridBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeCarouselProductListener
import com.tokopedia.shop.home.view.model.ShopHomeCarousellProductUiModel
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

//need to surpress this one, since there are no pii related data defined on this class
@SuppressLint("PII Data Exposure")
class ShopHomeCarouselProductItemBigGridViewHolder(
    itemView: View,
    private val shopHomeCarousellProductUiModel: ShopHomeCarousellProductUiModel,
    private val shopHomeCarouselProductListener: ShopHomeCarouselProductListener,
    private val parentPosition: Int
) : AbstractViewHolder<ShopHomeProductUiModel>(itemView) {

    private val viewBinding: ItemShopCarouselProductCardBigGridBinding? by viewBinding()
    private val productCardView: ProductCardGridView? by lazy {
        viewBinding?.productCard
    }
    private var productCardModel: ProductCardModel? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_carousel_product_card_big_grid
    }

    override fun bind(shopHomeProductUiModel: ShopHomeProductUiModel) {
        setProductData(shopHomeProductUiModel)
        setProductListener(shopHomeProductUiModel)
    }

    private fun setProductListener(shopHomeProductUiModel: ShopHomeProductUiModel) {
        setProductOnImpressionListener(shopHomeProductUiModel)
        setProductOnClickListener(shopHomeProductUiModel)
        setProductAddToCartOnClickListener(shopHomeProductUiModel)
        setProductOnItemAtcNonVariantClickListener(shopHomeProductUiModel)
        setProductOnItemAddVariantClickListener(shopHomeProductUiModel)
    }

    private fun setProductOnImpressionListener(shopHomeProductUiModel: ShopHomeProductUiModel) {
        productCardView?.setImageProductViewHintListener(
            shopHomeProductUiModel,
            object : ViewHintListener {
                override fun onViewHint() {
                    if (!isEtalaseCarousel()) {
                        shopHomeCarouselProductListener.onCarouselProductItemImpression(
                            parentPosition,
                            bindingAdapterPosition,
                            shopHomeCarousellProductUiModel,
                            shopHomeProductUiModel
                        )
                    } else {
                        shopHomeCarouselProductListener.onCarouselProductShowcaseItemImpression(
                            parentPosition,
                            bindingAdapterPosition,
                            shopHomeCarousellProductUiModel,
                            shopHomeProductUiModel
                        )
                    }

                    if (productCardModel?.isButtonAtcShown() == true) {
                        shopHomeCarouselProductListener.onImpressionProductAtc(
                            shopHomeProductUiModel,
                            parentPosition,
                            shopHomeCarousellProductUiModel.name
                        )
                    }
                }

            })
    }

    private fun setProductOnItemAddVariantClickListener(shopHomeProductUiModel: ShopHomeProductUiModel) {
        productCardView?.setAddVariantClickListener {
            shopHomeCarouselProductListener.onProductAtcVariantClick(shopHomeProductUiModel)
        }
    }

    private fun setProductOnItemAtcNonVariantClickListener(shopHomeProductUiModel: ShopHomeProductUiModel) {
        productCardView?.setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
            override fun onQuantityChanged(quantity: Int) {
                shopHomeCarouselProductListener.onProductAtcNonVariantQuantityEditorChanged(
                    shopHomeProductUiModel,
                    quantity,
                    shopHomeCarousellProductUiModel.name
                )
            }
        })
    }

    private fun setProductOnClickListener(shopHomeProductUiModel: ShopHomeProductUiModel) {
        productCardView?.setOnClickListener {
            if (!isEtalaseCarousel()) {
                shopHomeCarouselProductListener.onCarouselProductItemClicked(
                    parentPosition,
                    bindingAdapterPosition,
                    shopHomeCarousellProductUiModel,
                    shopHomeProductUiModel
                )
            } else {
                shopHomeCarouselProductListener.onCarouselProductShowcaseItemClicked(
                    parentPosition,
                    bindingAdapterPosition,
                    shopHomeCarousellProductUiModel,
                    shopHomeProductUiModel
                )
            }
        }
    }

    private fun setProductAddToCartOnClickListener(shopHomeProductUiModel: ShopHomeProductUiModel) {
        productCardView?.setAddToCartOnClickListener {
            if (shopHomeProductUiModel.isEnableDirectPurchase) {
                shopHomeCarouselProductListener.onProductAtcDefaultClick(
                    shopHomeProductUiModel,
                    shopHomeProductUiModel.minimumOrder,
                    shopHomeCarousellProductUiModel.name
                )
            } else {
                if (!isEtalaseCarousel()) {
                    shopHomeCarouselProductListener.onCarouselProductItemClickAddToCart(
                        parentPosition,
                        bindingAdapterPosition,
                        shopHomeCarousellProductUiModel,
                        shopHomeProductUiModel
                    )
                } else {
                    shopHomeCarouselProductListener.onCarouselProductShowcaseItemClickAddToCart(
                        parentPosition,
                        bindingAdapterPosition,
                        shopHomeCarousellProductUiModel,
                        shopHomeProductUiModel
                    )
                }
            }
        }
    }

    private fun setProductData(shopHomeProductUiModel: ShopHomeProductUiModel) {
        productCardModel = ShopPageHomeMapper.mapToProductCardModel(
            isHasAtc(),
            false,
            shopHomeProductUiModel,
            false
        )
        productCardModel?.let {
            viewBinding?.productCard?.setProductModel(it)
        }
    }

    private fun isEtalaseCarousel(): Boolean {
        val etalaseId = shopHomeCarousellProductUiModel.header.etalaseId
        return etalaseId.isNotEmpty() && etalaseId != Int.ZERO.toString()
    }

    private fun isHasAtc(): Boolean {
        return shopHomeCarousellProductUiModel.header.isATC == ShopHomeCarousellProductUiModel.IS_ATC
    }

}
