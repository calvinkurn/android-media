package com.tokopedia.play.ui.userreport.adapter.delegate

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.play.R
import com.tokopedia.play.ui.userreport.viewholder.UserReportSectionViewHolder
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel
import com.tokopedia.play.view.uimodel.PlayUserReportSection

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