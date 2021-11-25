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
import com.tokopedia.chooseaccount.common.analytics.LoginPhoneNumberAnalytics
import com.tokopedia.chooseaccount.common.di.DaggerLoginRegisterPhoneComponent
import com.tokopedia.chooseaccount.data.AccountListDataModel
import com.tokopedia.chooseaccount.data.ChooseAccountUiModel
import com.tokopedia.chooseaccount.data.UserDetailDataModel
import com.tokopedia.chooseaccount.di.DaggerChooseAccountComponent
import com.tokopedia.chooseaccount.view.base.BaseChooseAccountFragment
import com.tokopedia.chooseaccount.view.listener.ChooseAccountListener
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

/**
 * @author by nisie on 12/4/17.
 */

open class ChooseAccountFragment : BaseChooseAccountFragment(), ChooseAccountListener {

    private var uiModel: ChooseAccountUiModel = ChooseAccountUiModel()
    private var selectedAccount: UserDetailDataModel? = null
    private var selectedPhoneNo: String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var analytics: LoginPhoneNumberAnalytics

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSessionInterface: UserSessionInterface

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
            val loginRegisterPhoneComponent = DaggerLoginRegisterPhoneComponent.builder()
                    .baseAppComponent(appComponent)
                    .build()

            DaggerChooseAccountComponent.builder()
                    .loginRegisterPhoneComponent(loginRegisterPhoneComponent)
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
                uiModel.phoneNumber = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                uiModel.accessToken = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                uiModel.loginType = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, "")
                uiModel.isFromRegister = savedInstanceState.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false)
            }
            arguments != null -> {
                uiModel.phoneNumber = arguments?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                uiModel.accessToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                uiModel.loginType = arguments?.getString(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, "")
                uiModel.isFromRegister = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false) ?: false
            }
            activity != null -> activity?.finish()
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getAccountList()
    }

    override fun initObserver() {
        chooseAccountViewModel.getAccountListDataModelFBResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAccountList(it.data)
                is Fail -> onErrorGetAccountList(it.throwable)
            }
        })

        chooseAccountViewModel.getAccountListDataModelPhoneResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetAccountList(it.data)
                is Fail -> onErrorGetAccountList(it.throwable)
            }
        })

        chooseAccountViewModel.loginPhoneNumberResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessLoginToken()
                is Fail -> {
                    dismissLoadingProgress()
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
        selectedAccount = account
        selectedPhoneNo = phone
        showLoadingProgress()
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.COTP)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_AFTER_LOGIN_PHONE)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_MSISDN, phone)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, account.email)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ID_ENC, account.userIdEnc)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ACCESS_TOKEN, uiModel.accessToken)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ID, account.userId)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, OTP_MODE_PIN);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_RESET_PIN, true);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        startActivityForResult(intent, REQUEST_CODE_PIN_CHALLENGE)
    }

    fun getAccountList() {
        LetUtil.ifLet(uiModel.accessToken, uiModel.phoneNumber) { (accessToken, phoneNumber) ->
            chooseAccountViewModel.getAccountListPhoneNumber(accessToken, phoneNumber)
        }
    }

    private fun loginToken(account: UserDetailDataModel?, phone: String) {
        account?.let {
            LetUtil.ifLet(uiModel.accountListDataModel, uiModel.phoneNumber) { (accountList, phoneNumber) ->
                if (accountList is AccountListDataModel && phoneNumber is String) {
                    chooseAccountViewModel.loginTokenPhone(
                        accountList.key,
                        it.email,
                        phoneNumber
                    )
                }
            }
        }
    }

    private fun onSuccessLoginToken() {
        activity?.apply {
            setResult(Activity.RESULT_OK)
            finish()
        }
    }

    private fun onErrorLoginToken(throwable: Throwable) {
        checkExceptionType(throwable)
        val logException = Throwable("Failed LoginPN using token", throwable)
        logUnknownError(logException)
    }

    private fun onSuccessGetAccountList(accountListDataModel: AccountListDataModel) {
        this.uiModel.accountListDataModel = accountListDataModel

        if (accountListDataModel.userDetailDataModels.size == 1 && accountListDataModel.msisdn.isNotEmpty()) {
            adapter?.setList(accountListDataModel.userDetailDataModels, accountListDataModel.msisdn)
            val userDetail = accountListDataModel.userDetailDataModels[0]
            if (userDetail.challenge2Fa) {
                open2FA(userDetail, accountListDataModel.msisdn)
            } else {
                loginToken(userDetail, accountListDataModel.msisdn)
            }
        } else {
            dismissLoadingProgress()
            adapter?.setList(accountListDataModel.userDetailDataModels, accountListDataModel.msisdn)
        }
    }

    protected fun onErrorGetAccountList(e: Throwable) {
        dismissLoadingProgress()
        val errorMessage = ErrorHandler.getErrorMessage(context, e)
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
                    if (selectedAccount != null && !selectedPhoneNo.isNullOrEmpty()) {
                        loginToken(selectedAccount, selectedPhoneNo ?: "")
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ApplinkConstInternalGlobal.PARAM_UUID, uiModel.accessToken)
        outState.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, uiModel.phoneNumber)
    }

    override fun onDestroy() {
        super.onDestroy()
        chooseAccountViewModel.getAccountListDataModelFBResponse.removeObservers(this)
        chooseAccountViewModel.getAccountListDataModelPhoneResponse.removeObservers(this)
        chooseAccountViewModel.loginPhoneNumberResponse.removeObservers(this)
        chooseAccountViewModel.activationPage.removeObservers(this)
        chooseAccountViewModel.securityQuestion.removeObservers(this)
        chooseAccountViewModel.flush()
    }

    override fun onSelectedAccount(account: UserDetailDataModel, phone: String) {
        showLoadingProgress()
        if (account.challenge2Fa) {
            open2FA(account, phone)
        } else {
            loginToken(account, phone)
        }
    }

    companion object {
        private const val RESULT_CODE_RESET_PIN = 4

        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChooseAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
