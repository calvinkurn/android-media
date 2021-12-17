package com.tokopedia.play.broadcaster.data.repository

import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastChannelRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastInteractiveRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastPinnedMessageRepository
import com.tokopedia.play.broadcaster.domain.repository.PlayBroadcastRepository
import javax.inject.Inject

/**
 * Created by jegul on 12/10/21
 */
class PlayBroadcastRepositoryImpl @Inject constructor(
    private val channelRepo: PlayBroadcastChannelRepository,
    private val pinnedMessageRepo: PlayBroadcastPinnedMessageRepository,
    private val interactiveRepo: PlayBroadcastInteractiveRepository,
) : PlayBroadcastRepository,
    PlayBroadcastChannelRepository by channelRepo,
    PlayBroadcastPinnedMessageRepository by pinnedMessageRepo,
    PlayBroadcastInteractiveRepository by interactiveRepo