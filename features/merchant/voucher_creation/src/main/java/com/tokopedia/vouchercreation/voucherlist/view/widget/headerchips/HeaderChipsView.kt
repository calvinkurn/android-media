package com.tokopedia.vouchercreation.voucherlist.view.widget.headerchips

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.voucherlist.model.HeaderChipUiModel
import com.tokopedia.vouchercreation.voucherlist.view.adapter.HeaderChipsAdapter

/**
 * Created By @ilhamsuaib on 20/04/20
 */

class HeaderChipsView : RecyclerView {

    private var onChipClick: (element: HeaderChipUiModel) -> Unit = {}
    private val chipAdapter by lazy { HeaderChipsAdapter(onChipClick) }

    private val resetChip by lazy { HeaderChipUiModel(type = ChipType.CHIP_RESET, isVisible = false) }
    private val sortChip by lazy { HeaderChipUiModel(context.getString(R.string.mvc_sort), ChipType.CHIP_SORT) }
    private val filterChip by lazy { HeaderChipUiModel(context.getString(R.string.mvc_voucher_type), ChipType.CHIP_FILTER) }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    fun init() {
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
                val isResetButton = chipAdapter.items[position].type == ChipType.CHIP_RESET

                if (position == 0) {
                    if (isResetButton) {
                        outRect.left = view.resources.getDimensionPixelSize(R.dimen.layout_lvl2)
                    } else {
                        outRect.left = view.resources.getDimensionPixelSize(R.dimen.layout_lvl0)
                    }
                }

                if (position == chipAdapter.itemCount.minus(1)) {
                    outRect.right = view.resources.getDimensionPixelSize(R.dimen.layout_lvl1)
                }
            }
        }
    }

    private fun getItems(): List<HeaderChipUiModel> = listOf(resetChip, sortChip, filterChip, sortChip, filterChip, sortChip, filterChip)

    private fun notifyItem(model: HeaderChipUiModel) {
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

    fun setOnItemClickListener(onClick: (element: HeaderChipUiModel) -> Unit) {
        this.onChipClick = onClick
    }
}