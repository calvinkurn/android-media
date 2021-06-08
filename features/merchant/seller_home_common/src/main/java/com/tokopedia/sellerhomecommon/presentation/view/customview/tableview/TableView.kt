package com.tokopedia.sellerhomecommon.presentation.view.customview.tableview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.adapter.TablePageAdapter
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import kotlinx.android.synthetic.main.shc_table_view.view.*

/**
 * Created By @ilhamsuaib on 10/06/20
 */

class TableView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var slideImpressionListener: ((position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit)? = null
    private var htmlClickListener: ((url: String, isEmpty: Boolean) -> Unit)? = null
    private val mTablePageAdapter by lazy { TablePageAdapter() }
    private var alreadyAttachToSnapHelper = false

    init {
        View.inflate(context, R.layout.shc_table_view, this)
    }

    fun showTable(items: List<TablePageUiModel>) {
        tableViewPageControl.visibility = if (items.size > 1) View.VISIBLE else View.GONE
        tableViewPageControl.setIndicator(items.size)

        val mLayoutManager = object : LinearLayoutManager(context, HORIZONTAL, false) {
            override fun canScrollVertically(): Boolean = false
        }

        rvTableViewPage.run {
            layoutManager = mLayoutManager
            adapter = mTablePageAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val position = mLayoutManager.findFirstCompletelyVisibleItemPosition()
                    if (position != RecyclerView.NO_POSITION) {
                        mLayoutManager.findViewByPosition(position)?.let { view ->
                            refreshTableHeight(view)
                        }
                        this@TableView.tableViewPageControl.setCurrentIndicator(position)
                    }
                }
            })

            if (!alreadyAttachToSnapHelper) {
                PagerSnapHelper().attachToRecyclerView(this)
                alreadyAttachToSnapHelper = true
            }
        }

        mTablePageAdapter.setItems(items)
        slideImpressionListener?.let { onView ->
            mTablePageAdapter.addOnImpressionListener(onView)
        }
        htmlClickListener?.let { onHtmlClick ->
            mTablePageAdapter.addOnClickHtmlListener(onHtmlClick)
        }
    }

    fun addOnSlideImpressionListener(onView: (position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit) {
         this.slideImpressionListener = onView
    }

    fun addOnHtmlClickListener(onClick: (url: String, isEmpty: Boolean) -> Unit) {
        this.htmlClickListener = onClick
    }

    /**
     * Dynamically set recyclerview height according to view's measured height
     */
    private fun refreshTableHeight(view: View) {
        val wMeasureSpec = MeasureSpec.makeMeasureSpec(view.width, MeasureSpec.EXACTLY)
        val hMeasureSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        view.measure(wMeasureSpec, hMeasureSpec)

        if (rvTableViewPage?.layoutParams?.height != view.measuredHeight) {
            rvTableViewPage?.layoutParams = (rvTableViewPage?.layoutParams as? LayoutParams)
                    ?.also { lp -> lp.height = view.measuredHeight }
        }
    }
}