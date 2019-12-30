package com.tokopedia.settingbank.banklist.v2.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.domain.SettingBankErrorHandler
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankListAdapter
import com.tokopedia.settingbank.banklist.v2.view.adapter.BankListClickListener
import com.tokopedia.settingbank.banklist.v2.view.viewModel.SelectBankViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnBankListLoaded
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnBankListLoading
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnBankListLoadingError
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnBankSearchResult
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_choose_bank_v2.*
import java.util.*
import javax.inject.Inject

class SelectBankFragment : BaseDaggerFragment(), SearchInputView.Listener, SearchInputView.ResetListener, BankListClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var selectBankViewModel: SelectBankViewModel

    @Inject
    lateinit var bankListAdapter: BankListAdapter

    private lateinit var onBankSelectedListener: OnBankSelectedListener

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
        selectBankViewModel = viewModelProvider.get(SelectBankViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_bank_v2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBankRecyclerView()
        startObservingViewModels()
        loadBankList()
        tempFun()
    }

    private fun loadBankList() {
        selectBankViewModel.loadBankList()
    }

    private fun startObservingViewModels() {
        selectBankViewModel.bankListState.observe(this, Observer {
            when (it) {
                is OnBankListLoading -> showBankListLoading()
                is OnBankListLoaded -> showBankList(it.bankList)
                is OnBankListLoadingError -> {
                    hideUI()
                    showLoadingError(it.throwable)
                }
                is OnBankSearchResult -> showBankSearchResult(it.bankList)
            }
        })
    }

    private fun showBankSearchResult(bankList: ArrayList<Bank>) {
        bankListAdapter.updateItem(bankList)
    }

    private fun hideUI() {
        progressBar.gone()
        searchInputTextView.gone()
        rvChooseBank.gone()
    }

    private fun showLoadingError(throwable: Throwable) {
        view?.let {
            context?.let { context ->
                Toaster.make(it, SettingBankErrorHandler.getErrorMessage(context, throwable), Toast.LENGTH_SHORT)
            }
        }
    }

    private fun showBankList(bankList: ArrayList<Bank>) {
        if (bankList.isNotEmpty()) {
            progressBar.gone()
            searchInputTextView.visible()
            rvChooseBank.visible()
            bankListAdapter.updateItem(bankList)
        } else {
            searchInputTextView.gone()
            rvChooseBank.gone()
        }
    }

    private fun showBankListLoading() {
        progressBar.visible()
        rvChooseBank.gone()
        searchInputTextView.gone()
    }

    private fun initBankRecyclerView() {
        rvChooseBank.layoutManager = LinearLayoutManager(activity)
        rvChooseBank.adapter = bankListAdapter
        bankListAdapter.listener = this
    }

    override fun onBankSelected(bank: Bank) {
        if (::onBankSelectedListener.isInitialized)
            onBankSelectedListener.onBankSelected(bank)
    }

    private fun tempFun() {
        searchInputTextView.searchImageView.setImageDrawable(resources.getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_search_grayscale_24))
        searchInputTextView.closeImageButton.setImageDrawable(resources.getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_close_grayscale_16))
        searchInputTextView.setListener(this)
        searchInputTextView.setResetListener(this)
    }

    override fun onSearchSubmitted(text: String?) {
        text?.let {
            selectBankViewModel.searchBankByQuery(text)
        }
    }

    override fun onSearchTextChanged(text: String?) {
        text?.let {
            selectBankViewModel.searchBankByQuery(text)
        }
    }

    override fun onSearchReset() {
        selectBankViewModel.resetSearchResult()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnBankSelectedListener) {
            onBankSelectedListener = context
        }
    }

}

interface OnBankSelectedListener {
    fun onBankSelected(bank: Bank)
}