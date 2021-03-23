package com.tokopedia.recommendation_widget_common.widget.comparison.stickytitle

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.widget.comparison.ComparisonWidgetViewInterface
import kotlinx.android.synthetic.main.view_sticky_title.view.*

class StickyTitleView: FrameLayout{

    private var disableScrollTemp: Boolean = false
    private var comparisonWidgetInterface: ComparisonWidgetViewInterface? = null
    private var adapter: StickyTitleAdapter? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sticky_title, this)
        if (sticky_title_rv.itemDecorationCount == 0) sticky_title_rv.addItemDecoration(StickyTitleDecoration())
    }

    fun setStickyModelListData(
            stickyTitleModelList: StickyTitleModelList,
            stickyTitleInterface: StickyTitleInterface,
            comparisonWidgetViewInterface: ComparisonWidgetViewInterface
    ) {
        sticky_title_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = StickyTitleAdapter(stickyTitleModelList, stickyTitleInterface)
        sticky_title_rv.adapter = adapter
        adapter?.notifyDataSetChanged()
        this.comparisonWidgetInterface = comparisonWidgetViewInterface
        sticky_title_rv.clearOnScrollListeners()
        sticky_title_rv.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                this@StickyTitleView.disableScrollTemp = true
                this@StickyTitleView.comparisonWidgetInterface?.scrollX(dx)
                this@StickyTitleView.disableScrollTemp = false
            }
        })
    }

    fun scrollX(x: Int) {
        if (!disableScrollTemp) sticky_title_rv.scrollBy(x, 0)
    }

    fun hideStickyTitle() {
        sticky_title_rv.visibility = View.INVISIBLE
    }

    fun showStickyTitle() {
        sticky_title_rv.visibility = View.VISIBLE
    }
}

interface StickyTitleInterface {
    fun onStickyTitleClick(stickyTitleModel: StickyTitleModel)
}