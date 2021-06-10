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
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.analytics.BankSettingAnalytics
import com.tokopedia.settingbank.di.SettingBankComponent
import com.tokopedia.settingbank.domain.model.BankAccount
import com.tokopedia.settingbank.domain.model.KYCInfo
import com.tokopedia.settingbank.domain.model.SettingBankErrorHandler
import com.tokopedia.settingbank.domain.model.TemplateData
import com.tokopedia.settingbank.util.DeleteBankAccountException
import com.tokopedia.settingbank.view.activity.SettingBankCallback
import com.tokopedia.settingbank.view.adapter.BankAccountClickListener
import com.tokopedia.settingbank.view.adapter.BankAccountListAdapter
import com.tokopedia.settingbank.view.viewModel.SettingBankViewModel
import com.tokopedia.settingbank.view.widgets.AccountConfirmationBottomSheet
import com.tokopedia.settingbank.view.widgets.BankTNCBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_setting_bank_new.*
import javax.inject.Inject

class SettingBankFragment : BaseDaggerFragment(), BankAccountClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var bankSettingAnalytics: BankSettingAnalytics

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting_bank_new, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress_bar.setOnClickListener { //not required
        }
        setHasOptionsMenu(true)
        initBankAccountRecyclerView()
        startObservingViewModels()
        loadUserBankAccountList()
        add_account_button.gone()
        add_account_button.setOnClickListener {
            when (bankAccountListAdapter.getBankAccountListSize()) {
                0 -> bankSettingAnalytics.eventOnAddBankClick()
                else ->
                    bankSettingAnalytics.eventOnAddAnotherBankClick()
            }
            openAddBankAccountPage()
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
        account_list_rv.layoutManager = LinearLayoutManager(activity)
        account_list_rv.addItemDecoration(DividerItemDecoration(activity))
        bankAccountListAdapter.bankAccountClickListener = this
        account_list_rv.adapter = bankAccountListAdapter
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
        settingBankViewModel.bankAccountListLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.isEmpty()) {
                        showNoBankAccountState()
                    } else {
                        populateBankList(it.data)
                        loadBankNote()
                    }
                }
                is Fail -> {
                    onBankAccountLoadingFailed(it.throwable)
                }
            }
            progress_bar.gone()
        })

        settingBankViewModel.addBankAccountStateLiveData.observe(viewLifecycleOwner, Observer {
            updateAddBankAccountBtnState(it)
        })

        settingBankViewModel.tncNotesLiveData.observe(viewLifecycleOwner, Observer {
            if (it is Success) populateTNCNoteInAdapter(it.data)
        })

        settingBankViewModel.termsAndConditionLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> openTNCBottomSheet(it.data)
                is Fail -> showError(it.throwable, null)
            }
        })

        settingBankViewModel.deleteBankAccountLiveData.observe(viewLifecycleOwner, Observer {
            handleDeleteBankAccountState(it)
        })

        settingBankViewModel.kycInfoLiveData.observe(viewLifecycleOwner, Observer {
            progress_bar.gone()
            when (it) {
                is Success -> openCheckDataBottomSheet(it.data)
                is Fail -> showError(it.throwable, null)
            }
        })
    }

    private fun onBankAccountLoadingFailed(throwable: Throwable) {
        if (throwable is MessageErrorException) {
            showGlobalError(GlobalError.SERVER_ERROR, ::loadUserBankAccountList)
        } else {
            showGlobalError(GlobalError.NO_CONNECTION, ::loadUserBankAccountList)
        }
    }

    private fun showGlobalError(errorType: Int, retryAction: () -> Unit) {
        globalError.visible()
        globalError.setType(errorType)
        globalError.errorAction.visible()
        globalError.errorAction.setOnClickListener {
            showLoadingState(true)
            retryAction.invoke()
            globalError.gone()
        }
    }

    private fun updateAddBankAccountBtnState(isEnable: Boolean) {
        add_account_button.isEnabled = isEnable
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
        progress_bar.gone()
    }

    private fun showErrorToaster(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun openTNCBottomSheet(templateData: TemplateData?) {
        templateData?.let {
            BankTNCBottomSheet.showBankTNCBottomSheet(it, activity)
        }
    }

    private fun loadBankNote() {
        settingBankViewModel.loadTermsAndConditionNotes()
    }

    private fun showError(throwable: Throwable, retry: (() -> Unit)?) {
        context?.let { context ->
            view?.let { view ->
                retry?.let {
                    Toaster.build(view, SettingBankErrorHandler.getErrorMessage(context, throwable),
                            Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                            getString(R.string.sbank_promo_coba_lagi), View.OnClickListener { retry.invoke() }).show()
                } ?: run {
                    Toaster.build(view, SettingBankErrorHandler.getErrorMessage(context, throwable),
                            Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
                }

            }
        }
    }

    fun showToasterOnUI(message: String?) {
        message?.let {
            view?.let { Toaster.build(it, message, Toaster.LENGTH_SHORT).show() }
        }
    }

    private fun populateBankList(bankList: List<BankAccount>) {
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

    private fun showLoadingState(show: Boolean) {
        if (show) {
            account_list_rv.gone()
            view_btn_top_shadow.gone()
            add_account_button.gone()

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
            val description = context.getString(R.string.sbank_delete_bank_confirm,
                    bankAccount.bankName, bankAccount.accNumber, bankAccount.accName)
            DialogUnify(context = context, actionType = DialogUnify.HORIZONTAL_ACTION,
                    imageType = DialogUnify.NO_IMAGE).apply {
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


        /*activity?.let { activity ->
            deleteBankAccount = bankAccount
            val dialogBuilder = AlertDialog.Builder(activity)
            val inflater = activity.layoutInflater
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
        }*/
    }

    private fun deleteBankAccount() {
        deleteBankAccount?.let {
            progress_bar.visible()
            settingBankViewModel.deleteBankAccount(it)
        }
    }

    private fun getKYCInfoForUser(bankAccount: BankAccount) {
        confirmBankAccount = bankAccount
        progress_bar.visible()
        settingBankViewModel.getKYCInfo()
    }

}
