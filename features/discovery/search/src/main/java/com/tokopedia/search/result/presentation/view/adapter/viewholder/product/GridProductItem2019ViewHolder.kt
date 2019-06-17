package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.support.annotation.LayoutRes
import android.text.TextUtils
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.ProductItemViewModel
import com.tokopedia.search.result.presentation.view.listener.ProductListener
import kotlinx.android.synthetic.main.search_srp_item_grid_2019.view.*

class GridProductItem2019ViewHolder(
    itemView: View,
    protected val productListener: ProductListener
) : AbstractViewHolder<ProductItemViewModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_srp_item_grid_2019
    }

    protected val context = itemView.context!!

    override fun bind(productItem: ProductItemViewModel?) {
        if(productItem == null) return

        initProductCardContainer(productItem)
        initProductImage(productItem)
        initWishlistButtonContainer(productItem)
        initWishlistButton(productItem)
        initTitleTextView(productItem)
        initPriceTextView(productItem)
        initLocationTextView(productItem)
    }

    private fun initProductCardContainer(productItem: ProductItemViewModel) {
        itemView.productCardContainer.setOnLongClickListener {
            productListener.onLongClick(productItem, adapterPosition)
            true
        }

        itemView.productCardContainer.setOnClickListener {
            productListener.onItemClicked(productItem, adapterPosition)
        }
    }

    private fun initProductImage(productItem: ProductItemViewModel) {
        itemView.productImage.setViewHintListener(productItem) {
            productListener.onProductImpressed(productItem, adapterPosition)
        }

        ImageHandler.loadImageThumbs(context, itemView.productImage, productItem.imageUrl)
    }

    private fun initWishlistButtonContainer(productItem: ProductItemViewModel) {
        itemView.wishlistButtonContainer.isEnabled = productItem.isWishlistButtonEnabled

        itemView.wishlistButtonContainer.setOnClickListener {
            if(productItem.isWishlistButtonEnabled) {
                productListener.onWishlistButtonClicked(productItem)
            }
        }
    }

    private fun initWishlistButton(productItem: ProductItemViewModel) {
        if(productItem.isWishlisted) {
            itemView.wishlistButton.setBackgroundResource(R.drawable.search_ic_wishlist_red)
        }
        else {
            itemView.wishlistButton.setBackgroundResource(R.drawable.search_ic_wishlist)
        }
    }

    private fun initTitleTextView(productItem: ProductItemViewModel) {
        itemView.titleTextView.text = MethodChecker.fromHtml(productItem.productName)
    }

    private fun initPriceTextView(productItem: ProductItemViewModel) {
        itemView.priceTextView.text = getPriceText(productItem)
    }

    private fun getPriceText(productItem: ProductItemViewModel) : CharSequence {
        return if(!TextUtils.isEmpty(productItem.priceRange)) productItem.priceRange
        else productItem.price
    }

    private fun initLocationTextView(productItem: ProductItemViewModel) {
        if(!TextUtils.isEmpty(productItem.shopCity)) {
            itemView.locationTextView.text = getLocationText(productItem)
            itemView.locationTextView.visibility = View.VISIBLE
        }
        else {
            itemView.locationTextView.visibility = View.GONE
        }
    }

    private fun getLocationText(productItem: ProductItemViewModel) : CharSequence {
        return ( if(isBadgesExist(productItem)) " \u2022 " else "" ) + MethodChecker.fromHtml(productItem.shopCity)
    }

    private fun isBadgesExist(productItem: ProductItemViewModel): Boolean {
        val badgesList = productItem.badgesList

        if (badgesList == null || badgesList.isEmpty()) {
            return false
        }

        for (badgeItem in badgesList) {
            if (badgeItem.isShown) {
                return true
            }
        }
        return false
    }
}