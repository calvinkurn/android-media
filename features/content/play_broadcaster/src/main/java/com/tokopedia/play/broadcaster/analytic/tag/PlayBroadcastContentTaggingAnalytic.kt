package com.tokopedia.play.broadcaster.analytic.tag

/**
 * Created by jegul on 28/04/21
 */
interface PlayBroadcastContentTaggingAnalytic {

    fun selectRecommendedTag(channelId: String, tag: String, isChosen: Boolean)

    fun proceedFromContentTagging(channelId: String)

    fun clickTitleInputArea()
}