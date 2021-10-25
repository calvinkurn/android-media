package com.tokopedia.otp.silentverification.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.airbnb.lottie.LottieTask
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.otp.R
import com.tokopedia.otp.databinding.FragmentSilentVerificationBinding
import com.tokopedia.otp.silentverification.di.SilentVerificationComponent
import com.tokopedia.otp.silentverification.domain.model.RequestSilentVerificationResult
import com.tokopedia.otp.silentverification.view.viewmodel.SilentVerificationViewModel
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.pojo.ModeListData
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

    private var otpData: OtpData? = null
    private var modeListData: ModeListData? = null

    private var lottieTaskList: ArrayList<LottieTask<LottieComposition>> = arrayListOf()

    private var correlationId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        otpData = arguments?.getParcelable(OtpConstant.OTP_DATA_EXTRA) ?: OtpData()
        modeListData = arguments?.getParcelable(OtpConstant.OTP_MODE_EXTRA) ?: ModeListData()
        setupLottieAnimation()
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

        otpData?.run {
            viewModel.requestSilentVerification(
                otpType = otpType.toString(),
                mode = modeListData?.modeText ?: "",
                msisdn = msisdn,
                otpDigit = modeListData?.otpDigit ?: 0
            )
        }

        playLottieAnim(LOTTIE_BG_ANIMATION)
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

    private fun setupLottieAnimation() {
        context?.let {
            val lottieBgTask = LottieCompositionFactory.fromUrl(it, LOTTIE_BG_ANIMATION)
            val lottieSuccessTask = LottieCompositionFactory.fromUrl(it, LOTTIE_SUCCESS_ANIMATION)
            lottieTaskList.add(lottieBgTask)
            lottieTaskList.add(lottieSuccessTask)
        }
    }

    private fun playLottieAnim(type: String) {
        try {
            if(lottieTaskList.isNotEmpty()) {
                if (type == LOTTIE_BG_ANIMATION) {
                    lottieTaskList[0].addListener { result ->
                        binding?.fragmentSilentVerifAnimation?.setComposition(result)
                        binding?.fragmentSilentVerifAnimation?.visibility = View.VISIBLE
                        binding?.fragmentSilentVerifAnimation?.playAnimation()
                        binding?.fragmentSilentVerifAnimation?.repeatCount = LottieDrawable.INFINITE
                    }
                } else {
                    lottieTaskList[1].addListener { result ->
                        binding?.fragmentSilentVerifSuccessAnim?.show()
                        binding?.fragmentSilentVerifSuccessAnim?.setComposition(result)
                        binding?.fragmentSilentVerifSuccessAnim?.visibility = View.VISIBLE
                        binding?.fragmentSilentVerifSuccessAnim?.playAnimation()
                        binding?.fragmentSilentVerifSuccessAnim?.repeatCount = LottieDrawable.INFINITE

                        Handler().postDelayed({
                            renderFinalSuccess()
                        }, 1000)

                    }
                }
            }
        } catch (e: Exception) {

        }
    }

    private fun renderInitialSuccess() {
        binding?.fragmentSilentVerifTitle?.hide()
        binding?.fragmentSilentVerifSubtitle?.hide()
        binding?.fragmentSilentVerifSuccessImg?.hide()
        playLottieAnim(LOTTIE_SUCCESS_ANIMATION)
    }

    private fun renderFinalSuccess() {
        context?.run {
            binding?.fragmentSilentVerifTitle?.text = getString(R.string.fragment_silent_verif_title_success)
            binding?.fragmentSilentVerifSubtitle?.text = getString(R.string.fragment_silent_verif_subtitle_success)
            binding?.fragmentSilentVerifSuccessImg?.show()
            binding?.fragmentSilentVerifSuccessAnim?.hide()
        }
    }

    fun onFinishSilentVerif(data: OtpValidateData) {
        val bundle = Bundle().apply {
            putString(ApplinkConstInternalGlobal.PARAM_UUID, data.validateToken)
            putString(ApplinkConstInternalGlobal.PARAM_MSISDN, otpData?.msisdn)
        }
        val intent = Intent().putExtras(bundle)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun onSuccessValidate(data: OtpValidateData) {
        correlationId = ""
        if(data.success) {
            renderInitialSuccess()
            Handler().postDelayed({
                onFinishSilentVerif(data)
            }, 2500)
        } else {

        }
    }

    private fun onRequestSuccess(data: RequestSilentVerificationResult) {
        activity?.let {
            if (data.evUrl.isNotEmpty() && data.correlationId.isNotEmpty()) {
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
        otpData?.run {
            if(otpData?.msisdn?.isNotEmpty() == true && correlationId.isNotEmpty()) {
                viewModel.validate(
                    otpType = otpType.toString(),
                    msisdn = msisdn,
                    mode = modeListData?.modeText ?: "",
                    userId = userId.toIntOrZero(),
                    correlationId = correlationId
                )
            }
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

        private const val LOTTIE_SUCCESS_ANIMATION = ""
        private const val LOTTIE_BG_ANIMATION = ""

        const val SILENT_VERIFICATION_SCREEN = "silentVerification"

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = SilentVerificationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}