package com.tokopedia.hotel.search.presentation.widget

import android.app.Dialog
import android.graphics.Typeface
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import com.tokopedia.design.component.BottomSheets
import com.tokopedia.hotel.R
import com.tokopedia.hotel.search.data.model.Sort
import com.tokopedia.hotel.search.presentation.adapter.HotelOptionMenuAdapter
import com.tokopedia.hotel.search.presentation.adapter.HotelOptionMenuAdapter.Companion.MODE_NORMAL

class HotelClosedSortBottomSheets: BottomSheets() {
    private var title: String = ""
    private var mode: Int = MODE_NORMAL
    private var menu: List<Sort> = listOf()
    var selectedItem: Sort? = null
    var onMenuSelect: HotelOptionMenuAdapter.OnSortMenuSelected? = null

    fun setTitle(titleText: String): HotelClosedSortBottomSheets = this.apply { title = titleText }
    fun setMode(_mode: Int): HotelClosedSortBottomSheets = this.apply { mode = _mode }
    fun setMenu(_menu: List<Sort>): HotelClosedSortBottomSheets = this.apply { menu = _menu }
    fun setSelecetedItem(_sort: Sort): HotelClosedSortBottomSheets = this.apply { selectedItem = _sort }

    override fun getLayoutResourceId(): Int = R.layout.fragment_base_list

    override fun initView(view: View?) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycler_view)
        val adapter = HotelOptionMenuAdapter(mode, menu)
                .apply {
                    listener = onMenuSelect
                    selectedSort = selectedItem
                }
        recyclerView?.adapter = adapter
    }

    override fun title(): String = title

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        val btnClose = getDialog().findViewById<AppCompatImageView>(com.tokopedia.design.R.id.btn_close)
        btnClose.setOnClickListener { dismiss() }

        val title = getDialog().findViewById<TextView>(R.id.tv_title)
        title.typeface = Typeface.DEFAULT_BOLD
    }
}