package com.tokopedia.settingbank.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.domain.model.SettingBankErrorHandler
import com.tokopedia.settingbank.view.activity.AddBankActivity
import com.tokopedia.settingbank.view.activity.ChooseBankActivity
import com.tokopedia.settingbank.view.adapter.BankListAdapter
import com.tokopedia.settingbank.view.adapter.BankListClickListener
import com.tokopedia.settingbank.view.viewModel.SelectBankViewModel
import com.tokopedia.settingbank.view.viewState.OnBankListLoaded
import com.tokopedia.settingbank.view.viewState.OnBankListLoading
import com.tokopedia.settingbank.view.viewState.OnBankListLoadingError
import com.tokopedia.settingbank.view.viewState.OnBankSearchResult
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_choose_bank_v2.*
import java.util.*
import javax.inject.Inject

class SelectBankFragment : BottomSheetUnify(), SearchInputView.Listener, SearchInputView.ResetListener, BankListClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var selectBankViewModel: SelectBankViewModel

    @Inject
    lateinit var bankListAdapter: BankListAdapter

    private lateinit var onBankSelectedListener: OnBankSelectedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val isInjectorInitialized = initInjector()
        if (isInjectorInitialized) {
            setChild()
            initViewModels()
        } else {
            dismiss()
        }
    }


    private fun initInjector(): Boolean {
        activity?.let {
            return when (activity) {
                is ChooseBankActivity -> {
                    (activity as ChooseBankActivity).component.inject(this)
                    true
                }
                is AddBankActivity -> {
                    (activity as AddBankActivity).component.inject(this)
                    true
                }
                else -> {
                    false
                }
            }
        } ?: run {
            return false
        }
    }


    private fun setChild() {
        val childView = LayoutInflater.from(context).inflate(R.layout.fragment_choose_bank_v2,
                null, false)
        setChild(childView)
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        selectBankViewModel = viewModelProvider.get(SelectBankViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBankRecyclerView()
        startObservingViewModels()
        loadBankList()
        setupSearchInputView()
    }

    private fun loadBankList() {
        selectBankViewModel.loadBankList()
    }

    private fun startObservingViewModels() {
        selectBankViewModel.bankListState.observe(viewLifecycleOwner, Observer {
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
        rvChooseBank.post {
            bankListAdapter.updateItem(bankList)
        }
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
            rvChooseBank.post {
                bankListAdapter.updateItem(bankList)
            }
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
        dismiss()
    }

    private fun setupSearchInputView() {
        searchInputTextView.searchImageView.setImageDrawable(resources.
                getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_search_grayscale_24))
        searchInputTextView.closeImageButton.setImageDrawable(resources
                .getDrawable(com.tokopedia.resources.common.R.drawable.ic_system_action_close_grayscale_16))
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnBankSelectedListener) {
            onBankSelectedListener = context
        }
    }

    companion object {
        private const val TAG = "SelectBankFragment"
        fun showChooseBankBottomSheet(context: Context,
                                      fragmentManager: FragmentManager) : SelectBankFragment{
            val selectBankFragment = SelectBankFragment()
            selectBankFragment.setTitle(context.getString(R.string.sbank_choose_a_bank))
            selectBankFragment.isFullpage = true
            selectBankFragment.isDragable =  true
            selectBankFragment.isHideable= true
            selectBankFragment.show(fragmentManager, TAG)
            return selectBankFragment
        }
    }

}

interface OnBankSelectedListener {
    fun onBankSelected(bank: Bank)
}