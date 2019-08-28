package com.tokopedia.sellerorder.list.presentation.fragment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.design.quickfilter.QuickFilterItem
import com.tokopedia.design.quickfilter.custom.CustomViewQuickFilterItem
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.list.data.model.SomListTicker
import com.tokopedia.sellerorder.list.di.SomListComponent
import com.tokopedia.sellerorder.list.presentation.adapter.SomListItemAdapter
import com.tokopedia.sellerorder.list.presentation.viewmodel.SomListViewModel
import com.tokopedia.sellerorder.list.usecase.GetSomListTickerUseCase.Companion.INPUT_CLIENT
import com.tokopedia.sellerorder.list.usecase.GetSomListTickerUseCase.Companion.INPUT_SELLER
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_som_list.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-08-23.
 */
class SomListFragment: BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var somListItemAdapter: SomListItemAdapter
    private var tickerList: List<SomListTicker.Data.OrderTickers.Tickers> = listOf()

    private val somListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[SomListViewModel::class.java]
    }

    companion object {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @JvmStatic
        fun newInstance(): SomListFragment {
            return SomListFragment()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(SomListComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loadTicker()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_som_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observingTicker()

        // dummy layout
        initQuickFilter()
        initOrderList()
    }

    private fun loadTicker() {
        somListViewModel.getTickerList(GraphqlHelper.loadRawString(resources, R.raw.gql_som_ticker), INPUT_SELLER, INPUT_CLIENT, true)
    }

    private fun observingTicker() {
        somListViewModel.tickerListResult.observe(this, Observer {
            when (it) {
                is Success -> {
                    tickerList = it.data
                    renderInfoTicker()
                }
                is Fail -> {
                    ticker_info?.visibility = View.GONE
                }
            }
        })
    }

    private fun renderInfoTicker() {
        val listTickerData = arrayListOf<TickerData>()
        tickerList.forEach {
            if (it.isActive) {
                listTickerData.add(TickerData("", it.body, Ticker.TYPE_ANNOUNCEMENT, true))
            }
        }

        context?.let {
            val adapter = TickerPagerAdapter(it, listTickerData)
            ticker_info?.addPagerView(adapter, listTickerData)
        }
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
}