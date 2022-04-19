package com.tokopedia.topads.common.view.adapter.tips.viewholder

import android.view.View
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.topads.common.R
import com.tokopedia.topads.common.view.adapter.tips.viewmodel.TipsUiSortModel
import com.tokopedia.unifycomponents.selectioncontrol.RadioButtonUnify
import com.tokopedia.unifyprinciples.Typography

class TipsUiSortViewHolder(itemView: View, var onItemClick: OnUiSortItemClick? = null) : BaseViewHolder(itemView) {
    private val headerText: Typography = itemView.findViewById(R.id.headerText)
    private val subHeaderText: Typography = itemView.findViewById(R.id.subHeaderText)
    private val radioButton: RadioButtonUnify = itemView.findViewById(R.id.radioButton)

    fun bindSortItem(sortModel: TipsUiSortModel) {
        headerText.text = getString(sortModel.headerText)
        subHeaderText.text = getString(sortModel.subHeaderText)
        radioButton.isChecked = sortModel.isChecked
        itemView.setOnClickListener {
            onItemClick?.onItemClick(sortModel)
        }
    }

    interface OnUiSortItemClick {
        fun onItemClick(sortModel: TipsUiSortModel)
    }
}