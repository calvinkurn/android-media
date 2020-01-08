package com.tokopedia.settingbank.banklist.v2.view.fragment

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.analytics.BankSettingAnalytics
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.domain.KYCInfo
import com.tokopedia.settingbank.banklist.v2.domain.SettingBankErrorHandler
import com.tokopedia.settingbank.banklist.v2.domain.TemplateData
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankAccountClickListener
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankAccountListAdapter
import com.tokopedia.settingbank.banklist.v2.view.viewModel.*
import com.tokopedia.settingbank.banklist.v2.view.viewState.*
import com.tokopedia.settingbank.banklist.v2.view.widgets.AccountConfirmationBottomSheet
import com.tokopedia.settingbank.banklist.v2.view.widgets.BankTNCBottomSheet
import com.tokopedia.settingbank.banklist.v2.view.widgets.CloseableBottomSheetFragment
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_setting_bank_new.*
import javax.inject.Inject

class SettingBankFragment : BaseDaggerFragment(), BankAccountClickListener {

    private var confirmationDialog: AlertDialog? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var bankSettingAnalytics: BankSettingAnalytics

    private lateinit var tNCViewModel: SettingBankTNCViewModel
    private lateinit var settingBankViewModel: SettingBankViewModel
    private lateinit var makeAccountPrimaryViewModel: MakeAccountPrimaryViewModel
    private lateinit var deleteBankAccountViewModel: DeleteBankAccountViewModel
    private lateinit var kycViewModel: GetKYCViewModel

    private lateinit var tncBottomSheet: BankTNCBottomSheet

    private lateinit var bottomSheets: CloseableBottomSheetFragment
    private lateinit var confirmAccountBottomSheet: AccountConfirmationBottomSheet

    @Inject
    lateinit var bankAccountListAdapter: BankAccountListAdapter

    override fun getScreenName(): String? = null

    private var deleteBankAccount: BankAccount? = null
    private var makePrimaryBankAccount: BankAccount? = null
    private var confirmBankAccount: BankAccount? = null

    override fun initInjector() {
        getComponent(SettingBankComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        settingBankViewModel = viewModelProvider.get(SettingBankViewModel::class.java)
        tNCViewModel = viewModelProvider.get(SettingBankTNCViewModel::class.java)
        makeAccountPrimaryViewModel = viewModelProvider.get(MakeAccountPrimaryViewModel::class.java)
        deleteBankAccountViewModel = viewModelProvider.get(DeleteBankAccountViewModel::class.java)
        kycViewModel = viewModelProvider.get(GetKYCViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting_bank_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initBankAccountRecyclerView()
        startObservingViewModels()
        loadUserBankAccountList()
        add_account_button.visible()
        add_account_button.setOnClickListener {
            when (bankAccountListAdapter.getBankAccountListSize()) {
                0 -> bankSettingAnalytics.eventOnAddBankClick()
                else ->
                    bankSettingAnalytics.eventOnAddAnotherBankClick()
            }
            openAddBankAccountPage()
        }
    }

    private fun initBankAccountRecyclerView() {
        account_list_rv.layoutManager = LinearLayoutManager(activity)
        account_list_rv.addItemDecoration(DividerItemDecoration(activity))
        bankAccountListAdapter.bankAccountClickListener = this
        account_list_rv.adapter = bankAccountListAdapter
    }

    private fun openAddBankAccountPage() {
        bottomSheets = CloseableBottomSheetFragment.newInstance(SelectBankFragment(),
                true,
                getString(R.string.sbank_choose_a_bank),
                null,
                CloseableBottomSheetFragment.STATE_FULL)
        bottomSheets.showNow(activity!!.supportFragmentManager, "")
    }

    fun loadUserBankAccountList() {
        settingBankViewModel.loadUserAddedBankList()
    }

    private fun loadTermsAndCondition() {
        if (::tncBottomSheet.isInitialized) {
            openTNCBottomSheet(tncBottomSheet.templateData)
        } else {
            tNCViewModel.loadTNCPopUpTemplate()
        }
    }

    private fun startObservingViewModels() {
        settingBankViewModel.getBankListState.observe(this, Observer {
            when (it) {
                is OnShowLoading -> showLoadingState(it.show)
                is BankAccountListLoadingError -> {
                    progress_bar.gone()
                    showError(it.throwable, null)
                }
                is OnBankAccountListLoaded -> {
                    populateBankList(it.bankList, it.toastMessage)
                    loadBankNote()
                }
                is NoBankAccountAdded -> showNoBankAccountAddedState(it.toastMessage)
            }
        })

        settingBankViewModel.addNewBankAccountState.observe(this, Observer {
            updateAddBankAccountBtnState(it)
        })

        tNCViewModel.tncNoteTemplate.observe(this, Observer {
            populateTNCNoteInAdapter(it)
        })
        tNCViewModel.tncPopUpTemplate.observe(this, Observer {
            when (it) {
                is OnTNCSuccess -> openTNCBottomSheet(it.templateData)
                is OnTNCError -> showError(it.throwable, null)
            }
        })

        makeAccountPrimaryViewModel.makeAccountPrimaryState.observe(this, Observer {
            handleMakeAccountPrimaryState(it)
        })
        deleteBankAccountViewModel.deleteAccountState.observe(this, Observer {
            handleDeleteBankAccountState(it)
        })

        kycViewModel.kycInfoState.observe(this, Observer {
            handleKYCInfoResponse(it)
        })
    }

    private fun updateAddBankAccountBtnState(isEnable: Boolean) {
        add_account_button.isEnabled = isEnable
    }

    private fun handleKYCInfoResponse(kycInfoState: KYCInfoState) {
        when (kycInfoState) {
            is KYCInfoRequestStarted -> {
                progress_bar.visible()
            }
            is KYCInfoRequestEnded -> {
                progress_bar.gone()
            }
            is OnKYCInfoResponse -> {
                openCheckDataBottomSheet(kycInfoState.kycInfo)
            }
            is KYCInfoError -> {
                showError(kycInfoState.throwable, null)
            }
        }
    }

    private fun openCheckDataBottomSheet(kycInfo: KYCInfo) {
        if (!::confirmAccountBottomSheet.isInitialized) {
            activity?.let {
                confirmAccountBottomSheet = AccountConfirmationBottomSheet(it, kycInfo, bankSettingAnalytics)
            }
        }
        if (::confirmAccountBottomSheet.isInitialized)
            confirmBankAccount?.let {
                confirmAccountBottomSheet.show(bankAccount = it)
            }
    }

    private fun handleDeleteBankAccountState(state: DeleteAccountState) {
        when (state) {
            is OnDeleteAccountRequestStarted -> {
                progress_bar.visible()
            }
            is OnDeleteAccountRequestEnded -> {
                progress_bar.gone()
            }
            is OnDeleteAccountRequestSuccess -> {
                showToasterOnUI(state.message)
                loadUserBankAccountList()
            }
            is OnDeleteAccountRequestFailed -> {
                showError(state.throwable) { deleteBankAccount() }
            }
        }
    }

    private fun handleMakeAccountPrimaryState(state: MakePrimaryAccountResponseState) {
        when (state) {
            is OnMakePrimaryRequestStarted -> {
                progress_bar.visible()
            }
            is OnMakePrimaryRequestEnded -> {
                progress_bar.gone()
            }
            is OnMakePrimaryRequestSuccess -> {
                showToasterOnUI(state.message)
                loadUserBankAccountList()
            }
            is OnMakePrimaryRequestError -> {
                showError(state.throwable) { makeAccountPrimary() }
            }
        }
    }

    private fun openTNCBottomSheet(templateData: TemplateData?) {
        templateData?.let {
            if (::tncBottomSheet.isInitialized) {
                tncBottomSheet.templateData = it
                activity?.let {
                    tncBottomSheet.show(templateData)
                }
            } else {
                tncBottomSheet = BankTNCBottomSheet(activity!!)
                tncBottomSheet.show(templateData)
            }
        }
    }

    private fun loadBankNote() {
        tNCViewModel.loadTNCNoteTemplate()
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

    private fun showError(throwable: Throwable, retry: (() -> Unit)?) {
        context?.let { context ->
            view?.let { view ->
                retry?.let {
                    Toaster.make(view, SettingBankErrorHandler.getErrorMessage(context, throwable),
                            Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                            getString(R.string.sbank_promo_coba_lagi), View.OnClickListener { retry.invoke() })
                } ?: run {
                    Toaster.make(view, SettingBankErrorHandler.getErrorMessage(context, throwable),
                            Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }

            }
        }
    }

    fun showToasterOnUI(message: String?) {
        message?.let {
            view?.let { Toaster.make(it, message, Toaster.LENGTH_LONG) }
        }
    }

    private fun populateBankList(bankList: List<BankAccount>, toastMessage: String) {
        showBankAccountDisplayState()
        if (bankList.isNotEmpty()) {
            add_account_button.text = context?.getString(R.string.sbank_add_bank_account)
        }
        account_list_rv.post {
            bankAccountListAdapter.updateItem(bankList as ArrayList<BankAccount>)
        }
    }

    private fun populateTNCNoteInAdapter(templateData: TemplateData) {
        account_list_rv.post {
            bankAccountListAdapter.updateBankTNCNote(templateData)
        }
    }

    private fun showNoBankAccountAddedState(toastMessage: String) {
        showNoBankAccountState()
    }

    private fun showLoadingState(show: Boolean) {
        if (show) {

            account_list_rv.visible()
            view_btn_top_shadow.visible()
            add_account_button.visible()

            iv_noBankAccountAdded.gone()
            tv_no_save_account.gone()
            tv_comeOn_AddBankAccount.gone()

            progress_bar.visible()
        } else progress_bar.gone()
    }

    private fun showBankAccountDisplayState() {
        account_list_rv.visible()
        view_btn_top_shadow.visible()
        add_account_button.visible()

        iv_noBankAccountAdded.gone()
        tv_no_save_account.gone()
        tv_comeOn_AddBankAccount.gone()

        progress_bar.gone()
    }

    private fun showNoBankAccountState() {
        account_list_rv.gone()
        view_btn_top_shadow.visible()
        add_account_button.visible()

        iv_noBankAccountAdded.visible()
        tv_no_save_account.visible()
        tv_comeOn_AddBankAccount.visible()

        progress_bar.gone()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_info -> {
                bankSettingAnalytics.eventOnToolbarTNCClick()
                loadTermsAndCondition()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.let { it.inflate(getMenuRes(), menu) }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getMenuRes(): Int = R.menu.menu_info_add_bank_account

    fun closeBottomSheet() {
        if (::bottomSheets.isInitialized)
            bottomSheets.dismiss()
    }

    override fun onMakeAccountPrimary(bankAccount: BankAccount) {
        bankSettingAnalytics.eventMakeAccountPrimaryClick()
        makePrimaryBankAccount = bankAccount
        makeAccountPrimary()
    }

    override fun deleteBankAccount(bankAccount: BankAccount) {
        bankSettingAnalytics.eventDeleteAccountClick()
        openDeleteConfirmationPopUp(bankAccount)
    }

    override fun onClickDataContent(bankAccount: BankAccount) {
        bankSettingAnalytics.eventIsiDataClick()
        getKYCInfoForUser(bankAccount)
    }

    private fun makeAccountPrimary() {
        makePrimaryBankAccount?.let {
            makeAccountPrimaryViewModel.createBankAccountPrimary(it)
        }
    }

    private fun openDeleteConfirmationPopUp(bankAccount: BankAccount) {
        deleteBankAccount = bankAccount
        val dialogBuilder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.sbank_confirmation_dialog, null)
        (dialogView.findViewById(R.id.heading) as TextView).text = context?.getString(R.string.sbank_delete_this_account)
        (dialogView.findViewById<View>(R.id.continue_btn) as TextView).text = context?.getString(R.string.sbank_delete_account)
        (dialogView.findViewById<View>(R.id.back_btn) as TextView).text = context?.getString(R.string.sbank_back)
        (dialogView.findViewById(R.id.description) as TextView).text = context?.getString(R.string.sbank_delete_bank_confirm,
                bankAccount.bankName, bankAccount.accNumber, bankAccount.accName)
        dialogView.findViewById<View>(R.id.continue_btn).setOnClickListener {
            bankSettingAnalytics.eventDialogConfirmDeleteAccountClick()
            confirmationDialog?.dismiss()
            deleteBankAccount()
        }
        dialogView.findViewById<View>(R.id.back_btn).setOnClickListener {
            confirmationDialog?.dismiss()
        }
        confirmationDialog = dialogBuilder.setView(dialogView).show()
    }

    private fun deleteBankAccount() {
        deleteBankAccount?.let {
            deleteBankAccountViewModel.deleteAccount(it)
        }
    }

    private fun getKYCInfoForUser(bankAccount: BankAccount) {
        confirmBankAccount = bankAccount
        if (::confirmAccountBottomSheet.isInitialized) {
            confirmAccountBottomSheet.show(bankAccount)
        } else
            kycViewModel.getKYCInfo()
    }

}
