package com.tokopedia.content.common.report_content.adapter.delegate

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.R
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel

/**
 * @author by astidhiyaa on 10/12/21
 */
class UserReportShimmeringAdapterDelegate :
    TypedAdapterDelegate<PlayUserReportReasoningUiModel.Placeholder, PlayUserReportReasoningUiModel,
            RecyclerView.ViewHolder>(
        R.layout.item_play_user_report_shimmering
    ) {
    override fun onBindViewHolder(
        item: PlayUserReportReasoningUiModel.Placeholder,
        holder: RecyclerView.ViewHolder
    ) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, basicView: View): RecyclerView.ViewHolder {
        return BaseViewHolder(basicView)
    }
}
