package com.tokopedia.affiliate.ui.fragment.withdrawal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.Group
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.affiliate.NEW_DATE_FORMAT
import com.tokopedia.affiliate.TRANSACTION_ID
import com.tokopedia.affiliate.adapter.AffiliateSaldoWithdrawalStatusAdapter
import com.tokopedia.affiliate.di.AffiliateComponent
import com.tokopedia.affiliate.di.DaggerAffiliateComponent
import com.tokopedia.affiliate.model.response.FeeDetailData
import com.tokopedia.affiliate.model.response.WithdrawalInfoData
import com.tokopedia.affiliate.ui.custom.AffiliateWithdrawalDetailsList
import com.tokopedia.affiliate.ui.viewholder.AffiliateTransactionHistoryItemVH
import com.tokopedia.affiliate.utils.DateUtils
import com.tokopedia.affiliate.viewmodel.WithdrawalDetailViewModel
import com.tokopedia.affiliate_toko.R
import com.tokopedia.basemvvm.viewcontrollers.BaseViewModelFragment
import com.tokopedia.basemvvm.viewmodel.BaseViewModel
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.Label
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AffiliateSaldoWithdrawalDetailFragment : BaseViewModelFragment<WithdrawalDetailViewModel>() {

    @Inject
    @JvmField
    var viewModelProvider: ViewModelProvider.Factory? = null

    private var withdrawalDetailViewModel: WithdrawalDetailViewModel? = null

    private val transactionId: String by lazy {
        arguments?.getString(TRANSACTION_ID) ?: "0"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.affiliate_saldo_fragment_withdrawl_detail,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        loadWithdrawalDetail()
        initAdapter()
    }

    private fun loadWithdrawalDetail() {
        view?.findViewById<LoaderUnify>(R.id.withdrawalProgress)?.visible()
        withdrawalDetailViewModel?.getWithdrawalInfo(transactionId)
    }

    private fun initAdapter() {
        view?.findViewById<RecyclerView>(R.id.rvWithdrawalStatus)?.apply {
            adapter = AffiliateSaldoWithdrawalStatusAdapter(arrayListOf())
            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = false
        }
    }

    private fun initObservers() {
        withdrawalDetailViewModel?.withdrawalInfoLiveData?.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> onSuccessWithdrawalDetailLoaded(it.data)
                is Fail -> onErrorLoading(it.throwable)
            }
        }
    }

    private fun onErrorLoading(throwable: Throwable) {
        view?.findViewById<LoaderUnify>(R.id.withdrawalProgress)?.gone()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun setGlobalErrors(errorType: Int) {
        view?.findViewById<Group>(R.id.dataGroup)?.gone()
        view?.findViewById<GlobalError>(R.id.saldoWithdrawalDetailGlobalError)?.apply {
            setType(errorType)
            visible()
            setActionClickListener {
                gone()
                loadWithdrawalDetail()
            }
        }
    }

    private fun onSuccessWithdrawalDetailLoaded(data: WithdrawalInfoData) {
        data.feeDetailData = getFeeDetailData(data) ?: arrayListOf()
        setDataViewVisibility()
        setHeaderData(data)
        setFeeDetailBreakup(data)
        setWithdrawalStatusData(data)
    }

    private fun setHeaderData(data: WithdrawalInfoData) {
        data.ticker?.let { ticker ->
            view?.findViewById<Ticker>(R.id.saldoTicker)?.apply {
                show()
                if (!ticker.tickerTitle.isNullOrBlank()) {
                    tickerTitle = ticker.tickerTitle
                    setTextDescription(ticker.tickerDescription ?: "")
                } else {
                    hide()
                }
            }
        }
        view?.findViewById<Typography>(R.id.tvWithdrawalAmount)?.text = data.amountFormatted
        view?.findViewById<Typography>(R.id.tvBankName)?.text = data.bankName
        view?.findViewById<Typography>(R.id.tvAccountName)?.text = data.accountName
        view?.findViewById<Typography>(R.id.tvWithdrawalDate)?.text = data.createdTime?.let {
            DateUtils().formatDate(newFormat = NEW_DATE_FORMAT, dateString = it)
        }
        view?.findViewById<Label>(R.id.withdrawalStatusLabel)?.apply {
            when (data.statusLabel?.labelType) {
                AffiliateTransactionHistoryItemVH.DANGER -> setLabelType(Label.HIGHLIGHT_LIGHT_RED)
                AffiliateTransactionHistoryItemVH.SUCCESS -> setLabelType(Label.HIGHLIGHT_LIGHT_GREEN)
                AffiliateTransactionHistoryItemVH.WARNING -> setLabelType(Label.HIGHLIGHT_LIGHT_ORANGE)
                AffiliateTransactionHistoryItemVH.GRAY -> setLabelType(Label.HIGHLIGHT_LIGHT_GREY)
            }
            setLabel(data.statusLabel?.labelText ?: "")
        }
    }

    private fun setFeeDetailBreakup(data: WithdrawalInfoData) {
        view?.findViewById<AffiliateWithdrawalDetailsList>(R.id.llWithdrawalDetail)?.setData(
            data.feeDetailData,
            context?.getString(R.string.affiliate_saldo_withdrawal_info_details)
        )
    }

    private fun setWithdrawalStatusData(data: WithdrawalInfoData) {
        (
            view?.findViewById<RecyclerView>(R.id.rvWithdrawalStatus)
                ?.adapter as? AffiliateSaldoWithdrawalStatusAdapter
            )?.apply {
            historyList = data.withdrawalInfoHistory
            notifyItemRangeChanged(0, historyList.size)
        }
    }

    private fun setDataViewVisibility() {
        view?.findViewById<LoaderUnify>(R.id.withdrawalProgress)?.gone()
        view?.findViewById<Group>(R.id.dataGroup)?.visible()
    }

    private fun getFeeDetailData(data: WithdrawalInfoData) = context?.let { context ->
        arrayListOf(
            FeeDetailData(
                context.getString(R.string.affiliate_saldo_withdrawal_amount),
                data.amountFormatted
            ),
            FeeDetailData(
                context.getString(R.string.affiliate_saldo_withdrawal_fee),
                data.feeFormatted
            ),
            FeeDetailData(
                context.getString(R.string.affiliate_saldo_transferred_amount),
                data.transferredAmountFormatted
            )
        )
    }

    companion object {
        fun newInstance(transactionId: String): AffiliateSaldoWithdrawalDetailFragment {
            val fragment = AffiliateSaldoWithdrawalDetailFragment()
            val bundle = Bundle().apply { putString(TRANSACTION_ID, transactionId) }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getVMFactory(): ViewModelProvider.Factory? {
        return viewModelProvider
    }

    override fun initInject() {
        getComponent().injectWithdrawalDetailFragment(this)
    }

    private fun getComponent(): AffiliateComponent =
        DaggerAffiliateComponent
            .builder()
            .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
            .build()

    override fun getViewModelType(): Class<WithdrawalDetailViewModel> {
        return WithdrawalDetailViewModel::class.java
    }

    override fun setViewModel(viewModel: BaseViewModel) {
        withdrawalDetailViewModel = viewModel as WithdrawalDetailViewModel
    }
}
