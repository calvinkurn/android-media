package com.tokopedia.sellerhomecommon.presentation.view.customview.tableview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.orTrue
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.sellerhomecommon.databinding.ShcTableViewBinding
import com.tokopedia.sellerhomecommon.presentation.adapter.TablePageAdapter
import com.tokopedia.sellerhomecommon.presentation.model.TablePageUiModel
import com.tokopedia.sellerhomecommon.presentation.model.TableRowsUiModel

/**
 * Created By @ilhamsuaib on 10/06/20
 */

class TableView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {

    private var onSwipeListener: ((position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit)? =
        null
    private var slideImpressionListener: ((position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit)? =
        null
    private var htmlClickListener: ((url: String, text: String, meta: TableRowsUiModel.Meta, isEmpty: Boolean) -> Unit)? = null
    private val mTablePageAdapter by lazy { TablePageAdapter() }
    private var isPageIndicatorEnabled: Boolean = true
    private var alreadyAttachToSnapHelper = false
    private var highestHeight = Int.ZERO

    private val binding by lazy {
        ShcTableViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun showTable(items: List<TablePageUiModel>) {
        binding.run {
            val shouldShowPageControl = items.size > Int.ONE && isPageIndicatorEnabled
            tableViewPageControl.isVisible = shouldShowPageControl
            if (isPageIndicatorEnabled) {
                tableViewPageControl.setIndicator(items.size)
            }

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
                        if (position != RecyclerView.NO_POSITION && items.size > Int.ONE) {
                            mLayoutManager.findViewByPosition(position)?.let { view ->
                                refreshTableHeight(view)
                            }
                            if (isPageIndicatorEnabled) {
                                binding.tableViewPageControl.setCurrentIndicator(position)
                            }
                            val item = items.getOrNull(position)
                            val isEmpty = item?.rows?.isEmpty().orTrue()
                            onSwipeListener?.invoke(position, items.size, isEmpty)
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
                mTablePageAdapter.setOnImpressionListener(onView)
            }
            htmlClickListener?.let { onHtmlClick ->
                mTablePageAdapter.addOnClickHtmlListener(onHtmlClick)
            }
        }
    }

    fun addOnSlideImpressionListener(onView: (position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit) {
        this.slideImpressionListener = onView
    }

    fun setOnSwipeListener(onSwipe: (position: Int, maxPosition: Int, isEmpty: Boolean) -> Unit) {
        this.onSwipeListener = onSwipe
    }

    fun addOnHtmlClickListener(onClick: (url: String, text: String, meta: TableRowsUiModel.Meta, isEmpty: Boolean) -> Unit) {
        this.htmlClickListener = onClick
    }

    fun setPageIndicatorEnabled(isEnabled: Boolean) {
        this.isPageIndicatorEnabled = isEnabled
        binding.tableViewPageControl.isVisible = isEnabled
    }

    fun resetHeight() {
        binding.run {
            highestHeight = Int.ZERO
            rvTableViewPage.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            rvTableViewPage.requestLayout()
        }
    }

    /**
     * Dynamically set recyclerview height according to view's measured height
     */
    private fun refreshTableHeight(view: View) {
        binding.run {
            val wMeasureSpec = MeasureSpec.makeMeasureSpec(view.width, MeasureSpec.EXACTLY)
            val hMeasureSpec = MeasureSpec.makeMeasureSpec(Int.ZERO, MeasureSpec.UNSPECIFIED)
            view.measure(wMeasureSpec, hMeasureSpec)

            if (rvTableViewPage.layoutParams?.height != view.measuredHeight) {
                rvTableViewPage.layoutParams = (rvTableViewPage.layoutParams as? LayoutParams)
                    ?.also { lp ->
                        if (view.measuredHeight > highestHeight) {
                            highestHeight = view.measuredHeight
                            lp.height = view.measuredHeight
                        }
                    }
            }
        }
    }
}
