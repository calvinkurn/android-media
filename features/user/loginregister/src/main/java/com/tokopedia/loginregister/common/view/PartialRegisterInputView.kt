package com.tokopedia.loginregister.common.view

import android.content.Context
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
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
import com.tokopedia.loginregister.databinding.LayoutPartialRegisterInputBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifyprinciples.Typography
import java.util.*

/**
 * @author by alvinatin on 11/06/18.
 */
class PartialRegisterInputView : BaseCustomView {
    var emailExtension: EmailExtension? = null
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

    private val viewBinding: LayoutPartialRegisterInputBinding = LayoutPartialRegisterInputBinding
        .inflate(
            LayoutInflater.from(context)
        ).also {
            addView(it.root)
        }

    val inputEmailPhoneField by lazy {
        viewBinding.inputEmailPhone
    }

    val wrapperPassword by lazy {
        viewBinding.wrapperPassword
    }

    val buttonContinue by lazy {
        viewBinding.registerBtn
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context,
        attrs,
        defStyleAttr) {
        init()
    }

    private fun init() {
        viewBinding.changeButton
        viewBinding.inputEmailPhone.editText.typeface = Typography.getFontType(context, false, Typography.DISPLAY_2)
        renderData()
    }

    fun renderData() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            viewBinding.inputEmailPhone.editText.importantForAutofill = View.IMPORTANT_FOR_AUTOFILL_NO
        }
        val background = ContextCompat.getDrawable(
            context,
            R.drawable.bg_rounded_corner_autocomplete_partial_input
        )

        viewBinding.inputEmailPhone.editText.apply {
            setDropDownBackgroundDrawable(background)
            addTextChangedListener(watcherEmailPhone(viewBinding.inputEmailPhone))
            setOnItemClickListener { _, _, _, _ ->
                registerAnalytics.trackClickPhoneNumberSuggestion()
            }
            setOnEditorActionListener { v, actionId, _ ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val imm: InputMethodManager = v
                        .context
                        .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    if (isValidValue(v.text.toString()) && isButtonValidatorActivated) {
                        viewBinding.registerBtn.performClick()
                    }
                    handled = true
                }
                handled
            }

        }

        viewBinding.wrapperPassword.editText.addTextChangedListener(
            watcherPassword(viewBinding.wrapperPassword)
        )
        viewBinding.registerBtn.setOnClickListener(ClickRegister())
        viewBinding.changeButton.setOnClickListener {
            showDefaultView()
            hideEmailExtension()
        }
    }

    fun onErrorInputEmailPhoneValidate(message: String?) {
        setWrapperInputEmailPhoneError(viewBinding.inputEmailPhone, message)
    }

    fun onErrorPassword(message: String?) {
        setWrapperErrorPassword(viewBinding.wrapperPassword, message)
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

    private fun watcherEmailPhone(wrapper: TextFieldUnify2?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setWrapperInputEmailPhoneError(wrapper, null)
                if (isButtonValidatorActivated) {
                    validateValue(s.toString())
                }
                if (viewBinding.inputEmailPhone.editText.isFocused &&
                    viewBinding.inputEmailPhone.editText.text.toString().contains("@") &&
                    emailExtension != null
                ) {
                    showEmailExtension()
                    isExtensionSelected = false
                    val charEmail = viewBinding.inputEmailPhone.editText.text.toString()
                        .split("@").toTypedArray()
                    if (charEmail.size > 1) {
                        emailExtension?.filterExtensions(charEmail[1])
                    } else {
                        emailExtension?.updateExtensions(emailExtensionList ?: listOf())
                    }
                } else {
                    hideEmailExtension()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    private fun watcherPassword(wrapper: TextFieldUnify2?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setWrapperErrorPassword(wrapper, null)
                if (isButtonValidatorActivated) {
                    validateValue(s.toString())
                }
                if (viewBinding.inputEmailPhone.editText.isFocused &&
                    viewBinding.inputEmailPhone.editText.text.toString().contains("@") &&
                    emailExtension != null
                ) {
                    showEmailExtension()
                    isExtensionSelected = false
                    val charEmail = viewBinding.inputEmailPhone.editText.text.toString()
                        .split("@").toTypedArray()
                    if (charEmail.size > 1) {
                        emailExtension?.filterExtensions(charEmail[1])
                    } else {
                        emailExtension?.updateExtensions(emailExtensionList ?: listOf())
                    }
                } else {
                    hideEmailExtension()
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    fun setButtonValidator(status: Boolean) {
        isButtonValidatorActivated = status
        if (status) onInvalidValue() else onValidValue()
    }

    private fun isValidValue(value: String): Boolean {
        return isValidPhone(value) || isValidEmail(value)
    }

    private fun validateValue(value: String) {
        if (value.isNotEmpty()) {
            when (getType(value)) {
                PartialRegisterInputUtils.PHONE_TYPE -> {
                    if (isValidPhone(value)) {
                        onValidValue()
                    } else {
                        onInvalidValue()
                    }
                }
                PartialRegisterInputUtils.EMAIL_TYPE -> {
                    if (isValidEmail(value)) {
                        onValidValue()
                    } else {
                        onInvalidValue()
                    }
                }
            }
        } else {
            onInvalidValue()
        }
    }

    private fun onValidValue() {
        defaultMessageInputEmailPhone()
        viewBinding.registerBtn.isEnabled = true
    }

    private fun onInvalidValue() {
        viewBinding.registerBtn.isEnabled = false
    }

    val textValue: String
        get() = viewBinding.inputEmailPhone.editText.text.toString()

    fun setAdapterInputEmailPhone(
        adapter: ArrayAdapter<String>,
        onFocusChangeListener: OnFocusChangeListener?,
    ) {
        if (adapter.getItem(0) != null) {
            viewBinding.inputEmailPhone.editText.run {
                setText(adapter.getItem(0))
                setSelection(text.length)
                viewBinding.inputEmailPhone.editText.onFocusChangeListener = onFocusChangeListener
                setAdapter(adapter)
            }
        }
    }

    fun setEmailExtension(emailExtension: EmailExtension?, emailExtensionList: List<String>?) {
        this.emailExtensionList = emailExtensionList
        this.emailExtension = emailExtension
        emailExtensionList?.let { extension ->
            this.emailExtension?.setExtensions(extension, object : EmailExtensionAdapter.ClickListener {
                override fun onExtensionClick(extension: String, position: Int) {
                    viewBinding.inputEmailPhone.apply {
                        val textInput = editText.text
                        val charEmail: Array<String> = textInput.split("@").toTypedArray()
                        if (charEmail.isNotEmpty()) {
                            editText.setText(String.format(Locale.getDefault(), "%s@%s", charEmail[0], extension))
                        } else {
                            editText.setText(String.format(
                                Locale.getDefault(),
                                "%s@%s",
                                textInput.toString().replace("@", ""),
                                extension)
                            )
                        }
                        editText.setSelection(editText.text.trim { it <= ' ' }.length)
                        isExtensionSelected = true
                        hideEmailExtension()
                    }
                }
            })
        }
    }

    fun initKeyboardListener(view: View?) {
        view?.run {
            KeyboardHandler(view, object : OnKeyBoardVisibilityChangeListener {
                override fun onKeyboardShow() {
                    viewBinding.inputEmailPhone.editText.apply {
                        if (text.contains("@") && isFocused && !isExtensionSelected) {
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
            val id = viewBinding.inputEmailPhone.editText.text.toString()
            listener?.onActionPartialClick(id)
        }
    }

    private fun defaultMessageInputEmailPhone() {
        viewBinding.inputEmailPhone.apply {
            isInputError = false
            setMessage(
            context?.getString(R.string.default_placeholder).toString()
        )}
    }

    private fun hideMessageInputEmailPhone() {
        viewBinding.inputEmailPhone.apply {
            isInputError = false
            setMessage("")
        }
    }

    fun showLoginEmailView(email: String) {
        isButtonValidatorActivated = false
        showNeedHelp()
        viewBinding.wrapperPassword.show()
        viewBinding.changeButton.show()

        viewBinding.registerBtn.apply {
            text = context?.getString(R.string.login)
            contentDescription = context?.getString(R.string.content_desc_register_btn)
        }

        viewBinding.inputEmailPhone.apply {
            setLabel(context?.getString(R.string.title_email).toString())
            editText.setText(email)
            editText.isEnabled = false
        }
        hideMessageInputEmailPhone()
    }

    fun showDefaultView() {
        isButtonValidatorActivated = true
        hideNeedHelp()
        viewBinding.wrapperPassword.hide()
        viewBinding.changeButton.hide()
        defaultMessageInputEmailPhone()
        viewBinding.inputEmailPhone.apply {
            setLabel(context?.getString(R.string.phone_or_email_input).toString())
            editText.setText("")
            editText.isEnabled = true
        }
    }

    fun resetErrorWrapper() {
        hideMessageInputEmailPhone()
        setWrapperErrorPassword(viewBinding.wrapperPassword, null)
    }

    private fun showEmailExtension() {
        if (emailExtension != null) {
            emailExtension?.show()
        }
    }

    private fun hideEmailExtension() {
        if (emailExtension != null) {
            emailExtension?.hide()
        }
    }

    fun showNeedHelp() {
        viewBinding.needHelp.show()
    }

    fun hideNeedHelp() {
        viewBinding.needHelp.hide()
    }

    fun setPassword(pass: String) {
        viewBinding.wrapperPassword.editText.setText(pass)
    }

    fun setPasswordListener(listener: TextView.OnEditorActionListener) {
        viewBinding.wrapperPassword.editText.setOnEditorActionListener(listener)
    }

    fun getPassword(): String {
        return viewBinding.wrapperPassword.editText.text.toString()
    }

    companion object {
        private var isButtonValidatorActivated = false
    }
}
