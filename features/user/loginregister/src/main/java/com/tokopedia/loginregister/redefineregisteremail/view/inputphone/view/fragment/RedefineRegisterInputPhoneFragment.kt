package com.tokopedia.loginregister.redefineregisteremail.view.inputphone.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.devicefingerprint.appauth.AppAuthWorker
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.databinding.FragmentRedefineRegisterInputPhoneBinding
import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants
import com.tokopedia.loginregister.redefineregisteremail.common.analytics.RedefineRegisterEmailAnalytics
import com.tokopedia.loginregister.redefineregisteremail.common.intentGoToHome
import com.tokopedia.loginregister.redefineregisteremail.common.intentGoToLoginWithPhone
import com.tokopedia.loginregister.redefineregisteremail.common.intentGoToVerification
import com.tokopedia.loginregister.redefineregisteremail.common.routedataparam.GoToVerificationParam
import com.tokopedia.loginregister.redefineregisteremail.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.local.RegisterPreferences
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.model.RedefineParamUiModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.data.UserProfileUpdateParam
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.view.viewmodel.RedefineRegisterInputPhoneViewModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.view.viewmodel.RegistrationPhoneState
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sessioncommon.data.register.RegisterV2Param
import com.tokopedia.sessioncommon.util.TwoFactorMluHelper
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
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

    @Inject
    lateinit var firebaseRemoteConfig: RemoteConfig

    @Inject
    lateinit var redefineRegisterEmailAnalytics: RedefineRegisterEmailAnalytics

    private var _binding: FragmentRedefineRegisterInputPhoneBinding? = null
    private val binding get() = _binding

    private val args: RedefineRegisterInputPhoneFragmentArgs by navArgs()
    private var parameter: RedefineParamUiModel = RedefineParamUiModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        parameter = args.parameter
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRedefineRegisterInputPhoneBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.unifyToolbar?.title = getString(R.string.register_email_title)
        showNavigateBackToolbar(true)

        if (!parameter.isRequiredInputPhone) {
            onEntireScreenLoading()
            redefineRegisterEmailAnalytics.sendViewAddPhoneNumberOptionalPageEvent(false)
        } else {
            redefineRegisterEmailAnalytics.sendViewRegisterPageAddPhoneNumberEvent(true)
        }
        initListener()
        registrationProsesObserver()
        formValidateObserver()
        dataValidationObserver()
        editorChangesListener()
        initRegisterRequest()
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(RedefineRegisterEmailComponent::class.java).inject(this)
    }

    private fun showNavigateBackToolbar(isShow: Boolean) {
        binding?.unifyToolbar?.apply {
            isShowBackButton = isShow
            if (isShow) {
                actionText = RedefineRegisterEmailConstants.EMPTY_STRING
                setNavigationOnClickListener {
                    redefineRegisterEmailAnalytics.sendClickOnButtonBackEvent(parameter.isRequiredInputPhone)
                    requireActivity().onBackPressed()
                }
            } else {
                actionText = getString(R.string.register_email_input_phone_skip)
                actionTextView?.setOnClickListener {
                    redefineRegisterEmailAnalytics.sendClickOnButtonLewatiEvent(parameter.isRequiredInputPhone)
                    goToHome()
                }
            }
        }
    }

    override fun onFragmentBackPressed(): Boolean {
        return binding?.unifyToolbar?.isShowBackButton != true
    }

    private fun initRegisterRequest() {
        if (!parameter.isRequiredInputPhone) {
            submitRegisterV2()
        }
    }

    private fun submitRegisterV2() {
        viewModel.registerV2(
            RegisterV2Param(
                regType = REGISTRATION_TYPE,
                email = parameter.email,
                phone = binding?.fieldInputPhone?.editText?.text.toString(),
                fullName = parameter.name,
                password = parameter.password,
                validateToken = parameter.token,
                hash = parameter.hash
            )
        )
    }

    private fun editorChangesListener() {
        binding?.fieldInputPhone?.editText?.afterTextChanged {
            viewModel.validatePhone(it)
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

        if (parameter.isRequiredInputPhone) {
            redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_CLICK, parameter.isRequiredInputPhone)
        } else {
            redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPnPageOptionalEvent(RedefineRegisterEmailAnalytics.ACTION_CLICK, parameter.isRequiredInputPhone)
        }

        viewModel.submitForm(
            binding?.fieldInputPhone?.editText?.text.toString(),
            parameter.email,
            parameter.isRequiredInputPhone
        )
    }

    private fun registrationProsesObserver() {
        viewModel.getUserInfo.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    actionAfterLoggedIn()

                    if (parameter.isRequiredInputPhone) {
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
                    viewModel.getUserInfo()
                    redefineRegisterEmailAnalytics.sendClickOnButtonDaftarEmailEvent(RedefineRegisterEmailAnalytics.ACTION_SUCCESS, parameter.isRequiredInputPhone)
                    onEntireScreenLoading()

                    if (parameter.isRequiredInputPhone) {
                        redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_SUCCESS, parameter.isRequiredInputPhone)
                    }
                }
                is Fail -> {
                    val messageError = ErrorHandler.getErrorMessagePair(context, it.throwable, ErrorHandler.Builder()).first.orEmpty()
                    redefineRegisterEmailAnalytics.sendClickOnButtonDaftarEmailEvent(RedefineRegisterEmailAnalytics.ACTION_FAILED, parameter.isRequiredInputPhone, messageError)

                    if (parameter.isRequiredInputPhone) {
                        redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_FAILED, parameter.isRequiredInputPhone, messageError)
                    }

                    handleGlobalError(it.throwable)
                    onEntireLoadingFailed()
                }
            }
        }

        viewModel.userPhoneUpdate.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.data.errors.isEmpty()) {
                        redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_SUCCESS, parameter.isRequiredInputPhone)
                        goToHome()
                    } else {
                        val messageError = it.data.data.errors.first()
                        redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_FAILED, parameter.isRequiredInputPhone, messageError)
                        onUserPhoneUpdateFailed(MessageErrorException(messageError))
                    }
                }
                is Fail -> {
                    val messageError = ErrorHandler.getErrorMessagePair(context, it.throwable, ErrorHandler.Builder()).first.orEmpty()
                    redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_FAILED, parameter.isRequiredInputPhone, messageError)
                    onUserPhoneUpdateFailed(it.throwable)
                }
            }
        }
    }

    private fun formValidateObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {
            binding?.btnSubmit?.isEnabled = true
            binding?.fieldInputPhone?.setMessageField(it)
        }

        viewModel.submitRegisterLoading.observe(viewLifecycleOwner) {
            if (it) {
                onEntireScreenLoading()
            }
        }
    }

    private fun dataValidationObserver() {
        viewModel.isRegisteredPhone.observe(viewLifecycleOwner) {
            when (it) {
                is RegistrationPhoneState.Loading -> {
                    showRegisteredPhoneCheckLoading(true)
                }
                is RegistrationPhoneState.Registered -> {
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_FAILED, parameter.isRequiredInputPhone, RedefineRegisterEmailAnalytics.MESSAGE_REGISTERED_PHONE)
                    showRegisteredPhoneCheckLoading(false)
                    showDialogOfferLogin()
                }
                is RegistrationPhoneState.Unregistered -> {
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_SUCCESS, parameter.isRequiredInputPhone)
                    showRegisteredPhoneCheckLoading(false)
                    showDialogConfirmPhone(phone = it.message)
                }
                is RegistrationPhoneState.Ineligible -> {
                    showRegisteredPhoneCheckLoading(false)

                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_FAILED, parameter.isRequiredInputPhone, it.message)
                    binding?.fieldInputPhone?.setMessageField(it.message)
                }
                is RegistrationPhoneState.Failed -> {
                    showRegisteredPhoneCheckLoading(false)

                    val message = it.throwable?.let { it1 ->
                        ErrorHandler.getErrorMessagePair(context,
                            it1, ErrorHandler.Builder()).first.orEmpty()
                    }
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_FAILED, parameter.isRequiredInputPhone, message.orEmpty())

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

        viewModel.userProfileValidate.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.data.isValid) {
                        redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPnPageOptionalEvent(RedefineRegisterEmailAnalytics.ACTION_SUCCESS, parameter.isRequiredInputPhone)
                        showDialogConfirmPhone(binding?.fieldInputPhone?.editText?.text.toString())
                    } else {
                        val message = it.data.data.message
                        redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPnPageOptionalEvent(RedefineRegisterEmailAnalytics.ACTION_FAILED, parameter.isRequiredInputPhone, message)
                        onUserProfileValidateFailed(MessageErrorException(message))
                    }
                }
                is Fail -> {
                    val message = ErrorHandler.getErrorMessagePair(context, it.throwable, ErrorHandler.Builder()).first.orEmpty()
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPnPageOptionalEvent(RedefineRegisterEmailAnalytics.ACTION_FAILED, parameter.isRequiredInputPhone, message)
                    onUserProfileValidateFailed(it.throwable)
                }
            }
        }
    }

    private fun registerPushNotif() {
        context?.let {
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
        viewModel.saveFirstInstallTime()
        activity?.let {
            SubmitDeviceWorker.scheduleWorker(requireContext(), true)
            DataVisorWorker.scheduleWorker(it, true)
            AppAuthWorker.scheduleWorker(it, true)
            TwoFactorMluHelper.clear2FaInterval(it)
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
        viewModel.userProfileUpdate(
            UserProfileUpdateParam(
                binding?.fieldInputPhone?.editText?.text.toString(),
                parameter.token
            )
        )
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

    private fun onUserProfileValidateFailed(throwable: Throwable) {
        showRegisteredPhoneCheckLoading(false)

        when (throwable) {
            is AkamaiErrorException -> {
                showDialogFailed()
            }
            else -> {
                showToasterError(throwable)
            }
        }
    }

    private fun onUserPhoneUpdateFailed(throwable: Throwable) {
        handleGlobalError(throwable)
        onEntireLoadingFailed()
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

    private fun showRegisteredPhoneCheckLoading(isLoading: Boolean) {
        KeyboardHandler.hideSoftKeyboard(activity)
        binding?.btnSubmit?.isLoading = isLoading
        binding?.fieldInputPhone?.editText?.isEnabled = !isLoading
    }

    private fun showDialogOfferLogin() {
        val phone = binding?.fieldInputPhone?.editText?.text.toString()
        val offerLoginDialog =
            RegisteredDialog.createRedefineRegisterInputPhoneOfferLogin(requireActivity(), phone)

        offerLoginDialog.setPrimaryCTAClickListener {
            redefineRegisterEmailAnalytics.sendClickYaMasukTerdaftarPhoneNumberEvent(parameter.isRequiredInputPhone)
            goToPhoneLogin()
            offerLoginDialog.dismiss()
        }

        offerLoginDialog.setSecondaryCTAClickListener {
            redefineRegisterEmailAnalytics.sendClickUbahTerdaftarPhoneNumberEvent(parameter.isRequiredInputPhone)
            offerLoginDialog.dismiss()
        }

        redefineRegisterEmailAnalytics.sendViewPopUpPhoneNumberRegisteredPageEvent(parameter.isRequiredInputPhone)
        offerLoginDialog.show()
    }

    private fun showDialogConfirmPhone(phone: String) {
        val confirmDialog =
            RegisteredDialog.createRedefineRegisterInputPhoneOfferSuccess(requireActivity(), phone)

        confirmDialog.setPrimaryCTAClickListener {
            redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(RedefineRegisterEmailAnalytics.ACTION_CLICK, parameter.isRequiredInputPhone)
            goToVerification(
                phone,
                requireActivity()
            )
            confirmDialog.dismiss()
        }

        confirmDialog.setSecondaryCTAClickListener {
            redefineRegisterEmailAnalytics.sendClickUbahBenarPhoneNumberEvent(parameter.isRequiredInputPhone)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RedefineRegisterEmailConstants.VERIFICATION_PHONE_REGISTER -> {
                if (resultCode == Activity.RESULT_OK) {
                    submitRegisterV2()
                }
            }
            RedefineRegisterEmailConstants.VERIFICATION_PHONE_UPDATE_PROFILE -> {
                if (resultCode == Activity.RESULT_OK) {
                    parameter.token = data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
                    submitUpdatePhone()
                }
            }
        }
    }

    private fun goToVerification(phone: String, context: Context) {

        val otpType = if (parameter.isRequiredInputPhone) {
            RedefineRegisterEmailConstants.OTP_REDEFINE_REGISTER_EMAIL
        } else {
            RedefineRegisterEmailConstants.OTP_VERIFICATION_PHONE
        }

        val intent = context.intentGoToVerification(
            GoToVerificationParam(
                email = parameter.email,
                phone = phone,
                otpType = otpType,
                source = parameter.source,
                token = parameter.token
            )
        )

        val otpResultCode = if (parameter.isRequiredInputPhone) {
            RedefineRegisterEmailConstants.VERIFICATION_PHONE_REGISTER
        } else {
            RedefineRegisterEmailConstants.VERIFICATION_PHONE_UPDATE_PROFILE
        }
        startActivityForResult(intent, otpResultCode)
    }

    private fun goToPhoneLogin() {
        val intent = requireActivity().intentGoToLoginWithPhone(
            binding?.fieldInputPhone?.editText?.text.toString(),
            parameter.source
        )
        startActivity(intent)
    }

    private fun goToHome() {
        val intent = requireActivity().intentGoToHome()
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
                setMessage(RedefineRegisterEmailConstants.SPACE)
                false
            }
    }

    private fun showToasterError(throwable: Throwable) {
        val message = ErrorHandler.getErrorMessagePair(context, throwable, ErrorHandler.Builder()).first.orEmpty()
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val REGISTRATION_TYPE = "email"

        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
        private const val TOKOPEDIA_CARE_PATH = "help"

        private val SCREEN_NAME = RedefineRegisterInputPhoneFragment::class.java.simpleName
    }
}