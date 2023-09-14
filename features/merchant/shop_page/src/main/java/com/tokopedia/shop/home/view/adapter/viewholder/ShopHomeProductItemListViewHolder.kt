package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.databinding.ItemShopHomeProductCardListBinding
import com.tokopedia.shop.home.util.ProductCardColorOverrider
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeEndlessProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */

open class ShopHomeProductItemListViewHolder(
    itemView: View,
    private val shopHomeEndlessProductListener: ShopHomeEndlessProductListener?,
    private val isShowTripleDot: Boolean,
    shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomeProductUiModel>(itemView) {
    private val viewBinding: ItemShopHomeProductCardListBinding? by viewBinding()
    private var productCard: ProductCardListView? = null
    protected var shopHomeProductViewModel: ShopHomeProductUiModel? = null
    private val colorOverrider = ProductCardColorOverrider()

    init {
        findViews()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_card_list
    }

    private fun findViews() {
        productCard = viewBinding?.productCardView
    }

    override fun bind(shopHomeProductViewModel: ShopHomeProductUiModel) {
        this.shopHomeProductViewModel = shopHomeProductViewModel
        val productCardModel = ShopPageHomeMapper.mapToProductCardModel(
            isHasAddToCartButton = false,
            hasThreeDots = isShowTripleDot,
            shopHomeProductViewModel = shopHomeProductViewModel,
            isWideContent = false,
            productRating = shopHomeProductViewModel.averageRating
        )
        productCard?.setProductModel(productCardModel)
        setListener(productCardModel)
        colorOverrider.forceLightModeColor(productCard)
    }

    protected open fun setListener(productCardModel: ProductCardModel) {
        productCard?.setOnClickListener {
            shopHomeEndlessProductListener?.onAllProductItemClicked(
                adapterPosition,
                shopHomeProductViewModel
            )
        }
        shopHomeProductViewModel?.let { shopHomeProductViewModel ->
            productCard?.setImageProductViewHintListener(
                shopHomeProductViewModel,
                object : ViewHintListener {
                    override fun onViewHint() {
                        shopHomeEndlessProductListener?.onAllProductItemImpression(
                            adapterPosition,
                            shopHomeProductViewModel
                        )
                        if (productCardModel.isButtonAtcShown()) {
                            shopHomeEndlessProductListener?.onImpressionProductAtc(
                                shopHomeProductViewModel,
                                adapterPosition,
                                ShopPageConstant.ShopProductCardAtc.CARD_HOME
                            )
                        }
                    }
                }
            )

            productCard?.setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    shopHomeEndlessProductListener?.onProductAtcNonVariantQuantityEditorChanged(
                        shopHomeProductViewModel,
                        quantity,
                        ShopPageConstant.ShopProductCardAtc.CARD_HOME
                    )
                }
            })

            productCard?.setAddVariantClickListener {
                shopHomeEndlessProductListener?.onProductAtcVariantClick(
                    shopHomeProductViewModel
                )
            }

            productCard?.setAddToCartOnClickListener {
                shopHomeEndlessProductListener?.onProductAtcDefaultClick(
                    shopHomeProductViewModel,
                    shopHomeProductViewModel.minimumOrder,
                    ShopPageConstant.ShopProductCardAtc.CARD_HOME
                )
            }
        }

        productCard?.setThreeDotsOnClickListener {
            shopHomeProductViewModel?.let {
                shopHomeEndlessProductListener?.onThreeDotsAllProductClicked(it)
            }
        }
    }
}
