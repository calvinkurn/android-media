package com.tokopedia.sellerorder.filter.presentation.adapter.viewholder

import android.view.View
import androidx.core.view.ViewCompat
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.sellerorder.R
import com.tokopedia.sellerorder.filter.presentation.adapter.SomFilterListener
import com.tokopedia.sellerorder.filter.presentation.model.SomFilterUiModel
import kotlinx.android.synthetic.main.item_som_filter_list.view.*

class SomFilterViewHolder(view: View, private val filterListener: SomFilterListener) : AbstractViewHolder<SomFilterUiModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_som_filter_list
    }


    override fun bind(element: SomFilterUiModel) {
        with(itemView) {
            tvHeaderSeeAll.apply {
                if (element.canSelectMany) {
                    show()
                    setOnClickListener {
                        filterListener.onSeeAllFilter(element.nameFilter, adapterPosition)
                    }
                } else {
                    hide()
                }
            }
            dividerSomFilter.apply {
                if (element.isDividerVisible) {
                    show()
                } else {
                    hide()
                }
            }
        }
        setupChipsAdapter()
    }

    private fun setupChipsAdapter() {
        with(itemView) {
            val layoutManagerChips = ChipsLayoutManager.newBuilder(context)
                    .setOrientation(ChipsLayoutManager.HORIZONTAL)
                    .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                    .build()
            rvSomFilter?.also {
                it.layoutManager = layoutManagerChips
                ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR)
            }
        }
    }
}

//chipsItem.apply {
//                centerText = true
//                chipText = element.orderStatus
//                chipSize = ChipsUnify.SIZE_MEDIUM
//                chipType = if(element.isSelected) {
//                    ChipsUnify.TYPE_SELECTED
//                } else {
//                    ChipsUnify.TYPE_NORMAL
//                }
//                setOnClickListener {
//                    statusOrderListener.onChipsStatusClicked(element.id, adapterPosition, chipType.orEmpty())
//                }
//            }