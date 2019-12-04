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
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SaldoHoldDepositHistory
import com.tokopedia.saldodetails.response.model.saldoholdinfo.response.SellerDataItem
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class SaldoHoldInfoFragment : BaseDaggerFragment(),SaldoHoldInfoContract.View {

    lateinit var rvListOne: RecyclerView
    lateinit var rvListTwo: RecyclerView

    @Inject
    lateinit var saldoInfoPresenter: SaldoHoldInfoPresenter
    val saldoHoldInfoAdapter: SaldoHoldInfoAdapter by lazy { SaldoHoldInfoAdapter(ArrayList()) }

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
        val view = inflater.inflate(R.layout.fragment_hold_info, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvListOne = view.findViewById(R.id.rv_list1)
        rvListTwo = view.findViewById(R.id.rv_list2)
        initView()
    }

    fun initView() {
        rvListOne.layoutManager = LinearLayoutManager(context)
        rvListTwo.layoutManager = LinearLayoutManager(context)
        rvListOne.adapter = saldoHoldInfoAdapter
        rvListTwo.adapter = saldoHoldInfoAdapter
    }


    override fun renderSaldoHoldInfo(saldoHoldDepositHistory: SaldoHoldDepositHistory?) {
        saldoHoldInfoAdapter.list.clear()
        saldoHoldInfoAdapter.list.addAll(saldoHoldDepositHistory?.sellerData as ArrayList<SellerDataItem>)
        saldoHoldInfoAdapter.notifyDataSetChanged()
    }

}