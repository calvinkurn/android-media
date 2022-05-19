package com.tokopedia.homenav.mainnav.view.adapter.viewholder.wishlist

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderEmptyWishlistBinding
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.EmptyStateWishlistDataModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

class EmptyWishlistViewHolder(itemView: View): AbstractViewHolder<EmptyStateWishlistDataModel>(itemView) {
    private var binding: HolderEmptyWishlistBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_empty_wishlist
        private const val EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home/navigation/home_nav_empty_data.png"
    }

    override fun bind(emptyStateWishlistDataModel: EmptyStateWishlistDataModel) {
        binding?.cardEmptyWishlist?.cardType = CardUnify2.TYPE_BORDER
        val imageView = binding?.wishlistEmptyImage
        val shimmer = binding?.wishlistEmptyImageShimmer
        imageView?.scaleType = ImageView.ScaleType.CENTER_INSIDE
        Glide.with(itemView.context)
            .load(EMPTY_IMAGE_LINK)
            .centerInside()
            .error(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
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
}