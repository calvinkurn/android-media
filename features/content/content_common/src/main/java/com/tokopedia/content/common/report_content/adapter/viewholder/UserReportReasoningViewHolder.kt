package com.tokopedia.content.common.report_content.adapter.viewholder

import android.view.View
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.content.common.R
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel

/**
 * @author by astidhiyaa on 09/12/21
 */
class UserReportReasoningViewHolder(
    itemView: View,
    private val listener: Listener
) : BaseViewHolder(itemView) {

    private val tvTitleCategory: TextView = itemView.findViewById(R.id.tv_title)

    fun bind(item: PlayUserReportReasoningUiModel.Reasoning){
        tvTitleCategory.text = item.title
        itemView.setOnClickListener {
            listener.onItemCategoryClicked(item)
        }
    }

    interface Listener {
        fun onItemCategoryClicked(item: PlayUserReportReasoningUiModel.Reasoning)
    }
}
