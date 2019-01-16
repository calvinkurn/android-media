package com.tokopedia.common_digital.product.presentation.compoundview

import android.content.Context
import android.text.InputFilter
import android.text.InputType
import android.text.TextUtils
import android.text.TextWatcher
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide
import com.tokopedia.common_digital.R
import com.tokopedia.common_digital.product.presentation.model.ClientNumber
import com.tokopedia.common_digital.product.presentation.model.ClientNumberType
import com.tokopedia.common_digital.product.presentation.model.Validation
import java.util.regex.Pattern

/**
 * @author anggaprasetiyo on 5/6/17.
 */
abstract class CommonClientNumberInputView : LinearLayout {

    protected lateinit var tvLabel: TextView
    protected lateinit var autoCompleteTextView: AutoCompleteTextView
    protected lateinit var btnClear: Button
    protected lateinit var imgOperator: ImageView
    protected lateinit var btnContactPicker: Button
    protected lateinit var pulsaFramelayout: RelativeLayout
    protected lateinit var tvErrorClientNumber: TextView
    protected lateinit var buttonAditional: Button

    private var actionListener: ActionListener? = null

    protected var clientNumber: ClientNumber? = null

    protected open val focusChangeListener: View.OnFocusChangeListener
        get() = OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                actionListener!!.onClientNumberHasFocus((view as TextView).text.toString())
                if (autoCompleteTextView.text.length > 0) {
                    this@CommonClientNumberInputView.setBtnClearVisible()
                } else {
                    this@CommonClientNumberInputView.setBtnClearInvisible()
                }
            }
        }

    var text: String
        get() = this.autoCompleteTextView.text.toString()
        set(text) = this.autoCompleteTextView.setText(text)

    protected open val buttonContactPickerClickListener: View.OnClickListener
        get() = OnClickListener { actionListener!!.onButtonContactPickerClicked() }

    protected open val buttonClearClickListener: View.OnClickListener
        get() = OnClickListener {
            actionListener!!.onClientNumberCleared()
            autoCompleteTextView.setText("")
        }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defaultStyle: Int) : super(context, attrs, defaultStyle) {
        init(context)
    }

    private fun init(context: Context) {

        val view = LayoutInflater.from(context).inflate(R.layout.view_holder_common_client_number_input, this, true)
        tvLabel = view.findViewById(R.id.tv_label_client_number)
        autoCompleteTextView = view.findViewById(R.id.ac_client_number)
        btnClear = view.findViewById(R.id.btn_clear_client_number)
        imgOperator = view.findViewById(R.id.iv_pic_operator)
        btnContactPicker = view.findViewById(R.id.btn_contact_picker)
        pulsaFramelayout = view.findViewById(R.id.fl_holder_input_client_number)
        tvErrorClientNumber = view.findViewById(R.id.tv_error_client_number)
        buttonAditional = view.findViewById(R.id.button_additional)

        initialTextWatcher()
        setImgOperatorInvisible()
        setBtnClearInvisible()
    }

    private fun initialTextWatcher() {

    }

    fun setErrorText(errorMessage: String) {
        tvErrorClientNumber.text = errorMessage
        tvErrorClientNumber.visibility = View.VISIBLE
    }

    fun setHint(hint: String) {
        this.autoCompleteTextView.hint = hint
    }

    fun setImgOperatorInvisible() {
        this.imgOperator.visibility = View.GONE
    }

    fun setBtnClearVisible() {
        this.btnClear.visibility = View.VISIBLE
    }

    fun setBtnClearInvisible() {
        this.btnClear.visibility = View.GONE
    }

    fun enableImageOperator(imageUrl: String) {
        imgOperator.visibility = View.VISIBLE
        Glide.with(context).load(imageUrl).dontAnimate().into(this.imgOperator)
    }

    fun disableImageOperator() {
        imgOperator.visibility = View.GONE
        Glide.with(context).load("").dontAnimate().into(this.imgOperator)
    }

    fun setInputTypeNumber() {
        this.autoCompleteTextView.inputType = InputType.TYPE_CLASS_NUMBER
        this.autoCompleteTextView.keyListener = DigitsKeyListener.getInstance("0123456789")
    }

    fun setInputTypeText() {
        this.autoCompleteTextView.inputType = InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
    }

    fun setFilterMaxLength(maximumLength: Int) {
        this.autoCompleteTextView.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maximumLength))
    }

    fun setImgOperator(imgUrl: String) {
        Glide.with(getContext()).load(imgUrl).dontAnimate().into(this.imgOperator)
    }

    fun setActionListener(actionListener: ActionListener) {
        this.actionListener = actionListener
    }

    fun renderData(clientNumber: ClientNumber) {
        this.clientNumber = clientNumber
        if (!TextUtils.isEmpty(clientNumber.text)) {
            tvLabel.visibility = View.VISIBLE
            tvLabel.text = clientNumber.text
        } else {
            tvLabel.visibility = View.GONE
        }
        tvLabel.text = clientNumber.text
        autoCompleteTextView.hint = clientNumber.placeholder
        setupLayoutParamAndInputType(clientNumber)
        autoCompleteTextView.onFocusChangeListener = focusChangeListener
        val textWatcher = getTextWatcherInput(clientNumber)
        autoCompleteTextView.removeTextChangedListener(textWatcher)
        autoCompleteTextView.addTextChangedListener(textWatcher)
        this.btnClear.setOnClickListener(buttonClearClickListener)
        this.btnContactPicker.setOnClickListener(buttonContactPickerClickListener)
    }

    protected open fun setupLayoutParamAndInputType(clientNumber: ClientNumber) {
        val layoutParams = LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT)
        if (clientNumber.type!!.equals(ClientNumberType.TYPE_INPUT_TEL, ignoreCase = true)) {
            btnContactPicker.visibility = View.VISIBLE
            layoutParams.weight = 0.88f
        } else {
            btnContactPicker.visibility = View.GONE
            layoutParams.weight = 1f
        }
        pulsaFramelayout.layoutParams = layoutParams
        if (clientNumber.type!!.equals(ClientNumberType.TYPE_INPUT_TEL, ignoreCase = true) || clientNumber.type!!.equals(ClientNumberType.TYPE_INPUT_NUMERIC, ignoreCase = true)) {
            setInputTypeNumber()
        } else {
            setInputTypeText()
        }
    }

    protected abstract fun getTextWatcherInput(clientNumber: ClientNumber): TextWatcher

    protected fun isValidInput(prefixList: List<String>): Boolean {
        val clientNumberInput = autoCompleteTextView.text.toString()
        var isStartWithPrefix = false
        for (string in prefixList) {
            if (clientNumberInput.startsWith(string)) {
                isStartWithPrefix = true
                break
            }
        }
        if (!isStartWithPrefix) return false
        if (clientNumber != null) {
            var isValidRegex = false
            for (validation in clientNumber!!.validation) {
                if (Pattern.matches(validation.regex!!, clientNumberInput))
                    isValidRegex = true
            }
            return isValidRegex
        } else {
            return true
        }
    }

    fun resetInputTyped() {
        autoCompleteTextView.setText("")
    }

    interface ActionListener {

        fun onButtonContactPickerClicked()

        fun onClientNumberHasFocus(clientNumber: String)

        fun onClientNumberCleared()

    }

}