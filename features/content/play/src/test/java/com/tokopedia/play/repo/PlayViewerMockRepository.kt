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
            userReportRepository: PlayViewerUserReportRepository = mockk(relaxed = true)
    ): PlayViewerRepository {
        return PlayViewerRepositoryImpl(
                interactiveRepo = interactiveRepo,
                partnerRepo = partnerRepo,
                likeRepo = likeRepo,
                cartRepo = cartRepo,
                userReportRepo = userReportRepository
        )
    }
}