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

    fun getFragments() {

        item = ArrayList()

        val saldoHistoryTabItemSeller = SaldoHistoryTabItem()
        saldoHistoryTabItemSeller.fragment = SaldoHoldInfoFragment.createInstance()
        item.add(saldoHistoryTabItemSeller)

        val saldoHistoryTabItemBuyer = SaldoHistoryTabItem()
        saldoHistoryTabItemBuyer.fragment = SaldoHoldInfoFragment.createInstance()
        item.add(saldoHistoryTabItemBuyer)

    }


}