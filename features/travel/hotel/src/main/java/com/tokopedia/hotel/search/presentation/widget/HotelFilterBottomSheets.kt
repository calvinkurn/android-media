package com.tokopedia.hotel.search.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.FilterV2
import com.tokopedia.hotel.search.data.model.params.ParamFilterV2
import com.tokopedia.hotel.search.presentation.adapter.HotelSearchResultFilterV2Adapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton

/**
 * @author by jessica on 12/08/20
 */

class HotelFilterBottomSheets : BottomSheetUnify() {

    var filters: List<FilterV2> = listOf()

    var selectedFilters: MutableList<ParamFilterV2> = mutableListOf()

    lateinit var listener: SubmitFilterListener

    var adapter = HotelSearchResultFilterV2Adapter()

    fun setFilter(filter: List<FilterV2>): HotelFilterBottomSheets = this.apply { filters = filter }
    fun setSelected(paramFilterV2: List<ParamFilterV2>): HotelFilterBottomSheets = this.apply { selectedFilters = paramFilterV2.toMutableList() }
    fun setSubmitFilterListener(listener: SubmitFilterListener): HotelFilterBottomSheets = this.apply { this.listener = listener }

    init {
        setTitle("Filter")
        isFullpage = true
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheets_hotel_filters, null)
        setChild(view)
        initView(view)
    }

    override fun show(manager: FragmentManager, tag: String?) {
        super.show(manager, tag)
        combineFilterAndSelectedFilter()
    }

    private fun initView(view: View) {
        val recyclerView = view.findViewById<VerticalRecyclerView>(R.id.rv_hotel_filter)
        recyclerView.clearItemDecoration()

        adapter.filters = filters
        filters.forEach {
            adapter.onSelectedFilterChanged(it.name, it.optionSelected.toMutableList())
        }

        adapter.isSelectionWithOverflowLayout = true
        recyclerView.adapter = adapter

        setAction("Reset") {
            filters.forEach { it.optionSelected = listOf() }
            adapter.filters = filters
            adapter.selectedFilter = hashMapOf()
            adapter.notifyDataSetChanged()
        }

        val submitButton = view.findViewById<UnifyButton>(R.id.hotel_filter_submit_button)
        submitButton.setOnClickListener {
            selectedFilters = adapter.paramFilterV2.toMutableList()
            listener.onSubmitFilter(selectedFilters)
            this.dismiss()
        }
    }

    private fun combineFilterAndSelectedFilter() {
        filters.forEach { it.optionSelected = listOf() }
        selectedFilters.forEach { selectedFilters ->
            for (filter in filters) {
                if (selectedFilters.name == filter.name) {
                    filter.optionSelected = selectedFilters.values
                    break
                }
            }
        }
    }
}