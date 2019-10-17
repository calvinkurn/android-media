package com.tokopedia.search.result.shop.presentation.viewholder

import android.graphics.Bitmap
import android.support.annotation.LayoutRes
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.search.R
import com.tokopedia.search.result.shop.presentation.model.ShopViewModel
import com.tokopedia.search.result.presentation.view.listener.ShopListener
import kotlinx.android.synthetic.main.search_result_shop_item_product_card.view.*

class ShopProductItemViewHolder(
    itemView: View,
    private val shopListener: ShopListener?
) : RecyclerView.ViewHolder(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.search_result_shop_item_product_card
    }

    private var roundedImageViewTarget: BitmapImageViewTarget? = null

    fun bind(shopItemProductView: ShopViewModel.ShopItem.ShopItemProduct?) {
        if(shopItemProductView == null) return

        initShopItemProductImage(shopItemProductView)
        initShopItemProductPrice(shopItemProductView)
    }

    private fun initShopItemProductImage(shopItemProductView: ShopViewModel.ShopItem.ShopItemProduct) {
        if(shopItemProductView.imageUrl == "") return

        itemView.imageViewShopItemProductImage?.let { imageViewShopItemProductImage ->

            roundedImageViewTarget = createRoundedImageViewTarget(imageViewShopItemProductImage)

            Glide.with(itemView.context)
                    .load(shopItemProductView.imageUrl)
                    .asBitmap()
                    .centerCrop()
                    .dontAnimate()
                    .into(roundedImageViewTarget)
        }

        itemView.imageViewShopItemProductImage?.setOnClickListener {
            shopListener?.onProductItemClicked(shopItemProductView)
        }
    }

    private fun createRoundedImageViewTarget(imageView: ImageView): BitmapImageViewTarget {
        return object : BitmapImageViewTarget(imageView) {
            override fun setResource(resource: Bitmap) {
                val roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                roundedBitmapDrawable.cornerRadius = 6f
                imageView.setImageDrawable(roundedBitmapDrawable)
            }
        }
    }

    private fun initShopItemProductPrice(shopItemProductView: ShopViewModel.ShopItem.ShopItemProduct) {
        val isShopItemProductPriceVisible = getIsShopItemProductPriceVisible(shopItemProductView)

        itemView.textViewShopItemProductPrice?.visibility = if (isShopItemProductPriceVisible) View.VISIBLE else View.GONE

        if(isShopItemProductPriceVisible) {
            itemView.textViewShopItemProductPrice?.text = MethodChecker.fromHtml(shopItemProductView.priceFormat)
        }
    }

    private fun getIsShopItemProductPriceVisible(shopItemProductView: ShopViewModel.ShopItem.ShopItemProduct): Boolean {
        return shopItemProductView.priceFormat != ""
    }

    fun onViewRecycled() {
        roundedImageViewTarget?.let {
            Glide.clear(it)
        }
    }
}