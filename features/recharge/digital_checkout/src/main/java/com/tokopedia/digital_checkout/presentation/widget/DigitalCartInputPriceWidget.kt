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

    fun setMinMaxPayment(totalPayment: Long, minPayment: Long, maxPayment: Long,
                         minPaymentString: String, maxPaymentString: String) {

        setPriceInput(totalPayment)
        etDigitalCheckoutInputPrice.textFieldInput.setText(getFormattedPriceString(totalPayment))
        etDigitalCheckoutInputPrice.textFieldInput.setSelection(getFormattedPriceString(totalPayment).length)

        validateUserInput(priceInput, minPayment, maxPayment, minPaymentString, maxPaymentString)

        etDigitalCheckoutInputPrice.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val beforePrice = priceInput
                val price: Long? = s.toString().replace(".", "").toLongOrNull()
                val stringFormatted = getFormattedPriceString(price ?: 0)

                setPriceInput(price)

                actionListener?.onInputPriceByUserFilled(priceInput)
                validateUserInput(priceInput, minPayment, maxPayment, minPaymentString, maxPaymentString)

                val selectionPosition = etDigitalCheckoutInputPrice.textFieldInput.selectionStart

                etDigitalCheckoutInputPrice.textFieldInput.removeTextChangedListener(this)
                etDigitalCheckoutInputPrice.textFieldInput.setText(stringFormatted)
                etDigitalCheckoutInputPrice.textFieldInput.setSelection(getSelectionPosition(beforePrice, stringFormatted, selectionPosition))
                etDigitalCheckoutInputPrice.textFieldInput.addTextChangedListener(this)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun validateUserInput(priceInput: Long, minPayment: Long, maxPayment: Long,
                                  minPaymentString: String, maxPaymentString: String) {
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

    private fun isUserInputValid(priceInput: Long, minPayment: Long, maxPayment: Long): Boolean {
        return priceInput in minPayment..maxPayment
    }

    private fun getFormattedPriceString(price: Long): String {
        return String.format("%,d", price).replace(",", ".")
    }

    private fun setPriceInput(price: Long?) {
        priceInput = if (price != null) {
            price
        } else {
            etDigitalCheckoutInputPrice.setError(true)
            actionListener?.disableCheckoutButton()
            return
        }
    }

    private fun getSelectionPosition(beforePrice: Long, formattedPrice: String, selectionPosition: Int): Int {
        getFormattedPriceString(beforePrice).let { beforePriceFormatted ->
            return when (beforePriceFormatted.length) {
                formattedPrice.length - 2 -> {
                    // e.g. when before price 100, after user input: 1.000
                    // selection position must be +1 due to the addition of . (dot)
                    return selectionPosition + 1
                }
                formattedPrice.length + 2 -> {
                    // e.g. when before price 1.000, after user input: 100
                    // selection position must be -1 due to the removal of . (dot)
                    return selectionPosition - 1
                }
                else -> selectionPosition
            }
        }
    }

    interface ActionListener {
        fun onInputPriceByUserFilled(paymentAmount: Long)
        fun enableCheckoutButton()
        fun disableCheckoutButton()
    }

}