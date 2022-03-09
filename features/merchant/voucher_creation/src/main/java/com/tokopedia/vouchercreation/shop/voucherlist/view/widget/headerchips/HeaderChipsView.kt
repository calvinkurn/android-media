package com.tokopedia.vouchercreation.shop.voucherlist.view.widget.headerchips

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseFilterUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel.HeaderChip
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.BaseHeaderChipUiModel.ResetChip
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.SortUiModel
import com.tokopedia.vouchercreation.shop.voucherlist.view.adapter.HeaderChipsAdapter
import com.tokopedia.vouchercreation.shop.voucherlist.view.widget.sortbottomsheet.SortBy

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class HeaderChipsView : RecyclerView {

    private var onChipClick: (element: BaseHeaderChipUiModel) -> Unit = {}
    private val chipAdapter by lazy { HeaderChipsAdapter(onChipClick) }

    private val resetChip = ResetChip(false)
    private val sortChip by lazy { HeaderChip(context.getString(R.string.mvc_sort), ChipType.CHIP_SORT) }
    private val filterChip by lazy { HeaderChip(context.getString(R.string.mvc_voucher_type), ChipType.CHIP_FILTER) }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init(onClick: (element: BaseHeaderChipUiModel) -> Unit) {
        this.onChipClick = onClick

        layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        adapter = chipAdapter
        addItemDecoration(getItemDecoration())

        setupItems()
    }

    private fun setupItems() {
        chipAdapter.clearAllElements()
        chipAdapter.addElement(getItems())
    }

    private fun getItemDecoration(): ItemDecoration {
        return object : ItemDecoration() {

            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: State) {
                super.getItemOffsets(outRect, view, parent, state)

                val position = parent.getChildAdapterPosition(view)

                if (position == 0) {
                    val isVisible = chipAdapter.items[position].isVisible
                    if (isVisible) {
                        outRect.left = view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2)
                    } else {
                        outRect.left = view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
                    }
                }

                if (position == chipAdapter.itemCount.minus(1)) {
                    outRect.right = view.resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)
                }
            }
        }
    }

    private fun getItems(): List<BaseHeaderChipUiModel> = listOf(resetChip, sortChip, filterChip)

    private fun notifyItem(model: BaseHeaderChipUiModel) {
        val position = getItems().indexOf(model)
        if (position >= 0) {
            chipAdapter.notifyItemChanged(position)
        } else {
            chipAdapter.notifyDataSetChanged()
        }
    }

    fun showResetButton() {
        if (!resetChip.isVisible) {
            resetChip.isVisible = true
            notifyItem(resetChip)
        }
    }

    fun resetFilter() {
        resetChip.isVisible = false
        with(filterChip) {
            isActive = false
            text = context?.getString(R.string.mvc_voucher_type).toBlankOrString()
        }
        with(sortChip) {
            isActive = false
            text = context?.getString(R.string.mvc_sort).toBlankOrString()
        }
        chipAdapter.notifyDataSetChanged()
    }

    fun setActiveFilter(activeFilterList: List<BaseFilterUiModel.FilterItem>) {
        filterChip.run {
            val filterSize = activeFilterList.size
            val isShouldActive = filterSize > 0
            if (isShouldActive != isActive) {
                isActive = isShouldActive
            }
            text = when(filterSize) {
                0 -> context.getString(R.string.mvc_voucher_type)
                1 -> activeFilterList.first().label
                else -> String.format(context?.getString(R.string.mvc_chip_multiple_filter).toBlankOrString(), filterSize.toString())
            }
        }
        notifyItem(filterChip)
    }

    fun setActiveSort(activeSort: SortUiModel) {
        with(sortChip) {
            text = when(activeSort.key) {
                SortBy.OLDEST_DONE_DATE -> context?.getString(R.string.mvc_oldest).toBlankOrString()
                SortBy.NEWEST_DONE_DATE -> context?.getString(R.string.mvc_newest).toBlankOrString()
                else -> context?.getString(R.string.mvc_sort).toBlankOrString()
            }
            isActive = true
        }
        notifyItem(sortChip)
    }

    fun setOnItemClickListener(onClick: (element: BaseHeaderChipUiModel) -> Unit) {
        this.onChipClick = onClick
    }
}