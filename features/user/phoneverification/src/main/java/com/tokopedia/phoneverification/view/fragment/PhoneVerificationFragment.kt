package com.tokopedia.phoneverification.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.phoneverification.PhoneVerificationAnalytics
import com.tokopedia.phoneverification.PhoneVerificationConst
import com.tokopedia.phoneverification.R
import com.tokopedia.phoneverification.di.revamp.PhoneVerificationComponent
import com.tokopedia.phoneverification.view.activity.ChangePhoneNumberActivity
import com.tokopedia.phoneverification.view.listener.PhoneVerification
import com.tokopedia.phoneverification.view.presenter.VerifyPhoneNumberPresenter
import com.tokopedia.phoneverification.view.viewmodel.PhoneVerificationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.Toaster.TYPE_NORMAL
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.phonenumber.PhoneNumberUtil.transform
import javax.inject.Inject

/**
 * Created by nisie on 2/22/17.
 */
open class PhoneVerificationFragment : BaseDaggerFragment(), PhoneVerification.View {
    override fun getScreenName(): String {
        return PhoneVerificationConst.SCREEN_PHONE_VERIFICATION
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    private val phoneVerificationViewModel by lazy { ViewModelProvider(this).get(PhoneVerificationViewModel::class.java) }
    public override fun initInjector() {
//        if (activity != null && activity!!.application != null) {
//            val baseAppComponent = (activity!!.application as BaseMainApplication).baseAppComponent
//            val phoneVerificationComponent = DaggerPhoneVerificationComponent.builder().baseAppComponent(baseAppComponent).build()
//            phoneVerificationComponent.inject(this)
//        }

        getComponent(PhoneVerificationComponent::class.java).inject(this)
    }

    interface PhoneVerificationFragmentListener {
        fun onSkipVerification()
        fun onSuccessVerification()
    }

    protected var skipButton: TextView? = null
    protected var phoneNumberEditText: TextView? = null
    protected var requestOtpButton: TextView? = null
    private val phoneTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            requestOtpButton?.isEnabled = s.isNotEmpty()
        }
    }
    var listener: PhoneVerificationFragmentListener? = null
    private var phoneNumber: String? = null
    lateinit var analytics: PhoneVerificationAnalytics

//    @JvmField
//    @Inject
//    var presenter: VerifyPhoneNumberPresenter? = null
    private var isMandatory = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        analytics = PhoneVerificationAnalytics.createInstance()
        arguments?.let {
            if (it.containsKey(PhoneVerificationConst.EXTRA_IS_MANDATORY)) {
                isMandatory = it.getBoolean(PhoneVerificationConst.EXTRA_IS_MANDATORY)
            }
            phoneNumber = it.getString(EXTRA_PARAM_PHONE_NUMBER)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null) {
            phoneNumber = savedInstanceState.getString(EXTRA_PARAM_PHONE_NUMBER)
        }
    }

    fun setPhoneVerificationListener(listener: PhoneVerificationFragmentListener?) {
        this.listener = listener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_phone_verification, container, false)
        KeyboardHandler.DropKeyboard(activity, getView())
//        presenter!!.attachView(this)
        setupObserver()

        return view
    }

    private fun setupObserver() {
        phoneVerificationViewModel.responseLiveData.observe(
                viewLifecycleOwner, Observer {
            when(it){
                is Success -> {
                    onSuccessVerifyPhoneNumber()
                }

                is Fail -> {
                    onErrorVerifyPhoneNumber(ErrorHandler.getErrorMessage(activity, it.throwable))
                }
            }
        }
        )
    }

    open fun findView(view: View) {
        skipButton = view.findViewById(R.id.skip_button)
        phoneNumberEditText = view.findViewById(R.id.phone_number)
        requestOtpButton = view.findViewById(R.id.send_otp)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        findView(view)
        setViewListener()
        phoneNumberEditText?.addTextChangedListener(phoneTextWatcher)
        phoneNumberEditText?.text = transform(userSession.phoneNumber)
        if (phoneNumber != null && "".equals(phoneNumber!!.trim { it <= ' ' }, ignoreCase = true)) {
            phoneNumberEditText?.text = phoneNumber
        }
        phoneNumberEditText?.setOnClickListener { moveToChangePhoneNumberPage() }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_PARAM_PHONE_NUMBER, phoneNumber)
    }

    private fun setRequestOtpButtonListener() {
        requestOtpButton?.setOnClickListener {
            if (isPhoneNumberNotEmpty) {
                val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, getPhoneNumber())
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OtpConstant.OtpType.PHONE_NUMBER_VERIFICATION)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
                startActivityForResult(intent, RESULT_PHONE_VERIFICATION)
            } else {
                showErrorPhoneNumber(getString(R.string.error_field_required))
            }
        }
    }

    private fun moveToChangePhoneNumberPage() {
//        if (activity is PhoneVerificationActivationActivity) {
//            analytics!!.eventClickChangePhoneRegister()
//        }
        startActivityForResult(
                ChangePhoneNumberActivity.getChangePhoneNumberIntent(
                        activity,
                        getPhoneNumber()),
                ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER)
    }

    open fun setViewListener() {
        setRequestOtpButtonListener()
        skipButton?.setOnClickListener { v: View? ->
//            if (activity is PhoneVerificationActivationActivity) {
//                analytics!!.eventClickSkipRegister()
//            }
            if (listener != null) {
                listener!!.onSkipVerification()
            }
            else {
                activity?.let {
                    it.setResult(Activity.RESULT_CANCELED)
                    it.finish()
                }
            }
        }
        skipButton?.visibility = if (isMandatory) View.INVISIBLE else View.VISIBLE
    }

    private fun getPhoneNumber(): String {
        return phoneNumberEditText?.text.toString().replace("-", "")
    }

    override fun onSuccessVerifyPhoneNumber() {
        userSession.setIsMSISDNVerified(true)

        view?.let {
            Toaster.make(
                    it,
                    getString(R.string.success_verify_phone_number),
                    Toaster.LENGTH_LONG,
                    TYPE_NORMAL
            )
        }

        if (listener != null) {
            listener?.onSuccessVerification()
        }
        else {
            activity?.let {
                it.setResult(Activity.RESULT_OK)
                it.finish()
            }
        }
    }

    private fun showErrorPhoneNumber(errorMessage: String) {
        phoneNumberEditText?.error = errorMessage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER &&
                resultCode == Activity.RESULT_OK) {
            phoneNumberEditText?.text = data?.getStringExtra(ChangePhoneNumberFragment.EXTRA_PHONE_NUMBER)
        } else if (requestCode == RESULT_PHONE_VERIFICATION &&
                resultCode == Activity.RESULT_OK) {
//            presenter!!.verifyPhoneNumber(getPhoneNumber())
            phoneVerificationViewModel.verifyPhone(getPhoneNumber())
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onErrorVerifyPhoneNumber(errorMessage: String) {
//        if (errorMessage == "")
//            NetworkErrorHelper.showSnackbar(activity)
//        else
//            NetworkErrorHelper.showSnackbar(activity, errorMessage)
//
        view?.let {
            Toaster.make(it, errorMessage, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private val isPhoneNumberNotEmpty: Boolean get() = getPhoneNumber().isNotEmpty()

    override fun onDestroyView() {
        super.onDestroyView()
//        presenter!!.detachView()
    }

    companion object {
        private const val EXTRA_PARAM_PHONE_NUMBER = "EXTRA_PARAM_PHONE_NUMBER"
        private const val RESULT_PHONE_VERIFICATION = 11
        fun createInstance(listener: PhoneVerificationFragmentListener?): PhoneVerificationFragment {
            val fragment = PhoneVerificationFragment()
            fragment.setPhoneVerificationListener(listener)
            return fragment
        }

        fun createInstance(listener: PhoneVerificationFragmentListener?, canSkip: Boolean): PhoneVerificationFragment {
            val fragment = PhoneVerificationFragment()
            val args = Bundle()
            args.putBoolean(PhoneVerificationConst.EXTRA_IS_MANDATORY, canSkip)
            fragment.arguments = args
            fragment.setPhoneVerificationListener(listener)
            return fragment
        }

        fun createInstance(listener: PhoneVerificationFragmentListener?, phoneNumber: String?): PhoneVerificationFragment {
            val fragment = PhoneVerificationFragment()
            fragment.setPhoneVerificationListener(listener)
            val args = Bundle()
            args.putString(EXTRA_PARAM_PHONE_NUMBER, phoneNumber)
            fragment.arguments = args
            return fragment
        }
    }
}