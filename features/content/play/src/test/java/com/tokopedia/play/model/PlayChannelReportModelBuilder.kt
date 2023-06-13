package com.tokopedia.play.model

import com.tokopedia.play.view.uimodel.recom.PlayChannelReportUiModel

/**
 * Created by jegul on 23/08/21
 */
class PlayChannelReportModelBuilder {

    fun buildChannelReport(
            totalViewFmt: String = "0",
            totalLike: Long = 0L,
            totalLikeFmt: String = "0",
            performanceSummaryPageLink: String = "page_link",
    ) = PlayChannelReportUiModel(
            totalViewFmt = totalViewFmt,
            totalLike = totalLike,
            totalLikeFmt = totalLikeFmt,
            performanceSummaryPageLink = performanceSummaryPageLink,
    )
}
