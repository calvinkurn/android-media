package com.tokopedia.recharge_pdp_emoney.presentation.widget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.recharge_pdp_emoney.R
import com.tokopedia.recharge_pdp_emoney.databinding.WidgetEmoneyInputCardNumberBinding
import com.tokopedia.unifycomponents.BaseCustomView
import org.jetbrains.annotations.NotNull

/**
 * @author by jessica on 29/03/21
 */

class EmoneyPdpInputCardNumberWidget @JvmOverloads constructor(@NotNull context: Context,
                                                               attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    var listener: ActionListener? = null
    val binding :WidgetEmoneyInputCardNumberBinding
    var isShownError: Boolean = false

    init {
        binding = WidgetEmoneyInputCardNumberBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun initView(actionListener: ActionListener) {
        listener = actionListener

        binding.emoneyPdpCardInputNumber.textFieldInput.isClickable = true
        binding.emoneyPdpCardInputNumber.textFieldInput.isFocusable = false
        binding.emoneyPdpCardInputNumber.textFieldInput.filters = arrayOf<InputFilter>(LengthFilter(MAX_CHAR_EMONEY_CARD_NUMBER_WITH_SPACES))
        binding.emoneyPdpCardInputNumber.textFieldInput.setOnClickListener { listener?.onClickInputView(getNumber()) }
        binding.emoneyPdpCardInputNumber.textFieldInput.inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS

        binding.emoneyPdpCardInputNumber.getSecondIcon().setPadding(resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2), resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2), resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2), resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.spacing_lvl2))
        binding.emoneyPdpCardInputNumber.getSecondIcon().setOnClickListener {
            clearNumberAndOperator()
            listener?.onRemoveNumberIconClick()
        }
        binding.emoneyPdpCardCameraIcon.setOnClickListener {
            listener?.onClickCameraIcon()
        }

        binding.emoneyPdpCardInputNumber.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.emoneyPdpCardInputNumber.getFirstIcon().hide()
                listener?.onInputNumberChanged(getNumber())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    fun renderError(errorMsg: String) {
        isShownError = errorMsg.isNotEmpty()
        binding.emoneyPdpCardInputNumber.setError(errorMsg.isNotEmpty())
        binding.emoneyPdpCardInputNumber.setPadding(0, 0, 0,
                if (errorMsg.isNotEmpty()) resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
                else 0)
        binding.emoneyPdpCardInputNumber.setMessage(errorMsg)
    }

    fun setNumber(number: String) {
        var cardNumber = ""
        //to seperate emoney card number into 4
        //e.g. card number: 1234567890123456 will be converted to 1234 5678 9012 3456
        for ((i, char) in number.withIndex()) {
            if (i > 0 && i % MAX_CHAR_EMONEY_CARD_NUMBER_BLOCK == 0) cardNumber += " "
            cardNumber += char
        }
        binding.emoneyPdpCardInputNumber.textFieldInput.setText(cardNumber)
        if (number.isNotEmpty()) showClearIcon()
    }

    fun getNumber(): String = binding.emoneyPdpCardInputNumber.textFieldInput.text.toString().replace(" ", "")

    fun setOperator(imageUrl: String) {
        binding.emoneyPdpCardInputNumber.setFirstIcon(imageUrl)
        binding.emoneyPdpCardInputNumber.getFirstIcon().show()
    }

    private fun clearNumberAndOperator() {
        renderError("")
        binding.emoneyPdpCardInputNumber.textFieldInput.setText("")
        binding.emoneyPdpCardInputNumber.getSecondIcon().hide()
        binding.emoneyPdpCardInputNumber.getFirstIcon().hide()
    }

    private fun showClearIcon() {
        binding.emoneyPdpCardInputNumber.setSecondIcon(com.tokopedia.resources.common.R.drawable.ic_system_action_close_grayscale_16)
        binding.emoneyPdpCardInputNumber.getSecondIcon().show()
    }

    interface ActionListener {
        fun onClickCameraIcon()
        fun onClickInputView(inputNumber: String)
        fun onRemoveNumberIconClick()
        fun onInputNumberChanged(inputNumber: String)
    }

    companion object {
        private const val MAX_CHAR_EMONEY_CARD_NUMBER_WITH_SPACES = 20
        private const val MAX_CHAR_EMONEY_CARD_NUMBER_BLOCK = 4
    }
}
