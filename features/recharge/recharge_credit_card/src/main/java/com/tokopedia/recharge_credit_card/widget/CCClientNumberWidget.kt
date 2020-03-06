package com.tokopedia.recharge_credit_card.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.recharge_credit_card.R
import com.tokopedia.recharge_credit_card.RechargeCCUtil
import com.tokopedia.recharge_credit_card.getColorFromResources
import kotlinx.android.synthetic.main.widget_cc_number.view.*
import org.jetbrains.annotations.NotNull


class CCClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private lateinit var listener: ActionListener

    init {
        View.inflate(context, R.layout.widget_cc_number, this)

        cc_input_number.clearFocus()
        cc_input_number.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(input: Editable?) {
                input?.let {
                    if (it.length <= TOTAL_SYMBOLS) {
                        if (!RechargeCCUtil.isInputCorrect(it, TOTAL_SYMBOLS, DIVIDER_MODULO, DIVIDER)) {
                            it.replace(0, it.length, RechargeCCUtil.concatString(
                                    RechargeCCUtil.getDigitArray(input, TOTAL_DIGITS), DIVIDER_POSITION, DIVIDER))
                        }

                        if (it.length == TOTAL_SYMBOLS) {
                            val inputDigit = it.toString().replace(" ", "")
                            if (!RechargeCCUtil.isCreditCardValid(inputDigit)) {
                                cc_error_input_number.visibility = View.VISIBLE
                                cc_error_input_number.text = "Mohon Periksa kembali nomer CC Anda"
                                disableBtnNext()
                            } else {
                                cc_error_input_number.visibility = View.GONE
                                enableBtnNext()
                            }
                        } else {
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
                        cc_btn_clear_input_number.visibility = View.GONE
                    } else {
                        cc_btn_clear_input_number.visibility = View.VISIBLE
                    }
                }
            }
        })

        cc_btn_clear_input_number.setOnClickListener {
            cc_input_number.setText("")
            cc_error_input_number.visibility = View.GONE
        }

        cc_input_number.setOnClickListener {
            cc_input_number.requestFocus()
        }

        cc_button_next.setOnClickListener {
            listener.onClickNextButton(cc_input_number.text.toString())
        }
    }

    fun setListener(actionListener: ActionListener) {
        this.listener = actionListener
    }

    private fun disableBtnNext() {
        cc_button_next.isEnabled = false
        cc_button_next.setBackgroundResource(com.tokopedia.design.R.drawable.grey_button_rounded)
        cc_button_next.setTextColor(context.resources.getColorFromResources(context, R.color.cc_next_grey))
    }

    private fun enableBtnNext() {
        cc_button_next.isEnabled = true
        cc_button_next.setBackgroundResource(com.tokopedia.design.R.drawable.bg_button_green)
        cc_button_next.setTextColor(context.resources.getColorFromResources(context, R.color.white))
    }

    interface ActionListener {
        fun onClickNextButton(clientNumber: String)
    }

    companion object {
        const val TOTAL_SYMBOLS = 19
        const val TOTAL_DIGITS = 16
        const val DIVIDER_MODULO = 5
        const val DIVIDER_POSITION = DIVIDER_MODULO - 1
        const val DIVIDER = ' '
    }
}