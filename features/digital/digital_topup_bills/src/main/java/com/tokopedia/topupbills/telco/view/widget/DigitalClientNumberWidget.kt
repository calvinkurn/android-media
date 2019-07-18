package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
open class DigitalClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                               defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    protected val hintInputNumber: TextView
    protected val imgOperator: ImageView
    protected val btnClear: Button
    protected val autoCompleteInputNumber: AutoCompleteTextView
    protected val btnContactPicker: Button
    protected val errorInputNumber: TextView
    protected val view: View
    private lateinit var listener: ActionListener

    init {
        view = View.inflate(context, getLayout(), this)
        hintInputNumber = view.findViewById(R.id.hint_input_number)
        imgOperator = view.findViewById(R.id.img_operator)
        btnClear = view.findViewById(R.id.btn_clear_input_number)
        autoCompleteInputNumber = view.findViewById(R.id.ac_input_number)
        btnContactPicker = view.findViewById(R.id.btn_copy_promo)
        errorInputNumber = view.findViewById(R.id.error_input_number)

        autoCompleteInputNumber.clearFocus()
        btnContactPicker.setOnClickListener { listener.onNavigateToContact() }

        btnClear.setOnClickListener {
            autoCompleteInputNumber.setText("")
            errorInputNumber.visibility = View.GONE
        }

        autoCompleteInputNumber.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    listener.onClearAutoComplete()
                    imgOperator.visibility = View.GONE
                }
                when {
                    count in 1..9 -> setErrorInputNumber(context.getString(R.string.digital_telco_error_min_phone_number))
                    count in 10..14 -> {
                        listener.onRenderOperator()
                        imgOperator.visibility = View.VISIBLE
                        hideErrorInputNumber()
                    }
                    count >14 -> setErrorInputNumber(context.getString(R.string.digital_telco_error_max_phone_number))
                }
            }
        })

        autoCompleteInputNumber.onFocusChangeListener = object : OnFocusChangeListener {
            override fun onFocusChange(view: View?, hasFocus: Boolean) {
                if (hasFocus) {
                    view?.run {
                        listener.onClientNumberHasFocus((this as TextView).text.toString())
                    }
                }
            }
        }
    }

    open fun getLayout(): Int {
        return R.layout.view_digital_input_number
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setErrorInputNumber(errorMessage: String) {
        errorInputNumber.text = errorMessage
        errorInputNumber.visibility = View.VISIBLE
    }

    fun hideErrorInputNumber() {
        errorInputNumber.text = ""
        errorInputNumber.visibility = View.GONE
    }

    fun setInputNumber(inputNumber: String) {
        autoCompleteInputNumber.setText(formatPrefixClientNumber(inputNumber))
    }

    fun getInputNumber(): String {
        return formatPrefixClientNumber(autoCompleteInputNumber.text.toString())
    }

    fun setIconOperator(url: String) {
        ImageHandler.LoadImage(imgOperator, url)
    }

    fun validatePrefixClientNumber(phoneNumber: String): String {
        var phoneNumber = phoneNumber
        if (phoneNumber.startsWith("62")) {
            phoneNumber = phoneNumber.replaceFirst("62".toRegex(), "0")
        }
        if (phoneNumber.startsWith("+62")) {
            phoneNumber = phoneNumber.replace("+62", "0")
        }
        phoneNumber = phoneNumber.replace(".", "")

        return phoneNumber.replace("[^0-9]+".toRegex(), "")
    }

    fun formatPrefixClientNumber(phoneNumber: String?): String {
        phoneNumber?.run {
            if ("".equals(phoneNumber.trim { it <= ' ' }, ignoreCase = true)) {
                return phoneNumber
            }
            var phoneNumberWithPrefix = validatePrefixClientNumber(phoneNumber)
            if (!phoneNumberWithPrefix.startsWith("0")) {
                phoneNumberWithPrefix = "0$phoneNumber"
            }
            return phoneNumberWithPrefix
        }
        return ""
    }

    interface ActionListener {
        fun onNavigateToContact()
        fun onRenderOperator()
        fun onClearAutoComplete()
        fun onClientNumberHasFocus(clientNumber: String)
    }
}
