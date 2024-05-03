package com.tokopedia.loginregister.login.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.tiktok.open.sdk.auth.AuthApi
import com.tiktok.open.sdk.auth.utils.PKCEUtils
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.loginregister.databinding.ActivityLoginTiktokBinding
import com.tokopedia.loginregister.login.di.ActivityComponentFactory
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.view.viewmodel.LoginEmailPhoneViewModel
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class TiktokLoginActivity : BaseActivity(), HasComponent<LoginComponent> {

    private lateinit var authApi: AuthApi

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val loginComponent: LoginComponent by lazy {
        ActivityComponentFactory.instance.createLoginComponent(application)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(
            LoginEmailPhoneViewModel::class.java
        )
    }

    override fun getComponent(): LoginComponent = loginComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginComponent.inject(this)
        val binding = ActivityLoginTiktokBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authApi = AuthApi(activity = this)
        initObserver()

        val codeVerifier = PKCEUtils.generateCodeVerifier()
        saveCodeVerifier(codeVerifier)
        viewModel.authorize(authApi, codeVerifier)
    }

    fun saveCodeVerifier(codeVerifier: String) {
        val pref = getSharedPreferences(KEY_CODE_VERIFIER, Context.MODE_PRIVATE)
        pref.edit().putString("code_verifier", codeVerifier).apply()
    }

    fun getCodeVerifier(): String =
        getSharedPreferences(KEY_CODE_VERIFIER, Context.MODE_PRIVATE).getString("code_verifier", "") ?: ""

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        Toast.makeText(this, "onNewIntent ${intent?.data}", Toast.LENGTH_LONG).show()
        handleAuthResponse(intent!!)
    }

    private fun initObserver() {
        viewModel.loginTokenV2Response.observe(this) {
            if (it is Success) {
                viewModel.getUserInfo()
            }
        }
        viewModel.profileResponse.observe(this) {
//            when (it) {
//                is Success -> onSuccessGetUserInfo(it.data)
//                is Fail -> onErrorGetUserInfo().invoke(it.throwable)
//            }
        }
    }

    private fun handleAuthResponse(intent: Intent) {
        authApi.getAuthResponseFromIntent(intent, ApplinkConstInternalUserPlatform.TIKTOK_LOGIN)?.let {
            val authCode = it.authCode
            if (authCode.isNotEmpty()) {
                Toast.makeText(this, authCode, Toast.LENGTH_LONG).show()
                val intent = Intent().apply {
                    putExtra(KEY_CODE_VERIFIER, getCodeVerifier())
                    putExtra(KEY_AUTH_CODE, authCode)
                }
                setResult(Activity.RESULT_OK, intent)
            } else if (it.errorCode != 0) {
                val description = if (it.errorMsg != null) {
                    it.errorMsg
                } else if (it.authErrorDescription != null) {
                    it.authErrorDescription
                } else {
                    it.authError
                }
                val intent = Intent().apply {
                    putExtra(KEY_ERROR, description)
                }
                setResult(Activity.RESULT_OK, intent)
                Toast.makeText(this, description, Toast.LENGTH_LONG).show()
            }
            finish()
        }
    }

    companion object {
        const val KEY_CODE_VERIFIER = "code_verifier"
        const val KEY_AUTH_CODE = "auth_code"
        const val KEY_ERROR = "error"
    }
}
