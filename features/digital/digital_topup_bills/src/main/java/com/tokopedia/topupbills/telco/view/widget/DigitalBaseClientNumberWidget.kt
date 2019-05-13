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
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
open class DigitalBaseClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                                   defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val hintInputNumber: TextView
    private val imgOperator: ImageView
    private val btnClear: Button
    private val autoCompleteInputNumber: AutoCompleteTextView
    private val btnContactPicker: Button
    private val errorInputNumber: TextView
    private lateinit var listener: ActionListener

    init {
        val view = View.inflate(context, getLayout(), this)
        hintInputNumber = view.findViewById(R.id.hint_input_number)
        imgOperator = view.findViewById(R.id.img_operator)
        btnClear = view.findViewById(R.id.btn_clear_input_number)
        autoCompleteInputNumber = view.findViewById(R.id.ac_input_number)
        btnContactPicker = view.findViewById(R.id.btn_copy_promo)
        errorInputNumber = view.findViewById(R.id.error_input_number)

        autoCompleteInputNumber.clearFocus()
        btnContactPicker.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                listener.navigateToContact()
            }
        })

        btnClear.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                autoCompleteInputNumber.setText("")
                listener.clearAutoComplete()
            }
        })

        autoCompleteInputNumber.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (count == 0) {
                    listener.clearAutoComplete()
                }
                if (count >= 4) {
                    listener.renderProductList()
                }
            }
        })
    }

    open fun getLayout(): Int {
        return R.layout.view_digital_input_number
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }


    fun setInputNumber(inputNumber: String) {
        autoCompleteInputNumber.setText(inputNumber)
    }

    fun getInputNumber(): String {
        return formatPrefixClientNumber(autoCompleteInputNumber.text.toString());
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
            if (phoneNumber == null || "".equals(phoneNumber.trim { it <= ' ' }, ignoreCase = true)) {
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
        fun navigateToContact()
        fun renderProductList()
        fun clearAutoComplete()
    }
}
