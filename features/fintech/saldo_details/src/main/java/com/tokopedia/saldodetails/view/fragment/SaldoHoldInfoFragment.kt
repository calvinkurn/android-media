package com.tokopedia.saldodetails.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoHoldInfoAdapter
import com.tokopedia.saldodetails.contract.SaldoHoldInfoContract
import com.tokopedia.saldodetails.di.SaldoDetailsComponentInstance
import com.tokopedia.saldodetails.presenter.SaldoHoldInfoPresenter
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.BuyerDataItem
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldDepositHistory
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SellerDataItem
import kotlinx.android.synthetic.main.fragment_hold_info.*
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SaldoHoldInfoFragment : BaseDaggerFragment(), SaldoHoldInfoContract.View {

    @Inject
    lateinit var saldoInfoPresenter: SaldoHoldInfoPresenter
    val saldoHoldInfoAdapter: SaldoHoldInfoAdapter by lazy { SaldoHoldInfoAdapter(ArrayList(), ArrayList()) }

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_hold_info_new, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    fun initView() {
        rv_list1.layoutManager = LinearLayoutManager(context)
        rv_list1.adapter = saldoHoldInfoAdapter
    }

    override fun renderSaldoHoldInfo(saldoHoldDepositHistory: SaldoHoldDepositHistory?) {

        val headerList = ArrayList<String>(2)
        val combinedList = ArrayList<Any>()
        combinedList.addAll(headerList)
        saldoHoldDepositHistory?.let {
            combinedList.addAll(it.sellerData as ArrayList<SellerDataItem>)
            combinedList.addAll(it.buyerData as ArrayList<BuyerDataItem>)
        }
        renderLists(combinedList)
    }

    fun renderLists(combinedList: ArrayList<Any>) {
        saldoHoldInfoAdapter.list.clear()
        saldoHoldInfoAdapter.list.addAll(combinedList)
        saldoHoldInfoAdapter.notifyDataSetChanged()

    }
}