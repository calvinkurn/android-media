package com.tokopedia.loginregister.redefineregisteremail.view.inputphone

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.tokopedia.abstraction.AbstractionRouter
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.devicefingerprint.datavisor.workmanager.DataVisorWorker
import com.tokopedia.devicefingerprint.submitdevice.service.SubmitDeviceWorker
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.error.getMessage
import com.tokopedia.loginregister.common.utils.BasicIdlingResource
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.databinding.FragmentRedefineRegisterInputPhoneBinding
import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants
import com.tokopedia.loginregister.redefineregisteremail.common.analytics.RedefineRegisterEmailAnalytics
import com.tokopedia.loginregister.redefineregisteremail.common.intentGoToHome
import com.tokopedia.loginregister.redefineregisteremail.common.intentGoToLoginWithPhone
import com.tokopedia.loginregister.redefineregisteremail.common.intentGoToVerificationRegister
import com.tokopedia.loginregister.redefineregisteremail.common.intentGoToVerificationUpdatePhone
import com.tokopedia.loginregister.redefineregisteremail.common.routedataparam.GoToVerificationRegisterParam
import com.tokopedia.loginregister.redefineregisteremail.common.routedataparam.GoToVerificationUpdateParam
import com.tokopedia.loginregister.redefineregisteremail.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.param.RedefineParamUiModel
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.domain.param.UserProfileUpdateParam
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.loginregister.registerpushnotif.services.RegisterPushNotificationWorker
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.network.exception.MessageErrorException
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
    lateinit var idlingResource: BasicIdlingResource

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
        loadInitImage()

        binding?.unifyToolbar?.title = getString(R.string.register_email_title)
        showNavigateBackToolbar(true)

        if (!parameter.isRequiredInputPhone) {
            if (viewModel.registerV2.value !is Success) {
                onEntireScreenLoading()
            } else {
                showNavigateBackToolbar(false)
            }
            redefineRegisterEmailAnalytics.sendViewAddPhoneNumberOptionalPageEvent(false)
        } else {
            redefineRegisterEmailAnalytics.sendViewRegisterPageAddPhoneNumberEvent(true)
        }
        initListener()
        registrationProsesObserver()
        formSubmitObserver()
        dataValidationObserver()
        editorChangesListener()
        initRegisterRequest()
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(RedefineRegisterEmailComponent::class.java).inject(this)
    }

    private fun loadInitImage() {
        binding?.ivInputPhone?.loadImageWithoutPlaceholder(
            getString(R.string.redefine_phone_init_image)
        )
    }

    private fun showNavigateBackToolbar(isShow: Boolean) {
        binding?.unifyToolbar?.apply {
            isShowBackButton = isShow
            if (isShow) {
                actionText = ""
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
        if (viewModel.registerV2.value !is Success) {
            val regType = if (parameter.isRequiredInputPhone) {
                REGISTRATION_TYPE_EMAIL_PHONE
            } else {
                REGISTRATION_TYPE_EMAIL
            }

            viewModel.registerV2(
                RegisterV2Param(
                    regType = regType,
                    email = parameter.email,
                    phone = binding?.fieldInputPhone?.editText?.text.toString(),
                    fullName = parameter.name,
                    password = parameter.password,
                    validateToken = parameter.token,
                    hash = parameter.hash
                )
            )
        }
    }

    private fun editorChangesListener() {
        binding?.fieldInputPhone?.editText?.afterTextChanged {
            viewModel.validatePhone(it)
        }
    }

    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener {
            if (binding?.btnSubmit?.isLoading == false) {
                submitForm()
            }
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
            redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(
                RedefineRegisterEmailAnalytics.ACTION_CLICK,
                parameter.isRequiredInputPhone
            )
        } else {
            redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPnPageOptionalEvent(
                RedefineRegisterEmailAnalytics.ACTION_CLICK,
                parameter.isRequiredInputPhone
            )
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
                    redefineRegisterEmailAnalytics.sendClickOnButtonDaftarEmailEvent(
                        RedefineRegisterEmailAnalytics.ACTION_SUCCESS,
                        parameter.isRequiredInputPhone
                    )
                    onEntireScreenLoading()

                    if (parameter.isRequiredInputPhone) {
                        redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(
                            RedefineRegisterEmailAnalytics.ACTION_SUCCESS,
                            parameter.isRequiredInputPhone
                        )
                    }
                }
                is Fail -> {
                    idlingResource.decrement()
                    val messageError = it.throwable.getMessage(requireActivity())
                    redefineRegisterEmailAnalytics.sendClickOnButtonDaftarEmailEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED,
                        parameter.isRequiredInputPhone,
                        messageError
                    )

                    if (parameter.isRequiredInputPhone) {
                        redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(
                            RedefineRegisterEmailAnalytics.ACTION_FAILED,
                            parameter.isRequiredInputPhone,
                            messageError
                        )
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
                        redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(
                            RedefineRegisterEmailAnalytics.ACTION_SUCCESS,
                            parameter.isRequiredInputPhone
                        )
                        goToHome()
                    } else {
                        val messageError = it.data.data.errors.first()
                        redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(
                            RedefineRegisterEmailAnalytics.ACTION_FAILED,
                            parameter.isRequiredInputPhone,
                            messageError
                        )
                        onUserPhoneUpdateFailed(MessageErrorException(messageError))
                    }
                }
                is Fail -> {
                    val messageError = it.throwable.getMessage(requireActivity())
                    redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED,
                        parameter.isRequiredInputPhone,
                        messageError
                    )
                    onUserPhoneUpdateFailed(it.throwable)
                }
            }
        }
    }

    private fun formSubmitObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {
            binding?.btnSubmit?.isEnabled = true
            binding?.fieldInputPhone?.setMessageField(it)
        }

        viewModel.submitRegisterLoading.observe(viewLifecycleOwner) {
            if (it) {
                onEntireScreenLoading()
            }
        }

        viewModel.submitPhoneLoading.observe(viewLifecycleOwner) {
            showRegisteredPhoneCheckLoading(it)
        }
    }

    private fun dataValidationObserver() {
        viewModel.isRegisteredPhone.observe(viewLifecycleOwner) {
            when (it) {
                is RegistrationPhoneState.Registered -> {
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED,
                        parameter.isRequiredInputPhone,
                        RedefineRegisterEmailAnalytics.MESSAGE_REGISTERED_PHONE
                    )
                    showDialogOfferLogin()
                }
                is RegistrationPhoneState.Unregistered -> {
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(
                        RedefineRegisterEmailAnalytics.ACTION_SUCCESS,
                        parameter.isRequiredInputPhone
                    )
                    showDialogConfirmPhone(phone = it.message)
                }
                is RegistrationPhoneState.Ineligible -> {
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED,
                        parameter.isRequiredInputPhone,
                        it.message
                    )
                    binding?.fieldInputPhone?.setMessageField(it.message)
                }
                is RegistrationPhoneState.Failed -> {
                    val message = it.throwable?.getMessage(requireActivity())
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPhoneNumberEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED,
                        parameter.isRequiredInputPhone,
                        message.orEmpty()
                    )

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
                        redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPnPageOptionalEvent(
                            RedefineRegisterEmailAnalytics.ACTION_SUCCESS,
                            parameter.isRequiredInputPhone
                        )
                        showDialogConfirmPhone(binding?.fieldInputPhone?.editText?.text.toString())
                    } else {
                        val message = it.data.data.message
                        redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPnPageOptionalEvent(
                            RedefineRegisterEmailAnalytics.ACTION_FAILED,
                            parameter.isRequiredInputPhone,
                            message
                        )
                        onUserProfileValidateFailed(MessageErrorException(message))
                    }
                }
                is Fail -> {
                    val message = it.throwable.getMessage(requireActivity())
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutAddPnPageOptionalEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED,
                        parameter.isRequiredInputPhone,
                        message
                    )
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
            TwoFactorMluHelper.clear2FaInterval(it)
            initTokoChatConnection()
        }
    }

    private fun initTokoChatConnection() {
        activity?.let {
            if (it.application is AbstractionRouter) {
                (it.application as AbstractionRouter).connectTokoChat(false)
            }
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
            redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(
                RedefineRegisterEmailAnalytics.ACTION_CLICK,
                parameter.isRequiredInputPhone
            )
            if (parameter.isRequiredInputPhone) {
                goToVerificationPhoneRegister(phone)
            } else {
                goToVerificationPhoneUpdate(phone)
            }
            confirmDialog.dismiss()
        }

        confirmDialog.setSecondaryCTAClickListener {
            redefineRegisterEmailAnalytics.sendClickUbahPhoneNumberEvent(parameter.isRequiredInputPhone)
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
                } else {
                    redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED,
                        parameter.isRequiredInputPhone,
                        RedefineRegisterEmailAnalytics.MESSAGE_FAILED_OTP
                    )
                }
            }
            RedefineRegisterEmailConstants.VERIFICATION_PHONE_UPDATE_PROFILE -> {
                if (resultCode == Activity.RESULT_OK) {
                    parameter.token =
                        data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
                    submitUpdatePhone()
                } else {
                    redefineRegisterEmailAnalytics.sendClickYaBenarPhoneNumberEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED,
                        parameter.isRequiredInputPhone,
                        RedefineRegisterEmailAnalytics.MESSAGE_FAILED_OTP
                    )
                }
            }
        }
    }

    private fun goToVerificationPhoneUpdate(phone: String) {
        val otpType = RedefineRegisterEmailConstants.OTP_VERIFICATION_PHONE

        val param = GoToVerificationUpdateParam(
            phone = phone,
            otpType = otpType,
            otpMode = OTP_MODE_SMS,
            source = parameter.source
        )

        val intent = requireActivity().intentGoToVerificationUpdatePhone(param)
        val otpResultCode = RedefineRegisterEmailConstants.VERIFICATION_PHONE_UPDATE_PROFILE
        startActivityForResult(intent, otpResultCode)
    }

    private fun goToVerificationPhoneRegister(phone: String) {
        val otpType = RedefineRegisterEmailConstants.OTP_REDEFINE_REGISTER_EMAIL

        val param = GoToVerificationRegisterParam(
            email = parameter.email,
            phone = phone,
            otpType = otpType,
            otpMode = OTP_MODE_SMS,
            source = parameter.source,
            token = parameter.token
        )

        val intent = requireActivity().intentGoToVerificationRegister(param)

        val otpResultCode = RedefineRegisterEmailConstants.VERIFICATION_PHONE_REGISTER
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
            activity,
            String.format(
                TOKOPEDIA_CARE_STRING_FORMAT,
                ApplinkConst.WEBVIEW,
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
            if (stringResource != RedefineRegisterEmailConstants.EMPTY_RESOURCE && stringResource != RedefineRegisterEmailConstants.INITIAL_RESOURCE) {
                setMessage(getString(stringResource))
                true
            } else {
                setMessage(RedefineRegisterEmailConstants.SPACE)
                false
            }
    }

    private fun showToasterError(throwable: Throwable) {
        val message = throwable.getMessage(requireActivity())
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val OTP_MODE_SMS = "sms"

        private const val REGISTRATION_TYPE_EMAIL = "email"
        private const val REGISTRATION_TYPE_EMAIL_PHONE = "email_phone"

        private const val TOKOPEDIA_CARE_STRING_FORMAT = "%s?url=%s"
        private const val TOKOPEDIA_CARE_PATH = "help"

        private val SCREEN_NAME = RedefineRegisterInputPhoneFragment::class.java.simpleName
    }
}
