package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.UriUtil
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionReviewBinding
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderReviewModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by dhaba
 */
class OrderReviewViewHolder(itemView: View, val mainNavListener: MainNavListener) :
    AbstractViewHolder<OrderReviewModel>(itemView) {
    private var binding: HolderTransactionReviewBinding? by viewBinding()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_review
        private const val UTM_SOURCE_GLOBAL_NAV = "global_nav"
        private const val SOURCE = "source"
        private const val RATING = "rating"
    }

    override fun bind(element: OrderReviewModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(element: OrderReviewModel) {
        binding?.orderReviewProductName?.text = element.navReviewModel.productName

        if (element.navReviewModel.imageUrl.isNotEmpty()) {
            val imageView = binding?.orderReviewImage
            val shimmer = binding?.orderReviewImageShimmer
            imageView?.scaleType = ImageView.ScaleType.CENTER_INSIDE
            Glide.with(itemView.context)
                .load(element.navReviewModel.imageUrl)
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

        binding?.orderReviewContainer?.setOnClickListener {
            mainNavListener.showReviewProduct(generateUriShowBottomSheetReview(element))
        }
    }

    private fun generateUriShowBottomSheetReview(element: OrderReviewModel) : String {
        return Uri.parse(
            UriUtil.buildUri(
                ApplinkConstInternalMarketplace.CREATE_REVIEW,
                element.navReviewModel.reputationId,
                element.navReviewModel.productId
            )
        )
            .buildUpon()
            .appendQueryParameter(RATING, "3")
            .appendQueryParameter(
                SOURCE,
                UTM_SOURCE_GLOBAL_NAV
            )
            .build()
            .toString()
    }
}