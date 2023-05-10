package com.tokopedia.play.broadcaster.shorts.analytic

import com.tokopedia.play.broadcaster.shorts.analytic.general.PlayShortsGeneralAnalytic
import javax.inject.Inject

class PlayShortsAnalyticImpl @Inject constructor(
    private val playShortsGeneralAnalytic: PlayShortsGeneralAnalytic,
) : PlayShortsAnalytic,
    PlayShortsGeneralAnalytic by playShortsGeneralAnalytic
