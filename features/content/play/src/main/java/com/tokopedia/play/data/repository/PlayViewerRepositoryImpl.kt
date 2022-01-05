package com.tokopedia.play.data.repository

import com.tokopedia.play.domain.repository.*
import javax.inject.Inject

/**
 * Created by jegul on 12/08/21
 */
class PlayViewerRepositoryImpl @Inject constructor(
        private val interactiveRepo: PlayViewerInteractiveRepository,
        private val partnerRepo: PlayViewerPartnerRepository,
        private val likeRepo: PlayViewerLikeRepository,
        private val cartRepo: PlayViewerCartRepository,
        private val channelRepo: PlayViewerChannelRepository,
        private val tagItemRepo: PlayViewerTagItemRepository,
) : PlayViewerRepository,
        PlayViewerCartRepository by cartRepo,
        PlayViewerInteractiveRepository by interactiveRepo,
        PlayViewerLikeRepository by likeRepo,
        PlayViewerPartnerRepository by partnerRepo,
        PlayViewerChannelRepository by channelRepo,
        PlayViewerTagItemRepository by tagItemRepo