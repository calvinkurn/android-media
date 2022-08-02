package com.tokopedia.chooseaccount.view.general

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.chooseaccount.common.analytics.LoginPhoneNumberAnalytics
import com.tokopedia.chooseaccount.data.AccountListDataModel
import com.tokopedia.chooseaccount.data.ChooseAccountUiModel
import com.tokopedia.chooseaccount.data.UserDetailDataModel
import com.tokopedia.chooseaccount.di.DaggerChooseAccountComponent
import com.tokopedia.chooseaccount.view.base.BaseChooseAccountFragment
import com.tokopedia.chooseaccount.view.listener.ChooseAccountListener
import com.tokopedia.kotlin.extensions.view.toEmptyStringIfNull
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by nisie on 12/4/17.
 */

open class ChooseAccountFragment : BaseChooseAccountFragment(), ChooseAccountListener {

    private var uiModel: ChooseAccountUiModel? = ChooseAccountUiModel()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: LoginPhoneNumberAnalytics

    private val viewModelProvider by lazy {
        ViewModelProviders.of(this, viewModelFactory)
    }

    private val chooseAccountViewModel by lazy {
        viewModelProvider.get(com.tokopedia.chooseaccount.viewmodel.ChooseAccountViewModel::class.java)
    }

    override fun getScreenName(): String {
        return LoginPhoneNumberAnalytics.Screen.SCREEN_CHOOSE_TOKOCASH_ACCOUNT
    }

    override fun initInjector() {
        if (activity != null) {
            val appComponent = (activity?.application as BaseMainApplication).baseAppComponent
            DaggerChooseAccountComponent
                .builder()
                .baseAppComponent(appComponent)
                .build()
                .inject(this)
        }
    }

    override fun onStart() {
        super.onStart()
        analytics.sendScreen(activity, screenName)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        when {
            savedInstanceState != null -> {
                if(savedInstanceState.containsKey(KEY_UI_MODEL)) {
                    uiModel = savedInstanceState.getParcelable(KEY_UI_MODEL)
                }
            }
            arguments != null -> {
                uiModel?.phoneNumber = arguments?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")?.replace("-", "") ?: ""
                uiModel?.accessToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")?: ""
                uiModel?.loginType = arguments?.getString(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, "") ?: ""
                uiModel?.isFromRegister = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false) ?: false
            }
            activity != null -> activity?.finish()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(savedInstanceState == null || uiModel?.selectedEmail?.isEmpty() == true) {
            getAccountList()
        }
    }

    override fun initObserver() {
        chooseAccountViewModel.getAccountListDataModelPhoneResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAccountList(it.data)
                is Fail -> onErrorGetAccountList(it.throwable)
            }
        })

        chooseAccountViewModel.loginPhoneNumberResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            dismissLoadingProgress()
            when (it) {
                is Success -> onSuccessLoginToken()
                is Fail -> {
                    onErrorLoginToken(it.throwable)
                }
            }
        })

        chooseAccountViewModel.popupError.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it is Success) {
                showPopupError(it.data.header, it.data.body, it.data.action)
            }
        })

        chooseAccountViewModel.activationPage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it is Success){
                onGoToActivationPage(it.data)
            }
        })

        chooseAccountViewModel.securityQuestion.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it is Success){
                onGoToSecurityQuestion()
            }
        })
    }

    private fun open2FA(account: UserDetailDataModel, phone: String) {
        uiModel?.selectedEmail = account.email
        showLoadingProgress()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_AFTER_LOGIN_PHONE)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, account.email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ID_ENC, account.userIdEnc)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ACCESS_TOKEN, uiModel?.accessToken)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ID, account.userId)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, OTP_MODE_PIN);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_RESET_PIN, true);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        startActivityForResult(intent, REQUEST_CODE_PIN_CHALLENGE)
    }

    fun getAccountList() {
        showLoadingProgress()
        LetUtil.ifLet(uiModel?.accessToken, uiModel?.phoneNumber) { (accessToken, phoneNumber) ->
            chooseAccountViewModel.getAccountListPhoneNumber(accessToken, phoneNumber)
        }
    }

    private fun loginToken(email: String?) {
        showLoadingProgress()
        chooseAccountViewModel.loginTokenPhone(uiModel?.key.toEmptyStringIfNull(), email.toEmptyStringIfNull(), uiModel?.phoneNumber.toEmptyStringIfNull())
    }

    private fun onSuccessLoginToken() {
        activity?.apply {
            setResult(Activity.RESULT_OK)
            uiModel?.selectedEmail = ""
            finish()
        }
    }

    private fun onErrorLoginToken(throwable: Throwable) {
        checkExceptionType(throwable)
    }

    private fun onSuccessGetAccountList(accountListDataModel: AccountListDataModel) {
        uiModel?.key = accountListDataModel.key
        uiModel?.phoneNumber = accountListDataModel.msisdn

        if (accountListDataModel.userDetailDataModels.size == 1 && accountListDataModel.msisdn.isNotEmpty()) {
            adapter?.setList(accountListDataModel.userDetailDataModels, accountListDataModel.msisdn)
            val userDetail = accountListDataModel.userDetailDataModels[0]
            if (userDetail.challenge2Fa) {
                open2FA(userDetail, accountListDataModel.msisdn)
            } else {
                loginToken(userDetail.email)
            }
        } else {
            adapter?.setList(accountListDataModel.userDetailDataModels, accountListDataModel.msisdn)
        }
        dismissLoadingProgress()
    }

    protected fun onErrorGetAccountList(e: Throwable) {
        dismissLoadingProgress()
        val errorMessage = ErrorHandler.getErrorMessage(context, e, getErrorHandlerBuilder())
        NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
            showLoadingProgress()
            getAccountList()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PIN_CHALLENGE) {
            if (resultCode == Activity.RESULT_OK) {
                val isResetPin2FA = data?.extras?.getInt(ApplinkConstInternalGlobal.PARAM_SOURCE, 0) == RESULT_CODE_RESET_PIN
                if(isResetPin2FA) {
                    onSuccessLoginToken()
                } else {
                    loginToken(uiModel?.selectedEmail)
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_UI_MODEL, uiModel)
    }

    override fun onSelectedAccount(account: UserDetailDataModel, phone: String) {
        if (account.challenge2Fa) {
            open2FA(account, phone)
        } else {
            loginToken(account.email)
        }
    }

    companion object {
        private const val RESULT_CODE_RESET_PIN = 4
        private const val KEY_SELECTED_EMAIL = "selected_email"
        private const val KEY_UI_MODEL = "ui_model_choose_acc"

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChooseAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
