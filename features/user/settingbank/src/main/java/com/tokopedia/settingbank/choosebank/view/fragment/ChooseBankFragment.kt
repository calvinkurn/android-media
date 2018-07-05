package com.tokopedia.settingbank.choosebank.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tkpd.library.ui.view.LinearLayoutManager
import com.tkpd.library.utils.KeyboardHandler
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.addeditaccount.analytics.AddEditBankAnalytics.Companion.SCREEN_NAME_CHOOSE_BANK
import com.tokopedia.settingbank.addeditaccount.view.listener.ChooseBankContract
import com.tokopedia.settingbank.choosebank.di.ChooseBankDependencyInjector
import com.tokopedia.settingbank.choosebank.view.adapter.BankAdapter
import com.tokopedia.settingbank.choosebank.view.adapter.BankTypeFactoryImpl
import com.tokopedia.settingbank.choosebank.view.listener.BankListener
import com.tokopedia.settingbank.choosebank.view.presenter.ChooseBankPresenter
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import kotlinx.android.synthetic.main.fragment_choose_bank.*

/**
 * @author by nisie on 6/22/18.
 */

class ChooseBankFragment : ChooseBankContract.View, BankListener, SearchInputView.Listener,
        BaseDaggerFragment() {

    lateinit var adapter: BankAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var presenter: ChooseBankPresenter

    var query = ""

    override fun getScreenName(): String {
        return SCREEN_NAME_CHOOSE_BANK
    }

    override fun initInjector() {
        presenter = ChooseBankDependencyInjector.Companion.inject(activity.applicationContext)
        presenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_choose_bank, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        presenter.getBankList()
    }

    private fun setupView() {
        val adapterTypeFactory = BankTypeFactoryImpl(this)
        val listBank = ArrayList<Visitable<*>>()
        adapter = BankAdapter(adapterTypeFactory, listBank)
        linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        bank_list_rv.layoutManager = linearLayoutManager
        bank_list_rv.adapter = adapter

        search_input_view.setListener(this)
    }


    override fun onBankSelected(adapterPosition: Int, element: BankViewModel?) {
        //TODO SET RESULT
        adapter.setSelected(adapterPosition)
        activity.finish()
    }


    override fun showLoading() {
        search_input_view.visibility = View.GONE
        bank_list_rv.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        search_input_view.visibility = View.VISIBLE
        bank_list_rv.visibility = View.VISIBLE
        progress_bar.visibility = View.GONE
    }

    override fun onErrorGetBankList(errorMessage: String?) {
        search_input_view.visibility = View.GONE
        adapter.hideSearchNotFound()
        if (!errorMessage.isNullOrBlank()) {
            NetworkErrorHelper.showEmptyState(context, view, errorMessage, {
                presenter.getBankList()
            })
        } else {
            NetworkErrorHelper.showEmptyState(context, view, {
                presenter.getBankList()
            })
        }
    }

    override fun onSuccessGetBankList(listBank: ArrayList<BankViewModel>) {
        adapter.hideSearchNotFound()
        adapter.setList(listBank)
    }

    override fun onSearchSubmitted(query: String) {
        if(!query.isBlank()) {
            KeyboardHandler.DropKeyboard(context, view)
            presenter.searchBank(query)
        }
    }

    override fun onSearchTextChanged(query: String) {
        if (query.isEmpty()) {
            KeyboardHandler.DropKeyboard(context, view)
            presenter.getBankList()
        }
    }

    override fun onSuccessSearchBank(list: ArrayList<BankViewModel>) {
        adapter.hideSearchNotFound()
        adapter.setList(list)

    }

    override fun onEmptySearchBank() {
        adapter.showSearchNotFound()
    }
}