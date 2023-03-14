package com.tokopedia.play.broadcaster.data.config

import javax.inject.Inject

/**
 * Created by jegul on 03/07/20
 */
class HydraConfigStoreImpl @Inject constructor(
    private val broadcastingConfigStore: BroadcastingConfigStore,
    private val channelConfigStore: ChannelConfigStore,
    private val productConfigStore: ProductConfigStore,
    private val titleConfigStore: TitleConfigStore,
    private val broadcastScheduleConfigStore: BroadcastScheduleConfigStore,
    private val accountConfigStore: AccountConfigStore,
) : HydraConfigStore,
        BroadcastingConfigStore by broadcastingConfigStore,
        ChannelConfigStore by channelConfigStore,
        ProductConfigStore by productConfigStore,
        TitleConfigStore by titleConfigStore,
        BroadcastScheduleConfigStore by broadcastScheduleConfigStore,
        AccountConfigStore by accountConfigStore
