package com.tokopedia.hotel.search.presentation.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Sort
import com.tokopedia.hotel.search.presentation.adapter.HotelOptionMenuAdapter
import com.tokopedia.hotel.search.presentation.adapter.HotelOptionMenuAdapter.Companion.MODE_NORMAL
import com.tokopedia.unifycomponents.BottomSheetUnify

class HotelClosedSortBottomSheets : BottomSheetUnify() {
    private var sheetTitle: String = ""
    private var mode: Int = MODE_NORMAL
    private var menu: List<Sort> = listOf()
    var selectedItem: Sort? = null
    var onMenuSelect: HotelOptionMenuAdapter.OnSortMenuSelected? = null

    fun setSheetTitle(_sheetTitle: String): HotelClosedSortBottomSheets = this.apply { sheetTitle = _sheetTitle }
    fun setMode(_mode: Int): HotelClosedSortBottomSheets = this.apply { mode = _mode }
    fun setMenu(_menu: List<Sort>): HotelClosedSortBottomSheets = this.apply { menu = _menu }
    fun setSelectedItem(_sort: Sort): HotelClosedSortBottomSheets = this.apply { selectedItem = _sort }

    init {
        setTitle(sheetTitle)
        isFullpage = false
        isDragable = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initChildLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initChildLayout() {
        val view = View.inflate(context, R.layout.bottom_sheets_hotel_closed_sort, null)
        setChild(view)
        initView(view)
    }

    private fun initView(view: View) {
        val recyclerView = view.findViewById<VerticalRecyclerView>(R.id.hotel_closed_sort_recycler_view)
        recyclerView.clearItemDecoration()
        val adapter = HotelOptionMenuAdapter(mode, menu)
                .apply {
                    listener = onMenuSelect
                    selectedSort = selectedItem
                }
        recyclerView?.adapter = adapter
    }
}