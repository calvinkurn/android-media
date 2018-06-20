package com.tokopedia.settingbank.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.component.Dialog
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.common.analytics.SettingBankAnalytics
import com.tokopedia.settingbank.common.di.SettingBankDependencyInjector
import com.tokopedia.settingbank.view.adapter.BankAccountAdapter
import com.tokopedia.settingbank.view.adapter.BankAccountTypeFactoryImpl
import com.tokopedia.settingbank.view.listener.BankAccountPopupListener
import com.tokopedia.settingbank.view.listener.SettingBankContract
import com.tokopedia.settingbank.view.presenter.SettingBankPresenter
import com.tokopedia.settingbank.view.viewmodel.BankAccountListViewModel
import com.tokopedia.settingbank.view.viewmodel.BankAccountViewModel
import kotlinx.android.synthetic.main.fragment_setting_bank.*

/**
 * @author by nisie on 6/7/18.
 */

class SettingBankFragment : SettingBankContract.View, BankAccountPopupListener, BaseDaggerFragment() {

    lateinit var presenter: SettingBankPresenter
    lateinit var adapter: BankAccountAdapter
    lateinit var alertDialog: Dialog

    companion object {

        fun newInstance(): SettingBankFragment {
            return SettingBankFragment()
        }
    }


    override fun getScreenName(): String {
        return SettingBankAnalytics.SCREEN_NAME
    }

    override fun initInjector() {
        presenter = SettingBankDependencyInjector.Companion.inject(activity.applicationContext)
        presenter.attachView(this)
    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view: View = inflater!!.inflate(R.layout.fragment_setting_bank, container, false)
        return view
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        getBankList()
    }

    private fun setupView() {
        val adapterTypeFactory = BankAccountTypeFactoryImpl(this)
        val listBank = ArrayList<Visitable<*>>()
        listBank.add(BankAccountViewModel("1", "", "", "", true, "", "", false, ""))
        listBank.add(BankAccountViewModel("2", "", "", "", true, "", "", false, ""))
        listBank.add(BankAccountViewModel("3", "", "", "", true, "", "", false, ""))

        adapter = BankAccountAdapter(adapterTypeFactory, listBank)
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        account_list_rv.layoutManager = linearLayoutManager
        account_list_rv.adapter = adapter
    }

    private fun getBankList() {
        presenter.getBankList()
    }

    override fun onErrorGetListBank(errorMessage: String) {
        NetworkErrorHelper.showEmptyState(context, view) {
            getBankList()
        }
    }

    override fun onSuccessGetListBank(bankAccountList: BankAccountListViewModel) {
        adapter.addList(bankAccountList.list!!)
    }

    override fun makeMainAccount(adapterPosition: Int, element: BankAccountViewModel?) {
        if (!::alertDialog.isInitialized) {
            alertDialog = Dialog(activity, Dialog.Type.PROMINANCE)
        }

        alertDialog.setTitle(getString(R.string.make_main_account_prompt_title))
        alertDialog.setDesc(composeDescription(element))
        alertDialog.setBtnCancel(getString(R.string.No))
        alertDialog.setBtnOk(getString(R.string.yes))
        alertDialog.setOnCancelClickListener({ alertDialog.dismiss() })
        alertDialog.setOnOkClickListener({
            presenter.setMainAccount(adapterPosition, element)
            alertDialog.dismiss()
        })

        alertDialog.show()

    }

    override fun onSuccessSetMain(adapterPosition: Int) {
        adapter.setMain(adapterPosition)
    }

    private fun composeDescription(element: BankAccountViewModel?): String {
        if (element != null) {
            return String.format("%s %s %s %s %s %s",
                    getString(R.string.you_will_make_account),
                    element.bankName,
                    element.accountNumber,
                    getString(R.string.under_name),
                    element.accountName,
                    getString(R.string.become_default)
            )
        } else {
            return ""
        }
    }

    override fun editBankAccount(adapterPosition: Int, element: BankAccountViewModel?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteBankAccount(adapterPosition: Int, element: BankAccountViewModel?) {
        adapter.remove(adapterPosition)
    }
}