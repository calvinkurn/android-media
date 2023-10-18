package com.tokopedia.profilecompletion.addemail.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addemail.data.AddEmailResult
import com.tokopedia.profilecompletion.addemail.viewmodel.AddEmailViewModel
import com.tokopedia.profilecompletion.common.ColorUtils
import com.tokopedia.profilecompletion.databinding.FragmentAddEmailSettingProfileBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.profilecompletion.profileinfo.tracker.ProfileInfoTracker
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class AddEmailFragment : BaseDaggerFragment() {

    private var _binding: FragmentAddEmailSettingProfileBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var tracker: ProfileInfoTracker

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(AddEmailViewModel::class.java) }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ColorUtils.setBackgroundColor(context, activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddEmailSettingProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()
    }

    override fun onFragmentBackPressed(): Boolean {
        tracker.trackClickOnBtnBackAddEmail()
        return super.onFragmentBackPressed()
    }

    private fun setListener() {
        binding?.etEmail?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setErrorText("")
                } else if (binding?.etEmail?.editText?.text?.isEmpty() == true) {
                    setErrorText(getString(com.tokopedia.profilecompletion.R.string.error_cant_empty))
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding?.buttonSubmit?.setOnClickListener {
            tracker.trackOnBtnLanjutAddEmailClick()
            val email = binding?.etEmail?.editText?.text.toString()
            if (email.isBlank()) {
                setErrorText(getString(R.string.error_field_required))
            } else if (!isValidEmail(email)) {
                setErrorText(getString(R.string.wrong_email_format), isButtonEnabled = true)
            } else {
                showLoading()
                context?.run { viewModel.checkEmail(email) }
            }
        }
    }

    private fun goToVerificationActivity(email: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
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

    private fun setErrorText(s: String, isButtonEnabled: Boolean = false) {
        if (TextUtils.isEmpty(s)) {
            binding?.etEmail?.setMessage("")
            binding?.buttonSubmit?.isEnabled = true
            binding?.etEmail?.isInputError = false
        } else {
            binding?.etEmail?.setMessage(s)
            binding?.buttonSubmit?.isEnabled = isButtonEnabled
            binding?.etEmail?.isInputError = true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun setObserver() {
        viewModel.mutateAddEmailResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> onSuccessAddEmail(it.data)
                    is Fail -> onErrorAddEmail(it.throwable)
                }
            }
        )

        viewModel.mutateCheckEmailResponse.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> goToVerificationActivity(it.data)
                    is Fail -> onErrorAddEmail(it.throwable)
                }
            }
        )

    }

    private fun onErrorAddEmail(throwable: Throwable) {
        dismissLoading()
        view?.run {
            val errorMsg = ErrorHandlerSession.getErrorMessage(throwable, context, true)
            setErrorText(errorMsg)
            tracker.trackOnBtnLanjutAddEmailFailed(errorMsg)
            Toaster.showError(
                this,
                errorMsg,
                Snackbar.LENGTH_LONG
            )
        }
    }

    private fun onSuccessAddEmail(result: AddEmailResult) {
        dismissLoading()
        tracker.trackOnBtnLanjutAddEmailSuccess()
        // update userSession for a new email
        userSession.email = result.email

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
        binding?.mainView?.visibility = View.GONE
        binding?.progressBar?.visibility = View.VISIBLE
    }

    private fun dismissLoading() {
        binding?.mainView?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.GONE
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
            val validateToken = getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
            if (otpCode.isNotBlank()) {
                val email = binding?.etEmail?.editText?.text.toString().trim()
                viewModel.mutateAddEmail(email, otpCode, validateToken)
            } else {
                onErrorAddEmail(
                    MessageErrorException(
                        getString(com.tokopedia.abstraction.R.string.default_request_error_unknown),
                        ErrorHandlerSession.ErrorCode.UNSUPPORTED_FLOW.toString()
                    )
                )
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
        viewModel.flush()
        _binding = null
    }

}
