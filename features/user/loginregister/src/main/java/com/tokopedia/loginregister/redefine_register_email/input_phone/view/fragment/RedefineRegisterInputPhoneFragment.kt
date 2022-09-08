package com.tokopedia.loginregister.redefine_register_email.input_phone.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.devicefingerprint.appauth.AppAuthWorker
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.KeyboardHandler
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.databinding.FragmentRedefineRegisterInputPhoneBinding
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.redefine_register_email.common.RedefineRegisterEmailConstants
import com.tokopedia.loginregister.redefine_register_email.common.intentGoToHome
import com.tokopedia.loginregister.redefine_register_email.common.intentGoToLoginWithPhone
import com.tokopedia.loginregister.redefine_register_email.common.intentGoToVerification
import com.tokopedia.loginregister.redefine_register_email.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel.RedefineRegisterInputPhoneViewModel
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel.RegisteredPhoneState
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.sessioncommon.util.TwoFactorMluHelper
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.isUsingNightModeResources
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class RedefineRegisterInputPhoneFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            RedefineRegisterInputPhoneViewModel::class.java
        )
    }

    private val binding: FragmentRedefineRegisterInputPhoneBinding? by viewBinding()

    private var paramSource: String = RedefineRegisterEmailConstants.Common.EMPTY_STRING
    private var paramEmail: String = RedefineRegisterEmailConstants.Common.EMPTY_STRING
    private var paramPassword: String = RedefineRegisterEmailConstants.Common.EMPTY_STRING
    private var paramName: String = RedefineRegisterEmailConstants.Common.EMPTY_STRING
    private var paramIsRequiresInputPhone: Boolean = false
    private var paramToken = RedefineRegisterEmailConstants.Common.EMPTY_STRING
    private var paramHash = RedefineRegisterEmailConstants.Common.EMPTY_STRING
    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        paramSource = getParamString(ApplinkConstInternalGlobal.PARAM_SOURCE, arguments, savedInstanceState, RedefineRegisterEmailConstants.Common.EMPTY_STRING)
        paramEmail = getParamString(ApplinkConstInternalUserPlatform.PARAM_VALUE_EMAIL, arguments, savedInstanceState, RedefineRegisterEmailConstants.Common.EMPTY_STRING)
        paramPassword = getParamString(ApplinkConstInternalUserPlatform.PARAM_VALUE_ENCRYPTED_PASSWORD, arguments, savedInstanceState, RedefineRegisterEmailConstants.Common.EMPTY_STRING)
        paramName = getParamString(ApplinkConstInternalUserPlatform.PARAM_VALUE_NAME, arguments, savedInstanceState, RedefineRegisterEmailConstants.Common.EMPTY_STRING)
        paramIsRequiresInputPhone = getParamBoolean(ApplinkConstInternalUserPlatform.PARAM_IS_REGISTER_REQUIRED_INPUT_PHONE, arguments, savedInstanceState, false)
        paramToken = getParamString(ApplinkConstInternalGlobal.PARAM_TOKEN, arguments, savedInstanceState, RedefineRegisterEmailConstants.Common.EMPTY_STRING)
        paramHash = getParamString(ApplinkConstInternalUserPlatform.PARAM_HASH, arguments, savedInstanceState, RedefineRegisterEmailConstants.Common.EMPTY_STRING)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_redefine_register_input_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.unifyToolbar?.title = getString(R.string.register_email_title)
        showNavigateBackToolbar(true)

        if (!paramIsRequiresInputPhone) onEntireScreenLoading()
        initListener()
        initObserver()
        editorChangesListener()
        setUpKeyboardListener(view)
        initRegisterRequest()
    }

    private fun showNavigateBackToolbar(isShow: Boolean) {
        binding?.unifyToolbar?.apply {
            isShowShadow = false
            if (isShow) {
                setNavigationOnClickListener {
                    requireActivity().onBackPressed()
                }
                isShowBackButton = true
            } else {
                isShowBackButton = false
                actionText = getString(R.string.register_email_input_phone_skip)

                actionTextView?.setOnClickListener {
                    goToHome()
                }
            }
        }
    }

    private fun initRegisterRequest() {
        if (!paramIsRequiresInputPhone) {
            submitRegisterV2()
        }
    }

    private fun submitRegisterV2() {
        if (paramIsRequiresInputPhone) {
            viewModel.registerV2(
                email = paramEmail,
                phone = binding?.fieldInputPhone?.editText?.text.toString(),
                fullName = paramName,
                password = paramPassword,
                validateToken = paramToken,
                hash = paramHash
            )
        } else {
            viewModel.registerV2(
                email = paramEmail,
                fullName = paramName,
                password = paramPassword,
                validateToken = paramToken,
                hash = paramHash
            )
        }
    }

    private fun setUpKeyboardListener(view: View) {
        KeyboardHandler(view, object : KeyboardHandler.OnKeyBoardVisibilityChangeListener {
            override fun onKeyboardShow() {
                binding?.ivInputPhone?.hide()
            }

            override fun onKeyboardHide() {
                binding?.ivInputPhone?.show()
            }
        })
    }

    private fun editorChangesListener() {
        binding?.fieldInputPhone?.editText?.afterTextChanged {
            viewModel.validatePhone(it)
        }

        binding?.fieldInputPhone?.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitForm()
                true
            } else false
        }
    }

    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener {
            submitForm()
        }

        binding?.globalError?.setActionClickListener {
            reloadWithCondition()
        }

        binding?.globalError?.setSecondaryActionClickListener {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
        }
    }

    private fun submitForm() {
        if (isUsingNightModeResources()) {
            viewModel.submitForm(
                binding?.fieldInputPhone?.editText?.text.toString()
            )
        } else {
            viewModel.userProfileValidate(
                email = paramEmail,
                phone = binding?.fieldInputPhone?.editText?.text.toString()
            )
        }
    }

    private fun initObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {
            binding?.btnSubmit?.isEnabled = true
            binding?.fieldInputPhone?.setMessageField(it)
        }

        viewModel.isRegisteredPhone.observe(viewLifecycleOwner) {
            when (it) {
                is RegisteredPhoneState.Loading -> {
                    showRegisteredPhoneCheckLoading(true)
                }
                is RegisteredPhoneState.Registered -> {
                    showRegisteredPhoneCheckLoading(false)
                    showDialogOfferLogin()
                }
                is RegisteredPhoneState.Unregistered -> {
                    showRegisteredPhoneCheckLoading(false)
                    showDialogConfirmPhone(phone = it.message)
                }
                is RegisteredPhoneState.Ineligible -> {
                    showRegisteredPhoneCheckLoading(false)
                    binding?.fieldInputPhone?.setMessageField(it.message)
                }
                is RegisteredPhoneState.Failed -> {
                    showRegisteredPhoneCheckLoading(false)

                    when (it.throwable) {
                        is AkamaiErrorException -> {
                            showDialogFailed()
                        }
                        else -> {
                            it.throwable?.let { throwable -> showToasterError(throwable) }
                        }
                    }
                }
            }
        }

        viewModel.getUserInfo.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    actionAfterLoggedIn()

                    if (paramIsRequiresInputPhone) {
                        goToHome()
                    } else {
                        showNavigateBackToolbar(false)
                        onSuccessRegistered()
                    }
                }
                is Fail -> {
                    onEntireLoadingFailed()
                    showToasterError(it.throwable)
                }
            }
        }

        viewModel.registerV2.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onEntireScreenLoading()
                }
                is Fail -> {
                    handleGlobalError(it.throwable)
                    onEntireLoadingFailed()
                }
            }
        }

        viewModel.submitRegisterLoading.observe(viewLifecycleOwner) {
            if (it) {
                onEntireScreenLoading()
            }
        }

        viewModel.userPhoneUpdate.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    goToHome()
                }
                is Fail -> {
                    handleGlobalError(it.throwable)
                    onEntireLoadingFailed()
                }
            }
        }

        viewModel.userProfileValidate.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showDialogConfirmPhone(it.data)
                }
                is Fail -> {
                    showRegisteredPhoneCheckLoading(false)

                    when (it.throwable) {
                        is AkamaiErrorException -> {
                            showDialogFailed()
                        }
                        else -> {
                            showToasterError(it.throwable)
                        }
                    }
                }
            }
        }
    }

    private fun showGlobalError(errorType: Int) {
        binding?.globalError?.setType(errorType)
    }

    private fun handleGlobalError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> {
                        showGlobalError(GlobalError.NO_CONNECTION)
                    }
                    ReponseStatus.NOT_FOUND -> {
                        showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    }
                    ReponseStatus.INTERNAL_SERVER_ERROR -> {
                        showGlobalError(GlobalError.SERVER_ERROR)
                    }
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                }
            }
        }
        showToasterError(throwable)
    }

    private fun onSuccessRegistered() {
        binding?.apply {
            loaderUnify.hide()
            globalError.hide()
            content.show()
        }
    }

    private fun onEntireLoadingFailed() {
        binding?.apply {
            loaderUnify.hide()
            globalError.show()
            content.hide()
        }
    }

    private fun onEntireScreenLoading() {
        binding?.apply {
            loaderUnify.show()
            globalError.hide()
            content.hide()
        }
    }

    private fun registerPushNotif() {
        context?.let {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(it)
            val isHitRegisterPushNotif = firebaseRemoteConfig.getBoolean(
                RegisterConstants.RemoteConfigKey.REMOTE_CONFIG_KEY_REGISTER_PUSH_NOTIF,
                false
            )
            if (isHitRegisterPushNotif && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                RegisterPushNotificationWorker.scheduleWorker(it)
            }
        }
    }

    private fun actionAfterLoggedIn() {
        registerPushNotif()
        saveFirstInstallTime()
        activity?.let {
            SubmitDeviceWorker.scheduleWorker(requireContext(), true)
            DataVisorWorker.scheduleWorker(it, true)
            AppAuthWorker.scheduleWorker(it, true)
            TwoFactorMluHelper.clear2FaInterval(it)
        }
    }

    private fun saveFirstInstallTime() {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                LoginConstants.PrefKey.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE
            )
            sharedPrefs?.edit()?.putLong(
                LoginConstants.PrefKey.KEY_FIRST_INSTALL_TIME_SEARCH, 0
            )?.apply()
        }
    }

    private fun reloadWithCondition() {
        if (viewModel.registerV2.value is Fail) {
            submitRegisterV2()
        } else if (viewModel.getUserInfo.value is Fail) {
            viewModel.getUserInfo()
        } else if (viewModel.userPhoneUpdate.value is Fail) {
            submitUpdatePhone()
        }
    }

    private fun submitUpdatePhone() {
        onEntireScreenLoading()
        viewModel.userProfileUpdate(binding?.fieldInputPhone?.editText?.text.toString(), paramToken)
    }

    private fun showRegisteredPhoneCheckLoading(isLoading: Boolean) {
        com.tokopedia.abstraction.common.utils.view.KeyboardHandler.hideSoftKeyboard(activity)
        binding?.btnSubmit?.isLoading = isLoading
        binding?.fieldInputPhone?.editText?.isEnabled = !isLoading
    }

    private fun showDialogOfferLogin() {
        val phone = binding?.fieldInputPhone?.editText?.text.toString()
        val offerLoginDialog =
            RegisteredDialog.createRedefineRegisterInputPhoneOfferLogin(requireActivity(), phone)

        offerLoginDialog.setPrimaryCTAClickListener {
            goToPhoneLogin()
            offerLoginDialog.dismiss()
        }

        offerLoginDialog.setSecondaryCTAClickListener {
            offerLoginDialog.dismiss()
        }

        offerLoginDialog.show()
    }

    private fun showDialogConfirmPhone(phone: String) {
        val confirmDialog =
            RegisteredDialog.createRedefineRegisterInputPhoneOfferSuccess(requireActivity(), phone)

        confirmDialog.setPrimaryCTAClickListener {
            goToVerification(
                phone,
                requireActivity()
            )
            confirmDialog.dismiss()
        }

        confirmDialog.setSecondaryCTAClickListener {
            confirmDialog.dismiss()
        }

        confirmDialog.show()
    }

    private fun showDialogFailed() {
        val failedDialog =
            RegisteredDialog.createRedefineRegisterInputPhoneFailed(requireActivity())

        failedDialog.setPrimaryCTAClickListener {
            goToTokopediaCare()
            failedDialog.dismiss()
        }

        failedDialog.setSecondaryCTAClickListener {
            failedDialog.dismiss()
        }

        failedDialog.show()
    }

    private fun goToVerification(phone: String, context: Context) {

        val otpType = if (paramIsRequiresInputPhone) {
            RedefineRegisterEmailConstants.OtpType.OTP_REDEFINE_REGISTER_EMAIL
        } else {
            RedefineRegisterEmailConstants.OtpType.OTP_VERIFICATION_PHONE
        }

        val intent = intentGoToVerification(
            email = paramEmail,
            phone = phone,
            otpType = otpType,
            source = paramSource,
            token = paramToken,
            context = context
        )

        val otpResultCode = if (paramIsRequiresInputPhone) {
            RedefineRegisterEmailConstants.Request.VERIFICATION_PHONE_REGISTER
        } else {
            RedefineRegisterEmailConstants.Request.VERIFICATION_PHONE_UPDATE_PROFILE
        }
        startActivityForResult(intent, otpResultCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RedefineRegisterEmailConstants.Request.VERIFICATION_PHONE_REGISTER -> {
                if (resultCode == Activity.RESULT_OK) {
                    submitRegisterV2()
                }
            }
            RedefineRegisterEmailConstants.Request.VERIFICATION_PHONE_UPDATE_PROFILE -> {
                if (resultCode == Activity.RESULT_OK) {
                    paramToken = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
                    submitUpdatePhone()
                }
            }
        }
    }

    private fun goToPhoneLogin() {
        val intent = intentGoToLoginWithPhone(
            binding?.fieldInputPhone?.editText?.text.toString(),
            paramSource,
            requireActivity()
        )
        startActivity(intent)
    }

    private fun goToHome() {
        val intent = intentGoToHome(requireActivity())
        startActivity(intent)
    }

    private fun goToTokopediaCare() {
        RouteManager.route(
            activity, String.format(
                TOKOPEDIA_CARE_STRING_FORMAT, ApplinkConst.WEBVIEW,
                TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
            )
        )
    }

    private fun TextFieldUnify2.setMessageField(message: String) {
        isInputError = true
        setMessage(message)
    }

    private fun TextFieldUnify2.setMessageField(stringResource: Int) {
        isInputError =
            if (stringResource != RedefineRegisterInputPhoneViewModel.NOTHING_RESOURCE && stringResource != RedefineRegisterInputPhoneViewModel.RESOURCE_NOT_CHANGED) {
                setMessage(getString(stringResource))
                true
            } else {
                setMessage(RedefineRegisterEmailConstants.Common.SPACE)
                false
            }
    }

    private fun showToasterError(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessage(requireActivity(), throwable)
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(RedefineRegisterEmailComponent::class.java).inject(this)
    }

    companion object {

        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
        private const val TOKOPEDIA_CARE_PATH = "help"

        private val SCREEN_NAME = RedefineRegisterInputPhoneFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(bundle: Bundle): Fragment {
            val fragment = RedefineRegisterInputPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}