package com.tokopedia.play.broadcaster.domain.repository

import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository

/**
 * Created by jegul on 12/10/21
 */
interface PlayBroadcastRepository : PlayBroadcastChannelRepository,
    PlayBroadcastPinnedMessageRepository,
    PlayBroadcastInteractiveRepository,
    ContentProductPickerSellerRepository,
    PlayBroadcastBeautificationRepository
