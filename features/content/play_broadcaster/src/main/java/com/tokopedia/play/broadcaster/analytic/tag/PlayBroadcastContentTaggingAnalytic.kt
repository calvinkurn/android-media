package com.tokopedia.play.broadcaster.analytic.tag

/**
 * Created by jegul on 28/04/21
 */
interface PlayBroadcastContentTaggingAnalytic {

    fun selectRecommendedTags(selectedTags: Set<String>)

    fun proceedFromContentTagging()
}