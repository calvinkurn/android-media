package com.tokopedia.content.common.analytic.entrypoint

interface PlayPerformanceDashboardEntryPointAnalytic {

    /**
     * row 1
     **/
    fun onViewBottomSheetContentCard(authorId: String)

    /**
     * row 2
     **/
    fun onClickReportPageEntryPointShopPage(authorId: String)

    /**
     * row 3
     **/
    fun onClickPerformanceDashboardEntryPointNative(authorId: String)

    /**
     * row 4
     **/
    fun onViewBottomSheetContentCardShopPageChannel(authorId: String)

    /**
     * row 5
     **/
    fun onClickReportPageEntryPoint(authorId: String)

    /**
     * row 6
     **/
    fun onClickPerformanceDashboardEntryPointShortsPageChanel(authorId: String)

    /**
     * row 7
     **/
    fun onClickPerformanceDashboardEntryPointChannelPage(authorId: String, channelId: String)

    /**
     * row 8
     **/
    fun onClickReportPageEntryPointGroupChatRoom(authorId: String, channelId: String)

    /**
     * row 9
     **/
    fun onClickPerformanceDashboardEntryPointReportPage(authorId: String)

    /**
     * row 10
     **/
    fun onClickPerformanceDashboardEntryPointShopPage(authorId: String)

    /**
     * row 11
     **/
    fun onClickPerformanceDashboardEntryPointPrepPage(authorId: String)

    /**
     * row 12
     **/
    fun onViewCoachMarkPerformanceDashboardPrepPage(authorId: String)

    /**
     * row 13
     **/
    fun onClickCloseCoachMarkPerformanceDashboardPrepPage(authorId: String)

    /**
     * row 14
     **/
    fun onViewPerformanceDashboardEntryPointPrepPage(authorId: String, creativeSlot: Int)
}
