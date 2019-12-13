package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoHoldInfoAdapter
import com.tokopedia.saldodetails.contract.SaldoHoldInfoContract
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.presenter.SaldoHoldInfoPresenter
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.BuyerDataItem
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldDepositHistory
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SellerDataItem
import kotlinx.android.synthetic.main.fragment_container_saldo_info.*
import kotlinx.android.synthetic.main.saldo_hold_info_tabview.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SaldoHoldInfoFragment : BaseDaggerFragment(), SaldoHoldInfoContract.View {

    var isTickerShow: Boolean? = false
    var tickerMessage: String? = null

    @Inject
    lateinit var saldoInfoPresenter: SaldoHoldInfoPresenter
    val saldoHoldInfoAdapter: SaldoHoldInfoAdapter by lazy { SaldoHoldInfoAdapter(ArrayList()) }
    lateinit var allTransactionList: ArrayList<Any>
    lateinit var fakelist: ArrayList<Any>

    override fun getScreenName() = "SaldoHoldInfoFragment"

    override fun initInjector() {
        val saldoDetailsComponent = SaldoDetailsComponentInstance.getComponent(Objects.requireNonNull<FragmentActivity>(activity).application)
        saldoDetailsComponent.inject(this)
    }

    companion object {
        fun createInstance(): Fragment {
            return SaldoHoldInfoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_container_saldo_info, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        saldoInfoPresenter.attachView(this)
        saldoInfoPresenter.getSaldoHoldInfo()
    }

    fun initView() {
        rv_container.layoutManager = LinearLayoutManager(context)
        rv_container.adapter = saldoHoldInfoAdapter
    }

    override fun renderSaldoHoldInfo(saldoHoldDepositHistory: SaldoHoldDepositHistory?) {
        var resultList = ArrayList<Any>()
        saldoHoldDepositHistory?.let {
            resultList = combinedTransactionList(it.sellerData as ArrayList<SellerDataItem>, it.buyerData as ArrayList<BuyerDataItem>)
            isTickerShow = it.tickerMessageIsshow
            tickerMessage = it.tickerMessageId
        }

        if (resultList.size == 1) {
            activity?.ll_container?.visibility = View.GONE
        }
        if (isTickerShow as Boolean) {
            activity?.saldo_hold_info_ticker.let {
                it?.setTextDescription(tickerMessage.toString())
            }
        } else
            activity?.saldo_hold_info_ticker?.visibility = View.GONE

        activity?.btn_bantuan?.setOnClickListener {
            //TODO btn click
        }
        saldoHoldInfoAdapter.list.clear()
        saldoHoldInfoAdapter.list.addAll(resultList)
        saldoHoldInfoAdapter.notifyDataSetChanged()
    }

    fun combinedTransactionList(arrayList: ArrayList<SellerDataItem>, arrayList1: ArrayList<BuyerDataItem>): ArrayList<Any> {
        allTransactionList = ArrayList()
        allTransactionList.clear()
        allTransactionList.addAll(arrayList)
        allTransactionList.addAll(arrayList1)

        return allTransactionList
    }

    override fun onDestroy() {
        super.onDestroy()
        saldoInfoPresenter.detachView()
    }

}
