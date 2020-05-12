package com.tokopedia.vouchercreation.voucherlist.view.widget.sortbottomsheet

import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.SortUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.SortAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_sort.view.*

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class SortBottomSheet(
        parent: ViewGroup
) : BottomSheetUnify() {

    private val sortAdapter by lazy { SortAdapter(this::onSortItemClick) }

    private var onApplyClick: (SortUiModel?) -> Unit = {}
    private var onCancelApplySort: (List<SortUiModel>) -> Unit = {}
    private var applySort = false
    private var tmpSortList = listOf<SortUiModel>()

    init {
        setTitle(parent.context.getString(R.string.mvc_sort))

        val childView = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_sort, parent, false)

        setupView(childView)
    }

    private fun setupView(view: View) = with(view) {
        rvMcvSort.layoutManager = LinearLayoutManager(view.context)
        rvMcvSort.adapter = sortAdapter
        rvMcvSort.addItemDecoration(getSortItemDecoration())

        btnMvcApplySort.btnMvcApplySort.setOnClickListener {
            applySort = true
            applySort()
        }

        setChild(view)
    }

    private fun getSortItemDecoration() = object : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.top = view.resources.getDimensionPixelSize(R.dimen.layout_lvl1)
            }
            if (position == sortAdapter.sortItems.size.minus(1)) {
                outRect.bottom = view.resources.getDimensionPixelSize(R.dimen.layout_lvl2)
            }
        }
    }

    private fun applySort() {
        val sort = sortAdapter.sortItems.firstOrNull { it.isSelected }
        onApplyClick(sort)
        dismissAllowingStateLoss()
    }

    private fun onSortItemClick(sort: SortUiModel) {
        if (sort.isSelected) return
        sortAdapter.sortItems.forEach { it.isSelected = false }
        sort.isSelected = true
        sortAdapter.notifyDataSetChanged()
    }

    private fun List<SortUiModel>.copy(): List<SortUiModel> {
        return this.map { it.copy() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!applySort) {
            onCancelApplySort(tmpSortList)
        }
    }

    fun setOnApplySortListener(callback: (sort: SortUiModel?) -> Unit): SortBottomSheet {
        onApplyClick = callback
        return this
    }

    fun setOnCancelApply(callback: (List<SortUiModel>) -> Unit): SortBottomSheet {
        onCancelApplySort = callback
        return this
    }

    fun show(fm: FragmentManager, sortItems: List<SortUiModel>) {
        applySort = false
        tmpSortList = sortItems.copy()

        sortAdapter.clearAllElements()
        sortAdapter.addElement(sortItems)
        show(fm, TAG)
    }

    companion object {
        val TAG: String = SortBottomSheet::class.java.simpleName

        fun getMvcSortItems(context: Context): MutableList<SortUiModel> {
            return mutableListOf(
                    SortUiModel(context.getString(R.string.mvc_newest_done_date), SortBy.NEWEST_DONE_DATE, true),
                    SortUiModel(context.getString(R.string.mvc_oldest_done_date), SortBy.NEWEST_DONE_DATE)
            )
        }
    }
}