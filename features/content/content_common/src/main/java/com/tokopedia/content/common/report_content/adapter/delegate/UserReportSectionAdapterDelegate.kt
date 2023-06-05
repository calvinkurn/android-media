package com.tokopedia.content.common.report_content.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.common.report_content.adapter.viewholder.UserReportSectionViewHolder
import com.tokopedia.content.common.R
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel
import com.tokopedia.content.common.report_content.model.PlayUserReportSection

/**
 * @author by astidhiyaa on 16/12/21
 */
class UserReportSectionAdapterDelegate : TypedAdapterDelegate<PlayUserReportSection, PlayUserReportReasoningUiModel,
    UserReportSectionViewHolder>(
    R.layout.item_user_report_section
) {
    override fun onBindViewHolder(
        item: PlayUserReportSection,
        holder: UserReportSectionViewHolder
    ) {
        holder.bind(item)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        basicView: View
    ): UserReportSectionViewHolder {
        return UserReportSectionViewHolder(basicView)
    }
}
