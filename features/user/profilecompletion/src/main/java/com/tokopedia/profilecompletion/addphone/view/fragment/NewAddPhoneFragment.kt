package com.tokopedia.profilecompletion.addphone.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImageWithoutPlaceholder
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.profilecompletion.R
import com.tokopedia.profilecompletion.addphone.common.getMessage
import com.tokopedia.profilecompletion.addphone.data.analitycs.NewAddPhoneNumberTracker
import com.tokopedia.profilecompletion.addphone.viewmodel.NewAddPhoneViewModel
import com.tokopedia.profilecompletion.databinding.FragmentNewAddPhoneBinding
import com.tokopedia.profilecompletion.di.ProfileCompletionSettingComponent
import com.tokopedia.unifycomponents.TextFieldUnify2
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class NewAddPhoneFragment : BaseDaggerFragment() {

    private val binding: FragmentNewAddPhoneBinding? by viewBinding()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            NewAddPhoneViewModel::class.java
        )
    }

    @Inject
    lateinit var analytics: NewAddPhoneNumberTracker

    private var validateToken: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_new_add_phone, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadInitImage()
        initListener()
        iniObserver()
        editorChangesListener()
        initToolbar()
        analytics.sendView2FaMluAddPhoneNumberPageEvent()
    }

    private fun initToolbar() {
        binding?.unifyToolbar?.apply {
            actionText = ""
            setNavigationOnClickListener {
                analytics.sendClickOnButtonCloseEvent()
                requireActivity().onBackPressed()
            }
        }
    }

    private fun iniObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {
            binding?.btnSubmit?.isEnabled = true
            binding?.fieldInputPhone?.setMessageField(it)
        }

        viewModel.userProfileValidate.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    goToVerification()
                }
                is Fail -> {
                    val message = it.throwable.getMessage(requireActivity())
                    binding?.fieldInputPhone?.setMessageField(message)
                    analytics.sendClickOnButtonTambahNomorHpEvent(NewAddPhoneNumberTracker.ACTION_FAILED, message)
                }
            }
        }

        viewModel.userPhoneUpdate.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    analytics.sendClickOnButtonTambahNomorHpEvent(NewAddPhoneNumberTracker.ACTION_SUCCESS)
                    onSuccessAddPhone(
                        it.data.first,
                        it.data.second.completionScore
                    )
                }
                is Fail -> {
                    val message = it.throwable.getMessage(requireActivity())
                    analytics.sendClickOnButtonTambahNomorHpEvent(NewAddPhoneNumberTracker.ACTION_FAILED, message)
                    handleGlobalError(it.throwable)
                }
            }
        }

        viewModel.userValidateLoading.observe(viewLifecycleOwner) {
            showValidateLoading(it)
        }

        viewModel.userUpdateLoading.observe(viewLifecycleOwner) {
            if (it) {
                showUpdateLoading()
            }
        }
    }

    private fun initListener() {
        binding?.btnSubmit?.setOnClickListener {
            if (binding?.btnSubmit?.isLoading == false) {
                analytics.sendClickOnButtonTambahNomorHpEvent(NewAddPhoneNumberTracker.ACTION_CLICK)
                submitForm()
            }
        }

        binding?.globalError?.setActionClickListener {
            changePhoneNumber(validateToken)
        }

        binding?.globalError?.setSecondaryActionClickListener {
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
        }
    }

    private fun submitForm() {
        viewModel.submitForm(
            binding?.fieldInputPhone?.editText?.text.toString()
        )
    }

    private fun changePhoneNumber(validateToken: String) {
        val phone = binding?.fieldInputPhone?.editText?.text.toString()
        viewModel.userProfileUpdate(
            phone,
            validateToken
        )
    }

    private fun editorChangesListener() {
        binding?.fieldInputPhone?.editText?.afterTextChanged {
            viewModel.validatePhone(it)
        }
    }

    private fun showGlobalError() {
        binding?.apply {
            loaderUnify.hide()
            globalError.show()
            content.hide()
        }
    }

    private fun showUpdateLoading() {
        binding?.apply {
            loaderUnify.show()
            globalError.hide()
            content.hide()
        }
    }

    private fun showValidateLoading(isLoading: Boolean) {
        KeyboardHandler.hideSoftKeyboard(activity)
        binding?.btnSubmit?.isLoading = isLoading
        binding?.fieldInputPhone?.editText?.isEnabled = !isLoading
    }

    private fun showGlobalError(errorType: Int) {
        binding?.globalError?.setType(errorType)
    }

    private fun handleGlobalError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is MessageErrorException -> {
                showGlobalError(GlobalError.PAGE_NOT_FOUND)
            }
            else -> {
                showGlobalError(GlobalError.SERVER_ERROR)
            }
        }
        showGlobalError()
        showToasterError(throwable)
    }

    private fun showToasterError(throwable: Throwable) {
        val message = throwable.getMessage(requireActivity())
        Toaster.build(requireView(), message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
    }

    private fun TextFieldUnify2.setMessageField(stringResource: Int) {
        isInputError =
            if (stringResource != NewAddPhoneViewModel.EMPTY_RESOURCE && stringResource != NewAddPhoneViewModel.INITIAL_RESOURCE) {
                setMessage(getString(stringResource))
                true
            } else {
                setMessage(" ")
                false
            }
    }

    private fun TextFieldUnify2.setMessageField(message: String) {
        isInputError = true
        setMessage(message)
    }

    private fun loadInitImage() {
        binding?.ivInputPhone?.loadImageWithoutPlaceholder(
            getString(R.string.new_add_phone_init_image)
        )
    }

    override fun getScreenName(): String =
        NewAddPhoneFragment::class.java.simpleName

    override fun initInjector() {
        getComponent(ProfileCompletionSettingComponent::class.java).inject(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_OTP_PHONE_VERIFICATION && resultCode == Activity.RESULT_OK) {
            validateToken = data?.getStringExtra(ApplinkConstInternalGlobal.PARAM_TOKEN).toString()
            changePhoneNumber(validateToken)
        } else {
            analytics.sendClickOnButtonTambahNomorHpEvent(NewAddPhoneNumberTracker.ACTION_FAILED, NewAddPhoneNumberTracker.MESSAGE_FAILED_OTP)
        }
    }

    private fun goToVerification() {
        val phone = binding?.fieldInputPhone?.editText?.text.toString().trim()
        val bundle = Bundle().apply {
            putString(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
            putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_PHONE_VERIFICATION)
            putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        }
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_OTP_PHONE_VERIFICATION)
    }

    private fun onSuccessAddPhone(phone: String, completionScore: Int) {
        val bundle = Bundle().apply {
            putInt(AddPhoneFragment.EXTRA_PROFILE_SCORE, completionScore)
            putString(AddPhoneFragment.EXTRA_PHONE, phone)
            putString(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
        }
        val intent = Intent().putExtras(bundle)
        activity?.run {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    companion object {
        fun newInstance() = NewAddPhoneFragment()

        private const val REQUEST_OTP_PHONE_VERIFICATION = 101
        private const val OTP_TYPE_PHONE_VERIFICATION = 11
    }
}
