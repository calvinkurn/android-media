package com.tokopedia.settingbank.view.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.otp.common.di.OtpComponentBuilder.getComponent
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.analytics.BankSettingAnalytics
import com.tokopedia.settingbank.databinding.FragmentSettingBankNewBinding
import com.tokopedia.settingbank.di.SettingBankComponent
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.KYCInfo
import com.tokopedia.settingbank.domain.model.SettingBankErrorHandler
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.settingbank.util.DeleteBankAccountException
import com.tokopedia.settingbank.view.activity.SettingBankCallback
import com.tokopedia.settingbank.view.adapter.BankAccountClickListener
import com.tokopedia.settingbank.view.adapter.BankAccountListAdapter
import com.tokopedia.settingbank.view.decoration.DividerItemDecoration
import com.tokopedia.settingbank.view.viewModel.SettingBankViewModel
import com.tokopedia.settingbank.view.widgets.AccountConfirmationBottomSheet
import com.tokopedia.settingbank.view.widgets.BankTNCBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

class SettingBankFragment : BaseDaggerFragment(), BankAccountClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var bankSettingAnalytics: BankSettingAnalytics

    private var binding by autoClearedNullable<FragmentSettingBankNewBinding>()

    private var settingBankCallback: SettingBankCallback? = null

    private lateinit var settingBankViewModel: SettingBankViewModel

    @Inject
    lateinit var bankAccountListAdapter: BankAccountListAdapter

    override fun getScreenName(): String? = null

    private var deleteBankAccount: BankAccount? = null
    private var confirmBankAccount: BankAccount? = null

    override fun initInjector() {
        getComponent(SettingBankComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
    }

    private fun initViewModels() {
        settingBankViewModel = ViewModelProvider(this, viewModelFactory)
            .get(SettingBankViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBankNewBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.progressBar?.setOnClickListener { }
        setHasOptionsMenu(true)
        initBankAccountRecyclerView()
        startObservingViewModels()
        loadUserBankAccountList()
        binding?.addAccountButton?.run {
            setOnClickListener {
                when (bankAccountListAdapter.getBankAccountListSize()) {
                    Int.ZERO -> bankSettingAnalytics.eventOnAddBankClick()
                    else ->
                        bankSettingAnalytics.eventOnAddAnotherBankClick()
                }
                openAddBankAccountPage()
            }
            gone()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SettingBankCallback) {
            settingBankCallback = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        settingBankCallback = null
    }

    private fun initBankAccountRecyclerView() {
        binding?.accountListRv?.run {
            layoutManager = LinearLayoutManager(activity)
            addItemDecoration(DividerItemDecoration())
            bankAccountListAdapter.bankAccountClickListener = this@SettingBankFragment
            adapter = bankAccountListAdapter
        }
    }

    private fun openAddBankAccountPage() {
        settingBankCallback?.onAddBankAccountClick()
    }

    fun loadUserBankAccountList() {
        showLoadingState(true)
        settingBankViewModel.loadUserAddedBankList()
    }

    private fun loadTermsAndCondition() {
        settingBankViewModel.loadTermsAndCondition()
    }

    private fun startObservingViewModels() {
        settingBankViewModel.bankAccountListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> {
                        if (it.data.isEmpty()) {
                            showNoBankAccountState()
                        } else {
                            populateBankList(it.data)
                            addTNCNoteInAdapter()
                        }
                        configAddAccountButtonText(it.data)
                    }
                    is Fail -> {
                        onBankAccountLoadingFailed(it.throwable)
                    }
                }
                binding?.progressBar?.gone()
            }
        )

        settingBankViewModel.addBankAccountStateLiveData.observe(
            viewLifecycleOwner,
            Observer {
                updateAddBankAccountBtnState(it)
            }
        )

        settingBankViewModel.termsAndConditionLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> openTNCBottomSheet(it.data)
                    is Fail -> showError(it.throwable, null)
                }
            }
        )

        settingBankViewModel.deleteBankAccountLiveData.observe(
            viewLifecycleOwner,
            Observer {
                handleDeleteBankAccountState(it)
            }
        )

        settingBankViewModel.kycInfoLiveData.observe(
            viewLifecycleOwner,
            Observer {
                binding?.progressBar?.gone()
                when (it) {
                    is Success -> openCheckDataBottomSheet(it.data)
                    is Fail -> showError(it.throwable, null)
                }
            }
        )
    }

    private fun onBankAccountLoadingFailed(throwable: Throwable) {
        if (throwable is MessageErrorException) {
            showGlobalError(GlobalError.SERVER_ERROR, ::loadUserBankAccountList)
        } else {
            showGlobalError(GlobalError.NO_CONNECTION, ::loadUserBankAccountList)
        }
    }

    private fun showGlobalError(errorType: Int, retryAction: () -> Unit) {
        binding?.globalError?.let {
            it.visible()
            it.setType(errorType)
            it.errorAction.visible()
            it.errorAction.setOnClickListener {
                showLoadingState(true)
                retryAction.invoke()
                it.gone()
            }
        }
    }

    private fun updateAddBankAccountBtnState(isEnable: Boolean) {
        binding?.addAccountButton?.isEnabled = isEnable
    }

    private fun openCheckDataBottomSheet(kycInfo: KYCInfo) {
        confirmBankAccount?.let { bankAccount ->
            AccountConfirmationBottomSheet.showBottomSheet(bankAccount, kycInfo, activity)
        }
    }

    private fun handleDeleteBankAccountState(result: Result<String>) {
        when (result) {
            is Success -> {
                showToasterOnUI(result.data)
                loadUserBankAccountList()
                activity?.setResult(Activity.RESULT_OK, Intent())
            }
            is Fail -> {
                if (result.throwable is DeleteBankAccountException) {
                    showErrorToaster((result.throwable as DeleteBankAccountException).errorMessage)
                } else {
                    showError(result.throwable, null)
                }
            }
        }
        binding?.progressBar?.gone()
    }

    private fun showErrorToaster(message: String) {
        view?.let {
            Toaster.toasterCustomBottomHeight = binding?.addAccountButton?.measuredHeight ?: Int.ZERO
            Toaster.build(it, message, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun openTNCBottomSheet(templateData: TemplateData?) {
        templateData?.let {
            BankTNCBottomSheet.showBankTNCBottomSheet(it, activity)
        }
    }

    private fun showError(throwable: Throwable, retry: (() -> Unit)?) {
        context?.let { context ->
            view?.let { view ->
                Toaster.toasterCustomBottomHeight = binding?.addAccountButton?.measuredHeight ?: Int.ZERO
                retry?.let {
                    Toaster.build(
                        view,
                        SettingBankErrorHandler.getErrorMessage(context, throwable),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR,
                        getString(R.string.sbank_promo_coba_lagi),
                        View.OnClickListener { retry.invoke() }
                    ).show()
                } ?: run {
                    Toaster.build(
                        view,
                        SettingBankErrorHandler.getErrorMessage(context, throwable),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            }
        }
    }

    fun showToasterOnUI(message: String?) {
        message?.let {
            view?.let {
                Toaster.toasterCustomBottomHeight = binding?.addAccountButton?.measuredHeight ?: Int.ZERO
                Toaster.build(it, message, Toaster.LENGTH_SHORT).show()
            }
        }
    }

    private fun populateBankList(bankList: List<BankAccount>) {
        showBankAccountDisplayState()
        binding?.accountListRv?.post {
            bankAccountListAdapter.updateItem(bankList as ArrayList<BankAccount>)
        }
    }

    private fun configAddAccountButtonText(bankList: List<BankAccount>) {
        if (bankList.isNotEmpty()) {
            binding?.addAccountButton?.text = context?.getString(R.string.sbank_add_bank_account)
        } else {
            binding?.addAccountButton?.text = context?.getString(R.string.sbank_no_bank_add_bank_account)
        }
    }

    private fun addTNCNoteInAdapter() {
        binding?.accountListRv?.post {
            bankAccountListAdapter.addBankTNCNote()
        }
    }

    private fun showLoadingState(show: Boolean) {
        binding?.run {
            if (show) {
                accountListRv.gone()
                addAccountButton.gone()

                ivNoBankAccountAdded.gone()
                tvNoSaveAccount.gone()
                tvComeOnAddBankAccount.gone()

                progressBar.visible()
            } else {
                progressBar.gone()
            }
        }
    }

    private fun showBankAccountDisplayState() {
        binding?.run {
            accountListRv.visible()
            addAccountButton.visible()

            ivNoBankAccountAdded.gone()
            tvNoSaveAccount.gone()
            tvComeOnAddBankAccount.gone()

            progressBar.gone()
        }
    }

    private fun showNoBankAccountState() {
        binding?.run {
            accountListRv.gone()
            addAccountButton.visible()

            ivNoBankAccountAdded.visible()
            tvNoSaveAccount.visible()
            tvComeOnAddBankAccount.visible()

            progressBar.gone()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_info -> {
                bankSettingAnalytics.eventOnToolbarTNCClick()
                loadTermsAndCondition()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(getMenuRes(), menu)
        val menuItem = menu.findItem(R.id.menu_info)
        menuItem?.actionView?.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getMenuRes(): Int = R.menu.menu_info_add_bank_account

    override fun deleteBankAccount(bankAccount: BankAccount) {
        bankSettingAnalytics.eventDeleteAccountClick()
        openDeleteConfirmationPopUp(bankAccount)
    }

    override fun onClickDataContent(bankAccount: BankAccount) {
        bankSettingAnalytics.eventIsiDataClick()
        getKYCInfoForUser(bankAccount)
    }

    private fun openDeleteConfirmationPopUp(bankAccount: BankAccount) {
        context?.let { context ->
            deleteBankAccount = bankAccount
            val description = context.getString(
                R.string.sbank_delete_bank_confirm,
                bankAccount.bankName,
                bankAccount.accNumber,
                bankAccount.accName
            )
            DialogUnify(
                context = context,
                actionType = DialogUnify.HORIZONTAL_ACTION,
                imageType = DialogUnify.NO_IMAGE
            ).apply {
                setTitle(getString(R.string.sbank_delete_this_account))
                setDescription(description)
                setPrimaryCTAText(getString(R.string.sbank_delete_account))
                setSecondaryCTAText(getString(R.string.sbank_back))
                setPrimaryCTAClickListener {
                    bankSettingAnalytics.eventDialogConfirmDeleteAccountClick()
                    dismiss()
                    deleteBankAccount()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
                show()
            }
        }
    }

    private fun deleteBankAccount() {
        deleteBankAccount?.let {
            binding?.progressBar?.visible()
            settingBankViewModel.deleteBankAccount(it)
        }
    }

    private fun getKYCInfoForUser(bankAccount: BankAccount) {
        confirmBankAccount = bankAccount
        binding?.progressBar?.visible()
        settingBankViewModel.getKYCInfo()
    }
}
