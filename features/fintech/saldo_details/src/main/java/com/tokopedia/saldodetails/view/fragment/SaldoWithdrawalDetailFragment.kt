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
import kotlinx.android.synthetic.main.saldo_fragment_withdrawl_detail.*
import javax.inject.Inject

class SaldoWithdrawalDetailFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

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
        initAdapter()
        llWithdrawalDetail.setData()
    }

    private fun initAdapter() {
        rvWithdrawalStatus.adapter = SaldoWithdrawalStatusAdapter()
        rvWithdrawalStatus.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvWithdrawalStatus.isNestedScrollingEnabled = false
    }

    private fun initObservers() {

    }

    companion object {
        fun getInstance() = SaldoWithdrawalDetailFragment()
    }
}