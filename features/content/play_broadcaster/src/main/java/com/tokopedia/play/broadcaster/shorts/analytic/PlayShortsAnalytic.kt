package com.tokopedia.play.broadcaster.shorts.analytic

import com.tokopedia.play.broadcaster.shorts.analytic.affiliate.PlayShortsAffiliateAnalytic
import com.tokopedia.play.broadcaster.shorts.analytic.general.PlayShortsGeneralAnalytic

interface PlayShortsAnalytic: PlayShortsGeneralAnalytic, PlayShortsAffiliateAnalytic
