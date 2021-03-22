package com.tokopedia.managepassword.changepassword.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.changepassword.analytics.ChangePasswordAnalytics
import com.tokopedia.managepassword.changepassword.view.viewmodel.ChangePasswordViewModel
import com.tokopedia.managepassword.changepassword.view.viewmodel.LiveDataValidateResult
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
import kotlinx.android.synthetic.main.fragment_change_password.*
import javax.inject.Inject

/**
 * @author rival
 * @created 20/02/2020
 *
 * @team: @minion-kevin
 */

class ChangePasswordFragment : BaseDaggerFragment() {

    private lateinit var remoteConfigInstance: RemoteConfigInstance

    private var isEnableEncryption = false

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(ChangePasswordViewModel::class.java) }

    private var oldPassword = ""
    private var newPassword = ""
    private var confirmationPassword = ""

    override fun getScreenName(): String {
        return ChangePasswordAnalytics.SCREEN_NAME
    }

    override fun initInjector() {
        getComponent(ManagePasswordComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchRemoteConfig()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        setupViewListener()
    }

    private fun setupViewListener() {
        textOldPassword?.textFieldInput?.afterTextChanged {
            oldPassword = it
            textOldPassword?.setError(it.isEmpty())
        }

        textNewPassword?.textFieldInput?.afterTextChanged {
            newPassword = it
            textNewPassword?.setError(it.isEmpty())
        }

        textConfirmationPassword?.textFieldInput?.afterTextChanged {
            confirmationPassword = it
            textConfirmationPassword?.setError(it.isEmpty())

            if (confirmationPassword.isNotEmpty()) {
                buttonSubmit?.isEnabled = true
            }
        }

        buttonSubmit?.setOnClickListener {
            showLoading()
            viewModel.validatePassword(oldPassword, newPassword, confirmationPassword)
        }

        textForgotPassword?.setOnClickListener {
            onGoToForgotPass()
        }
    }

    private fun getAbTestPlatform(): AbTestPlatform {
        if (!::remoteConfigInstance.isInitialized) {
            remoteConfigInstance = RemoteConfigInstance(activity?.application)
        }
        return remoteConfigInstance.abTestPlatform
    }

    private fun fetchRemoteConfig() {
        if (context != null) {
            val firebaseRemoteConfig: RemoteConfig = FirebaseRemoteConfigImpl(context)
            isEnableEncryption = firebaseRemoteConfig.getBoolean(SessionConstants.FirebaseConfig.CONFIG_RESET_PASSWORD_ENCRYPTION, false)
        }
    }

    fun isEnableEncryptRollout(): Boolean {
        val rolloutKey = if(GlobalConfig.isSellerApp()) {
            SessionConstants.Rollout.ROLLOUT_RESET_PASS_ENCRYPTION_SELLER
        } else {
            SessionConstants.Rollout.ROLLOUT_RESET_PASS_ENCRYPTION
        }

        val variant = getAbTestPlatform().getString(rolloutKey)
        return variant.isNotEmpty()
    }

    private fun isUseEncryption(): Boolean {
        return isEnableEncryptRollout() && isEnableEncryption
    }

    private fun initObserver() {
        viewModel.validatePassword.observe(viewLifecycleOwner, {
            when (it) {
                LiveDataValidateResult.VALID -> {
                    if(isUseEncryption()){
                        viewModel.submitChangePasswordV2(newPassword, confirmationPassword)
                    } else{
                        viewModel.submitChangePassword(newPassword, confirmationPassword)
                    }
                }
                LiveDataValidateResult.INVALID_LENGTH -> {
                    onErrorTextField(textNewPassword, getString(R.string.change_password_invalid_length))
                }
                LiveDataValidateResult.SAME_WITH_OLD -> {
                    onErrorTextField(textNewPassword, getString(R.string.change_password_same_with_old))
                }
                LiveDataValidateResult.CONFIRMATION_INVALID -> {
                    onErrorTextField(textConfirmationPassword, getString(R.string.change_password_confirmation_invalid))
                }
            }
        })

        viewModel.response.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessChangePassword()
                is Fail -> onErrorChangePassword(it.throwable.toString())
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_LOGOUT) {
            showDialogGotoLogin(
                    getString(R.string.change_password_app_name),
                    getString(R.string.change_password_success))
        }
    }

    private fun onGoToForgotPass() {
        activity?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.FORGOT_PASSWORD)
            intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.email)
            startActivity(intent)
            it.finish()
        }
    }

    private fun showLoading() {
        progressBar?.visibility = View.VISIBLE
        container?.visibility = View.GONE
    }

    private fun hideLoading() {
        progressBar?.visibility = View.GONE
        container?.visibility = View.VISIBLE
    }

    private fun onErrorTextField(textFieldUnify: TextFieldUnify, message: String) {
        hideLoading()
        buttonSubmit?.isEnabled = false
        textFieldUnify.setError(true)
        textFieldUnify.setMessage(message)
    }

    private fun onSuccessChangePassword() {
        hideLoading()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.LOGOUT)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_RETURN_HOME, false)
        startActivityForResult(intent, REQUEST_LOGOUT)
    }

    private fun onErrorChangePassword(errorMessage: String) {
        view?.let {
            hideLoading()
            if (TextUtils.isEmpty(errorMessage)) {
                Toaster.make(it, getString(R.string.default_request_error_unknown), Toast.LENGTH_LONG)
            } else {
                Toaster.make(it, errorMessage, Toast.LENGTH_LONG)
            }
        }
    }

    private fun showDialogGotoLogin(title: String, message: String) {
        context?.let {
            DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(title)
                setDescription(message)
                setPrimaryCTAText(getString(R.string.change_password_login))
                setPrimaryCTAClickListener {
                    RouteManager.route(it, ApplinkConst.LOGIN)
                }
            }.show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.validatePassword.removeObservers(this)
        viewModel.response.removeObservers(this)
    }

    companion object {
        private const val REQUEST_LOGOUT = 1000
    }
}
