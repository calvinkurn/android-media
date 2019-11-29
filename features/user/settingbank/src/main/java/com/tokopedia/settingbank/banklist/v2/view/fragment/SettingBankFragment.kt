package com.tokopedia.settingbank.banklist.v2.view.fragment

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.domain.BankAccount
import com.tokopedia.settingbank.banklist.v2.domain.TemplateData
import com.tokopedia.settingbank.banklist.v2.view.activity.AddBankActivity
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankAccountListAdapter
import com.tokopedia.settingbank.banklist.v2.view.viewModel.SettingBankTNCViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewModel.SettingBankViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewState.BankAccountListLoadingError
import com.tokopedia.settingbank.banklist.v2.view.viewState.NoBankAccountAdded
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnBankAccountListLoaded
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnShowLoading
import com.tokopedia.settingbank.banklist.v2.view.widgets.BankTNCBottomSheet
import com.tokopedia.settingbank.banklist.v2.view.widgets.CloseableBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_setting_bank_new.*
import javax.inject.Inject

class SettingBankFragment : BaseDaggerFragment() {


    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var tNCViewModel: SettingBankTNCViewModel
    private lateinit var settingBankViewModel: SettingBankViewModel

    private lateinit var tncBottomSheet: BankTNCBottomSheet

    private lateinit var bottomSheets: CloseableBottomSheetFragment

    @Inject
    lateinit var bankAccountListAdapter: BankAccountListAdapter

    override fun getScreenName(): String? = null

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
        add_account_button.setOnClickListener { openAddBankAccountPage() }
    }

    private fun initBankAccountRecyclerView() {
        account_list_rv.layoutManager = LinearLayoutManager(activity)
        account_list_rv.addItemDecoration(DividerItemDecoration(activity))
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

    private fun loadUserBankAccountList() {
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
                is BankAccountListLoadingError -> showErrorOnUI(it.errMsg)
                is OnBankAccountListLoaded -> {
                    populateBankList(it.bankList, it.toastMessage)
                    loadBankNote()
                }
                is NoBankAccountAdded -> showNoBankAccountAddedState(it.toastMessage)
            }
        })

        tNCViewModel.tncNoteTemplate.observe(this, Observer {
            populateTNCNote(it)
        })
        tNCViewModel.tncPopUpTemplate.observe(this, Observer {
            openTNCBottomSheet(it)
        })
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

    private fun showErrorOnUI(errMsg: String) {
        //view?.let { Toaster.make(it, "error", Toast.LENGTH_SHORT) }
    }

    private fun populateBankList(bankList: List<BankAccount>, toastMessage: String) {
        showBankAccountDisplayState()
        account_list_rv.post {
            bankAccountListAdapter.updateItem(bankList as ArrayList<BankAccount>)
        }
    }

    private fun populateTNCNote(templateData: TemplateData) {
        account_list_rv.post {
            bankAccountListAdapter.updateBankTNCNote(templateData)
        }
    }

    private fun showNoBankAccountAddedState(toastMessage: String) {
        showNoBankAccountState()
    }

    override fun onDestroy() {
        settingBankViewModel.getBankListState.removeObservers(this)
        tNCViewModel.tncPopUpTemplate.removeObservers(this)
        tNCViewModel.tncNoteTemplate.removeObservers(this)
        super.onDestroy()
    }

    private fun showLoadingState(show: Boolean) {
        if (show) {
            groupProgressBar.visible()
            groupBankListAndButton.gone()
            groupBlankPage.gone()
        } else groupProgressBar.gone()
    }

    private fun showBankAccountDisplayState() {
        groupProgressBar.gone()
        groupBankListAndButton.visible()
        groupBlankPage.gone()
    }

    private fun showNoBankAccountState() {
        groupProgressBar.gone()
        groupBankListAndButton.gone()
        groupBlankPage.visible()
        add_account_button.visible()
        view_btn_top_shadow.visible()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_info -> loadTermsAndCondition()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.let { it.inflate(getMenuRes(), menu) }
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getMenuRes(): Int = R.menu.menu_info_add_bank_account

    fun closeBottomSheet(){
        if(::bottomSheets.isInitialized)
            bottomSheets.dismiss()
    }

}