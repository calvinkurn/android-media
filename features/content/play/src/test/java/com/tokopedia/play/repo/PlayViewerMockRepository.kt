package com.tokopedia.play.repo

import com.tokopedia.play.data.repository.PlayViewerRepositoryImpl
import com.tokopedia.play.domain.repository.*
import io.mockk.mockk

/**
 * Created by jegul on 23/08/21
 */
object PlayViewerMockRepository {

    fun get(
        interactiveRepo: PlayViewerInteractiveRepository = mockk(relaxed = true),
        partnerRepo: PlayViewerPartnerRepository = mockk(relaxed = true),
        likeRepo: PlayViewerLikeRepository = mockk(relaxed = true),
        cartRepo: PlayViewerCartRepository = mockk(relaxed = true),
        channelRepo: PlayViewerChannelRepository = mockk(relaxed = true),
        tagItemRepo: PlayViewerTagItemRepository = mockk(relaxed = true),
        broTrackerRepo: PlayViewerBroTrackerRepository = mockk(relaxed = true),
        userReportRepository: PlayViewerUserReportRepository = mockk(relaxed = true),
        socketRepo: PlayViewerSocketRepository = mockk(relaxed = true),
        exploreWidgetRepo: PlayExploreWidgetRepository = mockk(relaxed = true),
    ): PlayViewerRepository {
        return PlayViewerRepositoryImpl(
            interactiveRepo = interactiveRepo,
            partnerRepo = partnerRepo,
            likeRepo = likeRepo,
            cartRepo = cartRepo,
            channelRepo = channelRepo,
            tagItemRepo = tagItemRepo,
            broTrackerRepo = broTrackerRepo,
            userReportRepo = userReportRepository,
            socketRepo = socketRepo,
            widgetRepo = exploreWidgetRepo,
        )
    }
}
