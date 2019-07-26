package com.tokopedia.settingbank.choosebank.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.addeditaccount.view.listener.ChooseBankContract
import com.tokopedia.settingbank.choosebank.view.activity.ChooseBankActivity
import com.tokopedia.settingbank.choosebank.view.adapter.BankAdapter
import com.tokopedia.settingbank.choosebank.view.adapter.BankTypeFactoryImpl
import com.tokopedia.settingbank.choosebank.view.listener.BankListener
import com.tokopedia.settingbank.choosebank.view.presenter.ChooseBankPresenter
import com.tokopedia.settingbank.choosebank.view.viewmodel.BankViewModel
import com.tokopedia.settingbank.banklist.analytics.SettingBankAnalytics
import com.tokopedia.settingbank.choosebank.di.DaggerChooseBankComponent
import kotlinx.android.synthetic.main.fragment_choose_bank.*
import javax.inject.Inject

/**
 * @author by nisie on 6/22/18.
 */

class ChooseBankFragment : ChooseBankContract.View, BankListener, SearchInputView.Listener,
        BaseDaggerFragment() {

    lateinit var adapter: BankAdapter
    lateinit var linearLayoutManager: LinearLayoutManager

    @Inject
    lateinit var presenter: ChooseBankPresenter

    var query = ""
    private val MIN_CHAR_SEARCH: Int = 3

    override fun getScreenName(): String {
        return SettingBankAnalytics.SCREEN_NAME_CHOOSE_BANK
    }

    override fun initInjector() {
        if (activity != null && (activity as Activity).application != null) {
            val addChooseBankComponent = DaggerChooseBankComponent.builder().baseAppComponent(
                    ((activity as Activity).application as BaseMainApplication).baseAppComponent)
                    .build()

            addChooseBankComponent.inject(this)
            presenter.attachView(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
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
        adapter.setSelected(adapterPosition)
        val intent = Intent()
        val bundle = Bundle()
        bundle.putParcelable(ChooseBankActivity.Companion.PARAM_RESULT_DATA, element)
        intent.putExtras(bundle)
        activity!!.setResult(Activity.RESULT_OK, intent)
        activity!!.finish()
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
        adapter.setList(listBank)
        setSelectedBank()
    }

    private fun setSelectedBank() {
        if (activity != null
                && activity?.intent != null
                && !activity?.intent!!.getStringExtra(ChooseBankActivity.PARAM_BANK_ID)
                        .isNullOrEmpty()) {
            val bankId = activity?.intent!!.getStringExtra(ChooseBankActivity.PARAM_BANK_ID)
            adapter.setSelectedFromBankId(bankId)
        }
    }

    override fun onSearchSubmitted(query: String) {
        if (!query.isBlank()) {
            KeyboardHandler.DropKeyboard(context, view)
            presenter.searchBank(query)
        }
    }


    override fun onSearchTextChanged(query: String) {
        if (query.isEmpty()) {
            KeyboardHandler.DropKeyboard(context, view)
            presenter.getBankList()
        } else if (query.length >= MIN_CHAR_SEARCH) {
            presenter.searchBank(query)
        }
    }

    override fun onSuccessSearchBank(list: ArrayList<BankViewModel>) {
        adapter.setList(list)
        setSelectedBank()
    }

    override fun onEmptySearchBank() {
        adapter.showSearchNotFound()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detachView()
    }
}