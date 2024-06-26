package com.tokopedia.homenav.mainnav.view.adapter.viewholder.orderlist

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.homenav.R
import com.tokopedia.homenav.databinding.HolderTransactionPaymentRevampBinding
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.datamodel.orderlist.OrderPaymentRevampModel
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.unifycomponents.CardUnify2
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
            mainNavListener.onOrderCardImpressed(
                paymentRevampModel.navPaymentModel.statusText,
                paymentRevampModel.navPaymentModel.id,
                paymentRevampModel.position
            )
        }

        binding?.orderPaymentCard?.animateOnPress = CardUnify2.ANIMATE_OVERLAY

        //title
        binding?.orderPaymentName?.text = String.format(
                context.getString(R.string.transaction_rupiah_value),
                CurrencyFormatHelper.convertToRupiah(paymentRevampModel.navPaymentModel.paymentAmountText)
        )

        //image
        if (paymentRevampModel.navPaymentModel.imageUrl.isNotEmpty()) {
            binding?.orderPaymentImage?.setImageUrl(paymentRevampModel.navPaymentModel.imageUrl)
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
            try {
                paymentStatusColor = Color.parseColor(paymentRevampModel.navPaymentModel.statusTextColor)
            } catch (_: Exception) { }
        }
        binding?.orderPaymentStatus?.setTextColor(paymentStatusColor)

        binding?.orderPaymentContainer?.setOnClickListener {
            val applink = if(binding?.orderPaymentStatus?.text == context.getString(R.string.transaction_item_default_status))
                ApplinkConst.PMS
            else paymentRevampModel.navPaymentModel.applink
            mainNavListener.onOrderCardClicked(
                applink,
                binding?.orderPaymentStatus?.text.toString(),
                paymentRevampModel.navPaymentModel.id,
                paymentRevampModel.position
            )
        }
    }
}
