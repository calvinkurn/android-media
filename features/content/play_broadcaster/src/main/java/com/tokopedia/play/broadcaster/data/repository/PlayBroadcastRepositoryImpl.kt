package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.content.product.picker.seller.domain.repository.ContentProductPickerSellerRepository
import com.tokopedia.play.broadcaster.domain.repository.*
import javax.inject.Inject

/**
 * Created by jegul on 12/10/21
 */
class PlayBroadcastRepositoryImpl @Inject constructor(
    private val channelRepo: PlayBroadcastChannelRepository,
    private val pinnedMessageRepo: PlayBroadcastPinnedMessageRepository,
    private val interactiveRepo: PlayBroadcastInteractiveRepository,
    private val productRepository: ContentProductPickerSellerRepository,
    private val beautificationRepository: PlayBroadcastBeautificationRepository,
) : PlayBroadcastRepository,
    PlayBroadcastChannelRepository by channelRepo,
    PlayBroadcastPinnedMessageRepository by pinnedMessageRepo,
    PlayBroadcastInteractiveRepository by interactiveRepo,
    ContentProductPickerSellerRepository by productRepository,
    PlayBroadcastBeautificationRepository by beautificationRepository
