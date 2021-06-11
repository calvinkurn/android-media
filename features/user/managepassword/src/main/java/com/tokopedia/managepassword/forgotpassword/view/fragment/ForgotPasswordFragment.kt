package com.tokopedia.managepassword.forgotpassword.view.fragment

import android.os.Bundle
import android.text.*
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.managepassword.forgotpassword.analytics.ForgotPasswordAnalytics
import com.tokopedia.managepassword.forgotpassword.domain.data.ForgotPasswordResponseModel
import com.tokopedia.managepassword.forgotpassword.view.viewmodel.ForgotPasswordViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_forgot_password.*
import javax.inject.Inject

/**
 * @author rival
 * @created 14/05/2020
 * @team : @minion-kevin
 */

class ForgotPasswordFragment : BaseDaggerFragment() {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ForgotPasswordViewModel::class.java) }

    private var tracker: ForgotPasswordAnalytics = ForgotPasswordAnalytics()

    private val email: String
        get() = arguments?.getString(ApplinkConstInternalGlobal.PARAM_EMAIL, "") ?: ""

    private val isRemoveFooter: Boolean
        get() = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_REMOVE_FOOTER, false) ?: false

    override fun getScreenName(): String {
        return SCREEN_FORGOT_PASSWORD
    }

    override fun initInjector() {
        getComponent(ManagePasswordComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_forgot_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            parent_container?.setBackgroundColor(ContextCompat.getColor(it, R.color.Neutral_N0))
        }

        if (userSession.isLoggedIn || isRemoveFooter) {
            btnRegister?.visibility = View.GONE
        }

        btnRegister?.setOnClickListener {
            tracker.onCLickRegister()
            gotoRegister()
        }

        btnSubmit?.setOnClickListener {
            val email: String = txtEmail?.textFieldInput?.text?.toString() ?: ""
            if (txtEmail?.isTextFieldError != true && email.isNotEmpty()) {
                showLoading()
                viewModel.resetPassword(email)
            }
        }

        txtEmail?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 0) {
                    setTextFieldError(getString(R.string.error_field_required))
                } else {
                    clearErrorMessage()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 0) {
                    setTextFieldError(getString(R.string.error_field_required))
                } else {
                    clearErrorMessage()
                }
            }
        })

        txtEmail?.textFieldInput?.setText(email)
        btnRegister?.text = getSpannableTest()
    }

    private fun initObserver() {
        viewModel.response.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessReset(it.data.resetPassword)
                }
                is Fail -> {
                    onFailedReset(it.throwable.message.toString())
                }
            }
        })
    }

    private fun onSuccessReset(forgotPasswordResponseModel: ForgotPasswordResponseModel.ForgotPasswordModel) {
        hideLoading()
        tracker.onSuccessReset()
        layoutForgotPassword?.visibility = View.GONE
        if (forgotPasswordResponseModel.redirectUrl.isNotEmpty()) {
            activity?.let {
                RouteManager.route(it, String.format("%s?url=%s", ApplinkConst.WEBVIEW, forgotPasswordResponseModel.redirectUrl))
                it.finish()
            }
        } else {
            setOnSuccessView()
        }
    }

    private fun setOnSuccessView() {
        layoutSuccessReset?.visibility = View.VISIBLE
        txtSuccessDescription?.text = String.format("%s %s %s",
                getString(R.string.forgot_password_email_sent_body_1),
                txtEmail?.textFieldInput?.text?.toString(),
                getString(R.string.forgot_password_email_sent_body_2)
        )
    }

    private fun onFailedReset(message: String) {
        hideLoading()
        tracker.onError(message)
        view?.let {
            Toaster.make(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun setTextFieldError(message: String) {
        txtEmail?.setError(true)
        txtEmail?.setMessage(message)
        btnSubmit?.isEnabled = false
    }

    private fun clearErrorMessage() {
        txtEmail?.setError(false)
        txtEmail?.setMessage("")
        btnSubmit?.isEnabled = true
    }

    private fun showLoading() {
        loader?.visibility = View.VISIBLE
        layoutForgotPassword?.visibility = View.GONE
    }

    private fun hideLoading() {
        loader?.visibility = View.GONE
        layoutForgotPassword?.visibility = View.VISIBLE
    }

    private fun getSpannableTest(): Spannable {
        val sourceString = getString(R.string.forgot_password_register)
        val spannable: Spannable = SpannableString(sourceString)
        spannable.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) { }
            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = true
                ds.color = context?.let { ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_G500) }  ?: 0
            }
        }, sourceString.indexOf("Daftar"), sourceString.length, 0)
        return spannable
    }

    private fun gotoRegister() {
        activity?.let {
            val intent = RouteManager.getIntent(it, ApplinkConst.REGISTER)
            startActivity(intent)
            it.finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.response.removeObservers(this)
    }

    companion object {
        const val SCREEN_FORGOT_PASSWORD = "Forgot password page"
        fun createInstance(bundle: Bundle): ForgotPasswordFragment {
            val fragment = ForgotPasswordFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}