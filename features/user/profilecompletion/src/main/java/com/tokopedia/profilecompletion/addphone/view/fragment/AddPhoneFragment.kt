package com.tokopedia.profilecompletion.addphone.view.fragment

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
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.data.AddPhoneResult
import com.tokopedia.profilecompletion.addphone.data.UserValidatePojo
import com.tokopedia.profilecompletion.addphone.data.analitycs.AddPhoneNumberTracker
import com.tokopedia.profilecompletion.addphone.view.activity.AddPhoneActivity
import com.tokopedia.profilecompletion.addphone.viewmodel.AddPhoneViewModel
import com.tokopedia.profilecompletion.common.ColorUtils
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_add_phone.*
import javax.inject.Inject


open class AddPhoneFragment : BaseDaggerFragment() {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        splitCompatInstall()
        return inflater.inflate(R.layout.fragment_add_phone, container, false)
    }

    private fun splitCompatInstall() {
        activity?.let{
            SplitCompat.installActivity(it)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setObserver()
        buttonSubmit.isEnabled = false
        presetView()
    }

    private fun presetView() {
        arguments?.getString(AddPhoneActivity.PARAM_PHONE_NUMBER)?.let {
            phone -> etPhone.textFieldInput.setText(phone)
        }
    }

    private fun setListener() {
        etPhone?.textFieldInput?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setErrorText("")
                }else {
                    buttonSubmit.isEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable) {

            }
        })

        etPhone?.setOnFocusChangeListener { view, hasFocus ->
            if(hasFocus && !isOnclickEventTriggered) {
                isOnclickEventTriggered = true

                phoneNumberTracker.clickOnInputPhoneNumber()
            }
        }

        buttonSubmit?.setOnClickListener {
            val phone = etPhone?.textFieldInput?.text.toString()
            if (phone.isBlank()) {
                setErrorText(getString(R.string.error_field_required))
                phoneNumberTracker.clickOnButtonNext(false, getString(R.string.wrong_phone_format))
            } else if (!isValidPhone(phone)) {
                setErrorText(getString(R.string.wrong_phone_format))
                phoneNumberTracker.clickOnButtonNext(false, getString(R.string.wrong_phone_format))
            } else {
                showLoading()
                viewModel.userProfileValidate(phone)
            }
        }
    }

    private fun goToVerificationActivity() {
        val phone = etPhone?.textFieldInput?.text.toString().trim()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
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
            etPhone.setError(false)
            etPhone.setMessage(getString(R.string.sample_phone))
            buttonSubmit?.isEnabled = true
        } else {
            etPhone.setError(true)
            etPhone.setMessage(s)
            buttonSubmit?.isEnabled = false
        }
    }

    private fun isValidPhone(phone: String): Boolean {
        return android.util.Patterns.PHONE.matcher(phone).matches()
    }

    private fun setObserver() {
        viewModel.addPhoneResponse.observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> onSuccessAddPhone(it.data)
                        is Fail -> onErrorAddPhone(it.throwable)
                    }
                }
        )

        viewModel.userValidateResponse.observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> onSuccessUserValidate(it.data)
                        is Fail -> onErrorUserValidate(it.throwable)
                    }
                }
        )

    }

    private fun onErrorUserValidate(throwable: Throwable) {
        dismissLoading()
        phoneNumberTracker.clickOnButtonNext(false, ErrorHandler.getErrorMessage(context, throwable))
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
            phoneNumberTracker.clickOnButtonNext(false, ErrorHandler.getErrorMessage(context, throwable))
            Toaster.make(it, ErrorHandler.getErrorMessage(context, throwable), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    open fun onSuccessAddPhone(result: AddPhoneResult) {
        dismissLoading()
        storeLocalSession(result.phoneNumber)
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

    protected fun storeLocalSession(phone: String) {
        userSession.setIsMSISDNVerified(true)
        userSession.phoneNumber = phone
    }

    private fun showLoading() {
        mainView?.visibility = View.GONE
        progressBar?.visibility = View.VISIBLE
    }

    protected fun dismissLoading() {
        mainView?.visibility = View.VISIBLE
        progressBar?.visibility = View.GONE
    }

    private fun onSuccessVerifyPhone(data: Intent?) {
        val phone = etPhone.textFieldInput.text.toString()
        viewModel.mutateAddPhone(phone.trim())
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.addPhoneResponse.removeObservers(this)
        viewModel.userValidateResponse.removeObservers(this)
        viewModel.flush()
    }

}