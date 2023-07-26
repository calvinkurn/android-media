package com.tokopedia.saldodetails.transactionDetailPages.withdrawal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsAnalytics
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants
import com.tokopedia.saldodetails.commom.analytics.SaldoDetailsConstants.DetailScreenParams.Companion.WITHDRAWAL_ID
import com.tokopedia.saldodetails.commom.di.component.SaldoDetailsComponent
import com.tokopedia.saldodetails.commom.utils.SaldoDateUtil
import com.tokopedia.saldodetails.saldoDetail.saldoTransactionHistory.adapter.SaldoWithdrawalStatusAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.saldo_fragment_withdrawl_detail.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class SaldoWithdrawalDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var saldoDetailsAnalytics: SaldoDetailsAnalytics

    private val viewModel: WithdrawalDetailViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        activity?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory.get())
            viewModelProvider.get(WithdrawalDetailViewModel::class.java)
        } ?: run {
            null
        }
    }

    private val withdrawalId: Long by lazy {
        arguments?.getLong(WITHDRAWAL_ID) ?: run { 0 }
    }

    override fun getScreenName(): String? = null
    override fun initInjector() = getComponent(SaldoDetailsComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.saldo_fragment_withdrawl_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        loadWithdrawalDetail()
        initAdapter()
    }

    private fun loadWithdrawalDetail() {
        withdrawalProgress.visible()
        viewModel?.getWithdrawalInfo(withdrawalId.toString())
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
        withdrawalProgress.gone()
        saldoDetailsAnalytics.sendApiFailureEvents(SaldoDetailsConstants.EventLabel.SALDO_FETCH_WITHDRAWAL_DETAIL)
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }


    private fun setGlobalErrors(errorType: Int) {
        dataGroup.gone()
        saldoWithdrawalDetailGlobalError.setType(errorType)
        saldoWithdrawalDetailGlobalError.visible()
        saldoWithdrawalDetailGlobalError.setActionClickListener {
            saldoWithdrawalDetailGlobalError.gone()
            loadWithdrawalDetail()
        }
    }

    private fun onSuccessWithdrawalDetailLoaded(data: WithdrawalInfoData) {
        setDataViewVisibility()
        setHeaderData(data)
        setFeeDetailBreakup(data)
        setWithdrawalStatusData(data)
    }
    private fun setHeaderData(data: WithdrawalInfoData) {
        tvWithdrawalAmount.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(data.amount, false)
        tvBankName.text = data.bankName

        tvAccountName.text = if (data.accountNumber.isNotEmpty() && data.accountName.isNotEmpty()) {
            context?.let { it.getString(R.string.saldo_transaction_detail_account_name, data.accountNumber, data.accountName) }
        } else if (data.accountNumber.isNotEmpty()) data.accountNumber else data.accountName

        tvWithdrawalDate.text = data.createdTime
        withdrawalStatusLabel.setLabelType(SaldoDateUtil.getLocalLabelColor(data.labelColor))
        withdrawalStatusLabel.setLabel(data.labelStatus)

    }
    private fun setFeeDetailBreakup(data: WithdrawalInfoData) {
        llWithdrawalDetail.setData(data.feeDetailData, context?.getString(R.string.saldo_withdrawal_info_details))
    }

    private fun setWithdrawalStatusData(data: WithdrawalInfoData) {
        (rvWithdrawalStatus.adapter as SaldoWithdrawalStatusAdapter).apply {
            historyList = data.withdrawalInfoHistory
            notifyDataSetChanged()
        }
    }

    private fun setDataViewVisibility() {
        withdrawalProgress.gone()
        dataGroup.visible()
    }

    companion object {

        fun newInstance(withdrawalId: Long): SaldoWithdrawalDetailFragment {
            val fragment = SaldoWithdrawalDetailFragment()
            val bundle = Bundle().apply { putLong(WITHDRAWAL_ID, withdrawalId) }
            fragment.arguments = bundle
            return fragment
        }
    }
}
