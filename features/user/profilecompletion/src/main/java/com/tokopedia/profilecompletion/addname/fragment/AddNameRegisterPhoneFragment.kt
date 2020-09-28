package com.tokopedia.profilecompletion.addname.fragment

import android.app.Activity
import android.content.Intent
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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addname.AddNameRegisterPhoneAnalytics
import com.tokopedia.profilecompletion.addname.di.DaggerAddNameComponent
import com.tokopedia.profilecompletion.addname.listener.AddNameListener
import com.tokopedia.profilecompletion.addname.presenter.AddNamePresenter
import com.tokopedia.sessioncommon.data.register.RegisterInfo
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_add_name_register.*
import javax.inject.Inject

/**
 * @author by nisie on 22/04/19.
 */
class AddNameRegisterPhoneFragment : BaseDaggerFragment(), AddNameListener.View {

    private var phoneNumber: String? = ""
    private var uuid: String? = ""

    lateinit var bottomInfo: TextView
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
        val MAX_NAME = 35

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
        val view = inflater.inflate(com.tokopedia.profilecompletion.R.layout.fragment_add_name_register, container, false)
        bottomInfo = view.findViewById(R.id.bottom_info)
        progressBar = view.findViewById(R.id.progress_bar)
        mainContent = view.findViewById(R.id.main_content)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.attachView(this)
        setView()
        setViewListener()
        disableButton(btn_continue)
    }

    protected fun setViewListener() {
        et_name.textFieldInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length != 0) {
                    enableButton(btn_continue)

                } else {
                    disableButton(btn_continue)

                }
                if (isError) {
                    hideValidationError()
                }
            }

            override fun afterTextChanged(editable: Editable) {

            }
        })

        btn_continue.setOnClickListener { onContinueClick() }
        btn_continue.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
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
            registerPhoneAndName(et_name.textFieldInput.text.toString(), it)
            analytics.trackClickFinishAddNameButton()
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
                showValidationError(it.resources.getString(R.string.error_name_too_short))
                analytics.trackErrorFinishAddNameButton(it.resources.getString(R.string.error_name_too_short))
                return false
            }
            if (name.length > MAX_NAME) {
                showValidationError(it.resources.getString(R.string.error_name_too_long))
                analytics.trackErrorFinishAddNameButton(it.resources.getString(R.string.error_name_too_long))
                return false
            }
            hideValidationError()
        }
        return true
    }

    private fun setView() {
        disableButton(btn_continue)

        val joinString = getString(R.string.detail_term_and_privacy) +
                "<br>" + getString(R.string.link_term_condition) +
                " serta " + getString(R.string.link_privacy_policy)

        bottomInfo.text = MethodChecker.fromHtml(joinString)
        bottomInfo.movementMethod = LinkMovementMethod.getInstance()
        stripUnderlines(bottomInfo)
    }

    private fun hideValidationError() {
        isError = false
        et_name.setError(false)
        et_name.setMessage("")
    }

    private fun showValidationError(errorMessage: String) {
        isError = true
        et_name.setError(true)
        et_name.setMessage(errorMessage)
    }

    private fun enableButton(button: UnifyButton) {
        button.isEnabled = true
    }

    private fun disableButton(button: TextView) {
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
            context?.run {
                s.setSpan(ForegroundColorSpan(ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Green_G500)), start, end, 0)
            }
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
        analytics.trackErrorFinishAddNameButton(ErrorHandler.getErrorMessage(context, throwable))

    }

    override fun onSuccessRegister(registerInfo: RegisterInfo) {
        userSession.clearToken()
        userSession.setToken(registerInfo.accessToken, "Bearer", registerInfo.refreshToken)

        activity?.run {
            dismissLoading()
            analytics.trackSuccessRegisterPhoneNumber(registerInfo.userId)

            setResult(Activity.RESULT_OK, Intent().apply {
                putExtras(Bundle().apply {
                    putExtra(ApplinkConstInternalGlobal.PARAM_ENABLE_2FA, registerInfo.enable2Fa)
                    putExtra(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, registerInfo.enableSkip2Fa)
                })
            })

            finish()
        }
    }
}