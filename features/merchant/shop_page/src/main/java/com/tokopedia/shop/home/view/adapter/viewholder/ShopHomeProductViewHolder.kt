package com.tokopedia.shop.home.view.adapter.viewholder

import android.view.View

import androidx.annotation.LayoutRes

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.ProductCardModel
import com.tokopedia.productcard.v2.BlankSpaceConfig
import com.tokopedia.productcard.v2.ProductCardViewSmallGrid
import com.tokopedia.shop.R
import com.tokopedia.shop.home.util.mapper.ShopPageHomeMapper
import com.tokopedia.shop.home.view.listener.ShopPageHomeProductClickListener
import com.tokopedia.shop.home.view.model.ShopHomeProductViewModel

import java.text.NumberFormat
import java.text.ParseException
import java.util.ArrayList

/**
 * @author by alvarisi on 12/12/17.
 */

open class ShopHomeProductViewHolder(
        itemView: View,
        private val shopPageHomeProductClickListener: ShopPageHomeProductClickListener?
) : AbstractViewHolder<ShopHomeProductViewModel>(itemView) {
    lateinit var productCard: ProductCardGridView
    protected var shopHomeProductViewModel: ShopHomeProductViewModel? = null

    init {
        findViews(itemView)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_shop_home_product_card
    }

    private fun findViews(view: View) {
        productCard = view.findViewById(R.id.product_card)
    }

    override fun bind(shopHomeProductViewModel: ShopHomeProductViewModel) {
        this.shopHomeProductViewModel = shopHomeProductViewModel
        productCard.setProductModel(ShopPageHomeMapper.mapToProductCardModel(
                false,
                shopHomeProductViewModel
        ))
        setListener()
    }

    protected open fun setListener() {
        productCard.setOnClickListener {
            shopPageHomeProductClickListener?.onAllProductItemClicked(
                    adapterPosition,
                    shopHomeProductViewModel
            )
        }
        shopHomeProductViewModel?.let {
            productCard.setImageProductViewHintListener(it, object : ViewHintListener {
                override fun onViewHint() {
                    shopPageHomeProductClickListener?.onAllProductItemImpression(
                            adapterPosition,
                            shopHomeProductViewModel
                    )
                }
            })
        }

        productCard.setThreeDotsOnClickListener {
            shopHomeProductViewModel?.let {
                shopPageHomeProductClickListener?.onThreeDotsAllProductClicked(it)
            }
        }
    }
}