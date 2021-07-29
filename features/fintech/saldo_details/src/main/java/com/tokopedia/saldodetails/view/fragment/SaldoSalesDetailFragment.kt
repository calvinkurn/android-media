package com.tokopedia.saldodetails.view.fragment

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
import com.tokopedia.saldodetails.view.activity.SaldoSalesDetailActivity
import com.tokopedia.saldodetails.view.activity.SaldoWithdrawalDetailActivity
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.saldo_fragment_sales_detail.*
import javax.inject.Inject

class SaldoSalesDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

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
        btnViewOrderDetails.setOnClickListener {
            startActivity(SaldoWithdrawalDetailActivity.newInstance(requireContext()))
        }
        onSuccessSalesDetailLoaded()
    }

    private fun initObservers() {

    }

    private fun onSuccessSalesDetailLoaded() {
        tvWithdrawalAmount.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(600000, false)
        tvInvoiceNumber.text = "INV/20161025/XVI/X/55069657"
        tvWithdrawalDate.text = "22 Des 2020, 10:30 WIB"
        llWithdrawalDetail.setData()


    }

    companion object {
        fun getInstance() = SaldoSalesDetailFragment()
    }
}