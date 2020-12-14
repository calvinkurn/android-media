package com.tokopedia.recharge_credit_card.widget

import android.content.Context
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import com.tokopedia.recharge_credit_card.R
import com.tokopedia.recharge_credit_card.util.RechargeCCUtil
import com.tokopedia.unifycomponents.BaseCustomView
import kotlinx.android.synthetic.main.widget_cc_number.view.*
import org.jetbrains.annotations.NotNull


class CCClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var listener: ActionListener

    init {
        View.inflate(context, R.layout.widget_cc_number, this)

        cc_text_input.textFieldInput.clearFocus()

        cc_text_input.textFiedlLabelText.text = context.getString(R.string.cc_label_input_number)
        cc_text_input.setSecondIcon(com.tokopedia.unifycomponents.R.drawable.unify_clear_ic)
        cc_text_input.textFieldIcon2.visibility = View.GONE

        setLengthMaxTextField()

        cc_text_input.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(input: Editable?) {
                input?.let {
                    if (it.length <= TOTAL_SYMBOLS) {
                        cc_text_input.setError(false)
                        cc_text_input.setMessage("")

                        if (!RechargeCCUtil.isInputCorrect(it, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                            it.replace(0, it.length, RechargeCCUtil.concatString(
                                    RechargeCCUtil.getDigitArray(input, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER))
                        }

                        val inputDigit = it.toString().replace(" ", "")
                        if (it.length == TOTAL_SYMBOLS) {
                            if (!RechargeCCUtil.isCreditCardValid(inputDigit)) {
                                setErrorTextField(context.getString(R.string.cc_error_invalid_number))
                            } else {
                                listener.onCheckPrefix(inputDigit)
                                cc_text_input.setError(false)
                                enableBtnNext()
                            }
                        } else {
                            if (it.length > 7) {
                                listener.onCheckPrefix(inputDigit)
                            } else {
                                cc_text_input.setFirstIcon("")
                                cc_text_input.textFieldIcon1.visibility = View.GONE
                            }
                            disableBtnNext()
                        }
                    }
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(input: CharSequence?, p1: Int, p2: Int, p3: Int) {
                input?.let {
                    if (it.isEmpty()) {
                        cc_text_input.textFieldIcon2.visibility = View.GONE
                    } else {
                        cc_text_input.textFieldIcon2.visibility = View.VISIBLE
                    }
                }
            }
        })

        cc_text_input.textFieldIcon2.setOnClickListener {
            cc_text_input.textFieldInput.setText("")
            cc_text_input.setError(false)
        }

        cc_text_input.textFieldInput.setOnClickListener {
            cc_text_input.textFieldInput.requestFocus()
        }

        cc_button_next.setOnClickListener {
            listener.onClickNextButton(cc_text_input.textFieldInput.text.toString())
        }
    }

    private fun setLengthMaxTextField() {
        val filterArray = arrayOfNulls<InputFilter>(1)
        filterArray[0] = InputFilter.LengthFilter(TOTAL_SYMBOLS)
        cc_text_input.textFieldInput.filters = filterArray
    }

    fun setErrorTextField(message: String) {
        cc_text_input.setError(true)
        cc_text_input.setMessage(message)
        disableBtnNext()
    }

    fun setImageIcon(urlImg: String) {
        cc_text_input.setFirstIcon(urlImg)
        cc_text_input.textFieldIcon1.layoutParams.width = 150
        cc_text_input.textFieldIcon1.requestLayout()
        cc_text_input.textFieldIcon1.adjustViewBounds = true
        cc_text_input.textFieldIcon1.visibility = View.VISIBLE
    }

    fun getClientNumber(): String {
        return cc_text_input.textFieldInput.text.toString().replace(" ", "")
    }

    fun setListener(actionListener: ActionListener) {
        this.listener = actionListener
    }

    private fun disableBtnNext() {
        cc_button_next.isEnabled = false
    }

    private fun enableBtnNext() {
        cc_button_next.isEnabled = true
    }

    interface ActionListener {
        fun onClickNextButton(clientNumber: String)
        fun onCheckPrefix(clientNumber: String)
    }

    companion object {
        private const val TOTAL_SYMBOLS = 19
        private const val TOTAL_DIGITS = 16
        private const val DIVIDER_MODULO = 5
        private const val DIVIDER_POSITION = DIVIDER_MODULO - 1
        private const val DIVIDER = ' '
    }
}