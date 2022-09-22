package com.tokopedia.play.broadcaster.data.config

import javax.inject.Inject

/**
 * Created by jegul on 03/07/20
 */
class HydraConfigStoreImpl @Inject constructor(
        private val channelConfigStore: ChannelConfigStore,
        private val productConfigStore: ProductConfigStore,
        private val titleConfigStore: TitleConfigStore,
        private val broadcastScheduleConfigStore: BroadcastScheduleConfigStore,
        private val ugcConfigStore: UGCConfigStore,
) : HydraConfigStore,
        ChannelConfigStore by channelConfigStore,
        ProductConfigStore by productConfigStore,
        TitleConfigStore by titleConfigStore,
        BroadcastScheduleConfigStore by broadcastScheduleConfigStore,
        UGCConfigStore by ugcConfigStore