package com.tokopedia.play.broadcaster.setup

import androidx.lifecycle.SavedStateHandle
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.content.common.util.remoteconfig.PlayShortsEntryPointRemoteConfig
import com.tokopedia.content.product.picker.seller.util.PriceFormatUtil
import com.tokopedia.play.broadcaster.data.config.ChannelConfigStore
import com.tokopedia.play.broadcaster.data.config.HydraConfigStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastDataStore
import com.tokopedia.play.broadcaster.data.datastore.PlayBroadcastSetupDataStore
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import com.tokopedia.play.broadcaster.domain.usecase.CreateLiveStreamChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetAddedChannelTagsUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.GetSocketCredentialUseCase
import com.tokopedia.play.broadcaster.pusher.timer.PlayBroadcastTimer
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroProductUiMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastMapper
import com.tokopedia.play.broadcaster.ui.mapper.PlayBroadcastUiMapper
import com.tokopedia.play.broadcaster.util.helper.DefaultUriParser
import com.tokopedia.play.broadcaster.util.logger.PlayLogger
import com.tokopedia.play.broadcaster.util.logger.error.BroadcasterErrorLogger
import com.tokopedia.play.broadcaster.util.preference.HydraSharedPreferences
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastPrepareViewModel
import com.tokopedia.play.broadcaster.view.viewmodel.PlayBroadcastViewModel
import com.tokopedia.play_common.model.mapper.PlayInteractiveMapper
import com.tokopedia.play_common.transformer.DefaultHtmlTextTransformer
import com.tokopedia.play_common.websocket.PlayWebSocket
import com.tokopedia.remoteconfig.RemoteConfig
import com.tokopedia.user.session.UserSessionInterface
import io.mockk.mockk

/**
 * Created by fachrizalmrsln on 28/09/22
 */
fun parentBroViewModel(
    handle: SavedStateHandle = SavedStateHandle(),
    dataStore: PlayBroadcastDataStore = mockk(relaxed = true),
    hydraConfigStore: HydraConfigStore = mockk(relaxed = true),
    sharedPref: HydraSharedPreferences = mockk(relaxed = true),
    getChannelUseCase: GetChannelUseCase = mockk(relaxed = true),
    getAddedChannelTagsUseCase: GetAddedChannelTagsUseCase = mockk(relaxed = true),
    getSocketCredentialUseCase: GetSocketCredentialUseCase = mockk(relaxed = true),
    dispatcher: CoroutineDispatchers = mockk(relaxed = true),
    userSession: UserSessionInterface = mockk(relaxed = true),
    playBroadcastWebSocket: PlayWebSocket = mockk(relaxed = true),
    playBroadcastMapper: PlayBroadcastMapper = PlayBroadcastUiMapper(DefaultHtmlTextTransformer(), DefaultUriParser(), mockk(relaxed = true)),
    productMapper: PlayBroProductUiMapper = PlayBroProductUiMapper(PriceFormatUtil()),
    interactiveMapper: PlayInteractiveMapper = PlayInteractiveMapper(DefaultHtmlTextTransformer()),
    repo: PlayBroadcastRepository = mockk(relaxed = true),
    logger: PlayLogger = mockk(relaxed = true),
    broadcastTimer: PlayBroadcastTimer = mockk(relaxed = true),
    playShortsEntryPointRemoteConfig: PlayShortsEntryPointRemoteConfig,
    remoteConfig: RemoteConfig = mockk(relaxed = true),
    errorLogger: BroadcasterErrorLogger = mockk(relaxed = true),
): PlayBroadcastViewModel {
    return PlayBroadcastViewModel(
        handle = handle,
        dataStore = dataStore,
        hydraConfigStore = hydraConfigStore,
        sharedPref = sharedPref,
        getChannelUseCase = getChannelUseCase,
        getAddedChannelTagsUseCase = getAddedChannelTagsUseCase,
        getSocketCredentialUseCase = getSocketCredentialUseCase,
        dispatcher = dispatcher,
        userSession = userSession,
        playBroadcastWebSocket = playBroadcastWebSocket,
        playBroadcastMapper = playBroadcastMapper,
        productMapper = productMapper,
        interactiveMapper = interactiveMapper,
        repo = repo,
        logger = logger,
        broadcastTimer = broadcastTimer,
        playShortsEntryPointRemoteConfig = playShortsEntryPointRemoteConfig,
        remoteConfig = remoteConfig,
        errorLogger = errorLogger,
    )
}

fun preparationBroViewModel(
    mDataStore: PlayBroadcastDataStore = mockk(relaxed = true),
    sharedPref: HydraSharedPreferences = mockk(relaxed = true),
    hydraConfigStore: HydraConfigStore = mockk(relaxed = true),
    setupDataStore: PlayBroadcastSetupDataStore = mockk(relaxed = true),
    channelConfigStore: ChannelConfigStore = mockk(relaxed = true),
    dispatcher: CoroutineDispatchers = mockk(relaxed = true),
    createLiveStreamChannelUseCase: CreateLiveStreamChannelUseCase = mockk(relaxed = true),
    playBroadcastMapper: PlayBroadcastMapper = PlayBroadcastUiMapper(DefaultHtmlTextTransformer(), DefaultUriParser(), mockk(relaxed = true)),
): PlayBroadcastPrepareViewModel {
    return PlayBroadcastPrepareViewModel(
        mDataStore = mDataStore,
        sharedPref = sharedPref,
        hydraConfigStore = hydraConfigStore,
        setupDataStore = setupDataStore,
        channelConfigStore = channelConfigStore,
        dispatcher = dispatcher,
        createLiveStreamChannelUseCase = createLiveStreamChannelUseCase,
        playBroadcastMapper = playBroadcastMapper,
    )
}
