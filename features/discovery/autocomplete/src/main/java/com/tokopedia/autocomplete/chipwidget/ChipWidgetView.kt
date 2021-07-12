package com.tokopedia.autocomplete.chipwidget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.util.addItemDecorationIfNotExists
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifycomponents.toDp
import kotlinx.android.synthetic.main.autocomplete_chip_widget_view.view.*
import kotlinx.android.synthetic.main.autocomplete_chip_widget_view.view.autocompleteChipWidgetRecyclerView
import kotlinx.android.synthetic.main.layout_autocomplete_chip_widget.view.*

class ChipWidgetView: BaseCustomView {

    private var adapter: AutocompleteChipWidgetAdapter? = null
    private val spacingItemDecoration = ChipSpacingItemDecoration(
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8),
            context.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    )

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

        autocompleteChipWidgetRecyclerView?.let{
            it.layoutManager = createLayoutManager()
            it.isNestedScrollingEnabled = false
            it.adapter = adapter
            it.addItemDecorationIfNotExists(spacingItemDecoration)
        }
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