package com.tokopedia.managepassword.addpassword.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.afterTextChanged
import com.tokopedia.managepassword.R
import com.tokopedia.managepassword.addpassword.analytics.AddPasswordAnalytics
import com.tokopedia.managepassword.addpassword.view.viewmodel.AddPasswordViewModel
import com.tokopedia.managepassword.databinding.FragmentAddPasswordBinding
import com.tokopedia.managepassword.di.ManagePasswordComponent
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.sessioncommon.constants.SessionConstants
import com.tokopedia.unifycomponents.TextFieldUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author rival
 * @created 14/05/2020
 * @team : @minion-kevin
 */

class AddPasswordFragment : BaseDaggerFragment() {

    private var isEnableEncryption = false
    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(AddPasswordViewModel::class.java) }

    private val tracker: AddPasswordAnalytics = AddPasswordAnalytics()
    private var viewBinding by autoClearedNullable<FragmentAddPasswordBinding>()

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

    private fun isUseEncryption(): Boolean {
        return isEnableEncryption
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding =  FragmentAddPasswordBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        hasPasswordChecker()

        viewBinding?.txtPassword?.textFieldInput?.afterTextChanged {
            viewModel.validatePassword(it)
        }

        viewBinding?.txtPasswordConfirmation?.textFieldInput?.afterTextChanged {
            viewModel.validatePasswordConfirmation(it)
        }

        viewBinding?.btnSubmit?.setOnClickListener {
            val password = viewBinding?.txtPassword?.textFieldInput?.text?.toString().orEmpty()
            val passwordConfirmation = viewBinding?.txtPasswordConfirmation?.textFieldInput?.text?.toString().orEmpty()

            if (password != passwordConfirmation) {
                viewBinding?.txtPasswordConfirmation?.let {
                    setPasswordFieldError(it, resources.getString(R.string.add_password_confirmation_not_match))
                }
            } else if (viewBinding?.txtPassword?.isTextFieldError == false && viewBinding?.txtPasswordConfirmation?.isTextFieldError == false && password.isNotEmpty()) {
                showLoading()
                viewBinding?.btnSubmit?.isEnabled = false
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
        viewModel.profileDataModel.observe(this, {
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

        viewModel.validatePassword.observe(this, {
            when (it) {
                is Success -> {
                    viewBinding?.txtPassword?.let { txtField ->
                        clearErrorMessage(txtField)
                    }
                }
                is Fail -> {
                    viewBinding?.txtPassword?.let { txtField ->
                        setPasswordFieldError(txtField, it.throwable.message.toString())
                    }
                }
            }
        })

        viewModel.validatePasswordConfirmation.observe(this, {
            when (it) {
                is Success -> {
                    viewBinding?.txtPasswordConfirmation?.let { txtField ->
                        clearErrorMessage(txtField)
                        viewBinding?.btnSubmit?.isEnabled = true
                    }
                }
                is Fail -> {
                    viewBinding?.txtPasswordConfirmation?.let { txtField ->
                        setPasswordFieldError(txtField, it.throwable.message.toString())
                    }
                }
            }
        })

        viewModel.response.observe(this, {
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
        viewBinding?.btnSubmit?.isEnabled = false
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
        viewBinding?.btnSubmit?.isEnabled = true
        view?.let {
            Toaster.make(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR)
        }
    }

    private fun showLoading() {
        viewBinding?.loader?.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        viewBinding?.loader?.visibility = View.GONE
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