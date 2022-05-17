package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderWishlistBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.utils.view.binding.viewBinding

class WishlistItemViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<WishlistModel>(itemView) {
    private var binding: HolderWishlistBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_wishlist
    }

    override fun bind(element: WishlistModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(wishlistModel: WishlistModel) {
        val context = itemView.context

        itemView.addOnImpressionListener(wishlistModel) {
            mainNavListener.putEEToTrackingQueue(
                TrackingTransactionSection.getImpressionOnWishlist(
                    userId = mainNavListener.getUserId(),
                    position = adapterPosition,
                    wishlistModel = wishlistModel.navWishlistModel
                )
            )
        }

        binding?.textProductName?.text = wishlistModel.navWishlistModel.productName

        if (wishlistModel.navWishlistModel.imageUrl.isNotEmpty()) {
            binding?.imageWishlist?.loadImage(wishlistModel.navWishlistModel.imageUrl)
        }
        binding?.textPriceValue?.text = wishlistModel.navWishlistModel.priceFmt
        if(wishlistModel.navWishlistModel.discountPercentageFmt.isNotEmpty()){
            binding?.textSlashedPrice?.apply {
                text = wishlistModel.navWishlistModel.originalPriceFmt
                show()
            }
            binding?.textDiscountPercent?.apply {
                text = wishlistModel.navWishlistModel.discountPercentageFmt
                show()
            }
        } else{
            binding?.textSlashedPrice?.invisible()
            binding?.textDiscountPercent?.invisible()
        }

        binding?.containerWishlistItem?.setOnClickListener {
            mainNavListener.onWishlistItemClicked(wishlistModel.navWishlistModel, adapterPosition)
        }
    }
}