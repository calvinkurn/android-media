package com.tokopedia.play.domain.repository

/**
 * Created by jegul on 12/08/21
 */
interface PlayViewerRepository : PlayViewerCartRepository,
        PlayViewerInteractiveRepository,
        PlayViewerLikeRepository,
        PlayViewerPartnerRepository,
        PlayViewerUserReportRepository