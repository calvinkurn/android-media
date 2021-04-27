package com.tokopedia.otp.qrcode.view.fragment

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.otp.common.IOnBackPressed
import com.tokopedia.otp.common.abstraction.BaseOtpToolbarFragment
import com.tokopedia.otp.common.di.OtpComponent
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

    private var uuid: String = ""

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LoginByQrViewModel::class.java)
    }

    override val viewBound = LoginByQrViewBinding()

    override fun getToolbar(): Toolbar = viewBound.toolbar ?: Toolbar(context)

    override fun getScreenName(): String = ""

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

    override fun onResume() {
        super.onResume()
        checkLogin()
    }

    override fun onBackPressed(): Boolean = true

    private fun initVar() {
        arguments?.let {
            uuid = it.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
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
                is Success -> onSuccessVerifyPushNotif().invoke(it.data)
                is Fail -> onFailedVerifyPushNotif().invoke(it.throwable)
            }
        })
    }

    private fun initView() {
        viewBound.userName?.text = userSession.name ?: ""

        viewBound.approveButton?.setOnClickListener {
            verifyQrCode(uuid)
        }
        viewBound.rejectButton?.setOnClickListener {
            verifyQrCode(uuid)
        }
    }

    private fun verifyQrCode(uuid: String) {
        goToResult("asd", uuid, "dfg", "fgh", "ghj")
    }

    private fun onSuccessVerifyPushNotif(): (VerifyQrData) -> Unit {
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

    private fun onFailedVerifyPushNotif(): (Throwable) -> Unit {
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
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.QR_LOGIN_RERSULT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IMG_LINK, imglink)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_TITLE, messageTitle)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MESSAGE_BODY, messageBody)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CTA_TYPE, ctaType)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_STATUS, status)
        startActivity(intent)
        activity?.finish()
    }

    companion object {

        fun createInstance(bundle: Bundle): LoginByQrFragment {
            val fragment = LoginByQrFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}