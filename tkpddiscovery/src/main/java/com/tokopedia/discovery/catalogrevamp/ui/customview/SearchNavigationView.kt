package com.tokopedia.discovery.catalogrevamp.ui.customview

import android.content.Context
import androidx.constraintlayout.widget.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.productcard.R
import kotlinx.android.synthetic.main.search_navigation_custom_view.view.*

class SearchNavigationView : ConstraintLayout {
    private var searchNavClickListener: SearchNavClickListener? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(
            context: Context,
            attrs: AttributeSet?,
            defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        View.inflate(context, com.tokopedia.discovery.R.layout.search_navigation_custom_view, this)
        handleUI()
    }

    private fun handleUI() {
        sort.setOnClickListener {
            searchNavClickListener?.onSortButtonClicked()
        }
        filter.setOnClickListener {
            searchNavClickListener?.onFilterButtonClicked()
        }
    }

    fun onFilterSelected(showTick: Boolean) {
        if (showTick)
            icon_tick_filter.show()
        else
            icon_tick_filter.hide()
    }

    fun onSortSelected(showTick: Boolean) {
        if (showTick)
            icon_tick_sort.show()
        else
            icon_tick_sort.hide()
    }

    fun setSearchNavListener(searchNavClickListener: SearchNavClickListener?) {
        this.searchNavClickListener = searchNavClickListener
    }

    interface SearchNavClickListener {
        fun onSortButtonClicked() {}
        fun onFilterButtonClicked() {}
    }
}
