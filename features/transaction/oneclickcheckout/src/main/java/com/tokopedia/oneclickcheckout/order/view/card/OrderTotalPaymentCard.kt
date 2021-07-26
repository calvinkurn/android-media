package com.tokopedia.oneclickcheckout.order.view.card

import android.view.View
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContextCompat
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonType
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderTotalPaymentCard(private val view: View, private val listener: OrderTotalPaymentCardListener) {

    private val layoutPayment by lazy { view.findViewById<View>(R.id.layout_payment) }
    private val tvTotalPaymentValue by lazy { view.findViewById<Typography>(R.id.tv_total_payment_value) }
    private val btnOrderDetail by lazy { view.findViewById<IconUnify>(R.id.btn_order_detail) }
    private val btnPay by lazy { view.findViewById<UnifyButton>(R.id.btn_pay) }
    private val tickerPaymentError by lazy { view.findViewById<Ticker>(R.id.ticker_payment_error) }
    private val groupPayment by lazy { view.findViewById<Group>(R.id.group_payment) }
    private val groupLoaderPayment by lazy { view.findViewById<Group>(R.id.group_loader_payment) }

    fun setPaymentVisible(isVisible: Boolean) {
        layoutPayment?.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setupPayment(orderTotal: OrderTotal) {
        setupPaymentError(orderTotal.paymentErrorMessage)
        setupButtonBayar(orderTotal)
    }

    private fun setupButtonBayar(orderTotal: OrderTotal) {
        view.context?.let { context ->
            btnPay?.apply {
                when (orderTotal.buttonType) {
                    OccButtonType.CHOOSE_PAYMENT -> {
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        when (orderTotal.buttonState) {
                            OccButtonState.NORMAL -> {
                                isEnabled = true
                                    setText(R.string.change_payment_method)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            OccButtonState.DISABLE -> {
                                isEnabled = false
                                    setText(R.string.change_payment_method)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            else -> {
                                groupPayment?.gone()
                                groupLoaderPayment?.visible()
                            }
                        }
                    }
                    OccButtonType.PAY -> {
                        when (orderTotal.buttonState) {
                            OccButtonState.NORMAL -> {
                                val drawable = getIconUnifyDrawable(context, IconUnify.PROTECTION_CHECK, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_White))
                                drawable?.setBounds(ICON_BUTTON_LEFT_BOUND.toPx(), ICON_BUTTON_TOP_BOUND, ICON_BUTTON_RIGHT_BOUND.toPx(), ICON_BUTTON_BOTTOM_BOUND.toPx())
                                setCompoundDrawables(drawable, null, null, null)
                                compoundDrawablePadding = ICON_BUTTON_PADDING.toPx()
                                isEnabled = true
                                setText(R.string.pay)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            OccButtonState.DISABLE -> {
                                val drawable = getIconUnifyDrawable(context, IconUnify.PROTECTION_CHECK)
                                drawable?.setBounds(ICON_BUTTON_LEFT_BOUND.toPx(), ICON_BUTTON_TOP_BOUND, ICON_BUTTON_RIGHT_BOUND.toPx(), ICON_BUTTON_BOTTOM_BOUND.toPx())
                                setCompoundDrawables(drawable, null, null, null)
                                compoundDrawablePadding = ICON_BUTTON_PADDING.toPx()
                                isEnabled = false
                                setText(R.string.pay)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            else -> {
                                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                                groupPayment?.gone()
                                groupLoaderPayment?.visible()
                            }
                        }
                    }
                    OccButtonType.CONTINUE -> {
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        when (orderTotal.buttonState) {
                            OccButtonState.NORMAL -> {
                                isEnabled = true
                                setText(R.string.continue_pay)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            OccButtonState.DISABLE -> {
                                isEnabled = false
                                setText(R.string.continue_pay)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            else -> {
                                groupPayment?.gone()
                                groupLoaderPayment?.visible()
                            }
                        }
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

    companion object {
        private const val ICON_BUTTON_LEFT_BOUND = 24
        private const val ICON_BUTTON_TOP_BOUND = 0
        private const val ICON_BUTTON_RIGHT_BOUND = 44
        private const val ICON_BUTTON_BOTTOM_BOUND = 20
        private const val ICON_BUTTON_PADDING = 4
    }
}