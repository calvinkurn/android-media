package com.tokopedia.home.beranda.domain.interactor

import com.tokopedia.home.beranda.data.repository.HomeRepository
import javax.inject.Inject

class DismissHomeReviewUseCase @Inject constructor(
        private val repository: HomeRepository
){
    fun execute() = repository.dismissSuggestedReview()
}