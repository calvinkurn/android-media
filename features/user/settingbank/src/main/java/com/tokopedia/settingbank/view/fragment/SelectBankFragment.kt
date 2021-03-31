package com.tokopedia.settingbank.view.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.domain.model.Bank
import com.tokopedia.settingbank.view.activity.AddBankActivity
import com.tokopedia.settingbank.view.activity.ChooseBankActivity
import com.tokopedia.settingbank.view.adapter.BankListAdapter
import com.tokopedia.settingbank.view.adapter.BankListClickListener
import com.tokopedia.settingbank.view.viewModel.SelectBankViewModel
import com.tokopedia.settingbank.view.viewState.OnBankListLoaded
import com.tokopedia.settingbank.view.viewState.OnBankListLoadingError
import com.tokopedia.settingbank.view.viewState.OnBankSearchResult
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.fragment_choose_bank.*
import java.util.*
import javax.inject.Inject

class SelectBankFragment : BottomSheetUnify(),  BankListClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var selectBankViewModel: SelectBankViewModel

    @Inject
    lateinit var bankListAdapter: BankListAdapter

    private lateinit var onBankSelectedListener: OnBankSelectedListener

    var childView: View? = null

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
        childView = LayoutInflater.from(context).inflate(R.layout.fragment_choose_bank,
                null, false)
        setChild(childView)
    }

    private fun initViewModels() {
        selectBankViewModel = ViewModelProvider(this, viewModelFactory)
                .get(SelectBankViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBankRecyclerView()
        startObservingViewModels()
        loadBankList()
        setupSearchInputView()
    }

    private fun loadBankList() {
        showBankListLoading()
        selectBankViewModel.loadBankList()
    }

    private fun startObservingViewModels() {
        selectBankViewModel.bankListState.observe(viewLifecycleOwner, Observer {
            when (it) {
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
        when (throwable) {
            is MessageErrorException -> {
                showGlobalError(GlobalError.SERVER_ERROR, ::loadBankList)
            }
            else -> showGlobalError(GlobalError.NO_CONNECTION, ::loadBankList)
        }
    }

    private fun showGlobalError(errorType: Int, retryAction: () -> Unit) {
        globalError.visible()
        globalError.setType(errorType)
        globalError.errorAction.visible()
        globalError.errorAction.setOnClickListener {
            showBankListLoading()
            retryAction.invoke()
        }
    }

    private fun showBankList(bankList: ArrayList<Bank>) {
        globalError.gone()
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
        globalError.gone()
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
        searchInputTextView.searchBarTextField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                s?.toString()?.apply { onSearchTextChanged(this) } ?: run { onSearchTextChanged("")}
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
        searchInputTextView.clearListener = { onSearchReset() }
    }



    fun onSearchTextChanged(text: String?) {
        text?.let {
            selectBankViewModel.searchBankByQuery(text)
        }
    }

    fun onSearchReset() {
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
                                      fragmentManager: FragmentManager): SelectBankFragment {
            val selectBankFragment = SelectBankFragment()
            selectBankFragment.setTitle(context.getString(R.string.sbank_choose_a_bank))
            selectBankFragment.isFullpage = true
            selectBankFragment.isDragable = true
            selectBankFragment.isHideable = true
            selectBankFragment.show(fragmentManager, TAG)
            return selectBankFragment
        }
    }

}

interface OnBankSelectedListener {
    fun onBankSelected(bank: Bank)
}
