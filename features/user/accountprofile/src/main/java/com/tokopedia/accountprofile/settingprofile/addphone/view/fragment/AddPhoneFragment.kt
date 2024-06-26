package com.tokopedia.accountprofile.settingprofile.addphone.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.verification.common.OtpUtils.removeErrorCode
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.settingprofile.addphone.data.AddPhoneResult
import com.tokopedia.accountprofile.settingprofile.addphone.data.UserValidatePojo
import com.tokopedia.accountprofile.settingprofile.addphone.data.analitycs.AddPhoneNumberTracker
import com.tokopedia.accountprofile.settingprofile.addphone.view.activity.AddPhoneActivity
import com.tokopedia.accountprofile.settingprofile.addphone.viewmodel.AddPhoneViewModel
import com.tokopedia.accountprofile.common.ColorUtils
import com.tokopedia.accountprofile.databinding.FragmentAddPhoneBinding
import com.tokopedia.accountprofile.di.ProfileCompletionSettingComponent
import com.tokopedia.accountprofile.settingprofile.profileinfo.tracker.ProfileInfoTracker
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject


open class AddPhoneFragment : BaseDaggerFragment() {

    private var _binding: FragmentAddPhoneBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var tracker: ProfileInfoTracker

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(AddPhoneViewModel::class.java) }

    private val phoneNumberTracker = AddPhoneNumberTracker()
    private var isOnclickEventTriggered = false
    private var validateToken: String = ""

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
        splitCompatInstall()
        _binding = FragmentAddPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onFragmentBackPressed(): Boolean {
        tracker.trackClickOnBtnBackAddPhone()
        return super.onFragmentBackPressed()
    }

    private fun splitCompatInstall() {
        activity?.let {
            SplitCompat.installActivity(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()
        binding?.buttonSubmit?.isEnabled = false
        presetView()
    }

    private fun presetView() {
        arguments?.getString(AddPhoneActivity.PARAM_PHONE_NUMBER)?.let { phone ->
            binding?.etPhone?.editText?.setText(phone)
        }
    }

    private fun setListener() {
        binding?.etPhone?.editText?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setErrorText("")
                } else {
                    setErrorText(getString(R.string.error_cant_empty))
                    binding?.buttonSubmit?.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        binding?.etPhone?.setOnFocusChangeListener { view, hasFocus ->
            if (hasFocus && !isOnclickEventTriggered) {
                isOnclickEventTriggered = true

                phoneNumberTracker.clickOnInputPhoneNumber()
            }
        }

        binding?.buttonSubmit?.setOnClickListener {
            val phone = binding?.etPhone?.editText?.text.toString()
            if (phone.isBlank()) {
                setErrorText(getString(R.string.error_field_required))
                phoneNumberTracker.clickOnButtonNext(
                    false,
                    getString(R.string.add_phone_wrong_phone_format)
                )
            } else if (!isValidPhone(phone)) {
                setErrorText(getString(R.string.add_phone_wrong_phone_format))
                phoneNumberTracker.clickOnButtonNext(
                    false,
                    getString(R.string.add_phone_wrong_phone_format)
                )
            } else {
                showLoading()
                storeLocalSession(phone, false)
                viewModel.userProfileValidate(phone)
            }
        }
    }

    private fun goToVerificationActivity() {
        val phone = binding?.etPhone?.editText?.text.toString().trim()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        val bundle = Bundle()
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, "")
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_PHONE_VERIFICATION)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)

        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_COTP_PHONE_VERIFICATION)
    }

    private fun setErrorText(s: String) {
        if (TextUtils.isEmpty(s)) {
            binding?.etPhone?.isInputError = false
            binding?.etPhone?.setMessage(getString(R.string.add_phone_sample_phone))
            binding?.buttonSubmit?.isEnabled = true
        } else {
            binding?.etPhone?.isInputError = true
            binding?.etPhone?.setMessage(s)
            binding?.buttonSubmit?.isEnabled = false
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

    private fun setObserver() {
        viewModel.addPhoneResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessAddPhone(it.data)
                is Fail -> onErrorAddPhone(it.throwable)
            }
        }

        viewModel.userValidateResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessUserValidate(it.data)
                is Fail -> onErrorUserValidate(it.throwable)
            }
        }
    }

    private fun onErrorUserValidate(throwable: Throwable) {
        dismissLoading()
        phoneNumberTracker.clickOnButtonNext(
            false,
            ErrorHandler.getErrorMessage(context, throwable).removeErrorCode()
        )
        setErrorText(ErrorHandler.getErrorMessage(context, throwable))
    }

    private fun onSuccessUserValidate(pojo: UserValidatePojo) {
        if (pojo.userProfileValidate.isValid) {
            phoneNumberTracker.clickOnButtonNext(true, pojo.userProfileValidate.message)
            goToVerificationActivity()
        }
    }

    private fun onErrorAddPhone(throwable: Throwable) {
        dismissLoading()
        view?.let {
            phoneNumberTracker.clickOnButtonNext(
                false,
                ErrorHandler.getErrorMessage(context, throwable).removeErrorCode()
            )
            Toaster.make(
                it,
                ErrorHandler.getErrorMessage(context, throwable),
                Toaster.LENGTH_LONG,
                Toaster.TYPE_ERROR
            )
        }
    }

    open fun onSuccessAddPhone(result: AddPhoneResult) {
        dismissLoading()
        storeLocalSession(result.phoneNumber, true)
        activity?.run {
            val intent = Intent()
            val bundle = Bundle()
            bundle.putInt(EXTRA_PROFILE_SCORE, result.addPhonePojo.data.completionScore)
            bundle.putString(EXTRA_PHONE, result.phoneNumber)
            bundle.putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
            intent.putExtras(bundle)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    protected fun storeLocalSession(phone: String, isVerified: Boolean) {
        userSession.setIsMSISDNVerified(isVerified)
        userSession.phoneNumber = phone
    }

    private fun showLoading() {
        binding?.mainView?.visibility = View.GONE
        binding?.progressBar?.visibility = View.VISIBLE
    }

    protected fun dismissLoading() {
        binding?.mainView?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.GONE
    }

    private fun onSuccessVerifyPhone(data: Intent?) {
        val phone = binding?.etPhone?.editText?.text.toString()
        viewModel.mutateAddPhone(phone.trim(), validateToken)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_COTP_PHONE_VERIFICATION && resultCode == Activity.RESULT_OK) {
            validateToken = data?.getStringExtra(ApplinkConstInternalGlobal.PARAM_TOKEN).toString()
            onSuccessVerifyPhone(data)
        } else {
            dismissLoading()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            viewModel.addPhoneResponse.removeObservers(this)
            viewModel.userValidateResponse.removeObservers(this)
            viewModel.flush()
            _binding = null
        } catch (_: Throwable) { }
    }

    companion object {
        const val EXTRA_PROFILE_SCORE = "profile_score"
        const val EXTRA_PHONE = "phone"

        const val REQUEST_COTP_PHONE_VERIFICATION = 101
        const val OTP_TYPE_PHONE_VERIFICATION = 11

        fun createInstance(bundle: Bundle): AddPhoneFragment {
            val fragment = AddPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
