package com.tokopedia.buyerorder.recharge.presentation.adapter.viewholder

import android.graphics.Typeface
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.databinding.ItemOrderDetailRechargePaymentDetailBinding
import com.tokopedia.buyerorder.recharge.presentation.customview.RechargeOrderDetailSimpleView
import com.tokopedia.buyerorder.recharge.presentation.model.RechargeOrderDetailPaymentModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show


/**
 * @author by furqan on 01/11/2021
 */
class RechargeOrderDetailPaymentViewHolder(
        private val binding: ItemOrderDetailRechargePaymentDetailBinding,
        private val listener: RechargeOrderDetailPaymentListener
) : AbstractViewHolder<RechargeOrderDetailPaymentModel>(binding.root) {

    override fun bind(element: RechargeOrderDetailPaymentModel) {
        with(binding) {
            simpleRechargeOrderDetailPaymentMethod.setData(element.paymentMethod)

            if (containerRechargeOrderDetailPaymentDetail.childCount < element.paymentDetails.size) {
                containerRechargeOrderDetailPaymentDetail.removeAllViews()
                for (item in element.paymentDetails) {
                    val simpleView = RechargeOrderDetailSimpleView(root.context)
                    simpleView.setData(item)
                    containerRechargeOrderDetailPaymentDetail.addView(simpleView)
                }
            }

            tgRechargeOrderDetailTotalPriceLabel.text = element.totalPriceLabel
            tgRechargeOrderDetailTotalPrice.text = element.totalPrice

            tickerRechargeOrderDetailPayment.hide()
            element.additionalTicker?.let {
                tickerRechargeOrderDetailPayment.tickerTitle = it.title
                tickerRechargeOrderDetailPayment.setHtmlDescription(it.text)
                tickerRechargeOrderDetailPayment.show()
            }

            element.paymentInfoMessage?.let { paymentInfo ->
                if (paymentInfo.message.isNotEmpty()) {
                    tgRechargePaymentInfo.show()
                    icRechargePaymentInfo.show()
                    tgRechargePaymentInfo.text = String.format(
                        getString(R.string.payment_info_string_combine),
                        paymentInfo.message,
                        paymentInfo.urlText
                    )
                    tgRechargePaymentInfo.makeLinks(Pair(paymentInfo.urlText, View.OnClickListener {
                        listener.onClickTnC(paymentInfo.appLink)
                    }))

                    tgRechargePaymentInfo.setOnClickListener {
                        listener.onClickTnC(paymentInfo.appLink)
                    }
                } else {
                    tgRechargePaymentInfo.hide()
                    icRechargePaymentInfo.hide()
                }
            }
        }
    }

    fun TextView.makeLinks(vararg links: Pair<String, View.OnClickListener>) {
        val spannableString = SpannableString(this.text)
        for (link in links) {
            val clickableSpan = object : ClickableSpan() {
                override fun onClick(view: View) {
                    Selection.setSelection((view as TextView).text as Spannable, 0)
                    view.invalidate()
                    link.second.onClick(view)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.setTypeface(Typeface.DEFAULT_BOLD)
                    ds.isUnderlineText = false
                }
            }
            val startIndexOfLink = this.text.toString().indexOf(link.first)
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
        this.movementMethod = LinkMovementMethod.getInstance()
        this.setText(spannableString, TextView.BufferType.SPANNABLE)
    }

    companion object {
        val LAYOUT = R.layout.item_order_detail_recharge_payment_detail
    }

    interface RechargeOrderDetailPaymentListener{
        fun onClickTnC(applink: String)
    }

}
