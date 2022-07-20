package com.tokopedia.play.broadcaster.domain.repository

/**
 * Created by jegul on 12/10/21
 */
interface PlayBroadcastRepository : PlayBroadcastChannelRepository,
    PlayBroadcastPinnedMessageRepository,
    PlayBroadcastInteractiveRepository,
        PlayBroProductRepository