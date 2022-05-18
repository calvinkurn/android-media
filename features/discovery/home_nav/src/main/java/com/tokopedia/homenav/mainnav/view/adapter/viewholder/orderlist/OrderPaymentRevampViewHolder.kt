package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionPaymentRevampBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentRevampModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.view.binding.viewBinding

class OrderPaymentRevampViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OrderPaymentRevampModel>(itemView) {
    private var binding: HolderTransactionPaymentRevampBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_payment_revamp
    }

    override fun bind(element: OrderPaymentRevampModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(paymentRevampModel: OrderPaymentRevampModel) {
        val context = itemView.context

        itemView.addOnImpressionListener(paymentRevampModel)  {
            mainNavListener.putEEToTrackingQueue(
                    TrackingTransactionSection.getImpressionOnOrderStatus(
                        userId = mainNavListener.getUserId(),
                        orderLabel = paymentRevampModel.navPaymentModel.statusText,
                        position = adapterPosition,
                        orderId = paymentRevampModel.navPaymentModel.id)
            )
        }
        //title
        binding?.orderPaymentName?.text = String.format(
                context.getString(R.string.transaction_rupiah_value),
                CurrencyFormatHelper.convertToRupiah(paymentRevampModel.navPaymentModel.paymentAmountText)
        )

        //image
        if (paymentRevampModel.navPaymentModel.imageUrl.isNotEmpty()) {
            val imageView = binding?.orderPaymentImage
            val shimmer = binding?.orderPaymentImageShimmer
            imageView?.scaleType = ImageView.ScaleType.CENTER_INSIDE
            Glide.with(itemView.context)
                    .load(paymentRevampModel.navPaymentModel.imageUrl)
                    .centerInside()
                    .error(com.tokopedia.kotlin.extensions.R.drawable.ic_loading_placeholder)
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
        binding?.orderPaymentDescription?.text = paymentRevampModel.navPaymentModel.descriptionText

        //status
        binding?.orderPaymentStatus?.text =
                if (paymentRevampModel.navPaymentModel.statusText.isNotEmpty())
                    paymentRevampModel.navPaymentModel.statusText
                else
                    context.getString(R.string.transaction_item_default_status)

        var paymentStatusColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN500)
        if (paymentRevampModel.navPaymentModel.statusTextColor.isNotEmpty()) {
            paymentStatusColor = Color.parseColor(paymentRevampModel.navPaymentModel.statusTextColor)
        }
        binding?.orderPaymentStatus?.setTextColor(paymentStatusColor)

        binding?.orderPaymentContainer?.setOnClickListener {
            TrackingTransactionSection.clickOnOrderStatus(
                    mainNavListener.getUserId(),
                    binding?.orderPaymentStatus?.text.toString())
            RouteManager.route(context, if(binding?.orderPaymentStatus?.text == context.getString(R.string.transaction_item_default_status)) ApplinkConst.PMS else paymentRevampModel.navPaymentModel.applink)
        }
    }
}