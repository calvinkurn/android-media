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
        private val broTrackerRepo: PlayViewerBroTrackerRepository,
        private val userReportRepo: PlayViewerUserReportRepository,
        private val socketRepo: PlayViewerSocketRepository,
        private val widgetRepo: PlayExploreWidgetRepository,
) : PlayViewerRepository,
        PlayViewerCartRepository by cartRepo,
        PlayViewerInteractiveRepository by interactiveRepo,
        PlayViewerLikeRepository by likeRepo,
        PlayViewerPartnerRepository by partnerRepo,
        PlayViewerChannelRepository by channelRepo,
        PlayViewerTagItemRepository by tagItemRepo,
        PlayViewerBroTrackerRepository by broTrackerRepo,
        PlayViewerUserReportRepository by userReportRepo,
        PlayViewerSocketRepository by socketRepo,
        PlayExploreWidgetRepository by widgetRepo
