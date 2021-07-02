package com.tokopedia.loginphone.chooseaccount.view.fingerprint

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginphone.chooseaccount.data.AccountList
import com.tokopedia.loginphone.chooseaccount.data.UserDetail
import com.tokopedia.loginphone.chooseaccount.di.ChooseAccountComponent
import com.tokopedia.loginphone.chooseaccount.view.base.BaseChooseAccountFragment
import com.tokopedia.loginphone.chooseaccount.viewmodel.ChooseAccountFingerprintViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.sessioncommon.di.SessionModule
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject
import javax.inject.Named

class ChooseAccountFingerprintFragment: BaseChooseAccountFragment() {

    var validateToken: String = ""

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

    override fun initInjector() {
        getComponent(ChooseAccountComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = TAG

    override fun onSelectedAccount(account: UserDetail, phone: String) {
        selectAccount(account)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN) ?: ""
        if(validateToken.isEmpty()) {
            NetworkErrorHelper.showEmptyState(context, view, "Terjadi kesalahan") {
                showLoadingProgress()
                activity?.finish()
            }
        }
    }

    override fun initObserver() {
        viewModel.getAccountListResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetFingerprintAccounts(it.data)
                is Fail -> onErrorGetFingerprintAccounts(it.throwable)
            }
            dismissLoadingProgress()
        })

        viewModel.popupError.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if (it is Success) {
                val popupError = it.data
                showPopupError(popupError.header, popupError.body, popupError.action)
            }
        })

        viewModel.getUserInfoResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            when (it) {
                is Success -> onSuccessGetUserInfo(it.data)
                is Fail -> onErrorGetUserInfo(it.throwable)
            }
            dismissLoadingProgress()
        })

        viewModel.activationPage.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it is Success){
                onGoToActivationPage(it.data)
            }
        })

        viewModel.securityQuestion.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            if(it is Success) {
                onGoToSecurityQuestion()
            }
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
        showLoadingProgress()
        viewModel.getAccountListFingerprint(validateToken)
    }


    private fun onSuccessGetFingerprintAccounts(accountList: AccountList) {
        if (accountList.userDetails.size == 1) {
            adapter.setList(accountList.userDetails, accountList.msisdn)
            val userDetail = accountList.userDetails[0]
            selectAccount(userDetail)
        } else {
            adapter.setList(accountList.userDetails, accountList.msisdn)
        }
        dismissLoadingProgress()
    }

    private fun onErrorGetFingerprintAccounts(throwable: Throwable) {
        dismissLoadingProgress()
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
        NetworkErrorHelper.showEmptyState(context, view, errorMessage) {
            showLoadingProgress()
            getFingerprintAccounts()
        }
    }

    override fun onSuccessLogin(userId: String) {
        setFCM(userSessionInterface.deviceId)
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    fun selectAccount(userDetail: UserDetail) {
        val intent = Intent().apply {
            putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, userDetail.email)
            putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    companion object {
        const val TAG = "ChooseAccountFingerprintActivity"
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = ChooseAccountFingerprintFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}