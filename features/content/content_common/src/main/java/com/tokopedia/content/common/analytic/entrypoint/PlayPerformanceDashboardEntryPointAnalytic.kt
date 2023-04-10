package com.tokopedia.content.common.analytic.entrypoint

interface PlayPerformanceDashboardEntryPointAnalytic {

    fun onViewBottomSheetContentCard(authorId: String, authorType: String)
    fun onClickReportPageEntryPointShopPage(authorId: String, authorType: String)
    fun onClickPerformanceDashboardEntryPointNative(authorId: String, authorType: String)
    fun onViewBottomSheetContentCardShopPageChannel(authorId: String, authorType: String)
    fun onClickReportPageEntryPoint(authorId: String, authorType: String)
    fun onClickPerformanceDashboardEntryPointShortsPageChanel(authorId: String, authorType: String)
    fun onClickPerformanceDashboardEntryPointChannelPage(authorId: String, authorType: String, channelId: String)
    fun onClickReportPageEntryPointGroupChatRoom(authorId: String, authorType: String, channelId: String)
    fun onClickPerformanceDashboardEntryPointReportPage(authorId: String, authorType: String)
    fun onClickPerformanceDashboardEntryPointShopPage(authorId: String, authorType: String)
    fun onClickPerformanceDashboardEntryPointPrepPage(authorId: String, authorType: String)
    fun onViewCoachMarkPerformanceDashboardPrepPage(authorId: String, authorType: String)
    fun onClickCloseCoachMarkPerformanceDashboardPrepPage(authorId: String, authorType: String)
    fun onViewPerformanceDashboardEntryPointPrepPage(authorId: String, authorType: String)

}
