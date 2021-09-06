package com.tokopedia.tokopedianow.recentpurchase.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.recentpurchase.presentation.uimodel.RepurchaseSortFilterOnBuyingUiModel
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class RepurchaseSortFilterOnBuyingViewHolder(
    itemView: View,
    val listener: RepurchaseSortFilterOnBuyingViewHolderListener
): AbstractViewHolder<RepurchaseSortFilterOnBuyingUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_repurchase_sort_filter_on_buying
    }

    private var tpSortTitle: Typography? = null
    private var rbSort: RadioButtonUnify? = null
    private var container: ConstraintLayout? = null
    private var divider: View? = null

    init {
        tpSortTitle = itemView.findViewById(R.id.tp_sort_title)
        rbSort = itemView.findViewById(R.id.rb_sort)
        container = itemView.findViewById(R.id.container)
        divider = itemView.findViewById(R.id.divider)
    }

    override fun bind(element: RepurchaseSortFilterOnBuyingUiModel) {
        tpSortTitle?.text = getString(element.titleRes.orZero())
        rbSort?.isChecked = element.isChecked == true
        divider?.showWithCondition(!element.isLastItem)
        container?.setOnClickListener {
            listener.onClickItem(rbSort?.isChecked == true, adapterPosition, element.value)
        }
        rbSort?.setOnClickListener {
            listener.onClickItem(rbSort?.isChecked == true, adapterPosition, element.value)
        }
    }

    interface RepurchaseSortFilterOnBuyingViewHolderListener {
        fun onClickItem(isChecked: Boolean, position: Int, value: Int)
    }
}