package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.digital_checkout.R
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.item_digital_checkout_input_price_view.view.*
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 19/01/21
 */
class DigitalCartInputPriceWidget @JvmOverloads constructor(@NotNull context: Context,
                                                            attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private var priceInput: Long = 0
    var actionListener: ActionListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.item_digital_checkout_input_price_view, this, true)
    }

    fun getPriceInput(): Long = priceInput

    fun setMessageText(message: String) {
        etDigitalCheckoutInputPrice.setMessage(message)
    }

    fun setMinMaxPayment(totalPayment: String, minPayment: Long, maxPayment: Long,
                         minPaymentString: String, maxPaymentString: String) {
        etDigitalCheckoutInputPrice.textFieldInput.setText(totalPayment)
        etDigitalCheckoutInputPrice.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty()) {
                    val price: Long = s.toString().toLong()
                    if (price <= maxPayment) {
                        priceInput = price
                    }
                } else {
                    priceInput = 0
                }
                actionListener?.onInputPriceByUserFilled(priceInput)

                when {
                    isUserInputValid(priceInput, minPayment, maxPayment) -> {
                        if (minPayment > 0L && maxPayment > 0L) {
                            etDigitalCheckoutInputPrice.setError(false)
                            etDigitalCheckoutInputPrice.setMessage(resources
                                    .getString(R.string.digital_cart_error_input_price_less_than_min, minPaymentString))
                            actionListener?.enableCheckoutButton()
                        }
                    }
                    priceInput > maxPayment -> {
                        if (maxPayment > 0L) {
                            etDigitalCheckoutInputPrice.setError(true)
                            etDigitalCheckoutInputPrice.setMessage(resources
                                    .getString(R.string.digital_cart_error_input_price_more_than_max, maxPaymentString))
                            actionListener?.disableCheckoutButton()
                        }
                    }
                    else -> {
                        if (minPayment > 0L) {
                            etDigitalCheckoutInputPrice.setError(true)
                            etDigitalCheckoutInputPrice.setMessage(resources
                                    .getString(R.string.digital_cart_error_input_price_less_than_min, minPaymentString))
                            actionListener?.disableCheckoutButton()
                        }
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }
        })
    }

    private fun isUserInputValid(priceInput: Long, minPayment: Long, maxPayment: Long): Boolean {
        return priceInput in minPayment..maxPayment
    }

    interface ActionListener {
        fun onInputPriceByUserFilled(paymentAmount: Long)
        fun enableCheckoutButton()
        fun disableCheckoutButton()
    }

}