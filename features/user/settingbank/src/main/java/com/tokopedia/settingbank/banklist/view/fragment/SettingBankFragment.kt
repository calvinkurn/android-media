package com.tokopedia.settingbank.banklist.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.design.component.Dialog
import com.tokopedia.design.component.ToasterError
import com.tokopedia.settingbank.BankRouter
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.addeditaccount.view.activity.AddEditBankActivity
import com.tokopedia.settingbank.addeditaccount.view.viewmodel.BankFormModel
import com.tokopedia.settingbank.banklist.analytics.SettingBankAnalytics
import com.tokopedia.settingbank.banklist.di.SettingBankDependencyInjector
import com.tokopedia.settingbank.banklist.view.adapter.BankAccountAdapter
import com.tokopedia.settingbank.banklist.view.adapter.BankAccountTypeFactoryImpl
import com.tokopedia.settingbank.banklist.view.listener.BankAccountPopupListener
import com.tokopedia.settingbank.banklist.view.listener.EmptyBankAccountListener
import com.tokopedia.settingbank.banklist.view.listener.SettingBankContract
import com.tokopedia.settingbank.banklist.view.presenter.SettingBankPresenter
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountListViewModel
import com.tokopedia.settingbank.banklist.view.viewmodel.BankAccountViewModel
import kotlinx.android.synthetic.main.fragment_setting_bank.*

/**
 * @author by nisie on 6/7/18.
 */

class SettingBankFragment : SettingBankContract.View, BankAccountPopupListener, EmptyBankAccountListener,
        BaseDaggerFragment() {

    private val REQUEST_ADD_BANK: Int = 101
    private val REQUEST_EDIT_BANK: Int = 102
    private val REQUEST_PHONE_VERIFICATION: Int = 103


    lateinit var presenter: SettingBankPresenter
    lateinit var adapter: BankAccountAdapter
    lateinit var alertDialog: Dialog
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var analyticTracker: SettingBankAnalytics

    private var enableAddButton = false
    private var reason = ""

    companion object {

        fun newInstance(): SettingBankFragment {
            return SettingBankFragment()
        }
    }


    override fun getScreenName(): String {
        return SettingBankAnalytics.SCREEN_NAME
    }

    override fun initInjector() {
        presenter = SettingBankDependencyInjector.Companion.inject(activity!!.applicationContext)
        presenter.attachView(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity != null && activity!!.applicationContext != null) {
            analyticTracker = SettingBankAnalytics.createInstance()
        }
    }

    override fun onStart() {
        super.onStart()
        if (activity != null) {
            analyticTracker.sendScreen(activity!!, screenName)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        getBankList()
    }

    private fun setupView() {
        val adapterTypeFactory = BankAccountTypeFactoryImpl(this, this)
        val listBank = ArrayList<Visitable<*>>()
        adapter = BankAccountAdapter(adapterTypeFactory, listBank)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        account_list_rv.layoutManager = linearLayoutManager
        account_list_rv.adapter = adapter

        add_account_button.setOnClickListener { addNewAccount() }
        account_list_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val index = linearLayoutManager.findLastVisibleItemPosition()
                if (adapter.checkLoadMore(index)) {
                    presenter.loadMore()
                }
            }
        })
    }

    private fun getBankList() {
        presenter.getBankListFirstTime()
    }

    override fun onErrorGetListBankFirstTime(errorMessage: String) {
        NetworkErrorHelper.showEmptyState(activity, view, errorMessage) {
            getBankList()
        }
    }

    override fun onErrorGetListBank(errorMessage: String) {
        NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }

    override fun onSuccessRefresh(bankAccountList: BankAccountListViewModel, resultMessage: String) {
        onSuccessGetListBank(bankAccountList)
        NetworkErrorHelper.showSnackbar(activity!!, resultMessage)
    }

    override fun onSuccessGetListBank(bankAccountList: BankAccountListViewModel) {
        adapter.setList(bankAccountList.list!!)
        account_list_rv.visibility = View.VISIBLE
        add_account_button.visibility = View.VISIBLE

        enableAddButton = bankAccountList.enableAddButton
        reason = bankAccountList.reason

    }

    private fun showErrorAddAccount(reason: String) {
        var errorMessage = reason
        if (errorMessage.isEmpty()) {
            errorMessage = activity!!.getString(R.string.default_request_error_unknown)
        }
        ToasterError.make(view, errorMessage, BaseToaster.LENGTH_LONG)
                .show()
    }

    override fun onEmptyList(enableAddButton: Boolean, reason: String) {
        this.enableAddButton = enableAddButton
        this.reason = reason

        adapter.showEmpty()
        account_list_rv.visibility = View.VISIBLE
        add_account_button.visibility = View.GONE
    }

    override fun makeMainAccount(adapterPosition: Int) {
        if (adapter.getList() != null) {
            val element = adapter.getList()!![adapterPosition] as BankAccountViewModel
            analyticTracker.trackSetDefaultAccount()

            if (!::alertDialog.isInitialized) {
                alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
            }

            alertDialog.setTitle(getString(R.string.make_main_account_prompt_title))
            alertDialog.setDesc(composeMakeMainDescription(element))
            alertDialog.setBtnCancel(getString(R.string.cancel))
            alertDialog.setBtnOk(getString(R.string.yes))
            alertDialog.setOnCancelClickListener { alertDialog.dismiss() }
            alertDialog.setOnOkClickListener {
                analyticTracker.trackConfirmYesSetDefaultAccount()

                presenter.setMainAccount(adapterPosition, element)
                alertDialog.dismiss()
            }

            alertDialog.show()
        }

    }

    override fun onSuccessSetDefault(adapterPosition: Int) {
        adapter.changeMain(adapterPosition)
        linearLayoutManager.scrollToPosition(0)
        NetworkErrorHelper.showSnackbar(activity, getString(R.string.success_set_main_bank_account))
        activity?.setResult(Activity.RESULT_OK,  Intent())

    }

    override fun onErrorSetDefaultBank(errorMessage: String) {
        if (errorMessage.isEmpty()) {
            NetworkErrorHelper.showSnackbar(activity)
        } else {
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
        }
    }

    private fun composeMakeMainDescription(element: BankAccountViewModel?): String {
        return if (element != null) {
            String.format("%s %s %s %s %s %s.",
                    getString(R.string.you_will_make_account),
                    element.bankName,
                    element.accountNumber,
                    getString(R.string.under_name),
                    element.accountName,
                    getString(R.string.become_default)
            )
        } else {
            ""
        }
    }

    override fun editBankAccount(adapterPosition: Int) {
        if (adapter.getList() != null) {
            val element = adapter.getList()!![adapterPosition] as BankAccountViewModel

            if (presenter.isMsisdnVerified()) {
                analyticTracker.trackEditBankAccount()
                startActivityForResult(AddEditBankActivity.createIntentEditBank(
                        activity!!
                        , BankFormModel(
                        BankFormModel.Companion.STATUS_EDIT,
                        element.bankId!!,
                        element.accountId!!,
                        element.accountName!!,
                        element.accountNumber!!,
                        element.bankName!!,
                        adapterPosition
                )), REQUEST_EDIT_BANK)
            }else if (activity != null
                    && activity!!.applicationContext is BankRouter
                    && !presenter.isMsisdnVerified()) {
                val intentPhoneVerification = (activity!!.applicationContext as BankRouter)
                        .getPhoneVerificationActivityIntent(activity!!)
                startActivityForResult(intentPhoneVerification, REQUEST_PHONE_VERIFICATION)
            }

        }
    }

    override fun deleteBankAccount(adapterPosition: Int) {
        if (adapter.getList() != null) {
            val element = adapter.getList()!![adapterPosition] as BankAccountViewModel

            analyticTracker.trackDeleteBankAccount()

            if (!::alertDialog.isInitialized) {
                alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
            }

            alertDialog.setTitle(getString(R.string.delete_bank_account_prompt_title))
            alertDialog.setDesc(composeDeleteDescription(element))
            alertDialog.setBtnCancel(getString(R.string.cancel))
            alertDialog.setBtnOk(getString(R.string.yes_delete))
            alertDialog.setOnCancelClickListener { alertDialog.dismiss() }
            alertDialog.setOnOkClickListener {
                analyticTracker.trackConfirmYesDeleteBankAccount()
                presenter.deleteAccount(adapterPosition, element)
                alertDialog.dismiss()
            }

            alertDialog.show()
        }
    }

    private fun composeDeleteDescription(element: BankAccountViewModel?): String {
        return if (element != null) {
            String.format("%s %s %s %s %s.",
                    getString(R.string.you_will_delete),
                    element.bankName,
                    element.accountNumber,
                    getString(R.string.under_name),
                    element.accountName)
        } else {
            ""
        }
    }

    override fun onSuccessDeleteAccount(adapterPosition: Int) {
        this.enableAddButton = true
        this.reason = ""

        adapter.remove(adapterPosition)
        if (adapter.getList()!!.size > 0) {
            adapter.setMain(0)
        } else {
            onEmptyList(true, "")
        }

        NetworkErrorHelper.showSnackbar(activity, getString(R.string.success_delete_bank_account))
        activity?.setResult(Activity.RESULT_OK,  Intent())
    }

    override fun onErrorDeleteAccount(errorMessage: String) {

        if (errorMessage.isEmpty())
            NetworkErrorHelper.showSnackbar(activity)
        else
            NetworkErrorHelper.showSnackbar(activity, errorMessage)
    }


    override fun addNewAccount() {
        analyticTracker.trackAddNewBankAccount()
        if (presenter.isMsisdnVerified() && enableAddButton) {
            val intentBankForm = AddEditBankActivity.createIntentAddBank(activity!!)
            startActivityForResult(intentBankForm, REQUEST_ADD_BANK)
        } else if (activity != null
                && activity!!.applicationContext is BankRouter
                && !presenter.isMsisdnVerified()) {
            val intentPhoneVerification = (activity!!.applicationContext as BankRouter)
                    .getPhoneVerificationActivityIntent(activity!!)
            startActivityForResult(intentPhoneVerification, REQUEST_PHONE_VERIFICATION)
        } else {
            showErrorAddAccount(reason)
        }
    }

    override fun showLoadingFull() {
        progress_bar.visibility = View.VISIBLE
        main_view.visibility = View.GONE
    }

    override fun hideLoadingFull() {
        progress_bar.visibility = View.GONE
        main_view.visibility = View.VISIBLE

    }

    override fun showLoadingList() {
        adapter.showLoading()
    }

    override fun hideLoadingList() {
        adapter.hideLoading()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (activity != null && resultCode == Activity.RESULT_OK) {
            var resultMessage = ""
            when (requestCode) {
                REQUEST_ADD_BANK -> resultMessage = getString(R.string.success_add_bank_account)
                REQUEST_EDIT_BANK -> resultMessage = getString(R.string.success_edit_bank_account)
            }
            presenter.refreshBankList(resultMessage)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }
}