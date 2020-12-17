package com.tokopedia.vouchercreation.voucherlist.view.widget.filterbottomsheet

import android.content.Context
import android.content.DialogInterface
import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseFilterUiModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseFilterUiModel.*
import com.tokopedia.vouchercreation.voucherlist.view.adapter.FilterAdapter
import kotlinx.android.synthetic.main.bottomsheet_mvc_filter.view.*

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class FilterBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(): FilterBottomSheet = FilterBottomSheet().apply {
            setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        }

        const val TAG: String = "FilterBottomSheet"

        fun getMvcFilterItems(context: Context): MutableList<BaseFilterUiModel> {
            return mutableListOf(
                    FilterGroup(context.getString(R.string.mvc_type_of_voucher)),
                    FilterItem(context.getString(R.string.mvc_cashback), FilterBy.CASHBACK),
                    FilterItem(context.getString(R.string.mvc_free_shipping), FilterBy.FREE_SHIPPING),
                    FilterDivider,
                    FilterGroup(context.getString(R.string.mvc_voucher_target)),
                    FilterItem(context.getString(R.string.mvc_public), FilterBy.PUBLIC),
                    FilterItem(context.getString(R.string.mvc_special), FilterBy.SPECIAL)
            )
        }
    }

    private var applyFilter = false
    private var onItemClick: (String) -> Unit = {}
    private var onApplyClick: () -> Unit = {}
    private var onCancelApply: (items: List<BaseFilterUiModel>) -> Unit = {}
    private val filterAdapter by lazy { FilterAdapter(this::onItemFilterClick) }
    private var tmpFilterList = emptyList<BaseFilterUiModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        if (!applyFilter) {
            onCancelApply(tmpFilterList)
        }
    }

    private fun initBottomSheet() {
        context?.run {
            setTitle(getString(R.string.mvc_filter))

            val childView = View.inflate(this, R.layout.bottomsheet_mvc_filter, null)
            setChild(childView)
        }

    }

    private fun setupView(view: View) = with(view) {
        rvMcvFilter.layoutManager = LinearLayoutManager(context)
        rvMcvFilter.adapter = filterAdapter
        rvMcvFilter.addItemDecoration(getFilterItemDecoration())

        btnMvcApplyFilter.setOnClickListener {
            applyFilter = true
            onApplyClick()
            dismissAllowingStateLoss()
        }
    }

    private fun getFilterItemDecoration() = object : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            super.getItemOffsets(outRect, view, parent, state)
            val position = parent.getChildAdapterPosition(view)
            if (position == 0) {
                outRect.top = view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
            }
            if (position == filterAdapter.items.size.minus(1)) {
                outRect.bottom = view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
            }
        }
    }

    private fun onItemFilterClick(key: String) {
        onItemClick(key)
        val filterItems = filterAdapter.items.filterIsInstance<FilterItem>()
        val canReset = filterItems.any { it.isSelected }

        setupResetButton(canReset)
    }

    private fun setupResetButton(canReset: Boolean) {
        if (canReset) {
            setAction(context?.getString(R.string.mvc_reset).toBlankOrString()) {
                resetFilter()
            }
        } else {
            clearAction()
        }
    }

    private fun resetFilter() {
        filterAdapter.items.filterIsInstance<FilterItem>().forEach { it.isSelected = false }
        view?.rvMcvFilter?.post {
            filterAdapter.notifyDataSetChanged()
        }
        clearAction()
    }

    fun setOnItemClickListener(action: (String) -> Unit): FilterBottomSheet {
        onItemClick = action
        return this
    }

    fun setOnApplyClickListener(action: () -> Unit): FilterBottomSheet {
        onApplyClick = action
        return this
    }

    fun setCancelApplyFilter(callback: (items: List<BaseFilterUiModel>) -> Unit): FilterBottomSheet {
        this.onCancelApply = callback
        return this
    }

    private fun List<BaseFilterUiModel>.copy(): List<BaseFilterUiModel> {
        return this.map {
            return@map if (it is FilterItem) it.copy() else it
        }
    }

    fun show(fm: FragmentManager, items: List<BaseFilterUiModel>) {
        applyFilter = false
        tmpFilterList = items.copy()

        filterAdapter.clearAllElements()
        filterAdapter.addElement(items)

        show(fm, TAG)
    }
}