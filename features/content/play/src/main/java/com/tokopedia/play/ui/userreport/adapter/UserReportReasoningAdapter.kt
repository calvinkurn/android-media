package com.tokopedia.play.ui.userreport.adapter

import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.play.ui.userreport.adapter.delegate.UserReportAdapterDelegate
import com.tokopedia.play.ui.userreport.adapter.delegate.UserReportSectionAdapterDelegate
import com.tokopedia.play.ui.userreport.adapter.delegate.UserReportShimmeringAdapterDelegate
import com.tokopedia.play.ui.userreport.viewholder.UserReportReasoningViewHolder
import com.tokopedia.play.view.uimodel.PlayUserReportReasoningUiModel

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