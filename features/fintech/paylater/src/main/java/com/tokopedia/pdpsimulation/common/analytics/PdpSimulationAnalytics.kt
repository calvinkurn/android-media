package com.tokopedia.pdpsimulation.common.analytics

import com.tokopedia.pdpsimulation.common.di.qualifier.CoroutineBackgroundDispatcher
import com.tokopedia.pdpsimulation.common.di.qualifier.CoroutineMainDispatcher
import com.tokopedia.track.TrackApp
import com.tokopedia.track.interfaces.ContextAnalytics
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class PdpSimulationAnalytics @Inject constructor(
        val userSession: dagger.Lazy<UserSessionInterface>,
        @CoroutineMainDispatcher val mainDispatcher: CoroutineDispatcher,
        @CoroutineBackgroundDispatcher val bgDispatcher: CoroutineDispatcher,
) {

    private val analyticTracker: ContextAnalytics
        get() = TrackApp.getInstance().gtm

}