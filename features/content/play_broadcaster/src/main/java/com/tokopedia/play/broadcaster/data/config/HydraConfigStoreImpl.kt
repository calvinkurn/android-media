package com.tokopedia.play.broadcaster.data.config

import javax.inject.Inject

/**
 * Created by jegul on 03/07/20
 */
class HydraConfigStoreImpl @Inject constructor(
        private val channelConfigStore: ChannelConfigStore,
        private val productConfigStore: ProductConfigStore,
        private val coverConfigStore: CoverConfigStore,
        private val broadcastScheduleConfigStore: BroadcastScheduleConfigStore
) : HydraConfigStore,
        ChannelConfigStore by channelConfigStore,
        ProductConfigStore by productConfigStore,
        CoverConfigStore by coverConfigStore,
        BroadcastScheduleConfigStore by broadcastScheduleConfigStore