package com.tokopedia.tokopedianow.datefilter.presentation.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.tokopedianow.R
import com.tokopedia.tokopedianow.datefilter.presentation.uimodel.DateFilterUiModel
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class DateFilterViewHolder(
    itemView: View,
    val listener: DateFilterViewHolderListener
): AbstractViewHolder<DateFilterUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_tokopedianow_date_filter
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

    override fun bind(element: DateFilterUiModel) {
        tpSortTitle?.text = getString(element.titleRes.orZero())
        rbSort?.isChecked = element.isChecked == true
        divider?.showWithCondition(!element.isLastItem)
        container?.setOnClickListener {
            listener.onClickItem(rbSort?.isChecked == true, adapterPosition, 0)
        }
        rbSort?.setOnClickListener {
            listener.onClickItem(rbSort?.isChecked == true, adapterPosition, 0)
        }
    }

    interface DateFilterViewHolderListener {
        fun onClickItem(isChecked: Boolean, position: Int, value: Int)
    }
}