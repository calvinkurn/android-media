package com.tokopedia.otp.qrcode.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.SignatureUtil
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.analytics.TrackingOtpConstant
import com.tokopedia.otp.common.analytics.TrackingOtpUtil
import com.tokopedia.otp.common.di.OtpComponent
import com.tokopedia.otp.notif.data.SignResult
import com.tokopedia.otp.qrcode.domain.pojo.VerifyQrData
import com.tokopedia.otp.qrcode.view.viewbinding.LoginByQrViewBinding
import com.tokopedia.otp.qrcode.viewmodel.LoginByQrViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class LoginByQrFragment: BaseOtpToolbarFragment(), IOnBackPressed {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var analytics: TrackingOtpUtil

    private var uuid: String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LoginByQrViewModel::class.java)
    }

    override val viewBound = LoginByQrViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

    override fun getScreenName(): String = TrackingOtpConstant.Screen.SCREEN_LOGIN_BY_QR_APPROVAL_PAGE

    override fun initInjector() = getComponent(OtpComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkLogin()
        initVar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
    }

    override fun onStart() {
        super.onStart()
        analytics.trackScreen(screenName)
        analytics.trackViewApprovalPage()
    }

    override fun onResume() {
        super.onResume()
        checkLogin()
    }

    override fun onBackPressed(): Boolean {
        analytics.trackClickBackApprovalPage()
        return true
    }

    private fun initVar() {
        arguments?.let {
            uuid = it.getString(PARAM_DATA, "")
        }
    }

    private fun checkLogin() {
        if(!userSession.isLoggedIn) {
            goToLogin()
        }
    }

    private fun initObserver() {
        viewModel.verifyQrResult.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessVerifyQr().invoke(it.data)
                is Fail -> onFailedVerifyQr().invoke(it.throwable)
            }
        })
    }

    private fun initView() {
        viewBound.userName?.text = userSession.name ?: ""

        viewBound.approveButton?.setOnClickListener {
            analytics.trackClickApprovedApprovalPage()
            verifyQrCode(uuid, STATUS_APPROVE)
        }
        viewBound.rejectButton?.setOnClickListener {
            analytics.trackClickRejectedApprovalPage()
            verifyQrCode(uuid, STATUS_REJECT)
        }
    }

    private fun verifyQrCode(uuid: String, status: String) {
        var signResult = SignResult()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            signResult = signDataVerifyQr(uuid, status)
        }
        viewModel.verifyQrCode(uuid, status, signResult.signature)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun signDataVerifyQr(uuid: String, status: String): SignResult {
        return try {
            val datetime = (System.currentTimeMillis() / 1000).toString()
            val data = uuid + status
            if(SignatureUtil.generateKey(LOGIN_QR_ALIAS) != null) {
                SignatureUtil.signData(data, datetime, LOGIN_QR_ALIAS)
            } else {
                SignResult()
            }
        } catch (e: Exception) {
            SignResult()
        }
    }

    private fun onSuccessVerifyQr(): (VerifyQrData) -> Unit {
        return { verifyQrData ->
            goToResult(
                    verifyQrData.imglink,
                    verifyQrData.messageTitle,
                    verifyQrData.messageBody,
                    verifyQrData.buttonType,
                    verifyQrData.status
            )
        }
    }

    private fun onFailedVerifyQr(): (Throwable) -> Unit {
        return { throwable ->
            throwable.printStackTrace()
            LetUtil.ifLet(throwable.message, viewBound.containerView) { (message, containerView) ->
                Toaster.make(containerView as View, message as String, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun goToLogin() {
        val intent = RouteManager.getIntent(context, ApplinkConst.LOGIN)
        startActivity(intent)
        activity?.finish()
    }

    private fun goToResult(imglink: String, messageTitle: String, messageBody: String, ctaType: String, status: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.QR_LOGIN_RESULT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IMG_LINK, imglink)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_TITLE, messageTitle)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY, messageBody)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CTA_TYPE, ctaType)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_STATUS, status)
        startActivity(intent)
        activity?.finish()
    }

    companion object {

        private const val LOGIN_QR_ALIAS = "LoginByQr"
        private const val PARAM_DATA = "data"
        private const val STATUS_APPROVE = "approve"
        private const val STATUS_REJECT = "reject"

        fun createInstance(bundle: Bundle): LoginByQrFragment {
            val fragment = LoginByQrFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}