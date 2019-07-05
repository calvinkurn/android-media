package com.tokopedia.profilecompletion.addname.fragment

import android.app.Activity
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.text.TkpdHintTextInputLayout
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.profilecompletion.addname.listener.AddNameListener
import com.tokopedia.profilecompletion.addname.presenter.AddNamePresenter
import com.tokopedia.profilecompletion.addname.AddNameRegisterPhoneAnalytics
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addname.di.DaggerAddNameComponent
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * @author by nisie on 22/04/19.
 */
class AddNameRegisterPhoneFragment : BaseDaggerFragment(), AddNameListener.View {

    private var phoneNumber: String? = ""
    private var uuid: String? = ""

    lateinit var etName: EditText
    lateinit var bottomInfo: TextView
    lateinit var message: TextView
    lateinit var btnContinue: TextView
    lateinit var wrapperName: TkpdHintTextInputLayout
    lateinit var progressBar: ProgressBar
    lateinit var mainContent: View

    private var isError = false

    @Inject
    lateinit var presenter: AddNamePresenter

    @Inject
    lateinit var analytics: AddNameRegisterPhoneAnalytics

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        val MIN_NAME = 3
        val MAX_NAME = 128

        fun createInstance(bundle: Bundle): AddNameRegisterPhoneFragment {
            val fragment = AddNameRegisterPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        if (activity != null
                && activity?.application != null) {
            DaggerAddNameComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNumber =  getParamString(ApplinkConstInternalGlobal.PARAM_PHONE, arguments,
                savedInstanceState, "")
        uuid =  getParamString(ApplinkConstInternalGlobal.PARAM_UUID, arguments,
                savedInstanceState, "")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_name_register, container, false)
        etName = view.findViewById(R.id.et_name)
        btnContinue = view.findViewById(R.id.btn_continue)
        bottomInfo = view.findViewById(R.id.bottom_info)
        message = view.findViewById(R.id.message)
        wrapperName = view.findViewById(R.id.wrapper_name)
        progressBar = view.findViewById(R.id.progress_bar)
        mainContent = view.findViewById(R.id.main_content)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        setView()
        setViewListener()
        disableButton(btnContinue)
    }

    protected fun setViewListener() {
        etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length != 0) {
                    enableButton(btnContinue)

                } else {
                    disableButton(btnContinue)

                }
                if (isError) {
                    hideValidationError()
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        btnContinue.setOnClickListener { onContinueClick() }
        btnContinue.setOnEditorActionListener(TextView.OnEditorActionListener { v, id, event ->
            if (id == R.id.btn_continue || id == EditorInfo.IME_NULL) {
                onContinueClick()
                return@OnEditorActionListener true
            }
            false
        })
    }

    protected fun onContinueClick() {
        KeyboardHandler.DropKeyboard(activity, view)
        phoneNumber?.let{
            registerPhoneAndName(etName.text.toString(), it)
        }
    }

    private fun registerPhoneAndName(name: String, phoneNumber : String) {
        if (isValidate(name)) {
            presenter.registerPhoneNumberAndName(name, phoneNumber)
        }
    }

    private fun isValidate(name: String): Boolean {
        context?.let{
            if (name.length < MIN_NAME) {
                showValidationError(it.getResources().getString(R.string.error_name_too_short))
                return false
            }
            if (name.length > MAX_NAME) {
                showValidationError(it.getResources().getString(R.string.error_name_too_long))
                return false
            }
            hideValidationError()
        }
        return true
    }

    private fun setView() {
        disableButton(btnContinue)
        btnContinue.typeface = Typeface.create("sans-serif-medium", Typeface.NORMAL)

        val joinString = getString(R.string.detail_term_and_privacy) +
                "<br>" + getString(R.string.link_term_condition) +
                " serta " + getString(R.string.link_privacy_policy)

        bottomInfo.text = MethodChecker.fromHtml(joinString)
        bottomInfo.movementMethod = LinkMovementMethod.getInstance()
        stripUnderlines(bottomInfo)
    }

    private fun hideValidationError() {
        isError = false
        wrapperName.setErrorEnabled(false)
        wrapperName.error = ""
        message.visibility = View.VISIBLE
    }

    private fun showValidationError(errorMessage: String) {
        isError = true
        wrapperName.setErrorEnabled(true)
        wrapperName.error = errorMessage
        message.visibility = View.GONE
    }

    private fun enableButton(button: TextView) {
        button.setTextColor(MethodChecker.getColor(activity, R.color.white))
        button.background = MethodChecker.getDrawable(activity, R.drawable.bg_button_green_enabled)
        button.isEnabled = true
    }

    private fun disableButton(button: TextView) {
        button.setTextColor(MethodChecker.getColor(activity, R.color.black_12))
        button.background = MethodChecker.getDrawable(activity, R.drawable.bg_button_disabled)
        button.isEnabled = false
    }

    fun stripUnderlines(textView: TextView) {
        val s = SpannableString(textView.text)
        val spans = s.getSpans(0, s.length, URLSpan::class.java)
        for (sp in spans) {
            var span = sp
            val start = s.getSpanStart(span)
            val end = s.getSpanEnd(span)
            s.removeSpan(span)
            span = URLSpanNoUnderline(span.url)
            s.setSpan(span, start, end, 0)
            s.setSpan(ForegroundColorSpan(textView.context.resources.getColor(R.color.tkpd_main_green)), start, end, 0)
        }
        textView.text = s
    }

    override fun showLoading() {
        mainContent.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    fun dismissLoading() {
        mainContent.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    private class URLSpanNoUnderline(url: String) : URLSpan(url) {
        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }

    override fun onErrorRegister(throwable: Throwable) {
        userSession.clearToken()
        dismissLoading()
        showValidationError(ErrorHandler.getErrorMessage(context, throwable))
    }

    override fun onSuccessRegister(registerInfo: RegisterInfo) {
        userSession.clearToken()
        userSession.setToken(registerInfo.accessToken, "Bearer", registerInfo.refreshToken)

        activity?.run {
            dismissLoading()
            analytics.trackSuccessRegisterPhoneNumber(registerInfo.userId)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}