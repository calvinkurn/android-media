package com.tokopedia.tokopedianow.categoryfilter.presentation.view

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.tokopedianow.categoryfilter.presentation.adapter.CategoryFilterChipAdapter
import com.tokopedia.tokopedianow.categoryfilter.presentation.adapter.CategoryFilterChipAdapter.CategoryFilterChipAdapterTypeFactory
import com.tokopedia.tokopedianow.categoryfilter.presentation.decoration.CategoryChipFilterDecoration
import com.tokopedia.tokopedianow.categoryfilter.presentation.uimodel.CategoryFilterChip
import com.tokopedia.tokopedianow.categoryfilter.presentation.viewholder.CategoryFilterChipViewHolder

class CategoryFilterChipView: RecyclerView {

    private var onCLickFilterChip: ((CategoryFilterChip) -> Unit)? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        layoutManager = ChipsLayoutManager
            .newBuilder(context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()
        addItemDecoration(CategoryChipFilterDecoration())
    }

    fun setOnClickListener(onCLickFilterChip: ((CategoryFilterChip) -> Unit)?) {
        this.onCLickFilterChip = onCLickFilterChip
    }

    fun submitList(items: List<CategoryFilterChip>, ) {
        val typeFactory = CategoryFilterChipAdapterTypeFactory(onCLickFilterChip)
        adapter = CategoryFilterChipAdapter(typeFactory).apply {
            submitList(items)
        }
    }

    fun resetAllFilterChip() {
        for(i in 0..adapter?.itemCount.orZero()) {
            val viewHolder = findViewHolderForAdapterPosition(i)
            if(viewHolder is CategoryFilterChipViewHolder) {
                viewHolder.resetChip()
            }
        }
    }
}