package com.tokopedia.loginregister.login_sdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.loginregister.login_sdk.LoginSdkUtils.redirectToTargetUri
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.launch

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

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            viewModel.checkLoginStatus()
        }
    }

    private fun initObserver() {
//        viewModel.isLoggedIn.observe(viewLifecycleOwner) {
//            if (it) {
//                goToConsentPage()
//            }
//        }

        viewModel.validateClient.observe(viewLifecycleOwner) {
            when (it ) {
                is Success -> {
                    if (!it.data.status) {
                        redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.data.error)
                    } else {
                        dismissLoadingLogin()
                    }
                }
                is Fail -> {
                    redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.throwable.message ?: "Error")
                }
            }
        }
    }

    private fun goToConsentPage() {
        (activity as LoginSdkActivity).switchToConsentFragment()
    }

    override fun onSuccessLogin(shouldFinish: Boolean) {
        super.onSuccessLogin(shouldFinish = false)
        goToConsentPage()
    }

    override fun goToRegisterInitial(source: String) {
        activity?.let {
            analytics.eventClickRegisterFromLogin()
            var intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INIT_REGISTER)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
            if (GlobalConfig.isSellerApp()) {
                intent = RouteManager.getIntent(context, ApplinkConst.CREATE_SHOP)
            }
            startActivityForResult(intent, LoginConstants.Request.REQUEST_INIT_REGISTER_SDK)
        }
    }

    override fun gotoRegisterEmail(activity: Activity, email: String, isPending: Boolean) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INIT_REGISTER)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SMART_LOGIN, true)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_PENDING, isPending)
        startActivityForResult(intent, LoginConstants.Request.REQUEST_INIT_REGISTER_SDK)
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

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginSdkFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
