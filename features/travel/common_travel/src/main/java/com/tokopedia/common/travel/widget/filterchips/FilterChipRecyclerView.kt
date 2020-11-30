package com.tokopedia.common.travel.widget.filterchips

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.View
import com.tokopedia.common.travel.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import kotlinx.android.synthetic.main.widget_filter_chip_recycler_view.view.*

/**
 * @author by jessica on 18/04/19
 */

class FilterChipRecyclerView : BaseCustomView, FilterChipAdapter.ResetChipListener {

    lateinit var listener: FilterChipAdapter.OnClickListener
    lateinit var adapter: FilterChipAdapter
    lateinit var layoutManager: LinearLayoutManager

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
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        chip_recycler_view.layoutManager = layoutManager
    }

    fun setItem(strings: ArrayList<String>, selectedTextColor: Int = com.tokopedia.unifyprinciples.R.color.Unify_G300, initialSelectedItemPos: Int? = null) {
        if (listener != null) {
            adapter = FilterChipAdapter(strings, listener, this, selectedTextColor)
            if (initialSelectedItemPos != null) adapter.initialPositionSelected = initialSelectedItemPos
            chip_recycler_view.adapter = adapter
        }
    }

    fun getFirstSelectedItem(): String {
        for (i in 0 until adapter.itemCount) {
            with(chip_recycler_view.findViewHolderForAdapterPosition(i) as FilterChipAdapter.ViewHolder?) {
                if (this?.chips?.isSelected == true) return this.chips.chipText.toString()
            }
        }
        return ""
    }

    override fun onResetChip() {
        for (i in 0 until adapter.itemCount) {
            with(chip_recycler_view.findViewHolderForAdapterPosition(i) as FilterChipAdapter.ViewHolder?) {
                this?.chips?.isSelected = false
                this?.chips?.chipType = ChipsUnify.TYPE_NORMAL
            }
        }
    }

    fun selectOnlyOneChip(boolean: Boolean) {
        //call this only after setItem
        adapter.selectOnlyOneChip = boolean
    }

    fun canDiselectAfterSelect(boolean: Boolean) {
        adapter.canDiselectAfterSelect = boolean
    }

    fun selectChipByPosition(position: Int) {
        if (position < adapter.itemCount) {
            with(chip_recycler_view.findViewHolderForAdapterPosition(position) as FilterChipAdapter.ViewHolder?) {
                this?.selectChip()
            }
        }
    }
}
