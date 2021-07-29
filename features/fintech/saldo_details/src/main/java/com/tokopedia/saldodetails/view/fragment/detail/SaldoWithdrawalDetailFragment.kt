package com.tokopedia.saldodetails.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoWithdrawalStatusAdapter
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.response.model.saldo_detail_info.WithdrawalInfoData
import com.tokopedia.saldodetails.utils.SaldoDateUtil
import com.tokopedia.saldodetails.view.viewmodel.WithdrawalDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.saldo_fragment_withdrawl_detail.*
import javax.inject.Inject

class SaldoWithdrawalDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: WithdrawalDetailViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        parentFragment?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory.get())
            viewModelProvider.get(WithdrawalDetailViewModel::class.java)
        } ?: run {
            null
        }
    }

    override fun getScreenName(): String? = null
    override fun initInjector() = getComponent(SaldoDetailsComponent::class.java).inject(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.saldo_fragment_withdrawl_detail,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel?.getWithdrawalInfo("")
        initAdapter()
    }

    private fun initAdapter() {
        rvWithdrawalStatus.adapter = SaldoWithdrawalStatusAdapter(arrayListOf())
        rvWithdrawalStatus.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvWithdrawalStatus.isNestedScrollingEnabled = false
    }

    private fun initObservers() {
        viewModel?.withdrawalInfoLiveData?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessWithdrawalDetailLoaded(it.data)
                is Fail -> onErrorLoading(it.throwable)
            }
        })
    }

    private fun onErrorLoading(throwable: Throwable) {

    }

    private fun onSuccessWithdrawalDetailLoaded(data: WithdrawalInfoData) {
        tvWithdrawalAmount.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(data.amount, false)
        tvBankName.text = data.bankName
        tvAccountName.text = "${data.accountNumber} - ${data.accountName}"
        tvWithdrawalDate.text = data.createdTime
        withdrawalStatusLabel.setLabelType(
            SaldoDateUtil.getLocalLabelColor(data.labelColor)
        )
        withdrawalStatusLabel.setLabel(data.labelStatus)
        llWithdrawalDetail.setData(data.feeDetailData)
        (rvWithdrawalStatus.adapter as SaldoWithdrawalStatusAdapter).historyList =
            data.withdrawalInfoHistory
    }

    companion object {
        fun newInstance() = SaldoWithdrawalDetailFragment()
    }
}