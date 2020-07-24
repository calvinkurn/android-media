package com.tokopedia.oneclickcheckout.order.view.card

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.model.ButtonBayarState
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.purchase_platform.common.utils.Utils
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderTotalPaymentCard(private val view: View, private val listener: OrderTotalPaymentCardListener) {

    private val layoutPayment by lazy { view.findViewById<View>(R.id.layout_payment) }
    private val tvTotalPaymentValue by lazy { view.findViewById<Typography>(R.id.tv_total_payment_value) }
    private val btnOrderDetail by lazy { view.findViewById<ImageView>(R.id.btn_order_detail) }
    private val btnPay by lazy { view.findViewById<UnifyButton>(R.id.btn_pay) }
    private val tickerPaymentError by lazy { view.findViewById<Ticker>(R.id.ticker_payment_error) }

    fun setPaymentVisible(isVisible: Boolean) {
        layoutPayment?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setupPayment(orderTotal: OrderTotal) {
        setupPaymentError(orderTotal.paymentErrorMessage)
        setupButtonBayar(orderTotal)
    }

    private fun setupButtonBayar(orderTotal: OrderTotal) {
        view.context?.let { context ->
            if (orderTotal.isButtonChoosePayment) {
                when (orderTotal.buttonState) {
                    ButtonBayarState.NORMAL -> {
                        btnPay?.setText(com.tokopedia.purchase_platform.common.R.string.label_choose_payment)
                        btnPay?.layoutParams?.width = Utils.convertDpToPixel(160f, context)
                        btnPay?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        btnPay?.isLoading = false
                        btnPay?.isEnabled = true
                        btnPay?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        btnPay?.setText(com.tokopedia.purchase_platform.common.R.string.label_choose_payment)
                    }
                    ButtonBayarState.DISABLE -> {
                        btnPay?.setText(com.tokopedia.purchase_platform.common.R.string.label_choose_payment)
                        btnPay?.layoutParams?.width = Utils.convertDpToPixel(160f, context)
                        btnPay?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        btnPay?.isLoading = false
                        btnPay?.isEnabled = false
                        btnPay?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        btnPay?.setText(com.tokopedia.purchase_platform.common.R.string.label_choose_payment)
                    }
                    else -> {
                        btnPay?.layoutParams?.width = Utils.convertDpToPixel(160f, context)
                        btnPay?.layoutParams?.height = Utils.convertDpToPixel(48f, context)
                        btnPay?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        btnPay?.isLoading = true
                    }
                }
            } else {
                when (orderTotal.buttonState) {
                    ButtonBayarState.NORMAL -> {
                        btnPay?.setText(R.string.pay)
                        btnPay?.layoutParams?.width = Utils.convertDpToPixel(140f, context)
                        btnPay?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        btnPay?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
                        btnPay?.isLoading = false
                        btnPay?.isEnabled = true
                        btnPay?.setText(R.string.pay)
                    }
                    ButtonBayarState.DISABLE -> {
                        btnPay?.setText(R.string.pay)
                        btnPay?.layoutParams?.width = Utils.convertDpToPixel(140f, context)
                        btnPay?.layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                        btnPay?.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_btn_pay_shield, 0, 0, 0)
                        btnPay?.isLoading = false
                        btnPay?.isEnabled = false
                        btnPay?.setText(R.string.pay)
                    }
                    else -> {
                        btnPay?.layoutParams?.width = Utils.convertDpToPixel(140f, context)
                        btnPay?.layoutParams?.height = Utils.convertDpToPixel(48f, context)
                        btnPay?.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        btnPay?.isLoading = true
                    }
                }
            }

            if (orderTotal.orderCost.totalPrice > 0.0) {
                tvTotalPaymentValue?.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderTotal.orderCost.totalPrice, false).removeDecimalSuffix()
            } else {
                tvTotalPaymentValue?.text = "-"
            }

            btnOrderDetail?.setOnClickListener {
                if (orderTotal.orderCost.totalPrice > 0.0) {
                    listener.onOrderDetailClicked(orderTotal.orderCost)
                }
            }

            btnPay?.setOnClickListener {
                listener.onPayClicked()
            }
        }
    }

    private fun setupPaymentError(paymentErrorMessage: String?) {
        if (paymentErrorMessage.isNullOrEmpty()) {
            tickerPaymentError?.gone()
        } else {
            tickerPaymentError?.setTextDescription(paymentErrorMessage)
            tickerPaymentError?.visible()
        }
    }

    interface OrderTotalPaymentCardListener {

        fun onOrderDetailClicked(orderCost: OrderCost)

        fun onPayClicked()
    }
}