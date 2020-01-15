package com.tokopedia.saldodetails.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.saldodetails.R
import com.tokopedia.saldodetails.adapter.SaldoInfoVIewPagerAdapter
import com.tokopedia.saldodetails.view.fragment.SaldoHoldInfoFragment
import com.tokopedia.saldodetails.view.ui.SaldoHistoryTabItem
import kotlinx.android.synthetic.main.saldo_info_toolbar.*

class SaldoHoldInfoActivity : BaseActivity() {

    lateinit var item: ArrayList<SaldoHistoryTabItem>

    val saldoHoldInfoViewpagerAdapter: SaldoInfoVIewPagerAdapter by lazy { SaldoInfoVIewPagerAdapter(supportFragmentManager, ArrayList()) }

    fun newInstance(context: Context): Intent {
        return Intent(context, SaldoHoldInfoFragment::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.saldo_hold_info_tabview)
        getFragments()
        saldoHoldInfoViewpagerAdapter.arrayList = item

        val tabLayout = findViewById<TabLayout>(R.id.tabs_saldo_info_type)
        val viewPager = findViewById<ViewPager>(R.id.view_pager_saldo_info_type)
        viewPager.adapter = saldoHoldInfoViewpagerAdapter
        tabLayout.setupWithViewPager(viewPager)
        top_bar_close_button.setOnClickListener{
            onBackPressed()
        }
    }

    override fun getComponent(): SaldoDetailsComponent {
        return SaldoDetailsComponentInstance.getComponent(application)
    }

    override fun getNewFragment(): Fragment? = null

    override fun renderSaldoHoldInfo(saldoHoldDepositHistory: SaldoHoldDepositHistory?) {
        resultList = ArrayList()

        saldoHoldDepositHistory?.let {

            tv_valueTotalSaldoHold.text = it.totalFmt
            sellerListSize = it.sellerData?.size!!
            buyerListSize = it.buyerData?.size!!
            val arrayListSeller = it.sellerData as ArrayList<SellerDataItem>
            val arrayListBuyer = it.buyerData as ArrayList<BuyerDataItem>

            for (i in 0 until arrayListSeller.size) {
                sellerAmount = arrayListSeller[i].amountFmt?.removeRP()?.let { it1 -> sellerAmount?.plus(it1) }
            }

            for (i in 0 until arrayListBuyer.size) {
                buyerAmount = arrayListBuyer[i].amountFmt?.removeRP()?.let { it1 -> buyerAmount?.plus(it1) }
            }

            resultList = combinedTransactionList(it.sellerData, it.buyerData)
            isTickerShow = it.tickerMessageIsshow
            tickerMessage = it.tickerMessageId
        }

        setUpViewPager(sellerListSize, buyerListSize)
        initViewPagerAdapter()
        showLayout()

    }

    fun combinedTransactionList(arrayList: ArrayList<SellerDataItem>, arrayList1: ArrayList<BuyerDataItem>): ArrayList<Any> {
        allTransactionList = ArrayList()
        allTransactionList.clear()
        allTransactionList.addAll(arrayList)
        allTransactionList.addAll(arrayList1)

        return allTransactionList
    }

    private fun setUpViewPager(sellerListSize: Int, buyerListSize: Int) {
        item = ArrayList()

        val saldoHistoryTabItemSeller = SaldoHistoryTabItem()
        saldoHistoryTabItemSeller.fragment = SaldoHoldInfoFragment.createInstance()
        item.add(saldoHistoryTabItemSeller)

        val saldoHistoryTabItemBuyer = SaldoHistoryTabItem()
        saldoHistoryTabItemBuyer.fragment = SaldoHoldInfoFragment.createInstance()
        item.add(saldoHistoryTabItemBuyer)

    }

    override fun showErrorView() {
        container_cl.visibility = View.GONE
        viewflipper_container.displayedChild = 1
        globalerror.setType(5)
        globalerror.setActionClickListener {
            saldoInfoPresenter.getSaldoHoldInfo()
        }
    }

    private fun showLayout() {
        container_cl.visibility = View.VISIBLE
        viewflipper_container.displayedChild = 0
    }


}