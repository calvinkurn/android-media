package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoHoldInfoAdapter
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import kotlinx.android.synthetic.main.fragment_container_saldo_info.*
import java.util.*
import kotlin.collections.ArrayList

class SaldoHoldInfoFragment : BaseDaggerFragment() {

    val SALDO_SELLER_AMOUNT = "SALDO_SELLER_AMOUNT"
    val SALDO_BUYER_AMOUNT = "SALDO_BUYER_AMOUNT"
    val RESULT_LIST = "RESULT_LIST"

    val saldoHoldInfoAdapter: SaldoHoldInfoAdapter by lazy { SaldoHoldInfoAdapter(ArrayList()) }

    override fun getScreenName() = "SaldoHoldInfoFragment"

    override fun initInjector() {
        val saldoDetailsComponent = SaldoDetailsComponentInstance.getComponent(Objects.requireNonNull<FragmentActivity>(activity).application)
        saldoDetailsComponent.inject(this)
    }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val saldoHoldInfoFragment = SaldoHoldInfoFragment()
            saldoHoldInfoFragment.arguments = bundle
            return saldoHoldInfoFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_container_saldo_info, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        val sellerAmount = arguments?.getDouble(SALDO_SELLER_AMOUNT)
        val buyerAmount = arguments?.getDouble(SALDO_BUYER_AMOUNT)
        var resultAmount = 0.0
        if (sellerAmount == 0.0) {
            if (buyerAmount != null) {
                resultAmount = buyerAmount
                title_saldo.text = resources.getString(R.string.saldo_total_balance_seller)
            }
        } else if (buyerAmount == 0.0) {
            if (sellerAmount != null) {
                resultAmount = sellerAmount
                title_saldo.text = resources.getString(R.string.saldo_total_balance_buyer)
            }
        }


        title_saldo_value.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(resultAmount, false)
        rv_container.layoutManager = LinearLayoutManager(context)
        rv_container.adapter = saldoHoldInfoAdapter

        saldoHoldInfoAdapter.list.clear()
        saldoHoldInfoAdapter.list.addAll(arguments?.getParcelableArrayList(RESULT_LIST)!!)
        saldoHoldInfoAdapter.notifyDataSetChanged()

    }

}
