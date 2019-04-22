package com.tokopedia.hotel.roomlist.widget

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.hotel.R
import kotlinx.android.synthetic.main.widget_filter_chip_recycler_view.view.*

/**
 * @author by jessica on 18/04/19
 */

class FilterChipRecyclerView: BaseCustomView {

    lateinit var listener: ChipAdapter.OnClickListener

    constructor(context: Context) : super(context) { init() }
    constructor(context: Context, attributeSet: AttributeSet): super(context, attributeSet) { init() }
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int): super(context, attributeSet, defStyleAttr) { init() }

    fun init() {
        View.inflate(context, R.layout.widget_filter_chip_recycler_view, this)
        buildView()
    }

    fun buildView() {
        visibility = View.VISIBLE
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        chip_recycler_view.layoutManager = layoutManager
    }

    fun setItem(strings: ArrayList<String>, selectedTextColor: Int = 0) {
        if (listener != null) {
            chip_recycler_view.adapter = ChipAdapter(strings, listener, selectedTextColor)
        }
    }
}
