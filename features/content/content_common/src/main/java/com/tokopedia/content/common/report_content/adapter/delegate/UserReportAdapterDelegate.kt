package com.tokopedia.content.common.report_content.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.report_content.adapter.viewholder.UserReportReasoningViewHolder
import com.tokopedia.content.common.R
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel

/**
 * @author by astidhiyaa on 09/12/21
 */
class UserReportAdapterDelegate(
    listener: UserReportReasoningViewHolder.Listener
) : TypedAdapterDelegate<PlayUserReportReasoningUiModel.Reasoning, PlayUserReportReasoningUiModel, UserReportReasoningViewHolder>(
    R.layout.item_user_report
), UserReportReasoningViewHolder.Listener by listener {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        mView: View
    ): UserReportReasoningViewHolder {
        return UserReportReasoningViewHolder(mView, this)
    }

    override fun onBindViewHolder(
        item: PlayUserReportReasoningUiModel.Reasoning,
        holder: UserReportReasoningViewHolder
    ) {
        holder.bind(item)
    }

}
