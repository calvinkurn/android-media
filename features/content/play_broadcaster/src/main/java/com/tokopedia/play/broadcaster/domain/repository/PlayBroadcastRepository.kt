package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.content.product.picker.sgc.domain.ContentProductPickerSGCRepository

/**
 * Created by jegul on 12/10/21
 */
interface PlayBroadcastRepository : PlayBroadcastChannelRepository,
    PlayBroadcastPinnedMessageRepository,
    PlayBroadcastInteractiveRepository,
    ContentProductPickerSGCRepository,
    PlayBroadcastBeautificationRepository
