package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionEmptyBinding
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderEmptyModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class OrderEmptyViewHolder(itemView: View, val mainNavListener: MainNavListener) :
    AbstractViewHolder<OrderEmptyModel>(itemView) {
    private var binding: HolderTransactionEmptyBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_empty
        private const val EMPTY_IMAGE_LINK =
            "https://images.tokopedia.net/img/android/home_nav/home_nav_empty_transaction.png"
    }

    override fun bind(element: OrderEmptyModel) {
        binding?.cardEmptyTransaction?.cardType = CardUnify2.TYPE_BORDER
        val imageView = binding?.orderEmptyImage
        val shimmer = binding?.orderEmptyImageShimmer
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