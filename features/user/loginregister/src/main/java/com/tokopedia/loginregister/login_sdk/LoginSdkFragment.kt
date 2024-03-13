package com.tokopedia.loginregister.login_sdk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.tokopedia.loginregister.login.view.fragment.LoginEmailPhoneFragment
import kotlinx.coroutines.launch

class LoginSdkFragment: LoginEmailPhoneFragment() {

    private var redirectUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        redirectUrl = arguments?.getString("redirect_uri") ?: ""
        viewModel.validateClient(arguments?.getString("sign_cert") ?: "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
    }

    private fun checkLoginStatus() {
        lifecycleScope.launch {
            viewModel.checkLoginStatus()
        }
    }

    private fun initObserver() {
        viewModel.isLoggedIn.observe(viewLifecycleOwner) {
            if (it) {
                goToConsentPage()
            }
        }

        viewModel.validateClient.observe(viewLifecycleOwner) {
            if (!it) {
                redirectToTargetUri(redirectUrl, authCode = "", "Invalid Client Certificate")
            }
        }
    }

    private fun goToConsentPage() {
//        Toast.makeText(requireActivity(), "Logged In", Toast.LENGTH_LONG).show()
//        redirectToTargetUri(redirectUrl, "abc123xzz")
    }

    private fun redirectToTargetUri(redirectUrl: String, authCode: String, error: String = "") {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(redirectUrl))
        if (authCode.isNotEmpty()) {
            intent.putExtra("auth_code", authCode)
        }
        if (error.isNotEmpty()) {
            intent.putExtra("error", error)
        }
        startActivity(intent)
        activity?.finish()
    }

    override fun onSuccessLogin(shouldFinish: Boolean) {
        super.onSuccessLogin(shouldFinish)
        redirectToTargetUri(redirectUrl, "abc123xzz")
    }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = LoginSdkFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
