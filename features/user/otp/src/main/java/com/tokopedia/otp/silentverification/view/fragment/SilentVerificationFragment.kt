package com.tokopedia.otp.silentverification.view.fragment

import android.animation.Animator
import android.annotation.TargetApi
import android.app.Activity
import android.content.Intent
import android.net.Network
import android.os.Build
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
import com.tkpd.util.Base64
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.otp.R
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.databinding.FragmentSilentVerificationBinding
import com.tokopedia.otp.silentverification.di.SilentVerificationComponent
import com.tokopedia.otp.silentverification.domain.model.RequestSilentVerificationResult
import com.tokopedia.otp.silentverification.helper.NetworkClientHelper
import com.tokopedia.otp.silentverification.view.NetworkRequestListener
import com.tokopedia.otp.silentverification.view.viewmodel.SilentVerificationViewModel
import com.tokopedia.otp.verification.data.OtpData
import com.tokopedia.otp.verification.domain.data.OtpConstant
import com.tokopedia.otp.verification.domain.data.OtpValidateData
import com.tokopedia.otp.verification.domain.pojo.ModeListData
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.noreflection.viewBinding
import java.net.URLDecoder
import java.util.*
import javax.inject.Inject

/**
 * Created by Yoris on 18/10/21.
 */

class SilentVerificationFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: TrackingOtpUtil

    @Inject
    lateinit var networkClientHelper: NetworkClientHelper

    private lateinit var viewModel: SilentVerificationViewModel
    private val binding by viewBinding(FragmentSilentVerificationBinding::bind)
    private var otpData: OtpData? = null
    private var modeListData: ModeListData? = null
    private var lottieTaskList: ArrayList<LottieTask<LottieComposition>> = arrayListOf()
    private var tokenId = ""

    override fun getScreenName(): String = SILENT_VERIFICATION_SCREEN

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupLottieAnimation()

        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        ).get(SilentVerificationViewModel::class.java)

        otpData = arguments?.getParcelable(OtpConstant.OTP_DATA_EXTRA) ?: OtpData()
        modeListData = arguments?.getParcelable(OtpConstant.OTP_MODE_EXTRA) ?: ModeListData()
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
        showFullLoading()
        playLottieAnim(LOTTIE_BG_ANIMATION)
        initObserver()

        if(otpData != null && modeListData != null) {
            analytics.trackClickMethodOtpButton(otpData?.otpType ?: 0, modeListData?.modeText ?: "")
        }

        requestOtp()
    }

    private fun requestOtp() {
        showLoadingState()
        if(context != null) {
            otpData?.run {
                viewModel.requestSilentVerification(
                    otpType = otpType.toString(),
                    mode = modeListData?.modeText ?: "",
                    msisdn = msisdn,
                    signature = generateAuthenticitySignature()
                )
            }
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
                is Fail -> onVerificationError(it.throwable)
            }
        })

        viewModel.validationResponse.observe(viewLifecycleOwner, {
            when(it) {
                is Success -> onValidateSuccess(it.data)
                is Fail -> onVerificationError(it.throwable)
            }
        })
    }

    private fun showLoadingState() {
        binding?.fragmentSilentVerifTitle?.text = getString(R.string.fragment_silent_verif_title)
        binding?.fragmentSilentVerifSubtitle?.text = getString(R.string.fragment_silent_verif_subtitle)
        hideErrorState()
    }

    private fun showErrorState() {
        binding?.fragmentSilentVerifTitle?.text = getString(R.string.fragment_silent_verif_title_fail)
        binding?.fragmentSilentVerifSubtitle?.text = getText(R.string.fragment_silent_verif_subtitle_fail)
        binding?.fragmentSilentVerifTryAgainBtn?.show()
        binding?.fragmentSilentVerifTryChangeMethodBtn?.show()
    }

    private fun hideErrorState() {
        binding?.fragmentSilentVerifTryAgainBtn?.hide()
        binding?.fragmentSilentVerifTryChangeMethodBtn?.hide()
        binding?.fragmentSilentVerifTryAgainBtn?.setOnClickListener {
            requestOtp()
            showLoadingState()
        }
        binding?.fragmentSilentVerifTryChangeMethodBtn?.setOnClickListener {
            activity?.finish()
        }
    }

    private fun setupLottieAnimation() {
        context?.let {
            val lottieBgTask = LottieCompositionFactory.fromUrl(it, LOTTIE_BG_ANIMATION)
            val lottieSuccessTask = LottieCompositionFactory.fromUrl(it, LOTTIE_SUCCESS_ANIMATION)
            lottieTaskList.add(lottieBgTask)
            lottieTaskList.add(lottieSuccessTask)
        }
    }

    private fun showFullLoading() {
        binding?.fragmentSilentVerifLoader?.show()
        binding?.fragmentSilentVerifContainer?.invisible()
    }

    private fun hideFullLoading() {
        binding?.fragmentSilentVerifLoader?.hide()
        binding?.fragmentSilentVerifContainer?.visible()
    }

    private fun playLottieAnim(type: String, onFinish: () -> Unit = {}) {
        try {
            if(lottieTaskList.isNotEmpty()) {
                hideFullLoading()
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

                        binding?.fragmentSilentVerifSuccessAnim?.addAnimatorListener(object:
                            Animator.AnimatorListener {
                            override fun onAnimationRepeat(animation: Animator?) {}
                            override fun onAnimationEnd(animation: Animator?) {
                                onFinish()
                            }
                            override fun onAnimationCancel(animation: Animator?) {}
                            override fun onAnimationStart(animation: Animator?) {}
                        })
                    }
                }
            } else {
                hideFullLoading()
            }
        } catch (e: Exception) {
            hideFullLoading()
        }
    }

    private fun renderSuccessPage(data: OtpValidateData) {
        binding?.fragmentSilentVerifTitle?.hide()
        binding?.fragmentSilentVerifSubtitle?.hide()
        playLottieAnim(LOTTIE_SUCCESS_ANIMATION) {
            renderFinalSuccess()
            Handler().postDelayed({
                onFinishSilentVerif(data)
            }, 1000)
        }
    }

    private fun renderFinalSuccess() {
        context?.run {
            binding?.fragmentSilentVerifTitle?.show()
            binding?.fragmentSilentVerifTitle?.text = getString(R.string.fragment_silent_verif_title_success)
            binding?.fragmentSilentVerifSubtitle?.hide()
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

    private fun onValidateSuccess(data: OtpValidateData) {
        tokenId = ""
        if(data.success) {
            analytics.trackSilentVerificationResult(TrackingOtpConstant.Label.LABEL_SUCCESS)
            renderSuccessPage(data)
        } else {
            analytics.trackSilentVerificationResult("${TrackingOtpConstant.Label.LABEL_FAILED}isSuccess:${data.success} - ${data.errorMessage}")
        }
    }

    private fun onRequestSuccess(data: RequestSilentVerificationResult) {
        activity?.let {
            if (data.evUrl.isNotEmpty() && data.tokenId.isNotEmpty() && data.errorMessage.isEmpty()) {
                tokenId = data.tokenId
                verify(data.evUrl)
            } else {
                onVerificationError(Throwable(data.errorMessage))
            }
        }
    }

    private fun mapBokuResult(query: String): Map<String, String> {
        try {
            val queriesMap: MutableMap<String, String> = LinkedHashMap()
            val pairs = query.split("&").toTypedArray()
            for (pair in pairs) {
                val idx = pair.indexOf("=")
                queriesMap[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] =
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
            }
            return queriesMap
        }catch (e: Exception) {
            onVerificationError(Throwable(message = "Invalid Response"))
        }
        return mapOf()
    }

    private fun handleBokuResult(resultCode: String) {
        try {
            val result = mapBokuResult(resultCode)
            println("verify:$resultCode")
            if (result[KEY_ERROR_CODE] == "0" &&
                result[KEY_ERROR_DESC].equals(VALUE_SUCCESS, true)
            ) {
                onSuccessBokuVerification()
            } else {
                onVerificationError(Throwable("Verification Failed"))
            }
        }catch (e: Exception) {
            onVerificationError(Throwable("Verification Failed"))
        }
    }

    private fun generateAuthenticitySignature(): String =
        Base64.GetDecoder(context).trim()

    private fun onSuccessBokuVerification() {
        otpData?.run {
            if(otpData?.msisdn?.isNotEmpty() == true && tokenId.isNotEmpty()) {

                viewModel.validate(
                    otpType = otpType.toString(),
                    msisdn = msisdn,
                    mode = modeListData?.modeText ?: "",
                    userId = userId,
                    tokenId = tokenId,
                    signature = generateAuthenticitySignature()
                )
            }
        }
    }

    private fun onVerificationError(throwable: Throwable) {
        throwable.printStackTrace()
        showErrorState()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    fun verify(url: String) {
        context?.run {
            networkClientHelper.makeNetworkRequest(this, object: NetworkRequestListener {
                override fun onSuccess(network: Network) {
                    viewModel.verifyBoku(network, url)
                }
                override fun onError(throwable: Throwable) {
                    onVerificationError(Throwable("Network Unavailable"))
                }
            })
        }
    }

    companion object {
        private const val KEY_ERROR_CODE = "ErrorCode"
        private const val KEY_ERROR_DESC = "ErrorDescription"
        private const val KEY_CARRIER = "Carrier"

        private const val VALUE_SUCCESS = "Success"

        private const val LOTTIE_SUCCESS_ANIMATION = "https://assets.tokopedia.net/asts/android/user/silent_verification/silent_verif_success.json"
        private const val LOTTIE_BG_ANIMATION = "https://assets.tokopedia.net/asts/android/user/silent_verification/silent_verif_animation_bg_small.json"

        const val SILENT_VERIFICATION_SCREEN = "silentVerification"

        fun createInstance(bundle: Bundle?): Fragment {
            val fragment = SilentVerificationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}