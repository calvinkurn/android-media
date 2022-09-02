package com.tokopedia.loginregister.redefine_register_email.input_phone.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.getParamBoolean
import com.tokopedia.kotlin.util.getParamString
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.utils.KeyboardHandler
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.databinding.FragmentRedefineRegisterInputPhoneBinding
import com.tokopedia.loginregister.redefine_register_email.common.RedefineRegisterEmailConstants
import com.tokopedia.loginregister.redefine_register_email.common.intentGoToHome
import com.tokopedia.loginregister.redefine_register_email.common.intentGoToLoginWithPhone
import com.tokopedia.loginregister.redefine_register_email.common.intentGoToVerification
import com.tokopedia.loginregister.redefine_register_email.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel.RedefineRegisterInputPhoneViewModel
import com.tokopedia.loginregister.redefine_register_email.input_phone.view.viewmodel.RegisteredPhoneState
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
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
    private var paramToken = ""
    private var paramHash = ""

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
        showSubmitRegisterLoading(!paramIsRequiresInputPhone)
        initListener()
        initObserver()
        editorChangesListener()
        setUpKeyboardListener(view)
        initRegisterRequest()
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
            reloadLoadRegisterOrGetUserInfo()
        }

        binding?.globalError?.setSecondaryActionClickListener {

        }
    }

    private fun submitForm() {
        viewModel.submitForm(
            binding?.fieldInputPhone?.editText?.text.toString()
        )
    }

    private fun initObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {
            binding?.btnSubmit?.isEnabled = true
            binding?.fieldInputPhone?.setMessageFromResource(it)
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
                    binding?.fieldInputPhone?.setMessageFromString(it.message)
                }
                is RegisteredPhoneState.Failed -> {
                    showRegisteredPhoneCheckLoading(false)

                    when(it.throwable) {
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
                    goToHome()
                }
                is Fail -> {
                    onSubmitRegisterFailed()
                    showToasterError(it.throwable)
                }
            }
        }

        viewModel.registerV2.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showSubmitRegisterLoading(true)
                }
                is Fail -> {
                    onSubmitRegisterFailed()
                    showToasterError(it.throwable)
                }
            }
        }

        viewModel.submitRegisterLoading.observe(viewLifecycleOwner) {
            showSubmitRegisterLoading(it)
        }
    }

    private fun onSubmitRegisterFailed() {
        binding?.loaderUnify?.hide()
        binding?.content?.gone()
        binding?.globalError?.show()
    }

    private fun showSubmitRegisterLoading(isShow: Boolean) {
        if (isShow) {
            binding?.loaderUnify?.show()
            binding?.content?.hide()
            binding?.globalError?.hide()
        } else {
            binding?.loaderUnify?.hide()
            binding?.content?.show()
            binding?.globalError?.hide()
        }
    }

    private fun reloadLoadRegisterOrGetUserInfo() {
        if (viewModel.registerV2.value is Fail) {
            submitRegisterV2()
        } else if (viewModel.getUserInfo.value is Fail) {
            viewModel.getUserInfo()
        }
    }

    private fun showRegisteredPhoneCheckLoading(isLoading: Boolean) {
        com.tokopedia.abstraction.common.utils.view.KeyboardHandler.hideSoftKeyboard(activity)
        binding?.btnSubmit?.isLoading = isLoading
        binding?.fieldInputPhone?.editText?.isEnabled = !isLoading
    }

    private fun showDialogOfferLogin() {
        val phone = binding?.fieldInputPhone?.editText?.text.toString()
        val offerLoginDialog = RegisteredDialog.createRedefineRegisterInputPhoneOfferLogin(requireActivity(), phone)

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
        val confirmDialog = RegisteredDialog.createRedefineRegisterInputPhoneOfferSuccess(requireActivity(), phone)

        confirmDialog.setPrimaryCTAClickListener {
            goToVerification(
                phone,
                otpType = RegisterConstants.OtpType.OTP_REGISTER_PHONE_NUMBER,
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
        val failedDialog = RegisteredDialog.createRedefineRegisterInputPhoneFailed(requireActivity())

        failedDialog.setPrimaryCTAClickListener {
            goToTokopediaCare()
            failedDialog.dismiss()
        }

        failedDialog.setSecondaryCTAClickListener {
            failedDialog.dismiss()
        }

        failedDialog.show()
    }

    private fun goToVerification(phone: String, otpType: Int, context: Context) {
        val intent = intentGoToVerification(
            phone = phone,
            otpType = otpType,
            source = paramSource,
            context = context
        )
        startActivityForResult(intent, RedefineRegisterEmailConstants.Request.VERIFICATION_PHONE_REGISTER)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RedefineRegisterEmailConstants.Request.VERIFICATION_PHONE_REGISTER -> {
                if (resultCode == Activity.RESULT_OK) {
                    submitRegisterV2()
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
        RouteManager.route(activity, String.format(
            TOKOPEDIA_CARE_STRING_FORMAT, ApplinkConst.WEBVIEW,
            TokopediaUrl.getInstance().MOBILEWEB.plus(TOKOPEDIA_CARE_PATH)
        ))
    }

    private fun TextFieldUnify2.setMessageFromString(message: String) {
        isInputError = true
        setMessage(message)
    }

    private fun TextFieldUnify2.setMessageFromResource(stringResource: Int) {
        isInputError = if (stringResource != RedefineRegisterInputPhoneViewModel.NOTHING_RESOURCE && stringResource != RedefineRegisterInputPhoneViewModel.RESOURCE_NOT_CHANGED) {
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
        fun newInstance(bundle: Bundle) : Fragment {
            val fragment = RedefineRegisterInputPhoneFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}