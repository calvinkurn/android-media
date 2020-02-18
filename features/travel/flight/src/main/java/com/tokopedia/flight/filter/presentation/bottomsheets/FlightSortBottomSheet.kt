package com.tokopedia.flight.filter.presentation.bottomsheets

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.R
import com.tokopedia.flight.filter.presentation.adapter.FlightSortAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.layout_flight_search_sort_bottom_sheet.*

/**
 * @author by jessica on 2020-02-17
 */

class FlightSortBottomSheet : BottomSheetUnify() {

    lateinit var listener: ActionListener
    var selectedSortOption = TravelSortOption.EARLIEST_DEPARTURE

    lateinit var sortAdapter: FlightSortAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(DIALOG_TITLE)
        isHideable = true
        showCloseIcon = false
        showKnob = true
        isDragable = true
        setCloseClickListener { this.dismissAllowingStateLoss() }

        val childView = View.inflate(context, R.layout.layout_flight_search_sort_bottom_sheet, null)
        setChild(childView)

        arguments?.run {
            this.getInt(ARG_SELECTED_SORT_OPTION)?.let {
                selectedSortOption = it
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
    }

    private fun initView() {
        if (!::sortAdapter.isInitialized) {
            sortAdapter = FlightSortAdapter(getSortItems())
            sortAdapter.selectedId = selectedSortOption
            val layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            flight_sort_recycler_view.layoutManager = layoutManager
            flight_sort_recycler_view.adapter = sortAdapter

            sortAdapter.onClickItemListener = object : FlightSortAdapter.OnClickItemListener {
                override fun onClickItemListener(selectedId: Int) {
                    sortAdapter.onClickItem(selectedId)
                    selectedSortOption = selectedId
                    listener.onSortOptionClicked(selectedId)
                }
            }
        }
    }

    private fun getSortItems(): List<Pair<Int, String>> {
        return listOf(Pair(TravelSortOption.MOST_EXPENSIVE, getString(R.string.flight_search_sort_item_most_expensive_price)),
                Pair(TravelSortOption.EARLIEST_DEPARTURE, getString(R.string.flight_search_sort_item_earliest_departure)),
                Pair(TravelSortOption.LATEST_DEPARTURE, getString(R.string.flight_search_sort_item_latest_departure)),
                Pair(TravelSortOption.SHORTEST_DURATION, getString(R.string.flight_search_sort_item_shortest_duration)),
                Pair(TravelSortOption.LONGEST_DURATION, getString(R.string.flight_search_sort_item_longest_duration)),
                Pair(TravelSortOption.EARLIEST_ARRIVAL, getString(R.string.flight_search_sort_item_earliest_arrival)),
                Pair(TravelSortOption.LATEST_ARRIVAL, getString(R.string.flight_search_sort_item_latest_arrival))) }

    interface ActionListener {
        fun onSortOptionClicked(selectedSortOption: Int)
    }

    companion object {
        private const val ARG_SELECTED_SORT_OPTION = "arg_selected_sort_option"
        private const val DIALOG_TITLE = "Urutkan berdasarkan"

        fun newInstance(selectedSortOption: Int): FlightSortBottomSheet =
                FlightSortBottomSheet().also {
                    it.arguments = Bundle().apply {
                        putInt(ARG_SELECTED_SORT_OPTION, selectedSortOption)
                    }
                }
    }
}