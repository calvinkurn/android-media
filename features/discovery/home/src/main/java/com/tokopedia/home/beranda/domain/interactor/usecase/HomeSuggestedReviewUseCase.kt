package com.tokopedia.home.beranda.domain.interactor.usecase

import com.tokopedia.home.beranda.domain.interactor.repository.HomeDismissReviewSuggestedRepository
import javax.inject.Inject

class HomeSuggestedReviewUseCase @Inject constructor(
        private val homeDismissReviewSuggestedRepository: HomeDismissReviewSuggestedRepository) {
    suspend fun onReviewDismissed() {
        homeDismissReviewSuggestedRepository.getRemoteData()
    }
}