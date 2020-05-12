package com.tokopedia.vouchercreation.voucherlist.view.widget.headerchips

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseHeaderChipUiModel
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseHeaderChipUiModel.HeaderChip
import com.tokopedia.vouchercreation.voucherlist.model.ui.BaseHeaderChipUiModel.ResetChip
import com.tokopedia.vouchercreation.voucherlist.view.adapter.HeaderChipsAdapter

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
                        outRect.left = view.resources.getDimensionPixelSize(R.dimen.layout_lvl2)
                    } else {
                        outRect.left = view.resources.getDimensionPixelSize(R.dimen.layout_lvl1)
                    }
                }

                if (position == chipAdapter.itemCount.minus(1)) {
                    outRect.right = view.resources.getDimensionPixelSize(R.dimen.layout_lvl1)
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
        resetChip.isVisible = true
        notifyItem(resetChip)
    }

    fun hideResetButton() {
        resetChip.isVisible = false
        notifyItem(resetChip)
    }

    fun setOnItemClickListener(onClick: (element: BaseHeaderChipUiModel) -> Unit) {
        this.onChipClick = onClick
    }
}