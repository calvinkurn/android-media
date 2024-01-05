package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

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
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionPaymentBinding
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
            mainNavListener.onOrderCardImpressed(
                paymentModel.navPaymentModel.statusText,
                paymentModel.navPaymentModel.id,
                paymentModel.position
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
                    .error(com.tokopedia.utils.R.drawable.ic_loading_placeholder)
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

        binding?.orderPaymentStatus?.setTextColor(
                ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_YN400)
        )

        itemView.setOnClickListener {
            val applink = if(binding?.orderPaymentStatus?.text == context.getString(R.string.transaction_item_default_status))
                ApplinkConst.PMS
            else paymentModel.navPaymentModel.applink
            mainNavListener.onOrderCardClicked(
                applink,
                binding?.orderPaymentStatus?.text.toString(),
                paymentModel.navPaymentModel.id,
                paymentModel.position
            )
        }
    }
}
