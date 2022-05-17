package com.tokopedia.updateinactivephone.features.inputoldphone.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.updateinactivephone.R
import com.tokopedia.updateinactivephone.features.inputoldphone.model.PhoneFormState
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.updateinactivephone.databinding.FragmentInputOldPhoneNumberBinding
import com.tokopedia.updateinactivephone.di.InactivePhoneComponent
import com.tokopedia.updateinactivephone.features.inputoldphone.viewmodel.InputOldPhoneNumberViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class InputOldPhoneNumberFragment : BaseDaggerFragment() {

    private val binding : FragmentInputOldPhoneNumberBinding? by viewBinding()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(InputOldPhoneNumberViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_input_old_phone_number, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        formAction()
        onClickListener()
        formStateObserver()
        statusPhoneNumberObserver()
        isLoadingObserver()
    }

    override fun getScreenName(): String {
        return SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(InactivePhoneComponent::class.java).inject(this)
    }

    private fun formStateObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {

            binding?.textInputOldPhoneNumber?.setMessage(
                if (!it.isDataValid && it.numberError != PhoneFormState.DEFAULT_NUMBER_ERROR)
                    getString(it.numberError)
                else " "
            )

            binding?.textInputOldPhoneNumber?.isInputError = !it.isDataValid
        }
    }

    private fun onClickListener() {
        binding?.ubNext?.setOnClickListener {
            submitData()
        }
    }

    private fun formAction() {
        binding?.textInputOldPhoneNumber?.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitData()
                true
            } else false
        }

        binding?.textInputOldPhoneNumber?.editText?.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                submitData()
                true
            } else false
        }
    }

    private fun statusPhoneNumberObserver() {
        viewModel.statusPhoneNumber.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onGoToInactivePhoneNumber(it.data)
                }
                is Fail -> {
                    onError(it.throwable)
                }
            }
        }
    }

    private fun onGoToInactivePhoneNumber(currentNumber: String) {
        context?.let {
            val intent =
                RouteManager.getIntent(it, ApplinkConstInternalUserPlatform.CHANGE_INACTIVE_PHONE)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, currentNumber)
            startActivity(intent)
        }
    }

    private fun onError(throwable: Throwable) {
        val message = getErrorMsgWithLogging(throwable, withErrorCode = false, flow = "")
        binding?.textInputOldPhoneNumber?.apply {
            setMessage(message)
            isInputError = true
        }
    }

    private fun isLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding?.ubNext?.isLoading = it
        }
    }

    private fun hideKeyboard() {
        activity?.let {
            KeyboardHandler.hideSoftKeyboard(it)
        }
    }

    private fun submitData() {
        hideKeyboard()
        viewModel.submitNumber(binding?.textInputOldPhoneNumber?.getEditableValue().toString())
    }

    private fun getErrorMsgWithLogging(
        throwable: Throwable,
        flow: String,
        withErrorCode: Boolean = true
    ): String {
        val mClassName =
            if (flow.isEmpty()) InputOldPhoneNumberFragment::class.java.name else "${InputOldPhoneNumberFragment::class.java.name} - $flow"
        val message = ErrorHandler.getErrorMessage(context, throwable,
            ErrorHandler.Builder().apply {
                withErrorCode(withErrorCode)
                className = mClassName
            }.build()
        )
        return message
    }

    companion object {

        private val SCREEN_NAME = InputOldPhoneNumberFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(bundle: Bundle) =
            InputOldPhoneNumberFragment().apply {
                arguments = bundle
            }
    }
}