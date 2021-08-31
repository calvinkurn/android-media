package com.tokopedia.common.topupbills.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.tokopedia.common.topupbills.R
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sortfilter.SortFilterItem
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.toPx

/** Custom SortFilter.
 * This SortFilter use RELATIONSHIP_OR logic from the Unify's SortFilter and has specific usecase,
 * so currently it only can be used by TopupBills. Here we exclude the last index "Lihat Semua"
 * from all state changes. "Lihat Semua" have to be implemented manually by the calling class.
 * */

class TopupBillsSortFilter: LinearLayout {

    var sortFilterItems: LinearLayout
    var sortFilterHorizontalScrollView: HorizontalScrollView

    var chipItems: ArrayList<SortFilterItem>? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        View.inflate(context, R.layout.view_widget_topupbills_filter_chips, this)
        sortFilterItems = findViewById(R.id.common_topup_bills_filter_items)
        sortFilterHorizontalScrollView = findViewById(R.id.common_topup_bills_filter_items_wrapper)
    }

    fun addItems(items: ArrayList<SortFilterItem>, isLastIdxStatic: Boolean = false) {
        sortFilterItems.removeAllViews()
        chipItems = items

        chipItems?.size?.let { chipItemsSize ->
            for (i in 0 until chipItemsSize) {
                chipItems?.get(i)?.let { indexedChipItem ->
                    val chip = ChipsUnify(context)
                    chip.setChip(indexedChipItem.title, indexedChipItem.type, indexedChipItem.size)
                    sortFilterItems.addView(chip)

                    val chipParams = chip.layoutParams as LayoutParams
                    chipParams.rightMargin = 4.toPx()
                    chip.layoutParams = chipParams

                    indexedChipItem.refChipUnify = chip

                    chip.setOnClickListener {
                        indexedChipItem.listener()
                        highlightActiveChip(i, isLastIdxStatic)
                    }

                    indexedChipItem.updateTitle = {
                        chip.chip_text.text = it
                    }
                }
            }
        }

        if (isLastIdxStatic) {
            chipItems?.last()?.refChipUnify?.chip_right_icon?.run {
                setImageDrawable(getIconUnifyDrawable(
                    context,
                    IconUnify.CHEVRON_RIGHT,
                    ContextCompat.getColor(context, R.color.Unify_GN500)
                ))
                layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.layout_lvl3)
                layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.layout_lvl3)
                show()
            }
        }
    }

    fun clearFilter() {
        chipItems?.size?.let { chipItemsSize ->
            for (i in 0 until chipItemsSize-1) {
                chipItems?.get(i)?.let { indexedChipItem ->
                    indexedChipItem.type = ChipsUnify.TYPE_NORMAL
                    indexedChipItem.refChipUnify.chipType = indexedChipItem.type
                    indexedChipItem.refChipUnify.chip_text.text = indexedChipItem.title
                }
            }
        }
    }

    private fun highlightActiveChip(activeIndex: Int, excludeLastIdx: Boolean) {
        chipItems?.size?.let {
            val chipItemsSize = if (excludeLastIdx) it-1 else it
            for (i in 0 until chipItemsSize) {
                if (activeIndex != i) {
                    chipItems?.get(i)?.let { indexedChipItem ->
                        indexedChipItem.type = ChipsUnify.TYPE_NORMAL
                        indexedChipItem.refChipUnify.chipType = indexedChipItem.type
                        indexedChipItem.refChipUnify.chip_text.text = indexedChipItem.title
                    }
                } else {
                    chipItems?.get(activeIndex)?.let { indexedChipItem ->
                        indexedChipItem.type = ChipsUnify.TYPE_SELECTED
                        indexedChipItem.refChipUnify.chipType = indexedChipItem.type
                    }

                }
            }
        }
    }

    private fun ChipsUnify.setChip(title: CharSequence, type: String, size: String) {
        chip_text.text = title
        chipType = type
        chipSize = size
    }
}