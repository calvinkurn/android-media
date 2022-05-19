package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderEmptyNonLoggedInBinding
import com.tokopedia.homenav.mainnav.view.datamodel.EmptyStateNonLoggedInDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.EmptyStateWishlistDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.wishlist.OtherWishlistModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.view.binding.viewBinding

class EmptyNonLoggedInViewHolder(itemView: View): AbstractViewHolder<EmptyStateNonLoggedInDataModel>(itemView) {
    private var binding: HolderEmptyNonLoggedInBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_empty_non_logged_in
        private const val EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home/navigation/home_nav_empty_data.png"
    }

    override fun bind(emptyStateNonLoggedInDataModel: EmptyStateNonLoggedInDataModel) {
        val imageView = binding?.nonLoggedInEmptyImage
        val shimmer = binding?.nonLoggedInEmptyImageShimmer
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