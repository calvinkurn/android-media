package com.tokopedia.play.broadcaster.util

import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.play.broadcaster.data.config.*
import com.tokopedia.play.broadcaster.data.datastore.*
import com.tokopedia.play.broadcaster.domain.usecase.PlayBroadcastUpdateChannelUseCase
import com.tokopedia.play.broadcaster.domain.usecase.SetChannelTagsUseCase
import com.tokopedia.play.broadcaster.testdouble.MockChannelConfigStore
import com.tokopedia.play.broadcaster.testdouble.MockCoverDataStore
import com.tokopedia.play.broadcaster.testdouble.MockProductDataStore
import com.tokopedia.play.broadcaster.testdouble.MockSetupDataStore
import com.tokopedia.unit.test.dispatcher.CoroutineTestDispatchers
import com.tokopedia.user.session.UserSessionInterface
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
            productDataStore: ProductDataStore = buildProductDataStore(dispatcher),
            coverDataStore: CoverDataStore = buildCoverDataStore(dispatcher),
            scheduleDataStore: BroadcastScheduleDataStore = buildBroadcastScheduleDataStore(dispatcher),
            titleDataStore: TitleDataStore = buildTitleDataStore(dispatcher),
            tagsDataStore: TagsDataStore = buildTagsDataStore(dispatcher)
    ) = MockSetupDataStore(
            mProductDataStore = productDataStore,
            mCoverDataStore = coverDataStore,
            mScheduleDataStore = scheduleDataStore,
            mTitleDataStore = titleDataStore,
            mTagsDataStore = tagsDataStore
    )

    fun buildProductDataStore(
            dispatcher: CoroutineDispatchers = CoroutineTestDispatchers
    ) = MockProductDataStore(dispatcher)

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
            userSession: UserSessionInterface = mockk(relaxed = true)
    ) = TitleDataStoreImpl(
            dispatcher,
            updateChannelUseCase,
            userSession
    )

    fun buildTagsDataStore(
            dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
            setChannelTagsUseCase: SetChannelTagsUseCase = mockk(relaxed = true),
    ) = TagsDataStoreImpl(
            dispatcher,
            setChannelTagsUseCase
    )

    /**
     * Config Store
     */
    fun buildHydraConfigStore(
            channelConfigStore: ChannelConfigStore = buildChannelConfigStore(),
            productConfigStore: ProductConfigStore = buildProductConfigStore(),
            titleConfigStore: TitleConfigStore = buildTitleConfigStore(),
            broadcastScheduleConfigStore: BroadcastScheduleConfigStore = buildBroadcastScheduleConfigStore()
    ) = HydraConfigStoreImpl(
            channelConfigStore = channelConfigStore,
            productConfigStore = productConfigStore,
            titleConfigStore = titleConfigStore,
            broadcastScheduleConfigStore = broadcastScheduleConfigStore
    )

    fun buildChannelConfigStore(
            channelId: String = "12345"
    ) = MockChannelConfigStore(channelId)

    fun buildProductConfigStore(
    ) = ProductConfigStoreImpl()

    fun buildTitleConfigStore(
    ) = TitleConfigStoreImpl()

    fun buildBroadcastScheduleConfigStore(
    ) = BroadcastScheduleConfigStoreImpl()

    /**
     * Real Impl
     */
    /**
     * Data Store
     */
    fun buildRealTitleDataStore(
            dispatcher: CoroutineDispatchers = CoroutineTestDispatchers,
            updateChannelUseCase: PlayBroadcastUpdateChannelUseCase = mockk(relaxed = true),
            userSession: UserSessionInterface = mockk(relaxed = true)
    ) = TitleDataStoreImpl(
            dispatcher,
            updateChannelUseCase,
            userSession
    )
}