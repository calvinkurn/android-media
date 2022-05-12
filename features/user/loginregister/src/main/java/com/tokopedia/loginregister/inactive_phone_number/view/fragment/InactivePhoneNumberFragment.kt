package com.tokopedia.loginregister.inactive_phone_number.view.fragment

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.databinding.FragmentInactivePhoneNumberBinding
import com.tokopedia.loginregister.inactive_phone_number.di.InactivePhoneNumberComponent
import com.tokopedia.loginregister.inactive_phone_number.view.viewmodel.InactivePhoneNumberViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
import javax.inject.Inject

class InactivePhoneNumberFragment : BaseDaggerFragment() {

    private val binding : FragmentInactivePhoneNumberBinding? by viewBinding()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    val viewModel by lazy {
        ViewModelProviders.of(
            this,
            viewModelFactory
        ).get(InactivePhoneNumberViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_inactive_phone_number, container, false)
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
        getComponent(InactivePhoneNumberComponent::class.java).inject(this)
    }

    private fun formStateObserver() {
        viewModel.formState.observe(viewLifecycleOwner) {

            binding?.tfu2OldPhoneNumber?.setMessage(
                if (it.numberError != null) getString(it.numberError)
                else " "
            )

            binding?.tfu2OldPhoneNumber?.isInputError = !it.isDataValid
        }
    }

    private fun onClickListener() {
        binding?.ubNext?.setOnClickListener {
            submitData()
        }
    }

    private fun formAction() {
        binding?.tfu2OldPhoneNumber?.editText?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                submitData()
                true
            } else false
        }

        binding?.tfu2OldPhoneNumber?.editText?.setOnKeyListener { _, keyCode, event ->
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
        binding?.tfu2OldPhoneNumber?.apply {
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
        viewModel.submitNumber(binding?.tfu2OldPhoneNumber?.getEditableValue().toString())
    }

    private fun getErrorMsgWithLogging(
        throwable: Throwable,
        flow: String,
        withErrorCode: Boolean = true
    ): String {
        val mClassName =
            if (flow.isEmpty()) InactivePhoneNumberFragment::class.java.name else "${InactivePhoneNumberFragment::class.java.name} - $flow"
        val message = ErrorHandler.getErrorMessage(context, throwable,
            ErrorHandler.Builder().apply {
                withErrorCode(withErrorCode)
                className = mClassName
            }.build()
        )
        return message
    }

    companion object {

        private val SCREEN_NAME = InactivePhoneNumberFragment::class.java.simpleName

        @JvmStatic
        fun newInstance(bundle: Bundle) =
            InactivePhoneNumberFragment().apply {
                arguments = bundle
            }
    }
}