package com.tokopedia.oneclickcheckout.order.view.card

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.databinding.LayoutPaymentBinding
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonState
import com.tokopedia.oneclickcheckout.order.view.model.OccButtonType
import com.tokopedia.oneclickcheckout.order.view.model.OrderCost
import com.tokopedia.oneclickcheckout.order.view.model.OrderTotal
import com.tokopedia.purchase_platform.common.utils.removeDecimalSuffix
import com.tokopedia.unifycomponents.toPx
import com.tokopedia.utils.currency.CurrencyFormatUtil

class OrderTotalPaymentCard(private val binding: LayoutPaymentBinding, private val listener: OrderTotalPaymentCardListener): RecyclerView.ViewHolder(binding.root) {

    fun setupPayment(orderTotal: OrderTotal) {
        setupPaymentError(orderTotal.paymentErrorMessage)
        setupButtonBayar(orderTotal)
    }

    private fun setupButtonBayar(orderTotal: OrderTotal) {
        binding.apply {
            root.context?.let { context ->
                btnPay.apply {
                    when (orderTotal.buttonType) {
                        OccButtonType.CHOOSE_PAYMENT -> {
                            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            when (orderTotal.buttonState) {
                                OccButtonState.NORMAL -> {
                                    isEnabled = true
                                    setText(R.string.change_payment_method)
                                    groupLoaderPayment.gone()
                                    groupPayment.visible()
                                }
                                OccButtonState.DISABLE -> {
                                    isEnabled = false
                                    setText(R.string.change_payment_method)
                                    groupLoaderPayment.gone()
                                    groupPayment.visible()
                                }
                                else -> {
                                    groupPayment.gone()
                                    groupLoaderPayment.visible()
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
                                    groupLoaderPayment.gone()
                                    groupPayment.visible()
                                }
                                OccButtonState.DISABLE -> {
                                    val drawable = getIconUnifyDrawable(context, IconUnify.PROTECTION_CHECK)
                                    drawable?.setBounds(ICON_BUTTON_LEFT_BOUND.toPx(), ICON_BUTTON_TOP_BOUND, ICON_BUTTON_RIGHT_BOUND.toPx(), ICON_BUTTON_BOTTOM_BOUND.toPx())
                                    setCompoundDrawables(drawable, null, null, null)
                                    compoundDrawablePadding = ICON_BUTTON_PADDING.toPx()
                                    isEnabled = false
                                    setText(R.string.pay)
                                    groupLoaderPayment.gone()
                                    groupPayment.visible()
                                }
                                else -> {
                                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                                    groupPayment.gone()
                                    groupLoaderPayment.visible()
                                }
                            }
                        }
                        OccButtonType.CONTINUE -> {
                            setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
                            when (orderTotal.buttonState) {
                                OccButtonState.NORMAL -> {
                                    isEnabled = true
                                    setText(R.string.continue_pay)
                                    groupLoaderPayment.gone()
                                    groupPayment.visible()
                                }
                                OccButtonState.DISABLE -> {
                                    isEnabled = false
                                    setText(R.string.continue_pay)
                                    groupLoaderPayment.gone()
                                    groupPayment.visible()
                                }
                                else -> {
                                    groupPayment.gone()
                                    groupLoaderPayment.visible()
                                }
                            }
                        }
                    }
                }

                if (orderTotal.orderCost.totalPrice > 0.0) {
                    tvTotalPaymentValue.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(orderTotal.orderCost.totalPrice, false).removeDecimalSuffix()
                } else {
                    tvTotalPaymentValue.text = "-"
                }

                btnOrderDetail.setOnClickListener {
                    if (orderTotal.orderCost.totalPrice > 0.0) {
                        listener.onOrderDetailClicked(orderTotal.orderCost)
                    }
                }

                btnPay.setOnClickListener {
                    listener.onPayClicked()
                }
            }
        }
    }

    private fun setupPaymentError(paymentErrorMessage: String?) {
        binding.apply {
            if (paymentErrorMessage.isNullOrEmpty()) {
                tickerPaymentError.gone()
            } else {
                tickerPaymentError.setTextDescription(paymentErrorMessage)
                tickerPaymentError.visible()
            }
        }
    }

    interface OrderTotalPaymentCardListener {

        fun onOrderDetailClicked(orderCost: OrderCost)

        fun onPayClicked()
    }

    companion object {
        const val VIEW_TYPE = 7

        private const val ICON_BUTTON_LEFT_BOUND = 24
        private const val ICON_BUTTON_TOP_BOUND = 0
        private const val ICON_BUTTON_RIGHT_BOUND = 44
        private const val ICON_BUTTON_BOTTOM_BOUND = 20
        private const val ICON_BUTTON_PADDING = 4
    }
}