package com.tokopedia.loginregister.login_sdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.header.HeaderUnify
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.domain.pojo.RegisterCheckData
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.sessioncommon.util.LoginSdkUtils.redirectToTargetUri
import com.tokopedia.sessioncommon.util.LoginSdkUtils.setAsLoginSdkFlow
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success

class LoginSdkFragment: LoginEmailPhoneFragment() {

    private var redirectUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        redirectUrl = arguments?.getString("redirect_uri") ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        viewModel.validateClient(
            clientId = arguments?.getString("client_id") ?: "",
            signature = arguments?.getString("sign_cert") ?: "",
            packageName = arguments?.getString("package_name") ?: "",
            redirectUri = redirectUrl
        )
        showLoadingLogin()
    }

    private fun initObserver() {
        viewModel.validateClient.observe(viewLifecycleOwner) {
            when (it ) {
                is Success -> {
                    if (!it.data.status) {
                        redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.data.error)
                    } else {
                        setupAsLoginSdkFlow(it.data.appName)
                        dismissLoadingLogin()
                    }
                }
                is Fail -> {
                    redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.throwable.message ?: "Error")
                }
            }
        }
    }

    override fun setupToolbar() {
        super.setupToolbar()
        activity?.findViewById<HeaderUnify>(R.id.unifytoolbar)?.apply {
            headerTitle = "Masuk ke Tokopedia"
        }
    }

    private fun setupAsLoginSdkFlow(clientName: String) {
        requireContext().setAsLoginSdkFlow(clientName)
        analytics.clientName = clientName
        needHelpAnalytics.clientName = clientName
    }

    private fun goToConsentPage() {
        (activity as LoginSdkActivity).switchToConsentFragment()
    }

    override fun onSuccessLogin() {
        super.onSuccessLogin()
        goToConsentPage()
    }

    override fun goToRegisterInitial(source: String) {
        Toaster.build(viewBinding!!.root, "This feature is not available", Toaster.LENGTH_LONG, type = Toaster.TYPE_ERROR).show()
//        activity?.let {
//            analytics.eventClickRegisterFromLogin()
//            var intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INIT_REGISTER)
//            intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
//            if (GlobalConfig.isSellerApp()) {
//                intent = RouteManager.getIntent(context, ApplinkConst.CREATE_SHOP)
//            }
//            startActivityForResult(intent, LoginConstants.Request.REQUEST_INIT_REGISTER_SDK)
//        }
    }

    override fun gotoRegisterEmail(activity: Activity, email: String, isPending: Boolean) {
        Toaster.build(viewBinding!!.root, "This feature is not available", Toaster.LENGTH_LONG, type = Toaster.TYPE_ERROR).show()
//        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INIT_REGISTER)
//        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
//        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
//        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SMART_LOGIN, true)
//        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_PENDING, isPending)
//        startActivityForResult(intent, LoginConstants.Request.REQUEST_INIT_REGISTER_SDK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            LoginConstants.Request.REQUEST_INIT_REGISTER_SDK -> {
                if (resultCode == Activity.RESULT_OK) {
                    goToConsentPage()
                }
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSuccessRegisterCheck(): (RegisterCheckData) -> Unit {
        return {
            if(it.isExist) {
                super.onSuccessRegisterCheck().invoke(it)
            } else {
                // Handle Error
                dismissLoadingLogin()
                Toaster.build(viewBinding!!.root, "Account is not registered", Toaster.LENGTH_LONG, type = Toaster.TYPE_ERROR).show()
            }
        }
    }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginSdkFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
