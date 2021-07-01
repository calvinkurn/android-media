package com.tokopedia.loginphone.chooseaccount.view.general

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.mapper.TkpdAppsFlyerMapper
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.util.LetUtil
import com.tokopedia.linker.LinkerConstants
import com.tokopedia.linker.LinkerManager
import com.tokopedia.linker.LinkerUtils
import com.tokopedia.linker.model.UserData
import com.tokopedia.loginphone.chooseaccount.data.AccountListDataModel
import com.tokopedia.loginphone.chooseaccount.data.UserDetailDataModel
import com.tokopedia.loginphone.chooseaccount.di.DaggerChooseAccountComponent
import com.tokopedia.loginphone.chooseaccount.view.base.BaseChooseAccountFragment
import com.tokopedia.loginphone.chooseaccount.view.listener.ChooseAccountListener
import com.tokopedia.loginphone.chooseaccount.viewmodel.ChooseAccountViewModel
import com.tokopedia.loginphone.common.analytics.LoginPhoneNumberAnalytics
import com.tokopedia.loginphone.common.di.DaggerLoginRegisterPhoneComponent
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sessioncommon.data.LoginToken
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.track.TrackApp
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import java.util.*
import javax.inject.Inject
import javax.inject.Named
import kotlin.collections.HashMap

/**
 * @author by nisie on 12/4/17.
 */

open class ChooseAccountFragment : BaseChooseAccountFragment(), ChooseAccountListener {

    private var viewModel: com.tokopedia.loginphone.chooseaccount.data.ChooseAccountViewModel = com.tokopedia.loginphone.chooseaccount.data.ChooseAccountViewModel()
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
        viewModelProvider.get(ChooseAccountViewModel::class.java)
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
                viewModel.phoneNumber = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                viewModel.accessToken = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                viewModel.loginType = savedInstanceState.getString(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, "")
                viewModel.isFromRegister = savedInstanceState.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false)
                viewModel.isFacebook = savedInstanceState.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FACEBOOK, false)
            }
            arguments != null -> {
                viewModel.phoneNumber = arguments?.getString(ApplinkConstInternalGlobal.PARAM_MSISDN, "")
                viewModel.accessToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_UUID, "")
                viewModel.loginType = arguments?.getString(ApplinkConstInternalGlobal.PARAM_LOGIN_TYPE, "")
                viewModel.isFromRegister = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FROM_REGISTER, false) ?: false
                viewModel.isFacebook = arguments?.getBoolean(ApplinkConstInternalGlobal.PARAM_IS_FACEBOOK, false) ?: false
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
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ACCESS_TOKEN, viewModel.accessToken)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_USER_ID, account.userId)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, false)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_REQUEST_OTP_MODE, OTP_MODE_PIN);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_RESET_PIN, true);
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_IS_LOGIN_REGISTER_FLOW, true)
        startActivityForResult(intent, REQUEST_CODE_PIN_CHALLENGE)
    }

    fun getAccountList() {
        when (viewModel.loginType) {
            FACEBOOK_LOGIN_TYPE -> {
                viewModel.accessToken?.let {
                    if (it.isNotEmpty())
                        chooseAccountViewModel.getAccountListFacebook(it)
                }
            }
            else -> {
                LetUtil.ifLet(viewModel.accessToken, viewModel.phoneNumber) { (accessToken, phoneNumber) ->
                    chooseAccountViewModel.getAccountListPhoneNumber(accessToken, phoneNumber)
                }
            }
        }
    }

    private fun loginToken(account: UserDetailDataModel?, phone: String) {
        account?.let {
            when (viewModel.loginType) {
                FACEBOOK_LOGIN_TYPE -> {
                    if (phone.isNotEmpty()) {
                        viewModel.accountListDataModel?.key?.let { key ->
                            chooseAccountViewModel.loginTokenFacebook(
                                    key,
                                    it.email,
                                    phone
                            )
                        }
                    }
                }
                else -> {
                    LetUtil.ifLet(viewModel.accountListDataModel, viewModel.phoneNumber) { (accountList, phoneNumber) ->
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
        }
    }

    private fun onSuccessLoginToken() {
        activity?.apply {
            this.setResult(Activity.RESULT_OK)
            this.finish()
        }
    }

    private fun onErrorLoginToken(throwable: Throwable) {
        checkExceptionType(throwable)
        logUnknownError(Throwable("Login Phone Number Login Token is not success"))
    }

    private fun onSuccessGetAccountList(accountListDataModel: AccountListDataModel) {
        this.viewModel.accountListDataModel = accountListDataModel

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
                if (selectedAccount != null && !selectedPhoneNo.isNullOrEmpty()) {
                    loginToken(selectedAccount, selectedPhoneNo ?: "")
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(ApplinkConstInternalGlobal.PARAM_UUID, viewModel.accessToken)
        outState.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, viewModel.phoneNumber)
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
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChooseAccountFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
