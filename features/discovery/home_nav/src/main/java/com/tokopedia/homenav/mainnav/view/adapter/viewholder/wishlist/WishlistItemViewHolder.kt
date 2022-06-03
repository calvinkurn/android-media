package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.graphics.Paint
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderWishlistBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.WishlistModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.utils.view.binding.viewBinding

class WishlistItemViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<WishlistModel>(itemView) {
    private var binding: HolderWishlistBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_wishlist
    }

    override fun bind(element: WishlistModel) {
        setLayoutFullWidth(element)

        itemView.addOnImpressionListener(element) {
            mainNavListener.putEEToTrackingQueue(
                TrackingTransactionSection.getImpressionOnWishlist(
                    userId = mainNavListener.getUserId(),
                    position = adapterPosition,
                    wishlistModel = element.navWishlistModel
                )
            )
        }

        binding?.textProductName?.text = element.navWishlistModel.productName

        if (element.navWishlistModel.imageUrl.isNotEmpty()) {
            val imageView = binding?.imageWishlist
            imageView?.setImageUrl(element.navWishlistModel.imageUrl)
        }
        binding?.textPriceValue?.text = element.navWishlistModel.priceFmt
        setLabel(element)

        binding?.containerWishlistItem?.setOnClickListener {
            mainNavListener.onWishlistItemClicked(element.navWishlistModel, adapterPosition)
        }
    }

    private fun setLabel(element: WishlistModel){
        when {
            element.navWishlistModel.discountPercentageFmt.isNotEmpty() -> {
                binding?.textSlashedPrice?.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                    text = element.navWishlistModel.originalPriceFmt
                    show()
                }
                binding?.labelDiscountPercent?.apply {
                    text = element.navWishlistModel.discountPercentageFmt
                    show()
                }
                binding?.labelCashback?.invisible()
            }
            element.navWishlistModel.cashback -> {
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
    }

    private fun setLayoutFullWidth(element: WishlistModel) {
        val layoutParams = binding?.cardWishlist?.layoutParams
        if (element.navWishlistModel.fullWidth) {
            layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            layoutParams?.width = itemView.resources.getDimension(R.dimen.nav_card_me_page_size).toInt()
        }
        binding?.cardWishlist?.layoutParams = layoutParams
    }
}