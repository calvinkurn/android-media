package com.tokopedia.oneclickcheckout.order.view.card

import android.view.View
import android.view.ViewGroup
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
import com.tokopedia.purchase_platform.common.utils.Utils
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

    fun setupPayment(orderTotal: OrderTotal, isNewFlow: Boolean) {
        setupPaymentError(orderTotal.paymentErrorMessage)
        setupButtonBayar(orderTotal, isNewFlow)
    }

    private fun setupButtonBayar(orderTotal: OrderTotal, isNewFlow: Boolean) {
        view.context?.let { context ->
            btnPay?.apply {
                when (orderTotal.buttonType) {
                    OccButtonType.CHOOSE_PAYMENT -> {
                        layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        when (orderTotal.buttonState) {
                            OccButtonState.NORMAL -> {
                                layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                isEnabled = true
//                                isLoading = false
                                if (isNewFlow) {
                                    setText(R.string.change_payment_method)
                                } else {
                                    setText(com.tokopedia.purchase_platform.common.R.string.label_choose_payment)
                                }
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            OccButtonState.DISABLE -> {
                                layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                isEnabled = false
//                                isLoading = false
                                if (isNewFlow) {
                                    setText(R.string.change_payment_method)
                                } else {
                                    setText(com.tokopedia.purchase_platform.common.R.string.label_choose_payment)
                                }
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            else -> {
                                layoutParams?.width = Utils.convertDpToPixel(BUTTON_CHOOSE_PAYMENT_WIDTH, context)
                                layoutParams?.height = Utils.convertDpToPixel(BUTTON_LOADING_HEIGHT, context)
//                                isLoading = true
                                groupPayment?.gone()
                                groupLoaderPayment?.visible()
                            }
                        }
                    }
                    OccButtonType.PAY -> {
                        layoutParams?.width = Utils.convertDpToPixel(BUTTON_PAY_WIDTH, context)
                        when (orderTotal.buttonState) {
                            OccButtonState.NORMAL -> {
                                layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                val drawable = getIconUnifyDrawable(context, IconUnify.PROTECTION_CHECK, ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
                                drawable?.setBounds(24.toPx(), 0, 44.toPx(), 20.toPx())
                                setCompoundDrawables(drawable, null, null, null)
                                compoundDrawablePadding = 4.toPx()
                                isEnabled = true
//                                isLoading = false
                                setText(R.string.pay)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            OccButtonState.DISABLE -> {
                                layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                val drawable = getIconUnifyDrawable(context, IconUnify.PROTECTION_CHECK)
                                drawable?.setBounds(24.toPx(), 0, 44.toPx(), 20.toPx())
                                setCompoundDrawables(drawable, null, null, null)
                                compoundDrawablePadding = 4.toPx()
                                isEnabled = false
//                                isLoading = false
                                setText(R.string.pay)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            else -> {
                                layoutParams?.height = Utils.convertDpToPixel(BUTTON_LOADING_HEIGHT, context)
                                setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
//                                isLoading = true
                                groupPayment?.gone()
                                groupLoaderPayment?.visible()
                            }
                        }
                    }
                    OccButtonType.CONTINUE -> {
                        layoutParams?.width = Utils.convertDpToPixel(BUTTON_CHOOSE_PAYMENT_WIDTH, context)
                        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                        when (orderTotal.buttonState) {
                            OccButtonState.NORMAL -> {
                                layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                isEnabled = true
//                                isLoading = false
                                setText(R.string.continue_pay)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            OccButtonState.DISABLE -> {
                                layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
                                isEnabled = false
//                                isLoading = false
                                setText(R.string.continue_pay)
                                groupLoaderPayment?.gone()
                                groupPayment?.visible()
                            }
                            else -> {
                                layoutParams?.height = Utils.convertDpToPixel(BUTTON_LOADING_HEIGHT, context)
//                                isLoading = true
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
        private const val BUTTON_LOADING_HEIGHT = 48f
        private const val BUTTON_CHOOSE_PAYMENT_WIDTH = 160f
        private const val BUTTON_PAY_WIDTH = 140f
    }
}