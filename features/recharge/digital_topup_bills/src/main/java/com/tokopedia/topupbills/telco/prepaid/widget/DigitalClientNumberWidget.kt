package com.tokopedia.topupbills.telco.prepaid.widget

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topupbills.R
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 25/04/19.
 */
open class DigitalClientNumberWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                               defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    protected val hintInputNumber: TextView
    protected val imgOperator: ImageView
    protected val btnClear: ImageView
    protected val autoCompleteInputNumber: AutoCompleteTextView
    protected val btnContactPicker: ImageView
    protected val errorInputNumber: TextView
    protected val layoutInputNumber: ConstraintLayout

    private val inputNumberResult: TextView
    private val imgOperatorResult: ImageView
    private val layoutResult: ConstraintLayout

    protected val view: View
    private lateinit var listener: ActionListener

    init {
        view = View.inflate(context, getLayout(), this)
        hintInputNumber = view.findViewById(R.id.telco_hint_input_number)
        imgOperator = view.findViewById(R.id.telco_img_operator)
        btnClear = view.findViewById(R.id.telco_clear_input_number_btn)
        autoCompleteInputNumber = view.findViewById(R.id.telco_ac_input_number)
        btnContactPicker = view.findViewById(R.id.telco_contact_picker_btn)
        errorInputNumber = view.findViewById(R.id.telco_error_input_number)
        layoutInputNumber = view.findViewById(R.id.telco_input_number_layout)

        layoutResult = view.findViewById(R.id.telco_input_number_result_layout)
        imgOperatorResult = view.findViewById(R.id.telco_img_operator_result)
        inputNumberResult = view.findViewById(R.id.telco_phone_number_result)

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
                    btnClear.visibility = View.GONE
                } else {
                    btnClear.visibility = View.VISIBLE
                }
                listener.onRenderOperator()
            }
        })

        autoCompleteInputNumber.setOnClickListener {
            it?.run {
                listener.onClientNumberHasFocus((this as TextView).text.toString())
            }
        }
    }

    fun clearFocusAutoComplete() {
        autoCompleteInputNumber.clearFocus()
    }

    open fun getLayout(): Int {
        return R.layout.view_telco_input_number
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    fun setErrorInputNumber(errorMessage: String) {
        errorInputNumber.text = errorMessage
        errorInputNumber.visibility = View.VISIBLE
    }

    private fun hideErrorInputNumber() {
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
        ImageHandler.LoadImage(imgOperatorResult, url)
        imgOperator.visibility = View.VISIBLE
        hideErrorInputNumber()
    }

    fun setVisibleResultNumber(show: Boolean) {
        inputNumberResult.text = getInputNumber()
        if (show && getInputNumber().isNotEmpty()) {
            layoutResult.show()
            layoutInputNumber.hide()
        } else {
            layoutInputNumber.show()
            layoutResult.hide()
        }
    }

    private fun validatePrefixClientNumber(phoneNumber: String): String {
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

    private fun formatPrefixClientNumber(phoneNumber: String?): String {
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

    companion object {
        private const val ALPHA_0F = 0f
        private const val ALPHA_1F = 1f
    }

    interface ActionListener {
        fun onNavigateToContact()
        fun onRenderOperator()
        fun onClearAutoComplete()
        fun onClientNumberHasFocus(clientNumber: String)
    }
}
