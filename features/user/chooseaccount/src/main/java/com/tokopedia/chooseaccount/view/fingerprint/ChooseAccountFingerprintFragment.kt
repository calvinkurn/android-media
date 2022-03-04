package com.tokopedia.chooseaccount.view.fingerprint

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chooseaccount.R
import com.tokopedia.chooseaccount.data.AccountListDataModel
import com.tokopedia.chooseaccount.data.UserDetailDataModel
import com.tokopedia.chooseaccount.di.ChooseAccountComponent
import com.tokopedia.chooseaccount.view.base.BaseChooseAccountFragment
import com.tokopedia.chooseaccount.viewmodel.ChooseAccountFingerprintViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class ChooseAccountFingerprintFragment: BaseChooseAccountFragment() {

    private var validateToken: String = ""

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

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

    override fun onSelectedAccount(account: UserDetailDataModel, phone: String) {
        selectAccount(account)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateToken = arguments?.getString(ApplinkConstInternalGlobal.PARAM_TOKEN) ?: ""
    }

    override fun initObserver() {
        viewModel.getAccountListDataModelResponse.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
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
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(validateToken.isEmpty()) {
            activity?.setResult(Activity.RESULT_CANCELED)
            activity?.finish()
        } else {
            getFingerprintAccounts()
        }
    }

    private fun getFingerprintAccounts() {
        showLoadingProgress()
        viewModel.getAccountListFingerprint(validateToken)
    }

    private fun onSuccessGetFingerprintAccounts(accountListDataModel: AccountListDataModel) {
        if (accountListDataModel.userDetailDataModels.size == 1) {
            adapter?.setList(accountListDataModel.userDetailDataModels, accountListDataModel.msisdn)
            val userDetail = accountListDataModel.userDetailDataModels[0]
            selectAccount(userDetail)
        } else {
            adapter?.setList(accountListDataModel.userDetailDataModels, accountListDataModel.msisdn)
        }
        dismissLoadingProgress()
    }

    private fun onErrorGetFingerprintAccounts(throwable: Throwable) {
        dismissLoadingProgress()
        val errorMessage = ErrorHandler.getErrorMessage(context, throwable, builder = getErrorHandlerBuilder())
        showToaster(errorMessage) {
            showLoadingProgress()
            getFingerprintAccounts()
        }
    }

    fun selectAccount(userDetailDataModel: UserDetailDataModel) {
        val intent = Intent().apply {
            putExtra(ApplinkConstInternalGlobal.PARAM_EMAIL, userDetailDataModel.email)
            putExtra(ApplinkConstInternalGlobal.PARAM_TOKEN, validateToken)
        }
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun showToaster(message: String?, action: () -> Unit = {}) {
        if(context != null) {
            view?.let {
                Toaster.build(
                    it,
                    message ?: getString(R.string.error_default_fp_choose_account),
                    Toaster.LENGTH_INDEFINITE,
                    Toaster.TYPE_ERROR,
                    "Coba lagi",
                ) {
                    action.invoke()
                }.show()
            }
        }
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