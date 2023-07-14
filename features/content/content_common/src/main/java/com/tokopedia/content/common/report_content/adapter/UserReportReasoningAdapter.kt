package com.tokopedia.content.common.report_content.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.content.common.report_content.adapter.delegate.UserReportAdapterDelegate
import com.tokopedia.content.common.report_content.adapter.delegate.UserReportSectionAdapterDelegate
import com.tokopedia.content.common.report_content.adapter.delegate.UserReportShimmeringAdapterDelegate
import com.tokopedia.content.common.report_content.adapter.viewholder.UserReportReasoningViewHolder
import com.tokopedia.content.common.report_content.model.PlayUserReportReasoningUiModel

/**
 * @author by astidhiyaa on 09/12/21
 */
class UserReportReasoningAdapter(
    listener: UserReportReasoningViewHolder.Listener
) : BaseDiffUtilAdapter<PlayUserReportReasoningUiModel>() {

    init {
        delegatesManager
            .addDelegate(UserReportAdapterDelegate(listener))
            .addDelegate(UserReportShimmeringAdapterDelegate())
            .addDelegate(UserReportSectionAdapterDelegate())
    }

    override fun areItemsTheSame(
        oldItem: PlayUserReportReasoningUiModel,
        newItem: PlayUserReportReasoningUiModel
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: PlayUserReportReasoningUiModel,
        newItem: PlayUserReportReasoningUiModel
    ): Boolean {
        return oldItem == newItem
    }
}
