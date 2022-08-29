package com.tokopedia.play.broadcaster.analytic.summary

/**
 * Created By : Jonathan Darwin on March 10, 2022
 */
interface PlayBroadcastSummaryAnalytic {

    fun clickPostingVideoOnReportPage()

    fun clickPostingVideoNow()

    fun clickContentTag(tagName: String, isChosen: Boolean)

    fun clickCoverOnReportPage(channelID: String, channelTitle: String)

    fun impressReportPage(channelID: String)

    fun clickInteractiveParticipantDetail(channelID: String, channelTitle: String)
}