package com.tokopedia.play.broadcaster.util

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.config.*
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.testdouble.MockChannelConfigStore
import com.tokopedia.play.broadcaster.testdouble.MockCoverDataStore
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.play_common.domain.usecase.broadcaster.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import io.mockk.mockk

/**
 * Created by jegul on 11/05/21
 */
class TestDoubleModelBuilder {

    /**
     * Mock Impl
     */
    /**
     * Data Store
     */
    fun buildSetupDataStore(
        dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
        coverDataStore: CoverDataStore = buildCoverDataStore(dispatcher),
        scheduleDataStore: BroadcastScheduleDataStore = buildBroadcastScheduleDataStore(dispatcher),
        titleDataStore: TitleDataStore = buildTitleDataStore(dispatcher),
        tagsDataStore: TagsDataStore = buildTagsDataStore(dispatcher),
        interactiveDataStore: InteractiveDataStore = buildInteractiveDataStore()
    ) = MockSetupDataStore(
        mCoverDataStore = coverDataStore,
        mScheduleDataStore = scheduleDataStore,
        mTitleDataStore = titleDataStore,
        mTagsDataStore = tagsDataStore,
        mInteractiveDataStore = interactiveDataStore
    )

    fun buildCoverDataStore(
        dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
    ) = MockCoverDataStore(dispatcher)

    fun buildBroadcastScheduleDataStore(
        dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
        updateChannelUseCase: PlayBroadcastUpdateChannelUseCase = mockk(relaxed = true)
    ) = BroadcastScheduleDataStoreImpl(
        dispatcher,
        updateChannelUseCase
    )

    fun buildTitleDataStore(
        dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
        updateChannelUseCase: PlayBroadcastUpdateChannelUseCase = mockk(relaxed = true),
    ) = TitleDataStoreImpl(
        dispatcher,
        updateChannelUseCase,
    )

    fun buildTagsDataStore(
        dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
        setChannelTagsUseCase: SetChannelTagsUseCase = mockk(relaxed = true),
    ) = TagsDataStoreImpl(
        dispatcher,
        setChannelTagsUseCase
    )

    fun buildInteractiveDataStore() = InteractiveDataStoreImpl()

    /**
     * Config Store
     */
    fun buildHydraConfigStore(
        channelConfigStore: ChannelConfigStore = buildChannelConfigStore(),
        productConfigStore: ProductConfigStore = buildProductConfigStore(),
        titleConfigStore: TitleConfigStore = buildTitleConfigStore(),
        broadcastScheduleConfigStore: BroadcastScheduleConfigStore = buildBroadcastScheduleConfigStore(),
        accountConfigStore: AccountConfigStore = buildAccountConfigStore(),
        broadcastingConfigStore: BroadcastingConfigStore = buildBroadcastingConfigStore()
    ) = HydraConfigStoreImpl(
        channelConfigStore = channelConfigStore,
        productConfigStore = productConfigStore,
        titleConfigStore = titleConfigStore,
        broadcastScheduleConfigStore = broadcastScheduleConfigStore,
        accountConfigStore = accountConfigStore,
        broadcastingConfigStore = broadcastingConfigStore,
    )

    fun buildChannelConfigStore(
        channelId: String = "12345"
    ) = MockChannelConfigStore(channelId)

    fun buildProductConfigStore() = ProductConfigStoreImpl()

    fun buildTitleConfigStore() = TitleConfigStoreImpl()

    fun buildBroadcastScheduleConfigStore() = BroadcastScheduleConfigStoreImpl()

    fun buildAccountConfigStore() = AccountConfigStoreImpl()

    fun buildBroadcastingConfigStore() = BroadcastingConfigStoreImpl()

    /**
     * Real Impl
     */
    /**
     * Data Store
     */
    fun buildRealTitleDataStore(
        dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
        updateChannelUseCase: PlayBroadcastUpdateChannelUseCase = mockk(relaxed = true),
    ) = TitleDataStoreImpl(
        dispatcher,
        updateChannelUseCase,
    )
}
