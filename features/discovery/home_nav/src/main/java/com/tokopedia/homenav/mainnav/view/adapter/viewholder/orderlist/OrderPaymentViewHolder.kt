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
import com.tokopedia.homenav.databinding.HolderTransactionPaymentBinding
import com.tokopedia.homenav.mainnav.view.analytics.TrackingTransactionSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.utils.text.currency.CurrencyFormatHelper
import com.tokopedia.utils.view.binding.viewBinding

class OrderPaymentViewHolder(itemView: View, val mainNavListener: MainNavListener): AbstractViewHolder<OrderPaymentModel>(itemView) {
    private var binding: HolderTransactionPaymentBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_transaction_payment
    }

    override fun bind(element: OrderPaymentModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(paymentModel: OrderPaymentModel) {
        val context = itemView.context

        itemView.addOnImpressionListener(paymentModel)  {
            mainNavListener.putEEToTrackingQueue(
                    TrackingTransactionSection.getImpressionOnOrderStatus(
                        userId = mainNavListener.getUserId(),
                        orderLabel = paymentModel.navPaymentModel.statusText,
                        position = adapterPosition,
                        orderId = paymentModel.navPaymentModel.id)
            )
        }
        //title
        binding?.orderPaymentName?.text = String.format(
                context.getString(R.string.transaction_rupiah_value),
                CurrencyFormatHelper.convertToRupiah(paymentModel.navPaymentModel.paymentAmountText)
        )

        //image
        if (paymentModel.navPaymentModel.imageUrl.isNotEmpty()) {
            val imageView = binding?.orderPaymentImage
            val shimmer = binding?.orderPaymentImageShimmer
            imageView?.scaleType = ImageView.ScaleType.CENTER_INSIDE
            Glide.with(itemView.context)
                    .load(paymentModel.navPaymentModel.imageUrl)
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
        binding?.orderPaymentDescription?.text = paymentModel.navPaymentModel.descriptionText

        //status
        binding?.orderPaymentStatus?.text =
                if (paymentModel.navPaymentModel.statusText.isNotEmpty())
                    paymentModel.navPaymentModel.statusText
                else
                    context.getString(R.string.transaction_item_default_status)

        var paymentStatusColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN500)
        if (paymentModel.navPaymentModel.statusTextColor.isNotEmpty()) {
            paymentStatusColor = Color.parseColor(paymentModel.navPaymentModel.statusTextColor)
        }
        binding?.orderPaymentStatus?.setTextColor(paymentStatusColor)

        itemView.setOnClickListener {
            TrackingTransactionSection.clickOnOrderStatus(
                    mainNavListener.getUserId(),
                    binding?.orderPaymentStatus?.text.toString())
            RouteManager.route(context, if(binding?.orderPaymentStatus?.text == context.getString(R.string.transaction_item_default_status)) ApplinkConst.PMS else paymentModel.navPaymentModel.applink)
        }
    }
}