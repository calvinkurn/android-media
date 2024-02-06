package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.experiments.ProductCardCustomColor
import com.tokopedia.shop.R
import com.tokopedia.shop.common.constant.ShopPageConstant
import com.tokopedia.shop.common.util.ShopUtilExt.isButtonAtcShown
import com.tokopedia.shop.databinding.ItemShopHomeProductCardSmallGridBinding
import com.tokopedia.shop.home.util.DarkThemedShopColor
import com.tokopedia.shop.home.util.LightThemedShopColor
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeEndlessProductListener
import com.tokopedia.shop.home.view.listener.ShopHomeListener
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.shop.pageheader.presentation.uimodel.ShopPageHeaderLayoutUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */

open class ShopHomeProductViewHolder(
    itemView: View,
    private val shopHomeEndlessProductListener: ShopHomeEndlessProductListener?,
    private val isShowTripleDot: Boolean,
    private val shopHomeListener: ShopHomeListener
) : AbstractViewHolder<ShopHomeProductUiModel>(itemView) {
    private val viewBinding: ItemShopHomeProductCardSmallGridBinding? by viewBinding()
    private var productCard: ProductCardGridView? = null
    protected var shopHomeProductViewModel: ShopHomeProductUiModel? = null

    init {
        findViews()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_card_small_grid
    }

    private fun findViews() {
        productCard = viewBinding?.productCard
    }

    override fun bind(shopHomeProductViewModel: ShopHomeProductUiModel) {
        this.shopHomeProductViewModel = shopHomeProductViewModel
       
        val shopProductCardColor = buildShopProductCardColor()
        val productCardModel = ShopPageHomeMapper.mapToProductCardModel(
            isHasAddToCartButton = false,
            hasThreeDots = isShowTripleDot,
            shopHomeProductViewModel = shopHomeProductViewModel,
            isWideContent = false,
            productRating = shopHomeProductViewModel.averageRating,
            forceLightModeColor = shopHomeListener.isOverrideTheme(),
        ).copy(productCardCustomColor = shopProductCardColor)
        
        productCard?.setProductModel(productCardModel)
        setListener(productCardModel)
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

    private fun buildShopProductCardColor(): ProductCardCustomColor? {
        val context = productCard?.context ?: return null
        
        return if (shopHomeListener.isOverrideTheme()) {
            //Reimagine enabled-shop
            val isLightThemedShop = shopHomeListener.getPatternColorType() == ShopPageHeaderLayoutUiModel.ColorType.LIGHT.value
            
            if (isLightThemedShop) {
                LightThemedShopColor(
                    cardBackgroundColor = MethodChecker.getColor(context, android.R.color.transparent),
                    productNameColor = ContextCompat.getColor(context, R.color.dms_static_light_NN950_96),
                    productPriceColor = ContextCompat.getColor(context, R.color.dms_static_light_NN950_96),
                    productSlashPriceColor = ContextCompat.getColor(context, R.color.dms_static_light_NN950_44),
                    productSoldCountColor = ContextCompat.getColor(context, R.color.dms_static_light_NN950_68),
                    productDiscountColor = ContextCompat.getColor(context, R.color.dms_static_light_RN500),
                    productRatingColor = ContextCompat.getColor(context, R.color.dms_static_light_NN950_68)
                )
            } else {
                DarkThemedShopColor(
                    cardBackgroundColor = MethodChecker.getColor(context, android.R.color.transparent),
                    productNameColor = ContextCompat.getColor(context, R.color.dms_static_dark_NN950_96),
                    productPriceColor = ContextCompat.getColor(context, R.color.dms_static_dark_NN950_96),
                    productSlashPriceColor = ContextCompat.getColor(context, R.color.dms_static_dark_NN950_44),
                    productSoldCountColor = ContextCompat.getColor(context, R.color.dms_static_dark_NN950_68),
                    productDiscountColor = ContextCompat.getColor(context, R.color.dms_static_dark_RN500),
                    productRatingColor = ContextCompat.getColor(context, R.color.dms_static_dark_NN950_68)
                )
            }
        } else {
            //Non reimagine shop
            null
        }
    }
}
