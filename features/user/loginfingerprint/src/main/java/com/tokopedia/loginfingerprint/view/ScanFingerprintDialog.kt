package com.tokopedia.loginfingerprint.view

import android.annotation.TargetApi
import android.app.Dialog
import android.content.DialogInterface
import android.hardware.fingerprint.FingerprintManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.hardware.fingerprint.FingerprintManagerCompat
import androidx.core.os.CancellationSignal
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.loginfingerprint.R
import com.tokopedia.loginfingerprint.di.DaggerLoginFingerprintComponent
import com.tokopedia.loginfingerprint.di.LoginFingerprintQueryModule
import com.tokopedia.loginfingerprint.di.LoginFingerprintSettingModule
import com.tokopedia.loginfingerprint.listener.ScanFingerprintInterface
import com.tokopedia.loginfingerprint.viewmodel.ScanFingerprintViewModel
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fingerprint_scan_layout.*
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 2020-01-22.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

@TargetApi(Build.VERSION_CODES.M)
class ScanFingerprintDialog(val context: FragmentActivity, val listener: ScanFingerprintInterface?) : BottomSheetUnify() {

    private val viewState: MutableLiveData<Int> = MutableLiveData(STATE_DEFAULT)

    private var counter = 0
    var MAX_ATTEMPTS = 3
    val ANIM_DURATION = 1000L

    val TAG = "ScanFingerprintDialog"
    companion object {
        const val STATE_INVALID = 2
        const val STATE_SUCCESS = 1
        const val STATE_ERROR = 0
        const val STATE_DEFAULT = -1
        const val STATE_LOADING = 3

        const val FP_ERROR_NOT_RECOGNIZED = 100
        const val FP_ERROR_TOO_MANY_ATTEMPT = 101


        const val MODE_LOGIN = "fpLogin"
        const val MODE_REGISTER = "fpRegister"
        const val MODE_VERIFY = "fpVerify"

        var FP_MODE = MODE_LOGIN

        fun isFingerprintAvailable(mContext: FragmentActivity?): Boolean {
            val fingerprintManager = mContext?.getSystemService(FingerprintManager::class.java)
            return fingerprintManager?.isHardwareDetected == true && fingerprintManager.hasEnrolledFingerprints()
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }

    private val viewModel by lazy { viewModelProvider.get(ScanFingerprintViewModel::class.java) }

    private var cancellationSignal: CancellationSignal? = CancellationSignal()

    var fingerprintManager: FingerprintManagerCompat? = null

    init {
        setChild(View.inflate(context, R.layout.fingerprint_scan_layout, null))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setCloseClickListener { dismiss() }
    }

    private fun setupObserver(){
        viewState.observe(this, Observer {
            var text = context.getString(R.string.info_touch_fp)
            var textColor = ContextCompat.getColor(context, R.color.body_text_color)
            when (it) {
                STATE_DEFAULT -> {
                    text = context.getString(R.string.info_touch_fp)
                    textColor = ContextCompat.getColor(context, R.color.body_text_color)
                }
                STATE_SUCCESS -> {
                    text = context.getString(R.string.fp_verified)
                    textColor = ContextCompat.getColor(context, R.color.green_500)
                }
                STATE_INVALID -> {
                    text = context.getString(R.string.error_fp_retry)
                    textColor = ContextCompat.getColor(context, R.color.body_text_error_color)
                }
                STATE_ERROR -> {
                    text = context.getString(R.string.error_default_fp)
                    textColor = ContextCompat.getColor(context, R.color.body_text_error_color)
                }
                STATE_LOADING -> showProgressBar()
            }

            setBodyText(text, textColor)
        })

        viewModel.loginFingerprintResult.observe(this, Observer {
            when (it) {
                is Success -> onSuccessLoginFp()
                is Fail -> onErrorLoginFp(it.throwable)
            }
        })
    }

    private fun onSuccessLoginFp(){
        hideProgressBar()
        viewState.postValue(STATE_SUCCESS)
        listener?.onLoginFingerprintSuccess()
    }

    private fun onErrorLoginFp(e: Throwable){
        animateLottieError {
            startListening()
            hideProgressBar()
            viewState.postValue(STATE_ERROR)
        }
        Toaster.make(activity?.findViewById(android.R.id.content)!!, ErrorHandlerSession.getErrorMessage(context, e), type = Toaster.TYPE_ERROR, duration = Toaster.LENGTH_LONG)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        fingerprintManager = FingerprintManagerCompat.from(context)

        if (fingerprintManager?.isHardwareDetected == true) {
            if (fingerprintManager?.hasEnrolledFingerprints() == true) {
                DaggerLoginFingerprintComponent
                        .builder()
                        .loginFingerprintSettingModule(LoginFingerprintSettingModule())
                        .loginFingerprintQueryModule(LoginFingerprintQueryModule())
                        .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                        .build()
                        .inject(this)
            } else {
                listener?.onFingerprintError(context.getString(R.string.error_no_fp_enrolled), 112)
                        ?: Toaster.make(fingerprint_main_layout, context.getString(R.string.error_no_fp_enrolled), Toaster.LENGTH_LONG, type = Toaster.TYPE_ERROR)
            }
        } else {
            listener?.onFingerprintError(context.getString(R.string.error_no_fp_hardware), 111)
                    ?: Toaster.make(fingerprint_main_layout, context.getString(R.string.error_no_fp_hardware), Toaster.LENGTH_LONG, type = Toaster.TYPE_ERROR)
            dismiss()
        }

        return super.onCreateDialog(savedInstanceState)
}

    override fun onResume() {
        super.onResume()
        startListening()
    }

    fun startListening() {
        cancellationSignal = CancellationSignal()
        fingerprintManager?.authenticate(viewModel.cryptographyUtils.cryptoObject, 0, cancellationSignal, getAuthenticationCallback(), null)
    }

    fun stopListening() {
        if (cancellationSignal != null) {
            cancellationSignal?.cancel()
            cancellationSignal = null
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        stopListening()
        super.onDismiss(dialog)
    }

    override fun onDestroy() {
        stopListening()
        super.onDestroy()
    }

    override fun onPause() {
        stopListening()
        super.onPause()
    }

    fun showWithMode(mode: String) {
        if(mode == MODE_REGISTER || mode == MODE_LOGIN || mode == MODE_REGISTER){
            FP_MODE = mode
            context.supportFragmentManager.run {
                show(this, TAG)
            }
        }else listener?.onFingerprintError("Must pass valid mode", 0)
    }

    private fun showProgressBar(){
        stopListening()
        fingerprint_lottie_layout.visibility = View.GONE
        fingerprint_progressbar.visibility = View.VISIBLE
        fingerprint_body_text.visibility = View.INVISIBLE
    }

    private fun hideProgressBar(){
        fingerprint_lottie_layout.visibility = View.VISIBLE
        fingerprint_progressbar.visibility = View.GONE
        fingerprint_body_text.visibility = View.VISIBLE
    }

    private fun setBodyText(mText: String, mColor: Int) {
        fingerprint_body_text?.apply {
            text = mText
            setTextColor(mColor)
        }
    }

    fun animateLottieSuccess(action: () -> Unit) {
        fingerprint_lottie_error_animation_view?.cancelAnimation()
        fingerprint_lottie_success_animation_view?.visibility = View.VISIBLE
        fingerprint_lottie_error_animation_view?.visibility = View.INVISIBLE
        fingerprint_lottie_success_animation_view?.playAnimation()
        Handler().postDelayed(action, ANIM_DURATION)
    }

    fun animateLottieError(action: () -> Unit) {
        fingerprint_lottie_success_animation_view?.cancelAnimation()
        fingerprint_lottie_success_animation_view?.visibility = View.INVISIBLE
        fingerprint_lottie_error_animation_view?.visibility = View.VISIBLE
        fingerprint_lottie_error_animation_view?.playAnimation()
        Handler().postDelayed(action, ANIM_DURATION)
    }

    private fun handleAuthenticationSuccess(){
        viewState.postValue(STATE_SUCCESS)
        when(FP_MODE) {
            MODE_LOGIN -> {
                viewState.postValue(STATE_LOADING)
                viewModel.validateFingerprint()
            }
            MODE_VERIFY -> {
                listener?.onFingerprintValid()
            }
            MODE_REGISTER -> {
                listener?.onFingerprintValid()
            }
        }
    }

    private fun getAuthenticationCallback(): FingerprintManagerCompat.AuthenticationCallback {
        return object : FingerprintManagerCompat.AuthenticationCallback() {
            override fun onAuthenticationError(errMsgId: Int, errString: CharSequence) {
                animateLottieError {
                    listener?.onFingerprintError(errString.toString(), errMsgId)
                    viewState.postValue(STATE_ERROR)
                }
            }

            override fun onAuthenticationHelp(helpMsgId: Int, helpString: CharSequence) {
                listener?.onFingerprintError(helpString.toString(), helpMsgId)
            }

            override fun onAuthenticationSucceeded(result: FingerprintManagerCompat.AuthenticationResult) {
                animateLottieSuccess {
                    handleAuthenticationSuccess()
                }
                stopListening()
            }

            override fun onAuthenticationFailed() {
                counter++
                if (counter > MAX_ATTEMPTS) {
                    animateLottieError {
                        listener?.onFingerprintError(getString(R.string.error_too_many_attempts), FP_ERROR_TOO_MANY_ATTEMPT)
                        viewState.postValue(STATE_ERROR)
                        dismiss()
                    }
                    stopListening()
                } else {
                    animateLottieError {
                        listener?.onFingerprintError(getString(R.string.error_not_recognized), FP_ERROR_NOT_RECOGNIZED)
                        viewState.postValue(STATE_INVALID)

                    }
                }
                super.onAuthenticationFailed()
            }
        }
    }
}