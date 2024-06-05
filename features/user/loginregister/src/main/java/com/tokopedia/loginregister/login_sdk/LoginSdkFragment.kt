package com.tokopedia.loginregister.login_sdk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.const.LoginConstants
import com.tokopedia.loginregister.login.view.bottomsheet.NeedHelpBottomSheet
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.CLIENT_ID
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.PACKAGE_NAME
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.REDIRECT_URI
import com.tokopedia.loginregister.login_sdk.LoginSdkConstant.SIGN_CERT
import com.tokopedia.sessioncommon.util.LoginSdkUtils.ERR_CODE_API
import com.tokopedia.sessioncommon.util.LoginSdkUtils.ERR_CODE_UNKNOWN
import com.tokopedia.sessioncommon.util.LoginSdkUtils.redirectToTargetUri
import com.tokopedia.sessioncommon.util.LoginSdkUtils.setAsLoginSdkFlow
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.loginregister.R as loginregisterR

class LoginSdkFragment: LoginEmailPhoneFragment() {

    private var redirectUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        redirectUrl = arguments?.getString(REDIRECT_URI) ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        prepareLoginTiktokView()
        showLoadingLogin()
        viewModel.setAsLoginSdkFlow()
        viewModel.validateClient(
            clientId = arguments?.getString(CLIENT_ID) ?: "",
            signature = arguments?.getString(SIGN_CERT) ?: "",
            packageName = arguments?.getString(PACKAGE_NAME) ?: "",
            redirectUri = redirectUrl
        )
    }

    private fun prepareLoginTiktokView() {
        viewBinding?.apply {
            loginOptionTitle.text = getString(loginregisterR.string.login_sdk_or_text)
            socmedBtn.showWithCondition(false)
            registerButton.showWithCondition(false)

            btnLoginGoogle.showWithCondition(true)
            val googleIcon = MethodChecker.getDrawable(requireContext(), R.drawable.ic_login_google)
            btnLoginGoogle.setCompoundDrawablesWithIntrinsicBounds(googleIcon, null, null, null)
            btnLoginGoogle.setOnClickListener {
                onLoginGoogleClick()
            }
        }
    }

    override fun showNeedHelpBottomSheet() {
        if (needHelpBottomSheetUnify == null) {
            needHelpBottomSheetUnify = NeedHelpBottomSheet()
        }
        needHelpBottomSheetUnify?.shouldShowInactivePhone = false
        needHelpBottomSheetUnify?.show(childFragmentManager, TAG_NEED_HELP_BOTTOM_SHEET)
    }

    private fun initObserver() {
        viewModel.validateClient.observe(viewLifecycleOwner) {
            when (it ) {
                is Success -> {
                    if (!it.data.status) {
                        redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.data.error, errorCode = ERR_CODE_API)
                    } else {
                        setupAsLoginSdkFlow(it.data.appName)
                        dismissLoadingLogin()
                    }
                }
                is Fail -> {
                    redirectToTargetUri(requireActivity(), redirectUrl, authCode = "", it.throwable.message ?: ERR_CODE_UNKNOWN, errorCode = ERR_CODE_API)
                }
            }
        }
    }

    override fun autoFillWithDataFromLatestLoggedIn() {
        // no-op, for the sdk this function should be disabled
    }

    override fun checkLoginOption() {
        hideLoadingOverlay()
    }

    override fun setupToolbar() {
        super.setupToolbar()
        activity?.findViewById<HeaderUnify>(R.id.unifytoolbar)?.apply {
            headerTitle = getString(loginregisterR.string.login_sdk_toolbar_login_title)
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
        activity?.let {
            if (!GlobalConfig.isSellerApp()) {
                analytics.eventClickRegisterFromLogin()
                val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.INIT_REGISTER)
                intent.putExtra(ApplinkConstInternalGlobal.PARAM_SOURCE, source)
                startActivityForResult(intent, LoginConstants.Request.REQUEST_INIT_REGISTER_SDK)
            }
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

    override fun clearData() {
        // this function is disabled for login sdk flow to prevent logout
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
