package com.tokopedia.loginregister.common.view

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.View
import android.view.View.OnClickListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.PartialRegisterInputUtils
import com.tokopedia.loginregister.common.PartialRegisterInputUtils.Companion.getType
import com.tokopedia.loginregister.common.PartialRegisterInputUtils.Companion.isValidEmail
import com.tokopedia.loginregister.common.PartialRegisterInputUtils.Companion.isValidPhone
import com.tokopedia.loginregister.common.analytics.RegisterAnalytics
import com.tokopedia.loginregister.common.utils.KeyboardHandler
import com.tokopedia.loginregister.common.utils.KeyboardHandler.OnKeyBoardVisibilityChangeListener
import com.tokopedia.loginregister.common.view.emailextension.EmailExtension
import com.tokopedia.loginregister.common.view.emailextension.adapter.EmailExtensionAdapter
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

/**
 * @author by alvinatin on 11/06/18.
 */
class PartialRegisterInputView : BaseCustomView {
    var fieldUnifyInputEmailPhone: TextFieldUnify2? = null
    var etInputEmailPhone: AutoCompleteTextView? = null
    var btnAction: UnifyButton? = null
    var emailExtension: EmailExtension? = null
    var wrapperPassword: TextFieldUnify2? = null
    var btnForgotPassword: Typography? = null
    var btnChange: Typography? = null
    var registerAnalytics = RegisterAnalytics()
    private var isExtensionSelected = false
    private var listener: PartialRegisterInputViewListener? = null
    private var emailExtensionList: List<String>? = null
    fun setListener(listener: PartialRegisterInputViewListener?) {
        this.listener = listener
    }

    interface PartialRegisterInputViewListener {
        fun onActionPartialClick(id: String)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.layout_partial_register_input, this)
        fieldUnifyInputEmailPhone = view.findViewById(R.id.input_email_phone)
        etInputEmailPhone = fieldUnifyInputEmailPhone?.editText
        btnAction = view.findViewById(R.id.register_btn)
        wrapperPassword = view.findViewById(R.id.wrapper_password)
        btnForgotPassword = view.findViewById(R.id.forgot_pass)
        btnChange = view.findViewById(R.id.change_button)

        etInputEmailPhone?.typeface = Typography.getFontType(context, false, Typography.DISPLAY_2)

        renderData()
    }

    fun renderData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            etInputEmailPhone?.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
        }
        val background = ContextCompat.getDrawable(context, R.drawable.bg_rounded_corner_autocomplete_partial_input)
        etInputEmailPhone?.setDropDownBackgroundDrawable(background)
        etInputEmailPhone?.onItemClickListener = OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long -> registerAnalytics.trackClickPhoneNumberSuggestion() }
        etInputEmailPhone?.addTextChangedListener(watcher(fieldUnifyInputEmailPhone))
        etInputEmailPhone?.setOnEditorActionListener { v: TextView, actionId: Int, event: KeyEvent? ->
            var handled = false
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val imm: InputMethodManager = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)
                if (isValidValue(v.text.toString()) && isButtonValidatorActived) {
                    btnAction?.performClick()
                }
                handled = true
            }
            handled
        }
        wrapperPassword?.editText?.addTextChangedListener(watcherUnify(wrapperPassword))
        btnAction?.setOnClickListener(ClickRegister())
        btnChange?.setOnClickListener(OnClickListener {
            showDefaultView()
            hideEmailExtension()
        })
    }

    fun onErrorInputEmailPhoneValidate(message: String?) {
        setWrapperInputEmailPhoneError(fieldUnifyInputEmailPhone, message)
    }

    fun onErrorPassword(message: String?) {
        setWrapperErrorPassword(wrapperPassword, message)
    }

    private fun setWrapperInputEmailPhoneError(wrapper: TextFieldUnify2?, s: String?) {
        if (s == null) {
            defaultMessageInputEmailPhone()
        } else {
            wrapper?.isInputError = true
            wrapper?.setMessage(s)
        }
    }

    private fun setWrapperErrorPassword(wrapper: TextFieldUnify2?, s: String?) {
        if (s == null) {
            wrapper?.isInputError = false
            wrapper?.setMessage("")
        } else {
            wrapper?.isInputError = true
            wrapper?.setMessage(s)
        }
    }

    private fun watcher(wrapper: TextFieldUnify2?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setWrapperInputEmailPhoneError(wrapper, null)
                if (s != null) {
                    if (isButtonValidatorActived) {
                        validateValue(s.toString())
                    }
                    if (etInputEmailPhone?.isFocused == true && etInputEmailPhone?.text.toString().contains("@") && (emailExtension != null)) {
                        showEmailExtension()
                        isExtensionSelected = false
                        val charEmail = etInputEmailPhone?.text.toString().split("@").toTypedArray()
                        if (charEmail.size > 1) {
                            emailExtension?.filterExtensions(charEmail[1])
                        } else {
                            emailExtension?.updateExtensions((emailExtensionList ?: listOf()))
                        }
                    } else {
                        hideEmailExtension()
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    private fun watcherUnify(wrapper: TextFieldUnify2?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setWrapperErrorPassword(wrapper, null)
                if (s != null) {
                    if (isButtonValidatorActived) {
                        validateValue(s.toString())
                    }
                    if (etInputEmailPhone?.isFocused == true && etInputEmailPhone?.text.toString().contains("@") && (emailExtension != null)) {
                        showEmailExtension()
                        isExtensionSelected = false
                        val charEmail = etInputEmailPhone?.text.toString().split("@").toTypedArray()
                        if (charEmail.size > 1) {
                            emailExtension?.filterExtensions(charEmail[1])
                        } else {
                            emailExtension?.updateExtensions(emailExtensionList ?: listOf())
                        }
                    } else {
                        hideEmailExtension()
                    }
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    fun setButtonValidator(status: Boolean) {
        isButtonValidatorActived = status
        if (status) onInvalidValue() else onValidValue()
    }

    private fun isValidValue(value: String): Boolean {
        return isValidPhone(value) || isValidEmail(value)
    }

    private fun validateValue(value: String) {
        if(value.isNotEmpty()) {
            when (getType(value)) {
                PartialRegisterInputUtils.PHONE_TYPE -> {
                    if (isValidPhone(value)) {
                        onValidValue()
                    } else { onInvalidValue() }
                }
                PartialRegisterInputUtils.EMAIL_TYPE -> {
                    if (isValidEmail(value)) {
                        onValidValue()
                    } else { onInvalidValue() }
                }
            }
        }else {
            onInvalidValue()
        }
    }

    private fun onValidValue() {
        defaultMessageInputEmailPhone()
        btnAction?.isEnabled = true
    }

    private fun onInvalidValue() {
        btnAction?.isEnabled = false
    }

    val textValue: String
        get() = etInputEmailPhone?.text.toString()

    fun setAdapterInputEmailPhone(adapter: ArrayAdapter<String>,
                                  onFocusChangeListener: OnFocusChangeListener?
    ) {
        if (adapter.getItem(0) != null && etInputEmailPhone != null) {
            etInputEmailPhone?.run {
                setText(adapter.getItem(0))
                setSelection(text.length)
                etInputEmailPhone?.onFocusChangeListener = onFocusChangeListener
                setAdapter(adapter)
            }
        }
    }

    fun setEmailExtension(emailExtension: EmailExtension?, emailExtensionList: List<String>?) {
        this.emailExtensionList = emailExtensionList
        this.emailExtension = emailExtension
        emailExtensionList?.let {
            this.emailExtension?.setExtensions(it, object: EmailExtensionAdapter.ClickListener {
                override fun onExtensionClick(extension: String, position: Int) {
                    val charEmail: Array<String> = etInputEmailPhone?.text.toString().split("@").toTypedArray()
                    if (charEmail.size > 0) {
                        etInputEmailPhone?.setText(String.format("%s@%s", charEmail.get(0), extension))
                    } else {
                        etInputEmailPhone?.setText(String.format("%s@%s", etInputEmailPhone?.text.toString().replace("@", ""), extension))
                    }
                    etInputEmailPhone?.setSelection(etInputEmailPhone?.text.toString().trim { it <= ' ' }.length)
                    isExtensionSelected = true
                    hideEmailExtension()
                }
            })
        }
    }

    fun initKeyboardListener(view: View?) {
        view?.run {
            KeyboardHandler(view, object : OnKeyBoardVisibilityChangeListener {
                override fun onKeyboardShow() {
                    if (etInputEmailPhone != null) {
                        if (etInputEmailPhone?.text.toString().contains("@") && !isExtensionSelected && etInputEmailPhone?.isFocused == true) {
                            showEmailExtension()
                        }
                    }
                }

                override fun onKeyboardHide() {
                    hideEmailExtension()
                }
            })
        }
    }

    private inner class ClickRegister : OnClickListener {
        override fun onClick(v: View) {
            val id = etInputEmailPhone?.text.toString()
            listener?.onActionPartialClick(id)
        }
    }

    private fun defaultMessageInputEmailPhone() {
        fieldUnifyInputEmailPhone?.isInputError = false
        fieldUnifyInputEmailPhone?.setMessage(fieldUnifyInputEmailPhone?.context?.getString(R.string.default_placeholder).toString())
    }

    private fun hideMessageInputEmailPhone() {
        fieldUnifyInputEmailPhone?.isInputError = false
        fieldUnifyInputEmailPhone?.setMessage("")
    }

    fun showLoginEmailView(email: String) {
        isButtonValidatorActived = false
        showForgotPassword()
        wrapperPassword?.visibility = View.VISIBLE
        btnChange?.visibility = View.VISIBLE
        fieldUnifyInputEmailPhone?.setLabel(this.context?.getString(R.string.title_email).toString())
        btnAction?.text = btnAction?.context?.getString(R.string.login)
        btnAction?.contentDescription = btnAction?.context?.getString(R.string.content_desc_register_btn)
        etInputEmailPhone?.setText(email)
        etInputEmailPhone?.isEnabled = false
        hideMessageInputEmailPhone()
    }

    fun showDefaultView() {
        isButtonValidatorActived = true
        hideForgotPassword()
        wrapperPassword?.visibility = View.GONE
        btnChange?.visibility = View.GONE
        defaultMessageInputEmailPhone()
        fieldUnifyInputEmailPhone?.setLabel(this.context?.getString(R.string.phone_or_email_input).toString())
        etInputEmailPhone?.setText("")
        etInputEmailPhone?.isEnabled = true
    }

    fun resetErrorWrapper() {
        hideMessageInputEmailPhone()
        setWrapperErrorPassword(wrapperPassword, null)
    }

    private fun showEmailExtension() {
        if (emailExtension != null) {
            emailExtension?.visibility = View.VISIBLE
        }
    }

    private fun hideEmailExtension() {
        if (emailExtension != null) {
            emailExtension?.visibility = View.GONE
        }
    }

    fun showForgotPassword() {
        if (btnForgotPassword != null) {
            btnForgotPassword?.visibility = View.VISIBLE
        }
    }

    fun hideForgotPassword() {
        if (btnForgotPassword != null) {
            btnForgotPassword?.visibility = View.GONE
        }
    }

    companion object {
        private var isButtonValidatorActived = false
    }
}