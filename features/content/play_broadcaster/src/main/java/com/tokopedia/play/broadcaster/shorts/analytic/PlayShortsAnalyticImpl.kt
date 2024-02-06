package com.tokopedia.play.broadcaster.shorts.analytic

import com.tokopedia.play.broadcaster.shorts.analytic.affiliate.PlayShortsAffiliateAnalytic
import com.tokopedia.play.broadcaster.shorts.analytic.general.PlayShortsGeneralAnalytic
import com.tokopedia.play.broadcaster.shorts.analytic.interspersing.PlayShortsInterspersingAnalytic
import javax.inject.Inject

class PlayShortsAnalyticImpl @Inject constructor(
    private val playShortsGeneralAnalytic: PlayShortsGeneralAnalytic,
    private val playShortsAffiliateAnalytic: PlayShortsAffiliateAnalytic,
    private val playShortsInterspersingAnalytic: PlayShortsInterspersingAnalytic,
) : PlayShortsAnalytic,
    PlayShortsGeneralAnalytic by playShortsGeneralAnalytic,
    PlayShortsAffiliateAnalytic by playShortsAffiliateAnalytic,
    PlayShortsInterspersingAnalytic by playShortsInterspersingAnalytic
