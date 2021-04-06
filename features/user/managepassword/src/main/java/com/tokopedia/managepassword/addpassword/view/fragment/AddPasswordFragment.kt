package com.tokopedia.managepassword.addpassword.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.addpassword.analytics.AddPasswordAnalytics
import com.tokopedia.managepassword.addpassword.view.viewmodel.AddPasswordViewModel
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.remoteconfig.RemoteConfigInstance
import com.tokopedia.remoteconfig.abtest.AbTestPlatform
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_add_password.*
import javax.inject.Inject

/**
 * @author rival
 * @created 14/05/2020
 * @team : @minion-kevin
 */

class AddPasswordFragment : BaseDaggerFragment() {

    private lateinit var remoteConfigInstance: RemoteConfigInstance

    private var isEnableEncryption = false
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(AddPasswordViewModel::class.java) }

    private val tracker: AddPasswordAnalytics = AddPasswordAnalytics()

    override fun getScreenName(): String {
        return SCREEN_ADD_PASSWORD
    }

    override fun initInjector() {
        getComponent(ManagePasswordComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!userSession.isLoggedIn) {
            activity?.let {
                val intent = RouteManager.getIntent(it, ApplinkConst.LOGIN)
                startActivityForResult(intent, REQUEST_LOGIN)
            }
        }
        initObserver()
        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        if (context != null) {
            val firebaseRemoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)
            isEnableEncryption = firebaseRemoteConfig.getBoolean(SessionConstants.FirebaseConfig.CONFIG_ADD_PASSWORD_ENCRYPTION, false)
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    fun isEnableEncryptRollout(): Boolean {
        val rolloutKey = if(GlobalConfig.isSellerApp()) {
            SessionConstants.Rollout.ROLLOUT_ADD_PASS_ENCRYPTION_SELLER
        } else {
            SessionConstants.Rollout.ROLLOUT_ADD_PASS_ENCRYPTION
        }

        val variant = getAbTestPlatform().getString(rolloutKey)
        return variant.isNotEmpty()
    }

    private fun isUseEncryption(): Boolean {
        return isEnableEncryptRollout() && isEnableEncryption
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hasPasswordChecker()

        txtPassword?.textFieldInput?.afterTextChanged {
            viewModel.validatePassword(it)
        }

        txtPasswordConfirmation?.textFieldInput?.afterTextChanged {
            viewModel.validatePasswordConfirmation(it)
        }

        btnSubmit?.setOnClickListener {
            val password = txtPassword?.textFieldInput?.text?.toString() ?: ""
            val passwordConfirmation = txtPasswordConfirmation?.textFieldInput?.text?.toString() ?: ""

            if (password != passwordConfirmation) {
                txtPasswordConfirmation?.let {
                    setPasswordFieldError(it, resources.getString(R.string.add_password_confirmation_not_match))
                }
            } else if (txtPassword?.isTextFieldError == false && txtPasswordConfirmation?.isTextFieldError == false && password.isNotEmpty()) {
                showLoading()
                btnSubmit?.isEnabled = false
                tracker.onClickSubmit()
                if(isUseEncryption()){
                    viewModel.createPasswordV2(password, passwordConfirmation)
                }else {
                    viewModel.createPassword(password, passwordConfirmation)
                }
            }
        }
    }

    private fun initObserver() {
        viewModel.profileDataModel.observe(this, Observer {
            when(it) {
                is Success -> {
                    hasPasswordProcess(it.data.profileData.isCreatedPassword)
                }
                is Fail -> {
                    val message = it.throwable.message.toString()
                    if (message.isEmpty()) {
                        onError(getString(R.string.message_something_wrong))
                    } else {
                        onError(message)
                    }
                }
            }
        })

        viewModel.validatePassword.observe(this, Observer {
            when (it) {
                is Success -> {
                    txtPassword?.let { txtField ->
                        clearErrorMessage(txtField)
                    }
                }
                is Fail -> {
                    txtPassword?.let { txtField ->
                        setPasswordFieldError(txtField, it.throwable.message.toString())
                    }
                }
            }
        })

        viewModel.validatePasswordConfirmation.observe(this, Observer {
            when (it) {
                is Success -> {
                    txtPasswordConfirmation?.let { txtField ->
                        clearErrorMessage(txtField)
                        btnSubmit?.isEnabled = true
                    }
                }
                is Fail -> {
                    txtPasswordConfirmation?.let { txtField ->
                        setPasswordFieldError(txtField, it.throwable.message.toString())
                    }
                }
            }
        })

        viewModel.response.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessAdd()
                }
                is Fail -> {
                    onFailedAdd(it.throwable.message.toString())
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_LOGIN -> {
                if (!userSession.isLoggedIn) {
                    activity?.apply {
                        setResult(Activity.RESULT_CANCELED)
                        finish()
                    }
                }
            }
        }
    }

    private fun hasPasswordChecker() {
        if (userSession.hasPassword()) {
            activity?.finish()
        } else {
            viewModel.checkPassword()
        }
    }

    private fun hasPasswordProcess(hasPassword: Boolean) {
        if (hasPassword) {
            userSession.setHasPassword(true)
            activity?.finish()
        }
    }

    private fun clearErrorMessage(textFieldUnify: TextFieldUnify) {
        textFieldUnify.setError(false)
        textFieldUnify.setMessage("")
    }

    private fun setPasswordFieldError(textFieldUnify: TextFieldUnify, message: String) {
        textFieldUnify.setError(true)
        textFieldUnify.setMessage(message)
        btnSubmit?.isEnabled = false
    }

    private fun onSuccessAdd() {
        activity?.let {
            userSession.setHasPassword(true)
            tracker.onSuccessAddPassword()
            it.setResult(Activity.RESULT_OK)
            it.finish()
        }
    }

    private fun onFailedAdd(message: String) {
        tracker.onFailedAddPassword(message)
        onError(message)
    }

    private fun onError(message: String) {
        hideLoading()
        btnSubmit?.isEnabled = true
        view?.let {
            Toaster.make(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun showLoading() {
        loader?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        loader?.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.response.removeObservers(this)
    }

    companion object {
        private const val SCREEN_ADD_PASSWORD = "Add password page"
        private const val REQUEST_LOGIN = 1000

        fun createInstance(): AddPasswordFragment {
            return AddPasswordFragment()
        }
    }
}