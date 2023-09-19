package com.tokopedia.common.travel.widget.filterchips

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.common.travel.databinding.WidgetFilterChipRecyclerViewBinding
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

/**
 * @author by jessica on 18/04/19
 */

class FilterChipRecyclerView : BaseCustomView, FilterChipAdapter.ResetChipListener {

    lateinit var listener: FilterChipAdapter.OnClickListener
    lateinit var adapter: FilterChipAdapter
    lateinit var layoutManager: LinearLayoutManager

    private val binding = WidgetFilterChipRecyclerViewBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context) {
        buildView()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        buildView()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(context, attributeSet, defStyleAttr) {
        buildView()
    }

    fun buildView() {
        visibility = View.VISIBLE
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.chipRecyclerView.layoutManager = layoutManager
    }

    fun setItem(strings: ArrayList<String>, selectedTextColor: Int = unifyprinciplesR.color.Unify_GN300, initialSelectedItemPos: Int? = null) {
        if (listener != null) {
            adapter = FilterChipAdapter(strings, listener, this, selectedTextColor)
            if (initialSelectedItemPos != null) adapter.initialPositionSelected = initialSelectedItemPos
            binding.chipRecyclerView.adapter = adapter
        }
    }

    fun getFirstSelectedItem(): String {
        for (i in 0 until adapter.itemCount) {
            with(binding.chipRecyclerView.findViewHolderForAdapterPosition(i) as FilterChipAdapter.ViewHolder?) {
                if (this?.chips?.isSelected == true) return this.chips.chipText.toString()
            }
        }
        return ""
    }

    override fun onResetChip() {
        for (i in 0 until adapter.itemCount) {
            with(binding.chipRecyclerView.findViewHolderForAdapterPosition(i) as FilterChipAdapter.ViewHolder?) {
                this?.chips?.isSelected = false
                this?.chips?.chipType = ChipsUnify.TYPE_NORMAL
            }
        }
    }

    fun selectOnlyOneChip(boolean: Boolean) {
        // call this only after setItem
        adapter.selectOnlyOneChip = boolean
    }

    fun canDiselectAfterSelect(boolean: Boolean) {
        adapter.canDiselectAfterSelect = boolean
    }

    fun selectChipByPosition(position: Int) {
        if (position < adapter.itemCount) {
            with(binding.chipRecyclerView.findViewHolderForAdapterPosition(position) as FilterChipAdapter.ViewHolder?) {
                this?.selectChip()
            }
        }
    }
}
