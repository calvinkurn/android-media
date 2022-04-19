package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardListView
import com.tokopedia.shop.R
import com.tokopedia.shop.databinding.ItemShopHomeProductCardListBinding
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopHomeEndlessProductListener
import com.tokopedia.shop.home.view.model.ShopHomeProductUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * @author by alvarisi on 12/12/17.
 */

open class ShopHomeProductItemListViewHolder(
        itemView: View,
        private val shopHomeEndlessProductListener: ShopHomeEndlessProductListener?,
        private val isShowTripleDot: Boolean
) : AbstractViewHolder<ShopHomeProductUiModel>(itemView) {
    private val viewBinding: ItemShopHomeProductCardListBinding? by viewBinding()
    private var productCard: ProductCardListView? = null
    protected var shopHomeProductViewModel: ShopHomeProductUiModel? = null

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
        productCard?.setProductModel(ShopPageHomeMapper.mapToProductCardModel(
                isHasAddToCartButton = false,
                hasThreeDots = isShowTripleDot,
                shopHomeProductViewModel = shopHomeProductViewModel,
                isWideContent = false
        ))
        setListener()
    }

    protected open fun setListener() {
        productCard?.setOnClickListener {
            shopHomeEndlessProductListener?.onAllProductItemClicked(
                    adapterPosition,
                    shopHomeProductViewModel
            )
        }
        shopHomeProductViewModel?.let {
            productCard?.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    shopHomeEndlessProductListener?.onAllProductItemImpression(
                            adapterPosition,
                            shopHomeProductViewModel
                    )
                }
            })
        }

        productCard?.setThreeDotsOnClickListener {
            shopHomeProductViewModel?.let {
                shopHomeEndlessProductListener?.onThreeDotsAllProductClicked(it)
            }
        }
    }
}