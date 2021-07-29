package com.tokopedia.saldodetails.view.fragment.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.di.SaldoDetailsComponent
import com.tokopedia.saldodetails.response.model.saldo_detail_info.DepositHistoryData
import com.tokopedia.saldodetails.view.viewmodel.DepositHistoryInvoiceDetailViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.saldo_fragment_sales_detail.*
import javax.inject.Inject

class SaldoSalesDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: DepositHistoryInvoiceDetailViewModel? by lazy(LazyThreadSafetyMode.NONE) {
        parentFragment?.let {
            val viewModelProvider = ViewModelProvider(it, viewModelFactory.get())
            viewModelProvider.get(DepositHistoryInvoiceDetailViewModel::class.java)
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
            R.layout.saldo_fragment_sales_detail,
            container, false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObservers()
        viewModel?.getInvoiceDetail("")
        btnViewOrderDetails.setOnClickListener {
            // open order page
            openOrderDetailPage()
        }
    }

    private fun initObservers() {
        viewModel?.depositHistoryLiveData?.observe(viewLifecycleOwner, {
            when (it) {
                is Success -> onSuccessSalesDetailLoaded(it.data)
                is Fail -> onError(it.throwable)
            }
        })
    }


    private fun onSuccessSalesDetailLoaded(data: DepositHistoryData) {
        tvWithdrawalAmount.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(data.totalAmount, false)
        tvInvoiceNumber.text = data.invoiceNumber
        tvWithdrawalDate.text = data.createdTime
        llWithdrawalDetail.setData(data.depositDetail)
    }

    private fun onError(throwable: Throwable) {

    }

    private fun openOrderDetailPage() {
        val orderUrl = viewModel?.getOrderDetailUrl()
        if (orderUrl?.isEmpty() == false)
            RouteManager.route(context, orderUrl)
    }

    companion object {
        fun getInstance() = SaldoSalesDetailFragment()
    }
}