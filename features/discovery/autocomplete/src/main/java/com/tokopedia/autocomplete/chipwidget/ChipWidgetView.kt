package com.tokopedia.autocomplete.chipwidget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.autocomplete.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.autocomplete_chip_widget_view.view.*
import kotlinx.android.synthetic.main.autocomplete_chip_widget_view.view.autocompleteChipWidgetRecyclerView
import kotlinx.android.synthetic.main.layout_autocomplete_chip_widget.view.*

class ChipWidgetView: BaseCustomView {

    private var adapter: AutocompleteChipWidgetAdapter? = null

    constructor(context: Context): super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, R.layout.autocomplete_chip_widget_view, this)
        addDefaultItemDecorator()
    }

    private fun addDefaultItemDecorator() {
        if (autocompleteChipWidgetRecyclerView.itemDecorationCount > 0) autocompleteChipWidgetRecyclerView.removeItemDecorationAt(0)

        autocompleteChipWidgetRecyclerView.addItemDecoration(createItemDecoration())
    }

    private fun createItemDecoration() = object: RecyclerView.ItemDecoration() {
        val spacing = 8.toDp()
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            outRect.right = spacing
            outRect.bottom = spacing
        }
    }

    fun bindChipWidgetView(
            data: List<AutocompleteChipDataView>,
            listener: AutocompleteChipWidgetViewListener
    ) {
        initRecyclerView(listener)
        submitList(data)
    }

    private fun initRecyclerView(listener: AutocompleteChipWidgetViewListener) {
        adapter = AutocompleteChipWidgetAdapter(listener)

        autocompleteChipWidgetRecyclerView?.layoutManager = createLayoutManager()
        autocompleteChipWidgetRecyclerView?.adapter = adapter
    }

    private fun submitList(data: List<AutocompleteChipDataView>) {
        adapter?.setData(data)
    }

    private fun createLayoutManager(): RecyclerView.LayoutManager {
        return ChipsLayoutManager.newBuilder(context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
    }
}