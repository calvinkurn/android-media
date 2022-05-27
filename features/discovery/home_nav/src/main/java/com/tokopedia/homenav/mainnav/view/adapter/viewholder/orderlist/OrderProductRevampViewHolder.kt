package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionProductRevampBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderProductRevampModel
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.utils.view.binding.viewBinding

class OrderProductRevampViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OrderProductRevampModel>(itemView) {
    private var binding: HolderTransactionProductRevampBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_product_revamp
    }

    override fun bind(element: OrderProductRevampModel, payloads: MutableList<Any>) {
        bind(element)
    }

    private fun setLayoutFullWidth(element: OrderProductRevampModel) {
        val layoutParams = binding?.orderProductCard?.layoutParams
        if (element.navProductModel.fullWidth) {
            layoutParams?.width = ViewGroup.LayoutParams.MATCH_PARENT
        } else {
            layoutParams?.width =
                itemView.resources.getDimension(com.tokopedia.homenav.R.dimen.nav_card_me_page_size).toInt()
        }
        binding?.orderProductCard?.layoutParams = layoutParams
    }

    override fun bind(productRevampModel: OrderProductRevampModel) {
        val context = itemView.context
        setLayoutFullWidth(productRevampModel)
        itemView.addOnImpressionListener(productRevampModel)  {
            mainNavListener.putEEToTrackingQueue(
                    TrackingTransactionSection.getImpressionOnOrderStatus(
                        userId = mainNavListener.getUserId(),
                        orderLabel = productRevampModel.navProductModel.statusText,
                        position = adapterPosition,
                        orderId = productRevampModel.navProductModel.id)
            )
        }
        //title
        binding?.orderProductName?.text = productRevampModel.navProductModel.productNameText

        //image
        if (productRevampModel.navProductModel.imageUrl.isNotEmpty()) {
            val imageView = binding?.orderProductImage
            val shimmer = binding?.orderProductImageShimmer
            Glide.with(itemView.context)
                    .load(productRevampModel.navProductModel.imageUrl)
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

        //description
        binding?.orderProductDescription?.text = productRevampModel.navProductModel.estimatedArrival
        if (productRevampModel.navProductModel.descriptionTextColor.isNotEmpty()) {
            binding?.orderProductDescription?.setTextColor(
                   Color.parseColor(productRevampModel.navProductModel.descriptionTextColor)
            )
        }

        //status
        binding?.orderProductStatus?.text = productRevampModel.navProductModel.statusText
        var productStatusColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN500)
        if (productRevampModel.navProductModel.statusTextColor.isNotEmpty()) {
            productStatusColor = Color.parseColor(productRevampModel.navProductModel.statusTextColor)
        }
        binding?.orderProductStatus?.setTextColor(productStatusColor)

        //more than 1 product
        if (productRevampModel.navProductModel.additionalProductCount != 0) {
            binding?.orderProductImageLayer?.visibility = View.VISIBLE
            binding?.orderProductCount?.visibility = View.VISIBLE
            binding?.orderProductCount?.text = String.format(
                    context.getString(R.string.transaction_item_total_product),
                    productRevampModel.navProductModel.additionalProductCount
            )
        } else {
            binding?.orderProductImageLayer?.visibility = View.GONE
            binding?.orderProductCount?.visibility = View.GONE
        }

        binding?.orderProductContainer?.setOnClickListener {
            TrackingTransactionSection.clickOnOrderStatus(
                    mainNavListener.getUserId(),
                    productRevampModel.navProductModel.statusText)
            RouteManager.route(context, productRevampModel.navProductModel.applink)
        }
    }
}