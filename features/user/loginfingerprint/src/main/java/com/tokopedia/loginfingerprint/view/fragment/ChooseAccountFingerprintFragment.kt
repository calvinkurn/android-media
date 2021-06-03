package com.tokopedia.loginfingerprint.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.loginfingerprint.viewmodel.ChooseAccountFingerprintViewModel
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.UserDetail
import com.tokopedia.loginphone.chooseaccount.view.fragment.BaseChooseAccountFragment
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class ChooseAccountFingerprintFragment(val validateToken: String): BaseChooseAccountFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Named(SessionModule.SESSION_MODULE)
    @Inject
    lateinit var userSessionInterface: UserSessionInterface

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ChooseAccountFingerprintViewModel::class.java)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun onSelectedAccount(account: UserDetail, phone: String) {
        loginBiometric(account.email, validateToken)
    }

    override fun initObserver() {
        viewModel.getAccountListResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetFingerprintAccounts(it.data)
                is Fail -> onErrorGetFingerprintAccounts(it.throwable)
            }
        })

        viewModel.loginBiometricResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onSuccessLoginBiometric()
                is Fail -> onErrorLoginBiometric(it.throwable)
            }
        })

        viewModel.showPopup.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                showPopupError(it.header, it.body, it.action)
            }
        })

        viewModel.getUserInfoResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetUserInfo(it.data)
                is Fail -> {
                    dismissLoadingProgress()
                    onErrorGetUserInfo(it.throwable)
                }
            }
        })

        viewModel.showPopup.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it != null) {
                showPopupError(it.header, it.body, it.action)
            }
        })

        viewModel.goToActivationPage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            onGoToActivationPage()
        })

        viewModel.goToSecurityQuestion.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            onGoToSecurityQuestion()
        })

        viewModel.showAdminLocationPopUp.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when(it) {
                is Success -> showLocationAdminPopUp(userSessionInterface)
                is Fail -> showLocationAdminError(it.throwable)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getFingerprintAccounts()
    }

    private fun getFingerprintAccounts() {
        viewModel.getAccountListFingerprint(validateToken)
    }

    private fun loginBiometric(email: String, validateToken: String) {
        viewModel.loginTokenBiometric(email, validateToken)
    }

    private fun onSuccessGetFingerprintAccounts(accountList: AccountList) {
        if (accountList.userDetails.size == 1 && accountList.msisdn.isNotEmpty()) {
            adapter.setList(accountList.userDetails, accountList.msisdn)
            val userDetail = accountList.userDetails[0]
            loginBiometric(userDetail.email, validateToken)
        } else {
            dismissLoadingProgress()
            adapter.setList(accountList.userDetails, accountList.msisdn)
        }
    }

    private fun onErrorGetFingerprintAccounts(throwable: Throwable) {
        dismissLoadingProgress()
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
            showLoadingProgress()
            getFingerprintAccounts()
        }
    }

    private fun onSuccessLoginBiometric() {
        viewModel.getUserInfo()
    }

    private fun onErrorLoginBiometric(throwable: Throwable) {
        checkExceptionType(throwable)
        logUnknownError(Throwable("Login Fingerprint is not success"))
    }

    override fun onSuccessLogin(userId: String) {
        setFCM(userSessionInterface.deviceId)
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    companion object {
        fun createInstance(validateToken: String): Fragment {
            return ChooseAccountFingerprintFragment(validateToken)
        }
    }
}