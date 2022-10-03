package com.tokopedia.loginregister.redefineregisteremail.view.registeremail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.error.getMessage
import com.tokopedia.loginregister.common.utils.KeyboardHandler
import com.tokopedia.loginregister.common.view.dialog.RegisteredDialog
import com.tokopedia.loginregister.common.view.emailextension.adapter.EmailExtensionAdapter
import com.tokopedia.loginregister.databinding.FragmentRedefineRegisterEmailBinding
import com.tokopedia.loginregister.redefineregisteremail.common.RedefineRegisterEmailConstants
import com.tokopedia.loginregister.redefineregisteremail.common.analytics.RedefineRegisterEmailAnalytics
import com.tokopedia.loginregister.redefineregisteremail.common.intentGoToLoginWithEmail
import com.tokopedia.loginregister.redefineregisteremail.common.intentGoToVerificationRegister
import com.tokopedia.loginregister.redefineregisteremail.common.routedataparam.GoToVerificationRegisterParam
import com.tokopedia.loginregister.redefineregisteremail.di.RedefineRegisterEmailComponent
import com.tokopedia.loginregister.redefineregisteremail.view.inputphone.data.param.RedefineParamUiModel
import com.tokopedia.loginregister.redefineregisteremail.view.registeremail.domain.data.ValidateUserData
import com.tokopedia.loginregister.registerinitial.const.RegisterConstants
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class RedefineRegisterEmailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            RedefineRegisterEmailViewModel::class.java
        )
    }

    @Inject
    lateinit var redefineRegisterEmailAnalytics: RedefineRegisterEmailAnalytics

    private var binding: FragmentRedefineRegisterEmailBinding? = null

    private var paramSource: String = ""
    private var paramIsRequiredInputPhone: Boolean = false
    private var isExtensionSelected = false
    private var emailExtensionList = mutableListOf<String>()
    private var paramToken = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intentExtras = requireActivity().intent.extras

        paramSource = intentExtras?.getString(ApplinkConstInternalGlobal.PARAM_SOURCE).orEmpty()
        paramIsRequiredInputPhone =
            intentExtras?.getBoolean(ApplinkConstInternalUserPlatform.PARAM_IS_REGISTER_REQUIRED_INPUT_PHONE)
                .orFalse()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRedefineRegisterEmailBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        redefineRegisterEmailAnalytics.sendViewRegisterPageEnterDataEvent(paramIsRequiredInputPhone)
        setUpValue()
        setUpToolbar()
        setUpField()
        initListener()
        initObserver()
        initEmailExtension(view)
        editorChangesListener()
    }

    override fun getScreenName(): String = SCREEN_NAME

    override fun initInjector() {
        getComponent(RedefineRegisterEmailComponent::class.java).inject(this)
    }

    private fun setUpValue() {
        binding?.apply {
            if (viewModel.currentEmail.isNotEmpty()) {
                fieldEmail.editText.setText(viewModel.currentEmail)
            }
            if (viewModel.currentPassword.isNotEmpty()) {
                fieldPassword.editText.setText(viewModel.currentPassword)
            }
            if (viewModel.currentName.isNotEmpty()) {
                fieldName.editText.setText(viewModel.currentName)
            }
            btnSubmit.isEnabled =
                viewModel.currentEmail.isNotEmpty() || viewModel.currentPassword.isNotEmpty() || viewModel.currentName.isNotEmpty()
        }
    }

    private fun setUpToolbar() {
        binding?.unifyToolbar?.apply {
            title = getString(R.string.register_email_title)
            setNavigationOnClickListener {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun initEmailExtension(view: View) {
        setUpKeyboardListener(view)

        emailExtensionList.clear()
        emailExtensionList.addAll(requireContext().resources.getStringArray(R.array.email_extension))
        emailExtensionList.let { it ->
            binding?.emailExtension?.setExtensions(
                it,
                object : EmailExtensionAdapter.ClickListener {
                    override fun onExtensionClick(extension: String, position: Int) {
                        val charEmail: Array<String> =
                            binding?.fieldEmail?.editText?.text.toString().split(
                                DELIMITER_EMAIL
                            ).toTypedArray()
                        if (charEmail.isNotEmpty()) {
                            binding?.fieldEmail?.editText?.setText(
                                String.format(
                                    STRING_FORMAT_EMAIL,
                                    charEmail[0],
                                    extension
                                )
                            )
                        } else {
                            binding?.fieldEmail?.editText?.setText(
                                String.format(
                                    STRING_FORMAT_EMAIL,
                                    binding?.fieldEmail?.editText?.text.toString().replace(
                                        DELIMITER_EMAIL, ""
                                    ),
                                    extension
                                )
                            )
                        }
                        binding?.fieldEmail?.editText?.setSelection(
                            binding?.fieldEmail?.editText?.text.toString()
                                .trim { it <= RedefineRegisterEmailConstants.CHAR_SPACE }.length
                        )
                        isExtensionSelected = true
                        showEmailExtension(false)
                    }
                })
        }
    }

    private fun setUpKeyboardListener(view: View) {
        KeyboardHandler(view, object : KeyboardHandler.OnKeyBoardVisibilityChangeListener {
            override fun onKeyboardShow() {
                if (binding?.fieldEmail?.editText?.text.toString()
                        .contains(DELIMITER_EMAIL) && !isExtensionSelected && binding?.fieldEmail?.editText?.isFocused == true
                ) {
                    showEmailExtension(true)
                }
            }

            override fun onKeyboardHide() {
                showEmailExtension(false)
            }
        })
    }

    private fun showEmailExtension(isShow: Boolean) {
        binding?.emailExtension?.showWithCondition(isShow)
    }

    private fun editorChangesListener() {
        binding?.fieldEmail?.editText?.afterTextChanged {
            viewModel.validateEmail(it)

            setUpEmailExtension(it)
        }

        binding?.fieldPassword?.editText?.afterTextChanged {
            viewModel.validatePassword(it)
        }

        binding?.fieldName?.editText?.afterTextChanged {
            viewModel.validateName(it)
        }
    }

    private fun setUpEmailExtension(email: String) {
        if (binding?.fieldEmail?.editText?.isFocused == true && email.contains(DELIMITER_EMAIL)) {
            val charEmail = email.split(DELIMITER_EMAIL).toTypedArray()
            if (charEmail.size > 1) {
                binding?.emailExtension?.filterExtensions(charEmail[1])
            } else {
                binding?.emailExtension?.updateExtensions(emailExtensionList)
            }
            isExtensionSelected = false
            showEmailExtension(true)
        } else {
            showEmailExtension(false)
        }
    }

    private fun setUpField() {
        binding?.apply {
            fieldEmail.editText.apply {
                imeOptions = EditorInfo.IME_ACTION_NEXT
                isSaveEnabled = false
            }
            fieldPassword.editText.apply {
                imeOptions = EditorInfo.IME_ACTION_NEXT
                isSaveEnabled = false
            }
            fieldName.editText.apply {
                imeOptions = EditorInfo.IME_ACTION_DONE
                isSaveEnabled = false
                inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_WORDS
            }
        }
    }

    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener {
            if (binding?.btnSubmit?.isLoading == false) {
                redefineRegisterEmailAnalytics.sendClickOnButtonLanjutEvent(
                    RedefineRegisterEmailAnalytics.ACTION_CLICK, paramIsRequiredInputPhone
                )
                submitForm()
            }
        }
    }

    private fun submitForm() {
        viewModel.submitForm(
            binding?.fieldEmail?.editText?.text.toString(),
            binding?.fieldPassword?.editText?.text.toString(),
            binding?.fieldName?.editText?.text.toString()
        )
    }

    private fun initObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {
            binding?.btnSubmit?.isEnabled = true
            binding?.fieldEmail?.setMessageField(it.emailError)
            binding?.fieldPassword?.setMessageField(it.passwordError)
            binding?.fieldName?.setMessageField(it.nameError)
        }

        viewModel.validateUserData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    it.data.apply {
                        if (isValid) {
                            goToVerificationEmail()
                        } else {
                            onFieldError(this)
                        }
                    }
                }
                is Fail -> {
                    val message = it.throwable.getMessage(requireActivity())
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED,
                        paramIsRequiredInputPhone,
                        message
                    )
                    showToasterError(message)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding?.apply {
                btnSubmit.isLoading = it
                fieldEmail.editText.isEnabled = !it
                fieldPassword.editText.isEnabled = !it
                fieldName.editText.isEnabled = !it
            }
            binding?.btnSubmit?.isLoading = it

            com.tokopedia.abstraction.common.utils.view.KeyboardHandler.hideSoftKeyboard(activity)
        }
    }

    private fun onFieldError(data: ValidateUserData) {
        data.run {
            if (isExist) {
                showLoginDialog()
            }

            val listErrorMessage = ArrayList<String>()
            if (error.isNotEmpty()) listErrorMessage.add(error)
            if (errorEmail.isNotEmpty()) listErrorMessage.add(errorEmail)
            if (errorPassword.isNotEmpty()) listErrorMessage.add(errorPassword)
            if (errorFullName.isNotEmpty()) listErrorMessage.add(errorFullName)

            val allErrorMessage = listErrorMessage.joinToString(separator = SEPARATOR_MESSAGE_ERROR)

            redefineRegisterEmailAnalytics.sendClickOnButtonLanjutEvent(
                RedefineRegisterEmailAnalytics.ACTION_FAILED,
                paramIsRequiredInputPhone,
                allErrorMessage
            )

            if (error.isNotEmpty()) {
                showToasterError(error)
            }

            binding?.fieldEmail?.setMessageField(errorEmail)
            binding?.fieldName?.setMessageField(errorFullName)
            binding?.fieldPassword?.setMessageField(errorPassword)
        }
    }

    private fun showToasterError(message: String) {
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
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

    private fun TextFieldUnify2.setMessageField(message: String) {
        isInputError = if (message.isNotEmpty()) {
            setMessage(message)
            true
        } else {
            setMessage(RedefineRegisterEmailConstants.SPACE)
            false
        }
    }

    private fun showLoginDialog() {
        val email = binding?.fieldEmail?.editText?.text.toString()
        val offerToLoginDialog =
            RegisteredDialog.createRedefineRegisterEmailOfferLogin(requireActivity(), email)

        offerToLoginDialog.setPrimaryCTAClickListener {
            offerToLoginDialog.dismiss()
            goToLoginEmailPage(email)
        }

        offerToLoginDialog.setSecondaryCTAClickListener {
            offerToLoginDialog.dismiss()
        }

        redefineRegisterEmailAnalytics.sendViewPopUpEmailRegisteredPageEvent(
            paramIsRequiredInputPhone
        )
        offerToLoginDialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            RedefineRegisterEmailConstants.VERIFICATION_EMAIL -> {
                if (resultCode == Activity.RESULT_OK) {

                    paramToken =
                        data?.extras?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN).orEmpty()
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutEvent(
                        RedefineRegisterEmailAnalytics.ACTION_SUCCESS, paramIsRequiredInputPhone
                    )
                    goToInputPhone()
                } else {
                    redefineRegisterEmailAnalytics.sendClickOnButtonLanjutEvent(
                        RedefineRegisterEmailAnalytics.ACTION_FAILED, paramIsRequiredInputPhone,
                        RedefineRegisterEmailAnalytics.MESSAGE_FAILED_OTP
                    )
                }
            }
        }
    }

    private fun goToVerificationEmail() {
        val intent = requireActivity().intentGoToVerificationRegister(
            GoToVerificationRegisterParam(
                email = viewModel.currentEmail,
                otpType = RegisterConstants.OtpType.OTP_TYPE_REGISTER,
                otpMode = OTP_MODE_EMAIL,
                source = paramSource
            )
        )
        startActivityForResult(intent, RedefineRegisterEmailConstants.VERIFICATION_EMAIL)
    }

    private fun goToInputPhone() {

        handleNakamaRegistration()

        val parameter = RedefineParamUiModel(
            source = paramSource,
            email = viewModel.currentEmail,
            password = viewModel.encryptedPassword,
            name = viewModel.currentName,
            isRequiredInputPhone = paramIsRequiredInputPhone,
            token = paramToken,
            hash = viewModel.currentHash
        )

        val toRedefineRegisterInputPhoneNumber =
            RedefineRegisterEmailFragmentDirections.actionRedefineRegisterEmailFragmentToRedefineRegisterInputPhoneFragment(
                parameter = parameter
            )
        view?.findNavController()?.navigate(toRedefineRegisterInputPhoneNumber)
    }

    private fun handleNakamaRegistration() {
        if (GlobalConfig.DEBUG) {
            val partsEmail = viewModel.currentEmail.split(DELIMITER_EMAIL)
            if (partsEmail.size == SPLIT_EMAIL_SIZE && !partsEmail.first()
                    .contains(STRING_TESTING_REGISTER) && partsEmail[1] == DOMAIN_TOKOPEDIA_EMAIL
            ) {
                paramIsRequiredInputPhone = false
            }
        }
    }

    private fun goToLoginEmailPage(email: String) {
        val intent = requireActivity().intentGoToLoginWithEmail(email, paramSource)
        startActivity(intent)
        activity?.finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    companion object {
        private const val SEPARATOR_MESSAGE_ERROR = ", "
        private const val OTP_MODE_EMAIL = "email"
        private const val STRING_FORMAT_EMAIL = "%s@%s"
        private const val DELIMITER_EMAIL = "@"
        private val SCREEN_NAME = RedefineRegisterEmailFragment::class.java.simpleName

        private const val SPLIT_EMAIL_SIZE = 2
        private const val STRING_TESTING_REGISTER = "testreg"
        private const val DOMAIN_TOKOPEDIA_EMAIL = "tokopedia.com"
    }
}
