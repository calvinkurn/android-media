package com.tokopedia.profilecompletion.addemail.view.fragment

//import com.tokopedia.unifycomponents.Toaster
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.design.component.ButtonCompat
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addemail.viewmodel.AddEmailViewModel
import com.tokopedia.profilecompletion.addemail.data.AddEmailResult
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_add_email_setting_profile.*
import javax.inject.Inject


class AddEmailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(AddEmailViewModel::class.java) }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_add_email_setting_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()

    }

    private fun setListener() {
        et_email.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setErrorText("")
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        buttonSubmit.setOnClickListener {
            val email = et_email.text.toString()
            if (email.isBlank()) {
                setErrorText(getString(R.string.error_field_required))
            } else if (!isValidEmail(email)) {
                setErrorText(getString(R.string.wrong_email_format))
            } else {
                showLoading()
                context?.run { viewModel.checkEmail(this, email) }
            }
        }
    }

    private fun onSuccessCheckEmail(email: String) {

    }


    private fun goToVerificationActivity(email: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        val bundle = Bundle()
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_ADD_EMAIL)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, "email")
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)

        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_ADD_EMAIL_COTP)
    }

    private fun setErrorText(s: String) {
        if (TextUtils.isEmpty(s)) {
            tv_message.visibility = View.VISIBLE
            tv_error.visibility = View.GONE
            buttonSubmit.isEnabled = true
            buttonSubmit.buttonCompatType = ButtonCompat.PRIMARY
            wrapper_email.setErrorEnabled(true)
        } else {
            wrapper_email.setErrorEnabled(false)
            tv_error.visibility = View.VISIBLE
            tv_error.text = s
            tv_message.visibility = View.GONE
            buttonSubmit.isEnabled = false
            buttonSubmit.buttonCompatType = ButtonCompat.PRIMARY_DISABLED

        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setObserver() {
        viewModel.mutateAddEmailResponse.observe(
                this,
                Observer {
                    when (it) {
                        is Success -> onSuccessAddEmail(it.data)
                        is Fail -> onErrorShowSnackbar(it.throwable)
                    }
                }
        )

        viewModel.mutateCheckEmailResponse.observe(
                this,
                Observer {
                    when (it) {
                        is Success -> goToVerificationActivity(it.data)
                        is Fail -> onErrorShowSnackbar(it.throwable)
                    }
                }
        )

    }

    private fun onErrorShowSnackbar(throwable: Throwable) {
        dismissLoading()
        view?.run {
            Toaster.showError(
                    this,
                    ErrorHandlerSession.getErrorMessage(throwable, context, true),
                    Snackbar.LENGTH_LONG)
        }
    }

    private fun onSuccessAddEmail(result: AddEmailResult) {
        dismissLoading()
        activity?.run {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PROFILE_SCORE, result.addEmailPojo.data.completionScore)
            bundle.putString(EXTRA_EMAIL, result.email)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun showLoading() {
        mainView.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        mainView.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ADD_EMAIL_COTP && resultCode == Activity.RESULT_OK) {
            onSuccessVerifyAddEmail(data)
        } else {
            dismissLoading()
        }
    }

    private fun onSuccessVerifyAddEmail(data: Intent?) {
        data?.extras?.run {
            val otpCode = getString(ApplinkConstInternalGlobal.PARAM_OTP_CODE, "")
            if (otpCode.isNotBlank()) {
                val email = et_email.text.toString().trim()
                viewModel.mutateAddEmail(context!!, email, otpCode)
            } else {
                onErrorShowSnackbar(MessageErrorException(getString(com.tokopedia.abstraction.R.string.default_request_error_unknown),
                        ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW.toString()))
            }
        }
    }

    companion object {
        val EXTRA_PROFILE_SCORE = "profile_score"
        val EXTRA_EMAIL = "email"
        val REQUEST_ADD_EMAIL_COTP = 101
        val OTP_TYPE_ADD_EMAIL = 141

        fun createInstance(bundle: Bundle): AddEmailFragment {
            val fragment = AddEmailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.mutateAddEmailResponse.removeObservers(this)
        viewModel.clear()
    }

}