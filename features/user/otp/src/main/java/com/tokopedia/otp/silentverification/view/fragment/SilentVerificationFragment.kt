package com.tokopedia.otp.silentverification.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.otp.R
import com.tokopedia.otp.databinding.FragmentSilentVerificationBinding
import com.tokopedia.otp.silentverification.di.SilentVerificationComponent
import com.tokopedia.otp.silentverification.domain.model.RequestSilentVerificationResult
import com.tokopedia.otp.silentverification.domain.model.ValidateSilentVerificationResult
import com.tokopedia.otp.silentverification.view.viewmodel.SilentVerificationViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import javax.inject.Inject

/**
 * Created by Yoris on 18/10/21.
 */

class SilentVerificationFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SilentVerificationViewModel
    private val binding by viewBinding(FragmentSilentVerificationBinding::bind)

    override fun getScreenName(): String = SILENT_VERIFICATION_SCREEN

    private var phoneNo = ""
    private var correlationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        phoneNo = arguments?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN) ?: ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_silent_verification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()

        if(phoneNo.isNotEmpty()) {
            viewModel.requestSilentVerification(phoneNo)
        } else {
            onErrorVerification(Throwable("Phone can't be empty"))
        }
    }

    override fun initInjector() {
        getComponent(SilentVerificationComponent::class.java).inject(this)
    }

    private fun initObserver() {
        viewModel.requestSilentVerificationResponse.observe(viewLifecycleOwner, {
            when(it) {
                is Success -> onRequestSuccess(it.data)
                is Fail -> {}
            }
        })

        viewModel.bokuVerificationResponse.observe(viewLifecycleOwner, {
            when(it) {
                is Success -> handleBokuResult(it.data)
                is Fail -> onErrorVerification(it.throwable)
            }
        })

        viewModel.validationResponse.observe(viewLifecycleOwner, {
            when(it) {
                is Success -> onSuccessValidate(it.data)
                is Fail -> onErrorVerification(it.throwable)
            }
        })
    }

    private fun onSuccessValidate(data: ValidateSilentVerificationResult) {
        correlationId = ""
        if(data.isVerified) {

        } else {

        }
    }

    private fun onRequestSuccess(data: RequestSilentVerificationResult) {
        activity?.let {
            if (data.evUrl.isNotEmpty()) {
                correlationId = data.correlationId
                viewModel.verify(it, data.evUrl)
            }
        }
    }

    private fun handleBokuResult(resultCode: String) {
        when {
            resultCode == VALID_SCORE -> {
                onSuccessBokuVerification()
            }
            resultCode.startsWith(BOKU_ERROR_PREFIX) -> {
                onFailedBokuVerification(resultCode)
            }
            else -> {
                onErrorVerification(Throwable(message = "Error boku validation"))
            }
        }
    }

    private fun onSuccessBokuVerification() {
        if(phoneNo.isNotEmpty() && correlationId.isNotEmpty()) {
            viewModel.validate(phoneNo = phoneNo, correlationId = correlationId)
        }
    }

    private fun onFailedBokuVerification(resultCode: String) {
        when(resultCode) {

        }
    }

    private fun onErrorVerification(throwable: Throwable) {

    }

    companion object {

        private const val VALID_SCORE = "10"
        private const val BOKU_ERROR_PREFIX = "-50"

        const val SILENT_VERIFICATION_SCREEN = "silentVerification"

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = SilentVerificationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}