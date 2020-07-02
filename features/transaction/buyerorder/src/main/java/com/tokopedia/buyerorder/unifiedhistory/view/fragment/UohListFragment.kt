package com.tokopedia.buyerorder.unifiedhistory.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.RefreshHandler
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.buyerorder.R
import com.tokopedia.buyerorder.unifiedhistory.view.adapter.UohListItemAdapter
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.ticker.*
import kotlinx.android.synthetic.main.fragment_uoh_list.*


/**
 * Created by fwidjaja on 29/06/20.
 */
class UohListFragment: BaseDaggerFragment() {
    private lateinit var uohListItemAdapter: UohListItemAdapter
    private var refreshHandler: RefreshHandler? = null

    companion object {
        @JvmStatic
        fun newInstance(): UohListFragment {
            return UohListFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_uoh_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        renderChipsFilter()
        renderTicker()
        prepareLayout()
    }

    private fun prepareLayout() {
        // refreshHandler = RefreshHandler(swipe_refresh_layout, this)
        // refreshHandler?.setPullEnabled(true)
        uohListItemAdapter = UohListItemAdapter()
        // .setActionListener(this)

        rv_order_list?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = uohListItemAdapter
        }
    }

    private fun renderChipsFilter() {
        val chips = arrayListOf<SortFilterItem>()

        val filter1 = SortFilterItem("Semua Tanggal", ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_MEDIUM)
        filter1.listener = {
            filter1.type = if(filter1.type == ChipsUnify.TYPE_NORMAL) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            println("++ SHOW BOTTOMSHEET 1!")
        }
        chips.add(filter1)

        val filter2 = SortFilterItem("Semua Status", ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_MEDIUM)
        filter2.listener = {
            filter2.type = if(filter2.type == ChipsUnify.TYPE_NORMAL) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            println("++ SHOW BOTTOMSHEET 2!")
        }
        chips.add(filter2)

        val filter3 = SortFilterItem("Semua Kategori", ChipsUnify.TYPE_NORMAL, ChipsUnify.SIZE_MEDIUM)
        filter3.listener = {
            filter3.type = if(filter3.type == ChipsUnify.TYPE_NORMAL) {
                ChipsUnify.TYPE_SELECTED
            } else {
                ChipsUnify.TYPE_NORMAL
            }
            println("++ SHOW BOTTOMSHEET 3!")
        }
        chips.add(filter3)

        uoh_sort_filter?.addItem(chips)
        uoh_sort_filter?.sortFilterPrefix?.setOnClickListener {
            uoh_sort_filter?.resetAllFilters()
        }
        filter1.refChipUnify.setChevronClickListener {  }
        filter2.refChipUnify.setChevronClickListener {  }
        filter3.refChipUnify.setChevronClickListener {  }
    }

    private fun renderTicker() {
        val listTickerData = arrayListOf<TickerData>()
        listTickerData.add(TickerData("Ini Testing Feature Flag 1", "This content can consists of Html Tags", Ticker.TYPE_ANNOUNCEMENT, true))
        listTickerData.add(TickerData("Ini Testing Feature Flag 2", "This content can consists of Html Tags", Ticker.TYPE_ANNOUNCEMENT, true))
        context?.let {
            val adapter = TickerPagerAdapter(it, listTickerData)
            adapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    RouteManager.route(context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, linkUrl))
                }
            })
            ticker_info?.setDescriptionClickEvent(object: TickerCallback {
                override fun onDescriptionViewClick(linkUrl: CharSequence) {}

                override fun onDismiss() {
                }

            })
            ticker_info?.addPagerView(adapter, listTickerData)
        }
    }

    private fun renderOrderList() {
        empty_state_order_list?.visibility = View.GONE
        rv_order_list?.visibility = View.VISIBLE
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {}
}