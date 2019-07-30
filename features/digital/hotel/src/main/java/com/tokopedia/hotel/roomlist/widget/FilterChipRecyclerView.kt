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

class FilterChipRecyclerView : BaseCustomView, ChipAdapter.ResetChipListener {

    lateinit var listener: ChipAdapter.OnClickListener
    lateinit var adapter: ChipAdapter

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        init()
    }

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
            adapter = ChipAdapter(strings, listener, this, selectedTextColor)
            chip_recycler_view.adapter = adapter
        }
    }

    override fun onResetChip() {
        for (i in 0 until adapter.itemCount) {
            with(chip_recycler_view.findViewHolderForAdapterPosition(i) as ChipAdapter.ViewHolder?) {
                this?.chips?.isSelected = false
                this?.setTextColor(com.tokopedia.design.R.color.black_56)
            }
        }
    }

    fun selectOnlyOneChip(boolean: Boolean) {
        adapter?.selectOnlyOneChip = boolean
    }

    fun initiallySelectedChip(position: Int) {
        if (position < adapter.itemCount) {
            with(chip_recycler_view.findViewHolderForAdapterPosition(position) as ChipAdapter.ViewHolder?) {
                this?.chips?.isSelected = true
            }
        }
    }
}
