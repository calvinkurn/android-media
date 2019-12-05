package com.tokopedia.settingbank.banklist.v2.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.domain.AddBankRequest
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.util.AccountConfirmationType
import com.tokopedia.settingbank.banklist.v2.view.viewModel.SettingBankTNCViewModel
import com.tokopedia.unifycomponents.Toaster
import javax.inject.Inject

const val ARG_BANK_ACCOUNT_DATA = "arg_bank_account_data"
const val ARG_ACCOUNT_TYPE = "arg_account_type"

class AccountConfirmFragment : BaseDaggerFragment() {


    override fun getScreenName(): String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var tNCViewModel: SettingBankTNCViewModel

    val builder: AddBankRequest.Builder = AddBankRequest.Builder()

    lateinit var bankAccount: BankAccount
    lateinit var accountConfirmationType: AccountConfirmationType

    override fun initInjector() {
        getComponent(SettingBankComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_BANK_ACCOUNT_DATA))
                arguments?.getParcelable<BankAccount>(ARG_BANK_ACCOUNT_DATA)?.let { bankAccount ->
                    this.bankAccount = bankAccount
                }
            arguments?.getInt(ARG_ACCOUNT_TYPE)?.let { accountTypeInt ->
                when (accountTypeInt) {
                    AccountConfirmationType.COMPANY.accountType -> accountConfirmationType = AccountConfirmationType.COMPANY
                    AccountConfirmationType.FAMILY.accountType -> accountConfirmationType = AccountConfirmationType.FAMILY
                    AccountConfirmationType.OTHER.accountType -> accountConfirmationType = AccountConfirmationType.OTHER
                }
            }
        }
        initViewModels()
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        tNCViewModel = viewModelProvider.get(SettingBankTNCViewModel::class.java)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_confirm_bank_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (::accountConfirmationType.isInitialized) {
            activity?.finish()
        }
        startObservingViewModels()
        setBankAccountData()
    }

    private fun setBankAccountData() {

    }


    private fun startObservingViewModels() {

    }

    private fun showErrorOnUI(errorMessage: String?, retry: (() -> Unit)?) {
        errorMessage?.let {
            view?.let { view ->
                retry?.let {
                    Toaster.make(view, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                            getString(R.string.sbank_promo_coba_lagi), View.OnClickListener { retry.invoke() })
                } ?: run {
                    Toaster.make(view, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }

            }
        }
    }

}
