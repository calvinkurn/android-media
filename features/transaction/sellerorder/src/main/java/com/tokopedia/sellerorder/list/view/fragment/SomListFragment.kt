package com.tokopedia.sellerorder.list.view.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.view.adapter.SomListItemAdapter
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import kotlinx.android.synthetic.main.fragment_som_list.*

/**
 * Created by fwidjaja on 2019-08-23.
 */
class SomListFragment: BaseDaggerFragment() {

    private lateinit var somListItemAdapter: SomListItemAdapter

    companion object {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @JvmStatic
        fun newInstance(): SomListFragment {
            return SomListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initInfoTicker()
        initQuickFilter()
        initOrderList()
    }

    private fun initInfoTicker() {
        val listTickerData = arrayListOf<TickerData>()
        listTickerData.add(TickerData("Ticker Title1", "Ticker Desc1", Ticker.TYPE_ANNOUNCEMENT, true))
        listTickerData.add(TickerData("Ticker Title2", "Ticker Desc2", Ticker.TYPE_ANNOUNCEMENT, true))
        listTickerData.add(TickerData("Ticker Title3", "Ticker Desc3", Ticker.TYPE_ANNOUNCEMENT, true))

        context?.let {
            val adapter = TickerPagerAdapter(it, listTickerData)
            ticker_info?.addPagerView(adapter, listTickerData)
        }
    }

    private fun initQuickFilter() {
        //val listQuickFilter = List<QuickFilterItem>(5)
        val listQuickFilter = arrayListOf<QuickFilterItem>()

        val filterItem1 = CustomViewQuickFilterItem()
        filterItem1.name = "Siap Dikirim nih"
        filterItem1.setColorBorder(R.color.tkpd_main_green)
        filterItem1.isSelected = true
        listQuickFilter.add(filterItem1)

        val filterItem2 = CustomViewQuickFilterItem()
        filterItem2.name = "Pesanan mau dibatalin"
        filterItem2.setColorBorder(R.color.gray_background)
        listQuickFilter.add(filterItem2)

        val filterItem3 = CustomViewQuickFilterItem()
        filterItem3.name = "Pesanan Baru"
        filterItem3.setColorBorder(R.color.gray_background)
        listQuickFilter.add(filterItem3)

        val filterItem4 = CustomViewQuickFilterItem()
        filterItem4.name = "Pesanan Lama"
        filterItem4.setColorBorder(R.color.gray_background)
        listQuickFilter.add(filterItem4)

        quick_filter.renderFilter(listQuickFilter)
    }

    private fun initOrderList() {
        somListItemAdapter = SomListItemAdapter()
        order_list_rv?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = somListItemAdapter
        }

        val testSomListItem = arrayListOf<String>()
        testSomListItem.add("TEST INVOICE1")
        testSomListItem.add("TEST INVOICE2")
        testSomListItem.add("TEST INVOICE3")

        somListItemAdapter.somItemList = testSomListItem.toMutableList()
        somListItemAdapter.notifyDataSetChanged()
    }

    override fun getScreenName(): String {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return ""
    }

    override fun initInjector() {
        // TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}