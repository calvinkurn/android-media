package com.tokopedia.flight.filter.presentation.bottomsheets

import android.os.Bundle
import android.view.View
import com.tokopedia.common.travel.constant.TravelSortOption
import com.tokopedia.flight.R
import com.tokopedia.unifycomponents.BottomSheetUnify

/**
 * @author by jessica on 2020-02-17
 */

class FlightSortBottomSheet : BottomSheetUnify() {

    lateinit var listener: ActionListener
    var selectedSortOption = TravelSortOption.EARLIEST_DEPARTURE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTitle(DIALOG_TITLE)
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
        setLayoutMargin()
    }

    private fun setLayoutMargin() {
        val ll = view?.findViewById(com.tokopedia.unifycomponents.R.id.bottom_sheet_wrapper) as View
        ll.setPadding(0,0,0,0)

        val header = view?.findViewById(com.tokopedia.unifycomponents.R.id.bottom_sheet_header) as View
        val padding = resources.getDimension(com.tokopedia.unifycomponents.R.dimen.layout_lvl2).toInt()
        header.setPadding(padding, 0, padding, 0)
    }

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