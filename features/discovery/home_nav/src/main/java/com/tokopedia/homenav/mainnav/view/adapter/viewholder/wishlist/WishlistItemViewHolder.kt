package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderWishlistBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.*
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
            val imageView = binding?.imageWishlist
            val shimmer = binding?.imageWishlistShimmer
            Glide.with(itemView.context)
                .load(wishlistModel.navWishlistModel.imageUrl)
                .placeholder(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
                .error(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
                .dontAnimate()
                .fitCenter()
                .into(object : CustomTarget<Drawable>() {
                    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                        imageView?.setImageDrawable(resource)
                        shimmer?.gone()
                    }

                    override fun onLoadStarted(placeholder: Drawable?) {
                        shimmer?.visible()
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                        shimmer?.gone()
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        shimmer?.gone()
                    }
                })
        }
        binding?.textPriceValue?.text = wishlistModel.navWishlistModel.priceFmt
        when {
            wishlistModel.navWishlistModel.discountPercentageFmt.isNotEmpty() -> {
                binding?.textSlashedPrice?.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = wishlistModel.navWishlistModel.originalPriceFmt
                    show()
                }
                binding?.labelDiscountPercent?.apply {
                    text = wishlistModel.navWishlistModel.discountPercentageFmt
                    show()
                }
                binding?.labelCashback?.invisible()
            }
            wishlistModel.navWishlistModel.cashback -> {
                binding?.textSlashedPrice?.invisible()
                binding?.labelDiscountPercent?.invisible()
                binding?.labelCashback?.visible()
            }
            else -> {
                binding?.textSlashedPrice?.invisible()
                binding?.labelDiscountPercent?.invisible()
                binding?.labelCashback?.invisible()
            }
        }

        binding?.containerWishlistItem?.setOnClickListener {
            mainNavListener.onWishlistItemClicked(wishlistModel.navWishlistModel, adapterPosition)
        }
    }
}