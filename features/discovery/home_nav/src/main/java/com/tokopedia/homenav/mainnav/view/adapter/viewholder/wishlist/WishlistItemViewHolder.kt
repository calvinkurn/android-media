package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

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
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
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

        binding?.textName?.text = element.navWishlistModel.name

        if (element.navWishlistModel.images.isEmpty()) {
            binding?.imageWishlist?.gone()
            binding?.imageWishlist2?.gone()
        } else {
            binding?.imageWishlist?.visible()
            binding?.imageWishlist?.setImageUrl(element.navWishlistModel.images[0])
            if(element.navWishlistModel.images.size > 1) {
                binding?.imageWishlist2?.apply {
                    visible()
                    setImageUrl(element.navWishlistModel.images[1])
                }
            } else {
                binding?.imageWishlist2?.gone()
            }
        }

        binding?.textDescription?.text = itemView.context.getString(
            com.tokopedia.homenav.R.string.wishlist_item_count_format
        ).format(element.navWishlistModel.totalItem)

        binding?.containerWishlistItem?.setOnClickListener {
            mainNavListener.onWishlistCollectionClicked(element.navWishlistModel, adapterPosition)
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
