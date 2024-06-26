package com.tokopedia.play.robot.parent

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.analytic.PlayAnalytic
import com.tokopedia.play.domain.repository.PlayViewerRepository
import com.tokopedia.play.view.monitoring.PlayPltPerformanceCallback
import com.tokopedia.play.view.storage.PlayChannelData
import com.tokopedia.play.view.storage.PlayChannelStateStorage
import com.tokopedia.play.view.storage.PlayQueryParamStorage
import com.tokopedia.play.view.viewmodel.PlayParentViewModel
import com.tokopedia.play_common.util.PlayPreference
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchersProvider
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.every
import io.mockk.mockk

/**
 * Created by jegul on 10/02/21
 */
class PlayParentViewModelRobot(
    private val savedStateHandle: SavedStateHandle,
    channelStateStorage: PlayChannelStateStorage,
    dispatchers: CoroutineDispatchers,
    userSession: UserSessionInterface,
    repo: PlayViewerRepository,
    pageMonitoring: PlayPltPerformanceCallback,
    queryParamStorage: PlayQueryParamStorage,
    preference: PlayPreference,
    analytic: PlayAnalytic,
    remoteConfig: RemoteConfig
) {

    val viewModel: PlayParentViewModel

    init {
        every { savedStateHandle.get<Any>(any()) } returns "123"
        every { savedStateHandle.get<Any>("start_vod_millis") } returns 123L

        viewModel = PlayParentViewModel(
            handle = savedStateHandle,
            playChannelStateStorage = channelStateStorage,
            dispatchers = dispatchers,
            userSession = userSession,
            repo = repo,
            pageMonitoring = pageMonitoring,
            queryParamStorage = queryParamStorage,
            preference = preference,
            analytic = analytic,
            remoteConfig = remoteConfig
        )
    }

    fun setChannelData(channelId: String, channelData: PlayChannelData) = viewModel.setLatestChannelStorageData(channelId, channelData)
}

fun givenParentViewModelRobot(
    savedStateHandle: SavedStateHandle = mockk(relaxed = true),
    channelStateStorage: PlayChannelStateStorage = PlayChannelStateStorage(),
    dispatchers: CoroutineDispatchers = CoroutineTestDispatchersProvider,
    userSession: UserSessionInterface = mockk(relaxed = true),
    repo: PlayViewerRepository = mockk(relaxed = true),
    pageMonitoring: PlayPltPerformanceCallback = mockk(relaxed = true),
    queryParamStorage: PlayQueryParamStorage = mockk(relaxed = true),
    preference: PlayPreference = mockk(relaxed = true),
    analytic: PlayAnalytic = mockk(relaxed = true),
    remoteConfig: RemoteConfig = mockk(relaxed = true),
    fn: PlayParentViewModelRobot.() -> Unit = {}
): PlayParentViewModelRobot {
    return PlayParentViewModelRobot(
        savedStateHandle = savedStateHandle,
        channelStateStorage = channelStateStorage,
        dispatchers = dispatchers,
        userSession = userSession,
        repo = repo,
        pageMonitoring = pageMonitoring,
        queryParamStorage = queryParamStorage,
        preference = preference,
        analytic = analytic,
        remoteConfig = remoteConfig
    ).apply(fn)
}

infix fun PlayParentViewModelRobot.andWhen(
    fn: PlayParentViewModelRobot.() -> Unit
): PlayParentViewModelRobot {
    return apply(fn)
}

infix fun PlayParentViewModelRobot.andThen(
    fn: PlayParentViewModelRobot.() -> Unit
): PlayParentViewModelRobot {
    return apply(fn)
}

infix fun PlayParentViewModelRobot.thenVerify(
    fn: PlayParentViewModelRobotResult.() -> Unit
): PlayParentViewModelRobot {
    PlayParentViewModelRobotResult(viewModel).apply { fn() }
    return this
}
