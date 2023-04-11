package com.tokopedia.digital_checkout.presentation.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.digital_checkout.R
import com.tokopedia.digital_checkout.databinding.ItemDigitalCheckoutInputPriceViewBinding
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull
import kotlin.math.min

/**
 * @author by jessica on 19/01/21
 */
class DigitalCartInputPriceWidget @JvmOverloads constructor(
    @NotNull context: Context,
    attrs: AttributeSet? = null, defStyleAttr: Int = Int.ZERO
) : BaseCustomView(context, attrs, defStyleAttr) {

    private companion object {
        const val INT_TWO = 2
        const val ZERO_LONG = 0L
        const val DOT_CHARACTER = "."
        const val COMMA_CHARACTER = ","
        const val EMPTY_STRING = ""
        const val PRICE_FORMAT = "%,d"
    }

    private var priceInput: Long? = null

    private var minPayment: Long = ZERO_LONG
    private var maxPayment: Long = ZERO_LONG
    private var minPaymentString: String = ""
    private var maxPaymentString: String = ""

    var actionListener: ActionListener? = null

    private val binding = ItemDigitalCheckoutInputPriceViewBinding.inflate(
        LayoutInflater.from(context),
        this, true
    )

    fun getPriceInput(): Long? = priceInput

    fun setMinMaxPayment(
        minPayment: Long, maxPayment: Long,
        minPaymentString: String, maxPaymentString: String
    ) {
        this.minPayment = minPayment
        this.maxPayment = maxPayment
        this.minPaymentString = minPaymentString
        this.maxPaymentString = maxPaymentString

        binding.etDigitalCheckoutInputPrice.textFieldInput.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                onAfterTextChanged(this, s.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    private fun onAfterTextChanged(watcher: TextWatcher, s: String) {
        val beforePrice = priceInput
        val price: Long? = s.replace(DOT_CHARACTER, EMPTY_STRING).toLongOrNull()
        val stringFormatted = getFormattedPriceString(price ?: ZERO_LONG)
        val selectionPosition = binding.etDigitalCheckoutInputPrice.textFieldInput.selectionStart

        binding.etDigitalCheckoutInputPrice.textFieldInput.removeTextChangedListener(watcher)
        setPriceInput(price)
        getSelectionPosition(beforePrice ?: ZERO_LONG, stringFormatted, selectionPosition).let {
            if (it >= Int.ZERO) binding.etDigitalCheckoutInputPrice.textFieldInput.setSelection(it)
        }
        binding.etDigitalCheckoutInputPrice.textFieldInput.addTextChangedListener(watcher)
    }

    private fun validateUserInput(priceInput: Long?) {
        if (priceInput == null) return
        when {
            isUserInputValid(priceInput, minPayment, maxPayment) -> {
                if (minPayment > ZERO_LONG && maxPayment > ZERO_LONG) {
                    binding.etDigitalCheckoutInputPrice.setError(false)
                    binding.etDigitalCheckoutInputPrice.setMessage(
                        resources
                            .getString(
                                R.string.digital_cart_error_input_price_less_than_min,
                                minPaymentString
                            )
                    )
                    actionListener?.enableCheckoutButton()
                }
            }
            priceInput > maxPayment -> {
                if (maxPayment > ZERO_LONG) {
                    binding.etDigitalCheckoutInputPrice.setError(true)
                    binding.etDigitalCheckoutInputPrice.setMessage(
                        resources
                            .getString(
                                R.string.digital_cart_error_input_price_more_than_max,
                                maxPaymentString
                            )
                    )
                    actionListener?.disableCheckoutButton()
                }
            }
            else -> {
                if (minPayment > ZERO_LONG) {
                    if (priceInput > ZERO_LONG) binding.etDigitalCheckoutInputPrice.setError(true)
                    binding.etDigitalCheckoutInputPrice.setMessage(resources.getString(
                                R.string.digital_cart_error_input_price_less_than_min,
                                minPaymentString
                            ))
                    actionListener?.disableCheckoutButton()
                }
            }
        }
    }

    private fun isUserInputValid(priceInput: Long, minPayment: Long, maxPayment: Long): Boolean {
        return priceInput in minPayment..maxPayment
    }

    private fun getFormattedPriceString(price: Long): String {
        return if (price > ZERO_LONG) String.format(PRICE_FORMAT, price).replace(COMMA_CHARACTER, DOT_CHARACTER) else EMPTY_STRING
    }

    fun setPriceInput(price: Long?) {
        if (price != null) {
            price.let {
                this.priceInput = it
                binding.etDigitalCheckoutInputPrice.textFieldInput.setText(getFormattedPriceString(it))
                binding.etDigitalCheckoutInputPrice.textFieldInput.setSelection(getFormattedPriceString(it).length)
                actionListener?.onInputPriceByUserFilled(it)
                validateUserInput(it)
            }
        } else {
            priceInput = ZERO_LONG
            actionListener?.onInputPriceByUserFilled(priceInput)
            actionListener?.disableCheckoutButton()
            return
        }
    }

    private fun getSelectionPosition(
        beforePrice: Long,
        formattedPrice: String,
        selectionPosition: Int
    ): Int {
        getFormattedPriceString(beforePrice).let { beforePriceFormatted ->
            return when (beforePriceFormatted.length) {
                formattedPrice.length - INT_TWO -> {
                    // e.g. when before price 100, after user input: 1.000
                    // selection position must be +1 due to the addition of . (dot)
                    return selectionPosition + Int.ONE
                }
                formattedPrice.length + INT_TWO -> {
                    // e.g. when before price 1.000, after user input: 100
                    // selection position must be -1 due to the removal of . (dot)
                    return selectionPosition - Int.ONE
                }
                else -> min(selectionPosition, formattedPrice.length)
            }
        }
    }

    interface ActionListener {
        fun onInputPriceByUserFilled(paymentAmount: Long?)
        fun enableCheckoutButton()
        fun disableCheckoutButton()
    }

}